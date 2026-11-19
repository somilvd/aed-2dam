package aula.tareas;

import aula.tareas.api.TareaRequest;

import java.util.ArrayList;
import java.util.List;

public record DocumentoTareas(List<TareaRequest> tareaRequests) {

    public DocumentoTareas(List<TareaRequest> tareaRequests) {
        this.tareaRequests = new ArrayList<>(tareaRequests);
    }
}