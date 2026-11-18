package aula.tareas;

import aula.tareas.modelo.Tarea;

import java.util.ArrayList;
import java.util.List;

public record DocumentoTareas(List<Tarea> tareas) {

    public DocumentoTareas(List<Tarea> tareas) {
        this.tareas = new ArrayList<>(tareas);
    }
}