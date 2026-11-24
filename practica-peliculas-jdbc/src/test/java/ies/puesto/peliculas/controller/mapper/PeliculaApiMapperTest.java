package ies.puesto.peliculas.controller.mapper;

import ies.puesto.peliculas.controller.api.PeliculaEdicionRequest;
import ies.puesto.peliculas.controller.api.PeliculaRequest;
import ies.puesto.peliculas.controller.api.PeliculaResponse;
import ies.puesto.peliculas.model.Pelicula;
import ies.puesto.peliculas.service.modelo.DatosCreacionPelicula;
import ies.puesto.peliculas.service.modelo.DatosEdicionPelicula;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PeliculaApiMapperTest {

    private final PeliculaApiMapper mapper = new PeliculaApiMapper();

    @Test
    void conviertePeticionEnDatosDeCreacion() {
        PeliculaRequest peticion =
                new PeliculaRequest("PEL003", "La princesa Mononoke", 1997, 134);

        DatosCreacionPelicula datos = mapper.aDatosCreacion(peticion);

        assertEquals("PEL003", datos.referenciaSolicitada());
        assertEquals("La princesa Mononoke", datos.nombreSolicitado());
        assertEquals(1997, datos.anioSolicitado());
        assertEquals(134, datos.minutosSolicitados());
    }

    @Test
    void conviertePeticionEnDatosDeEdicion() {
        PeliculaEdicionRequest peticion =
                new PeliculaEdicionRequest("La princesa Mononoke", 1997, 134);

        DatosEdicionPelicula datos = mapper.aDatosEdicion(peticion);

        assertEquals("La princesa Mononoke", datos.nombreSolicitado());
        assertEquals(1997, datos.anioSolicitado());
        assertEquals(134, datos.minutosSolicitados());
    }

    @Test
    void conviertePeliculaEnRespuesta() {
        Pelicula pelicula =
                new Pelicula("PEL003", "La princesa Mononoke", 1997, 134);

        PeliculaResponse respuesta = mapper.aRespuesta(pelicula);

        assertEquals("PEL003", respuesta.codigo());
        assertEquals("La princesa Mononoke", respuesta.titulo());
        assertEquals(1997, respuesta.anioEstreno());
        assertEquals(134, respuesta.duracionMinutos());
    }

    @Test
    void convierteListaDePeliculasEnRespuestas() {
        List<Pelicula> peliculas = List.of(
                new Pelicula("PEL001", "El viaje de Chihiro", 2001, 125),
                new Pelicula("PEL002", "Mi vecino Totoro", 1988, 86)
        );

        List<PeliculaResponse> respuestas = mapper.aRespuestas(peliculas);

        assertEquals(2, respuestas.size());
        assertEquals("PEL001", respuestas.get(0).codigo());
        assertEquals("El viaje de Chihiro", respuestas.get(0).titulo());
        assertEquals(2001, respuestas.get(0).anioEstreno());
        assertEquals(125, respuestas.get(0).duracionMinutos());
        assertEquals("PEL002", respuestas.get(1).codigo());
        assertEquals("Mi vecino Totoro", respuestas.get(1).titulo());
        assertEquals(1988, respuestas.get(1).anioEstreno());
        assertEquals(86, respuestas.get(1).duracionMinutos());
    }
}
