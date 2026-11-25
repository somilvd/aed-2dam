package aula.tareas.repository;

import aula.tareas.modelo.Tarea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("tareaRepositoryJson")
public class TareaRepositoryJson implements ITareaRepository {
    private final Path path;
    private final JsonMapper mapper;

    @Autowired
    public TareaRepositoryJson(JsonMapper mapper) {
        this(Path.of("data", "tarea.json"), mapper);
    }

    public TareaRepositoryJson(Path path, JsonMapper mapper) {
        this.path = path;
        this.mapper = mapper;
    }

    @Override
    public synchronized List<Tarea> findAll() {
        if (Files.exists(path)) {
            return List.of();
        }

        return mapper.readValue(path.toFile(), new TypeReference<List<Tarea>>() {});
    }

    @Override
    public synchronized Optional<Tarea> findById(long id) {
        List<Tarea> items = findAll();

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).identificador() == id) {
                return Optional.of(items.get(i));
            }
        }

        return Optional.empty();
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
            if (items.get(i).identificador() == tarea.identificador()) {
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
        mapper.writeValue(path.toFile(), tareas);
    }
}
