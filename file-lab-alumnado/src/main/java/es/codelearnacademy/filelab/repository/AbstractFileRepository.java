package es.codelearnacademy.filelab.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractFileRepository<T, ID> implements IRepository<T, ID> {

    @Override
    public List<T> findAll() {
        try {
            return readAll();
        } catch (IOException e) {
            return List.of();
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        try {
            for (T entidad : readAll()){
                if (getId(entidad).equals(id)) {
                    return Optional.of(entidad);
                }
            }
            return Optional.empty();
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean create(T entity) {
        if (entity == null) {
            return false;
        }
        try {
            List<T> entidades = new ArrayList<>(readAll());
            for (T entidad : entidades) {
                if (getId(entidad).equals(getId(entity))) {
                    return false;
                }
            }
            entidades.add(entity);
            writeAll(entidades);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean update(T entity) {
        if (entity == null || getId(entity) == null) {
            return false;
        }
        try {
            List<T> entidades = readAll();
            for (int i = 0; i < entidades.size(); i++) {
                if (getId(entidades.get(i)).equals(getId(entity))) {
                    entidades.set(i, entity);
                    writeAll(entidades);
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean delete(ID id) {
        if (id == null) {
            return false;
        }
        try {
            List<T> entidades = new ArrayList<>(readAll());

            for (int i = 0; i < entidades.size(); i++) {
                T entidad = entidades.get(i);

                if (getId(entidades.get(i)).equals(id)) {
                    entidades.remove(i);
                    writeAll(entidades);
                    return true;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;

    }

    protected abstract ID getId(T entity);

    protected abstract List<T> readAll() throws IOException;

    protected abstract void writeAll(List<T> entities) throws IOException;
}
