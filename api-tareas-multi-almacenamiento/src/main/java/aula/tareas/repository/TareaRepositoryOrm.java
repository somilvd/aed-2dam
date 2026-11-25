package aula.tareas.repository;

import aula.tareas.entity.TareaEntityOrm;
import aula.tareas.modelo.Tarea;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("tareaRepositoryOrm")
public class TareaRepositoryOrm implements ITareaRepository {
    private final ITareaRepositoryJpa jpa;

    public TareaRepositoryOrm(ITareaRepositoryJpa jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<Tarea> findAll() {

        return jpa.findAll().stream()
                    .map(entity -> new Tarea(entity.getIdTarea(), entity.getNombreTarea()))
                    .toList();

    }

    @Override
    public Optional<Tarea> findById(long id) {

        return jpa.findById(id).stream().map(entity -> new Tarea(id, entity.getNombreTarea())).findFirst();
    }

    @Override
    public Tarea create(Tarea tarea) {
        TareaEntityOrm entidad = new TareaEntityOrm(
                tarea.identificador(),
                tarea.nombre()
        );

        TareaEntityOrm guardada = jpa.save(entidad);

        return new Tarea(guardada.getIdTarea(), guardada.getNombreTarea());
    }

    @Override
    public Optional<Tarea> update(Tarea tarea) {

        return jpa.findById(tarea.identificador())
                .map(entity -> {
                    entity.setNombreTarea(tarea.nombre());
                    TareaEntityOrm actualizada = jpa.save(entity);
                    return new Tarea(actualizada.getIdTarea(), actualizada.getNombreTarea());
                });

    }

    @Override
    public boolean delete(long id) {
        if (jpa.findById(id).isEmpty()) {
            return false;
        }

        jpa.deleteById(id);
        return true;
    }
}
