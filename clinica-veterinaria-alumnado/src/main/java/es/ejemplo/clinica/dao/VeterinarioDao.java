package es.ejemplo.clinica.dao;

import es.ejemplo.clinica.modelo.Veterinario;

import java.sql.*;
import java.util.*;

public final class VeterinarioDao extends AbstractDao<Veterinario> implements IVeterinarioDao {

    private String url;
    public VeterinarioDao(String url) {
        super(url);
        this.url = url;

    }

    @Override
    protected Optional<Veterinario> mapToClass(ResultSet rs) {
        try {
            return Optional.of(new Veterinario(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("especialidad"),
                    rs.getBigDecimal("tarifa_hora")
            ));
        } catch (SQLException e) {
            throw new DaoException("MapToClass veterinarios ha fallado", e);
        }
    }

    @Override
    public List<Veterinario> listar() {
        String sql = "SELECT id, nombre, especialidad, tarifa_hora FROM veterinarios";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            return consultar(ps);
        } catch (SQLException e) {
            throw new DaoException("Listar veterinarios ha fallado", e);
        }
    }

    @Override
    public Optional<Veterinario> buscarPorId(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        String sql = "SELECT id, nombre, especialidad, tarifa_hora FROM veterinarios WHERE id = ?";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            return consultarUno(ps);
        } catch (SQLException e) {
            throw new DaoException("Buscar veterinario por su id ha fallado", e);
        }
    }

    @Override
    public int insertar(Veterinario entidad) {
        if (entidad == null) {
            return 0;
        }
        String sql = "INSERT INTO veterinarios (nombre, especialidad, tarifa_hora) VALUES (?,?,?)";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getEspecialidad());
            ps.setBigDecimal(3, entidad.getTarifaHora());

            int filas = ps.executeUpdate();

            if (filas == 0) {
                return 0;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new DaoException("Insertar veterinarios ha fallado", e);
        }
    }

    @Override
    public int actualizar(Veterinario entidad) {
        if (entidad == null || entidad.getId() == null) {
            return 0;
        }
        String sql = "UPDATE veterinarios SET nombre = ?, especialidad = ?, tarifa_hora = ? WHERE id = ?";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getEspecialidad());
            ps.setBigDecimal(3, entidad.getTarifaHora());
            ps.setInt(4, entidad.getId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Actualizar veterinarios ha fallado", e);
        }
    }

    @Override
    public int eliminar(Integer id) {
        if (id == null) {
            return 0;
        }
        String sql = "DELETE FROM veterinarios WHERE id = ?";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Eliminar veterinarios por su id ha fallado", e);
        }
    }

    @Override
    public List<Veterinario> buscarPorEspecialidad(String especialidad) {
        if (especialidad == null) {
            return List.of();
        }
        String sql = "SELECT id, nombre, especialidad, tarifa_hora FROM veterinarios WHERE especialidad = ? ORDER BY tarifa_hora ASC";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, especialidad);
            return consultar(ps);
        } catch (SQLException e) {
            throw new DaoException("Buscar veterinario por su id ha fallado", e);
        }
    }
}

 