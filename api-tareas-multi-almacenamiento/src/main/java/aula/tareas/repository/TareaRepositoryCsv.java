package aula.tareas.repository;

import aula.tareas.api.TareaRequest;
import aula.tareas.modelo.Tarea;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.apache.commons.csv.CSVFormat;

import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("tareaRepositoryCsv")
public class TareaRepositoryCsv implements ITareaRepository {
    private final Path path;

    public TareaRepositoryCsv() {
        this(Path.of("data", "tarea.csv"));
    }

    public TareaRepositoryCsv(Path path) {
        this.path = path;
    }

    private static final CSVFormat ENTRADA = CSVFormat.DEFAULT.builder()
            .setHeader().setSkipHeaderRecord(true).get();
    private static final CSVFormat SALIDA = CSVFormat.DEFAULT.builder()
            .setHeader("id", "titulo").get();

    @Override
    public synchronized List<Tarea> findAll() {
        if (Files.notExists(path)) {
            return new ArrayList<>();
        }
        try {
            List<Tarea> result = new ArrayList<>();
            try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                 CSVParser parser = ENTRADA.parse(reader)) {
                for (CSVRecord row : parser) {
                    result.add(new Tarea(
                            Long.parseLong(row.get("id")),
                            row.get("titulo")));
                }
            }
            return result;
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public synchronized Optional<Tarea> findById(long id) {
        if (id < 0) {
            return null;
        }
        return findAll().stream().filter(t -> t.identificador() == id).findFirst();
    }

    @Override
    public synchronized Tarea create(Tarea tarea) {
        List<Tarea> items = findAll();

        long id = items.stream()
                .mapToLong(Tarea::identificador)
                .max()
                .orElse(0) + 1;

        Tarea tareaCreada = new Tarea(id, tarea.nombre());

        items.add(tareaCreada);
        saveAll(items);

        return tareaCreada;
    }

    @Override
    public synchronized Optional<Tarea> update(Tarea tarea) {
        if (tarea == null) {
            return Optional.empty();
        }
        List<Tarea> items = findAll();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).identificador().equals(tarea.identificador())) {
                items.set(i, tarea);
                saveAll(items);
                return Optional.of(tarea);
            }
        }
        return Optional.empty();
    }

    @Override
    public synchronized boolean delete(long id) {
        List<Tarea> items = findAll();
        boolean removed = items.removeIf(t -> t.identificador() == id);
        if (removed) {
            saveAll(items);
        }
        return false;
    }

    private void saveAll(List<Tarea> tareas) {
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
                 CSVPrinter printer = new CSVPrinter(writer, SALIDA)) {
                for (Tarea t : tareas) {
                    printer.printRecord(t.identificador(), t.nombre());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
