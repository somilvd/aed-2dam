package es.codelearnacademy.filelab.repository;

import java.util.List;
import java.util.Optional;

public interface IRepository<T, ID> {

    /**
     * Funcion que busca todo
     * @return lista
     */
    List<T> findAll();

    /**
     * Funcion que busca una entidad por su identificador
     * @param id identificador de la entidad
     * @return lista
     */
    Optional<T> findById(ID id);

    /**
     * Funcion que crea una entidad
     * @param entity entidad
     * @return true/false
     */
    boolean create(T entity);

    /**
     * Funcion que actualiza una entidad
     * @param entity entidad
     * @return true/false
     */
    boolean update(T entity);

    /**
     * Funcion que elimina una entidad por su identificador
     * @param id identificador de la entidad
     * @return true/false
     */
    boolean delete(ID id);
}
