package aula.tareas.repository;

import aula.tareas.modelo.Tarea;
import java.util.List;
import java.util.Optional;

public interface ITareaRepository {

    /**
     * Metodo que busca todas las tareas
     * @return Lista de tareas
     */
    List<Tarea> findAll();

    /**
     * Metodo que busca una tarea por su identificador
     * @param id identificador de la tarea
     * @return Optional de tarea
     */
    Optional<Tarea> findById(long id);

    /**
     * Metodo que crea una tarea
     * @param tarea tarea a crear
     * @return Tarea creada
     */
    Tarea create(Tarea tarea);

    /**
     * Metodo que actualiza una tarea
     * @param tarea tarea a actualizar
     * @return Optional de tarea
     */
    Optional<Tarea> update(Tarea tarea);

    /**
     * Metodo que elimina una tarea por su identificador
     * @param id identificador de la tarea a eliminar
     * @return true si se elimino correctamente / false si no
     */
    boolean delete(long id);
}
