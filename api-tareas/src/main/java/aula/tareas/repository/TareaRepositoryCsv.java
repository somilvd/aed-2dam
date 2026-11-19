package aula.tareas.repository;

import aula.tareas.api.TareaRequest;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Repository;

import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Repository("repositoryCsv")
public class TareaRepositoryCsv implements ITareaRepository {
    private Path path;
    private final CSVFormat inputFormat = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true).build();
    private final CSVFormat outputFormat = CSVFormat.DEFAULT.builder()
            .setHeader("id", "titulo")
            .build();

    public TareaRepositoryCsv() {
        path = Path.of("data", "tarea.csv");
    }

    @Override
    public TareaRequest create(String titulo) {
        List<TareaRequest> items = findAll();

        long id = items.stream()
                .mapToLong(TareaRequest::id)
                .max()
                .orElse(0) + 1;

        TareaRequest tareaRequest = new TareaRequest(id, titulo);

        items.add(tareaRequest);
        saveAll(items);

        return tareaRequest;
    }

    @Override
    public boolean delete(long identificador) {
        List<TareaRequest> items = findAll();
        boolean removed = items.removeIf(t -> t.id() == identificador);
        if (removed) {
            saveAll(items);
        }
        return removed;
    }

    @Override
    public List<TareaRequest> findAll() {
        if (Files.notExists(path)) {
            return new ArrayList<>();
        }
        try {
            List<TareaRequest> result = new ArrayList<>();
            try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                 CSVParser parser = inputFormat.parse(reader)) {
                for (CSVRecord row : parser) {
                    result.add(new TareaRequest(
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
    public TareaRequest findById(long identificador) {
        if (identificador < 0) {
            return null;
        }
        return findAll().stream().filter(t -> t.id() == identificador).findFirst().orElse(null);
    }

    @Override
    public TareaRequest update(TareaRequest tareaRequest) {
        if (tareaRequest == null) {
            return null;
        }
        List<TareaRequest> items = findAll();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).id() == tareaRequest.id()) {
                items.set(i, tareaRequest);
                saveAll(items);
            }
        }
        return null;
    }

    private void saveAll(List<TareaRequest> items) {
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
                 CSVPrinter printer = new CSVPrinter(writer, outputFormat)) {
                for (TareaRequest t : items) {
                    printer.printRecord(t.id(), t.titulo());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
