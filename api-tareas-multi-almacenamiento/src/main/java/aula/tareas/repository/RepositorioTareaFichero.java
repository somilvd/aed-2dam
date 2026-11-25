package aula.tareas.repository;

import aula.tareas.modelo.Tarea;

import java.util.List;
import java.util.Optional;

/** CRUD compartido por los formatos de fichero, con lectura del fichero en cada operación. */
abstract class RepositorioTareaFichero implements ITareaRepository {
    abstract List<Tarea> readAll();
    abstract void writeAll(List<Tarea> tareas);

    @Override
    public synchronized List<Tarea> findAll() {
        return readAll();
    }

    @Override
    public synchronized Optional<Tarea> findById(long id) {
        return findAll().stream().filter(tarea -> tarea.identificador() == id).findFirst();
    }

    @Override
    public synchronized Tarea create(Tarea tarea) {
        List<Tarea> tareas = findAll();

        long id = tareas.stream()
                .mapToLong(Tarea::identificador)
                .max()
                .orElse(0) + 1;

        Tarea tareaCreada = new Tarea(id, tarea.nombre());

        tareas.add(tareaCreada);
        writeAll(tareas);

        return tareaCreada;
    }

    @Override
    public synchronized Optional<Tarea> update(Tarea tarea) {
        List<Tarea> tareas = findAll();

        for (int i = 0; i < tareas.size(); i++) {
            if (tareas.get(i).identificador() == tarea.identificador()) {
                tareas.set(i, tarea);
                writeAll(tareas);
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
            writeAll(items);
        }
        return false;
    }
}
