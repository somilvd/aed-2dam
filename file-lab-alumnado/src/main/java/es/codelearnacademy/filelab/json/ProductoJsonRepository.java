package es.codelearnacademy.filelab.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.codelearnacademy.filelab.model.Producto;
import es.codelearnacademy.filelab.repository.AbstractFileRepository;
import es.codelearnacademy.filelab.repository.IProductoRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
        return producto.id();
    }

    @Override
    protected List<Producto> readAll() throws IOException {
        if (!Files.exists(path)) {
            return List.of();
        }
        Producto[] productos = mapper.readValue(path.toFile(), Producto[].class);

        return new ArrayList<>(List.of(productos));
    }

    @Override
    protected void writeAll(List<Producto> productos) throws IOException {
        Path destino = path.toAbsolutePath();
        Path directorio = destino.getParent();
        Files.createDirectories(directorio);

        Path temporal = Files.createTempFile(directorio, "productos-", ".json.temp" );
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(temporal.toFile(), productos);
            Files.move(temporal, destino, java.nio.file.StandardCopyOption.REPLACE_EXISTING );
        } finally {
            Files.deleteIfExists(temporal);
        }
    }
}
