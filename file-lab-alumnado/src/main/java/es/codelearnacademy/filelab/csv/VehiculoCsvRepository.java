package es.codelearnacademy.filelab.csv;

import es.codelearnacademy.filelab.model.Producto;
import es.codelearnacademy.filelab.model.Vehiculo;
import es.codelearnacademy.filelab.repository.AbstractFileRepository;
import es.codelearnacademy.filelab.repository.IVehiculoRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class VehiculoCsvRepository
        extends AbstractFileRepository<Vehiculo, String>
        implements IVehiculoRepository {

    private final Path path;

    public VehiculoCsvRepository(Path path) {
        this.path = path;
    }

    @Override
    protected String getId(Vehiculo vehiculo) {
        return vehiculo.matricula();
    }

    @Override
    protected List<Vehiculo> readAll() throws IOException {
        List<Vehiculo> vehiculos = new ArrayList<>();
        if (!Files.exists(path) || Files.size(path) == 0) {
            return vehiculos;
        }

        List<String> lineas = Files.readAllLines(path);

        for (int i = 1; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            String[] datos = linea.split(",");

            String matricula = datos[0].trim();
            String marca = datos[1].trim();
            String modelo = datos[2].trim();
            int anio = Integer.parseInt(datos[3].trim());

            vehiculos.add(new Vehiculo(matricula, marca, modelo, anio));

        }
        return vehiculos;
    }

    @Override
    protected void writeAll(List<Vehiculo> vehiculos) throws IOException {
        List<String> lineas = new ArrayList<>();
        lineas.add("matricula,marca,modelo,anio");

        for (Vehiculo vehiculo : vehiculos) {
            lineas.add(vehiculo.matricula() + "," + vehiculo.marca() + "," + vehiculo.modelo() + "," +vehiculo.anio());
        }
        Files.write(path, lineas);
    }
}
