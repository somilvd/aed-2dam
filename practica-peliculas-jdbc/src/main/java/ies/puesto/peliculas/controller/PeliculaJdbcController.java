package ies.puesto.peliculas.controller;

import ies.puesto.peliculas.controller.api.PeliculaEdicionRequest;
import ies.puesto.peliculas.controller.api.PeliculaRequest;
import ies.puesto.peliculas.controller.api.PeliculaResponse;
import ies.puesto.peliculas.controller.mapper.PeliculaApiMapper;
import ies.puesto.peliculas.model.Pelicula;
import ies.puesto.peliculas.service.PeliculaService;
import ies.puesto.peliculas.service.modelo.DatosCreacionPelicula;
import ies.puesto.peliculas.service.modelo.DatosEdicionPelicula;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.Optional;

@RestController
@Profile("jdbc")
@RequestMapping("/api/v1/jdbc/peliculas")
public class PeliculaJdbcController {

    private final PeliculaService peliculaService;
    private final PeliculaApiMapper peliculaApiMapper;

    public PeliculaJdbcController(
            PeliculaService peliculaService,
            PeliculaApiMapper peliculaApiMapper) {
        this.peliculaService = peliculaService;
        this.peliculaApiMapper = peliculaApiMapper;
    }

    @Operation(summary = "Listar películas", description = "Obtiene todas las películas registradas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PeliculaResponse.class))))
    })
    @GetMapping
    public List<PeliculaResponse> listar() {
        return peliculaApiMapper.aRespuestas(peliculaService.listar());
    }

    @Operation(summary = "Obtener película",
            description = "Busca una película mediante su código.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Película encontrada", content = @Content(schema = @Schema(implementation = PeliculaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Película no encontrada", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Código no válido")
    })
    @GetMapping("/{codigo}")
    public ResponseEntity<?> obtener(
            @PathVariable @Pattern(regexp = "^PEL[0-9]{3}$") String codigo) {

        return peliculaService.obtener(codigo)
                .<ResponseEntity<?>>map(pelicula ->
                        ResponseEntity.ok(peliculaApiMapper.aRespuesta(pelicula)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                                "No se encontró la película con código " + codigo)));
    }

    @Operation(summary = "Crear película", description = "Registra una nueva película.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Datos de la película que se va a crear",
            content = @Content(schema = @Schema(implementation = PeliculaRequest.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Película creada", content = @Content(schema = @Schema(implementation = PeliculaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos no válidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe una película con ese código", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody PeliculaRequest peticion) {

        DatosCreacionPelicula datos = peliculaApiMapper.aDatosCreacion(peticion);
        Optional<Pelicula> creada = peliculaService.crear(datos);

        return creada.<ResponseEntity<?>>map(pelicula ->
                        ResponseEntity.status(HttpStatus.CREATED)
                                .body(peliculaApiMapper.aRespuesta(pelicula)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                                "Ya existe una película con código " + peticion.codigo())));
    }

    @Operation(summary = "Actualizar película", description = "Actualiza los datos de una película existente.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Nuevos datos de la película",
            content = @Content(schema = @Schema(implementation = PeliculaEdicionRequest.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Película actualizada", content = @Content(schema = @Schema(implementation = PeliculaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos o código no válidos"),
            @ApiResponse(responseCode = "404", description = "Película no encontrada", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable @Pattern(regexp = "^PEL[0-9]{3}$") String codigo, @Valid @RequestBody PeliculaEdicionRequest peticion) {

        DatosEdicionPelicula datos = peliculaApiMapper.aDatosEdicion(peticion);

        return peliculaService.actualizar(codigo, datos)
                .<ResponseEntity<?>>map(pelicula ->
                        ResponseEntity.ok(peliculaApiMapper.aRespuesta(pelicula)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                                "No se encontró la película con código " + codigo)));
    }

    @Operation(summary = "Eliminar película", description = "Elimina una película mediante su código.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Película eliminada"),
            @ApiResponse(responseCode = "404", description = "Película no encontrada",content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Código no válido")
    })
    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> eliminar(@PathVariable @Pattern(regexp = "^PEL[0-9]{3}$") String codigo) {

        boolean eliminada = peliculaService.eliminar(codigo);

        if (eliminada) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                        "No se encontró la película con código " + codigo));
    }
}
