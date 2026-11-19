package aula.tareas.repository;

import aula.tareas.DocumentoTareas;
import aula.tareas.api.TareaRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Repository;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository("repositoryXml")
public class TareaRepositoryXml implements ITareaRepository {


    private final AtomicLong secuencia = new AtomicLong();
    private Path path;
    private XmlMapper mapper;
    private List<TareaRequest> lista;

    public TareaRepositoryXml() {
        this("data/tarea.xml");
        lista = getAll();
    }

    public TareaRepositoryXml(String url) {
        path = Path.of(url);
        mapper = new XmlMapper();
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public TareaRequest create(String titulo) {
        long id = secuencia.incrementAndGet();
        var tarea = new TareaRequest(id, titulo.strip());
        lista.add(tarea);
        saveAll(lista);
        return tarea;
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
        return List.copyOf(lista);
    }

    @Override
    public TareaRequest findById(long identificador) {
        if (identificador < 0) {
            return null;
        }
        return findAll().stream().filter(p -> p.id() == identificador).findFirst().orElse(null);
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
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(path.toFile(), new DocumentoTareas(items));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public List<TareaRequest> getAll() {
        try {
            return new ArrayList<>(mapper.readValue(
                    path.toFile(), new TypeReference<List<TareaRequest>>() {
                    }));
        } catch (Exception exception) {
            //Logger
            return new ArrayList<>();
        }
    }
}
