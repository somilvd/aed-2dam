package aula.tareas.repository;

import aula.tareas.api.TareaRequest;
import aula.tareas.modelo.Tarea;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("tareaRepositoryJdbc")
public class TareaRepositoryJdbc implements ITareaRepository {
    private final JdbcTemplate jdbc;

    public TareaRepositoryJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Tarea> findAll() {
        String sql = "SELECT id, titulo FROM tareas_jdbc ORDER BY id";

        return jdbc.query(sql, (rs, rowNum) -> new Tarea(rs.getLong("id"), rs.getString("titulo")));
    }

    @Override
    public Optional<Tarea> findById(long id) {
        String sql = "SELECT id, titulo FROM tareas_jdbc WHERE id = ?";

        return jdbc.query(sql, (rs, rowNum) -> new Tarea(rs.getLong("id"), rs.getString("titulo")), id)
                .stream().findFirst();
    }

    @Override
    public Tarea create(Tarea tarea) {
        String sql = "INSERT INTO tareas_jdbc (id, titulo) VALUES (?,?)";

        jdbc.update(sql, tarea.identificador(), tarea.nombre());

        return tarea;

    }

    @Override
    public Optional<Tarea> update(Tarea tarea) {
        String sql = "UPDATE tareas_jdbc SET titulo = ? WHERE id = ?";

        int filasActualizar = jdbc.update(sql, tarea.nombre(), tarea.identificador());

        if (filasActualizar > 0) {
            return Optional.of(tarea);
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(long id) {
        String sql = "DELETE FROM tareas_jdbc WHERE id = ?";

        int filasEliminar = jdbc.update(sql, id);

        if (filasEliminar > 0) {
            return true;
        }
        return false;
    }
}
