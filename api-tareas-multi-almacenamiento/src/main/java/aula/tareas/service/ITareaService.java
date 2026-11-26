package aula.tareas.service;

import aula.tareas.api.TareaRequest;
import aula.tareas.modelo.Tarea;
import java.util.List;
import java.util.Optional;

public interface ITareaService {

    /**
     * Metodo que lista todas las tareas
     * @return Lista de tareas
     */
    List<Tarea> listar();

    Optional<Tarea> buscar(long id);

    Tarea crear(TareaRequest request);

    Optional<Tarea> actualizar(long id, TareaRequest request);

    boolean eliminar(long id);
}
