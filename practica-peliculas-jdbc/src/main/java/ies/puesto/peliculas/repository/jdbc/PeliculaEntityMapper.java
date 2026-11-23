package ies.puesto.peliculas.repository.jdbc;

import ies.puesto.peliculas.model.Pelicula;

public class PeliculaEntityMapper {

    PeliculaEntity aEntity(Pelicula pelicula) {
        return new PeliculaEntity(pelicula.getIdentificador(), pelicula.getNombre(), pelicula.getAnioLanzamiento(), pelicula.getAnioLanzamiento());
    }

    Pelicula aPelicula(PeliculaEntity entidad) {
        return new Pelicula(entidad.getCodigoFila(), entidad.getTituloFila(), entidad.getAnioFila(), entidad.getMinutosFila());
    }

}
