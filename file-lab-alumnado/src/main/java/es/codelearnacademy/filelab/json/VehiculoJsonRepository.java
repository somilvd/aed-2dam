package es.codelearnacademy.filelab.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.codelearnacademy.filelab.model.Producto;
import es.codelearnacademy.filelab.model.Vehiculo;
import es.codelearnacademy.filelab.repository.AbstractFileRepository;
import es.codelearnacademy.filelab.repository.IVehiculoRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class VehiculoJsonRepository
        extends AbstractFileRepository<Vehiculo, String>
        implements IVehiculoRepository {

    private final Path path;
    private final ObjectMapper mapper = new ObjectMapper();

    public VehiculoJsonRepository(Path path) {
        this.path = path;
    }

    @Override
    protected String getId(Vehiculo vehiculo) {
        return vehiculo.matricula();
    }

    @Override
    protected List<Vehiculo> readAll() throws IOException {
        if (!Files.exists(path)) {
            return List.of();
        }
        Vehiculo[] vehiculos = mapper.readValue(path.toFile(), Vehiculo[].class);
        return new ArrayList<>(List.of(vehiculos));
    }

    @Override
    protected void writeAll(List<Vehiculo> vehiculos) throws IOException {
        Path destino = path.toAbsolutePath();
        Path directorio = destino.getParent();
        Files.createDirectories(directorio);

        Path temporal = Files.createTempFile(directorio, "vehiculos-", ".json.temp");

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(temporal.toFile(), vehiculos);
            Files.move(temporal, destino, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temporal);
        }
    }
}
