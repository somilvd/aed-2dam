package ies.puesto.peliculas.service.mapper;

import ies.puesto.peliculas.model.Pelicula;
import ies.puesto.peliculas.service.modelo.DatosCreacionPelicula;
import ies.puesto.peliculas.service.modelo.DatosEdicionPelicula;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PeliculaDominioMapperTest {

    private final PeliculaDominioMapper mapper = new PeliculaDominioMapper();

    @Test
    void convierteDatosDeCreacionEnPelicula() {
        DatosCreacionPelicula datos =
                new DatosCreacionPelicula("PEL003", "La princesa Mononoke", 1997, 134);

        Pelicula pelicula = mapper.aPelicula(datos);

        assertEquals("PEL003", pelicula.getIdentificador());
        assertEquals("La princesa Mononoke", pelicula.getNombre());
        assertEquals(1997, pelicula.getAnioLanzamiento());
        assertEquals(134, pelicula.getMinutosProyeccion());
    }

    @Test
    void convierteDatosDeEdicionEnPelicula() {
        DatosEdicionPelicula datos =
                new DatosEdicionPelicula("La princesa Mononoke", 1997, 134);

        Pelicula pelicula = mapper.aPelicula("PEL003", datos);

        assertEquals("PEL003", pelicula.getIdentificador());
        assertEquals("La princesa Mononoke", pelicula.getNombre());
        assertEquals(1997, pelicula.getAnioLanzamiento());
        assertEquals(134, pelicula.getMinutosProyeccion());
    }
}