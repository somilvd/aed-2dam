package aula.tareas.controller;

import aula.tareas.api.TareaRequest;
import aula.tareas.service.TareaServiceXml;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/xml/tareas")
public class TareaControllerXml {
    private final TareaServiceXml servicio;

    TareaControllerXml(@Qualifier("tareaServiceXml") TareaServiceXml servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    List<TareaRequest> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    ResponseEntity<TareaRequest> buscar(@PathVariable long id) {
        return ResponseEntity.ofNullable(servicio.buscar(id));
    }

    @PostMapping
    ResponseEntity<TareaRequest> crear(@Valid @RequestBody TareaControllerXml.NuevaTarea peticion) {
        TareaRequest tareaRequest = servicio.crear(peticion.titulo());
        return ResponseEntity.created(URI.create("/api/tareas/" + tareaRequest.id())).body(tareaRequest);
    }

    public record NuevaTarea(@NotBlank String titulo) {}
}
