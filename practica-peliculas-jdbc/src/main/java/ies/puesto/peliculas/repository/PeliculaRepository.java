package ies.puesto.peliculas.repository;

import ies.puesto.peliculas.model.Pelicula;

import java.util.List;
import java.util.Optional;

public interface PeliculaRepository {

    /**
     * Metodo que busca todas las peliculas
     * @return Lista de peliculas
     */
    List<Pelicula> buscarTodas();

    /**
     * Metodo que busca una pelicula por su codigo
     * @param identificador codigo de la pelicula
     * @return Optional de Pelicula
     */
    Optional<Pelicula> buscarPorCodigo(String identificador);

    /**
     * Metodo que inserta una pelicula si esta ausente
     * @param pelicula pelicula a insertar
     * @return true si se inserto / false si no se inserto
     */
    boolean insertarSiAusente(Pelicula pelicula);

    /**
     * Metodo que actualiza una pelicula existente
     * @param pelicula pelicula que se actualiza
     * @return true si se actualizo / false si no se actualizo
     */
    boolean actualizarSiExiste(Pelicula pelicula);

    /**
     * Metodo que elimina una pelicula por su codigo
     * @param identificador codigo de la pelicula
     * @return true si se elimino / false si no se elimino
     */
    boolean eliminar(String identificador);

}
