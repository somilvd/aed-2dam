package es.codelearnacademy.filelab.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.codelearnacademy.filelab.model.Producto;
import es.codelearnacademy.filelab.repository.AbstractFileRepository;
import es.codelearnacademy.filelab.repository.IProductoRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ProductoJsonRepository
        extends AbstractFileRepository<Producto, Long>
        implements IProductoRepository {

    private final Path path;
    private final ObjectMapper mapper;

    public ProductoJsonRepository(Path path) {
        this(path, new ObjectMapper());
    }

    public ProductoJsonRepository(Path path, ObjectMapper mapper) {
        this.path = path;
        this.mapper = mapper;
    }

    @Override
    protected Long getId(Producto producto) {
        throw new UnsupportedOperationException("Función no implementada");
    }

    @Override
    protected List<Producto> readAll() throws IOException {
        throw new UnsupportedOperationException("Función no implementada");
    }

    @Override
    protected void writeAll(List<Producto> productos) throws IOException {
        Path temporal = null;
        try {
            Path destino = path.toAbsolutePath();
            Path directorio = destino.getParent();
            Files.createDirectories(directorio);
            temporal = Files.createTempFile(directorio, "productos-", ".json.temp");
            mapper.writerWithDefaultPrettyPrinter().writeValue(temporal.toFile(), productos);
            try {
                Files.move(temporal, destino);
            } catch (IOException e) {

            }
        } catch (IOException e) {

        }
    }
}
