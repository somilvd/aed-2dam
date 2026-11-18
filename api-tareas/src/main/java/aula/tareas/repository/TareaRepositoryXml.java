package aula.tareas.repository;

import aula.tareas.DocumentoTareas;
import aula.tareas.modelo.Tarea;
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
    private List<Tarea> lista;

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
    public Tarea create(String titulo) {
        long id = secuencia.incrementAndGet();
        var tarea = new Tarea(id, titulo.strip());
        lista.add(tarea);
        saveAll(lista);
        return tarea;
    }

    @Override
    public boolean delete(long identificador) {
        List<Tarea> items = findAll();
        boolean removed = items.removeIf(t -> t.id() == identificador);
        if (removed) {
            saveAll(items);
        }
        return removed;
    }

    @Override
    public List<Tarea> findAll() {
        return List.copyOf(lista);
    }

    @Override
    public Tarea findById(long identificador) {
        if (identificador < 0) {
            return null;
        }
        return findAll().stream().filter(p -> p.id() == identificador).findFirst().orElse(null);
    }

    @Override
    public Tarea update(Tarea tarea) {
        if (tarea == null) {
            return null;
        }
        List<Tarea> items = findAll();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).id() == tarea.id()) {
                items.set(i, tarea);
                saveAll(items);
            }
        }
        return null;
    }

    private void saveAll(List<Tarea> items) {
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


    public List<Tarea> getAll() {
        try {
            return new ArrayList<>(mapper.readValue(
                    path.toFile(), new TypeReference<List<Tarea>>() {
                    }));
        } catch (Exception exception) {
            //Logger
            return new ArrayList<>();
        }
    }
}
