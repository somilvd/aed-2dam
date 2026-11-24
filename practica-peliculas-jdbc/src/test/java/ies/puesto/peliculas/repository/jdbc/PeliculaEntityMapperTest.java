package ies.puesto.peliculas.repository.jdbc;

import ies.puesto.peliculas.model.Pelicula;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PeliculaEntityMapperTest {

    private final PeliculaEntityMapper mapper = new PeliculaEntityMapper();

    @Test
    void conviertePeliculaEnEntity() {
        Pelicula pelicula =
                new Pelicula("PEL003", "La princesa Mononoke", 1997, 134);

        PeliculaEntity entidad = mapper.aEntity(pelicula);

        assertEquals("PEL003", entidad.getCodigoFila());
        assertEquals("La princesa Mononoke", entidad.getTituloFila());
        assertEquals(1997, entidad.getAnioFila());
        assertEquals(134, entidad.getMinutosFila());
    }

    @Test
    void convierteEntityEnPelicula() {
        PeliculaEntity entidad =
                new PeliculaEntity("PEL003", "La princesa Mononoke", 1997, 134);

        Pelicula pelicula = mapper.aPelicula(entidad);

        assertEquals("PEL003", pelicula.getIdentificador());
        assertEquals("La princesa Mononoke", pelicula.getNombre());
        assertEquals(1997, pelicula.getAnioLanzamiento());
        assertEquals(134, pelicula.getMinutosProyeccion());
    }
}