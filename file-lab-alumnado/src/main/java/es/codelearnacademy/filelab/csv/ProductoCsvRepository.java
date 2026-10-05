package es.codelearnacademy.filelab.csv;

import es.codelearnacademy.filelab.model.Producto;
import es.codelearnacademy.filelab.repository.AbstractFileRepository;
import es.codelearnacademy.filelab.repository.IProductoRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProductoCsvRepository extends AbstractFileRepository<Producto, Long> implements IProductoRepository {

    private final Path path;
    public ProductoCsvRepository(Path path) {
        this.path = path;
    }

    @Override
    protected Long getId(Producto producto) {
        return producto.id();
    }

    @Override
    protected List<Producto> readAll() throws IOException {
        List<Producto> productos = new ArrayList<>();
        if (!Files.exists(path) || Files.size(path) == 0) {
            return productos;
        }

        List<String> lineas = Files.readAllLines(path);

        for (int i = 1; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            String[] datos = linea.split(",");

            Long id = Long.parseLong(datos[0].trim());
            String nombre = datos[1].trim();
            double precio = Double.parseDouble(datos[2].trim());
            int stock = Integer.parseInt(datos[3].trim());

            productos.add(new Producto(id, nombre, precio, stock));

        }
        return productos;
    }

    @Override
    protected void writeAll(List<Producto> productos) throws IOException {
        List<String> lineas = new ArrayList<>();
        lineas.add("id,nombre,precio,stock");

        for (Producto producto : productos) {
            lineas.add(producto.id() + "," + producto.nombre() + "," + producto.precio() + "," + producto.stock());
        }
        Files.write(path, lineas);
    }
}
