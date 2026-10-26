package es.ejemplo.clinica.dao;

import es.ejemplo.clinica.modelo.Consulta;

import java.sql.*;
import java.util.*;


public final class ConsultaDao extends AbstractDao<Consulta> implements IConsultaDao {

    private String url;

    public ConsultaDao(String url) {
        super(url);
        this.url = url;

    }

    @Override
    protected Optional<Consulta> mapToClass(ResultSet rs) {
        try {
            return Optional.of(new Consulta(
                    rs.getInt("id"),
                    rs.getInt("id_mascota"),
                    rs.getInt("id_veterinario"),
                    rs.getString("fecha"),
                    rs.getBigDecimal("horas"),
                    rs.getString("estado")
            ));
        } catch (SQLException e) {
            throw new DaoException("MapToClass consultas ha fallado", e);
        }
    }

    @Override
    public List<Consulta> listar() {
        String sql = "SELECT id, id_mascota, id_veterinario, fecha, horas, estado FROM consultas";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            return consultar(ps);
        } catch (SQLException e) {
            throw new DaoException("Listar consultas ha fallado", e);
        }
    }

    @Override
    public Optional<Consulta> buscarPorId(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        String sql = "SELECT id, id_mascota, id_veterinario, fecha, horas, estado FROM consultas WHERE id = ?";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            return consultarUno(ps);
        } catch (SQLException e) {
            throw new DaoException("Buscar consultas por su id ha fallado", e);
        }
    }

    @Override
    public int insertar(Consulta entidad) {
        if (entidad == null) {
            return 0;
        }
        String sql = "INSERT INTO consultas (id_mascota, id_veterinario, fecha, horas, estado) VALUES (?,?,?,?,?)";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, entidad.getIdMascota());
            ps.setInt(2, entidad.getIdVeterinario());
            ps.setString(3, entidad.getFecha());
            ps.setBigDecimal(4, entidad.getHoras());
            ps.setString(5, entidad.getEstado());

            int filas = ps.executeUpdate();

            if (filas == 0) {
                return 0;
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new DaoException("Insertar consultas ha fallado", e);
        }
    }

    @Override
    public int actualizar(Consulta entidad) {
        if (entidad == null) {
            return 0;
        }
        String sql = "UPDATE consultas SET id_mascota = ?, id_veterinario = ?, fecha = ?, horas = ?, estado = ? WHERE id = ?";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, entidad.getIdMascota());
            ps.setInt(2, entidad.getIdVeterinario());
            ps.setString(3, entidad.getFecha());
            ps.setBigDecimal(4, entidad.getHoras());
            ps.setString(5, entidad.getEstado());
            ps.setInt(6, entidad.getId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Insertar consultas ha fallado", e);
        }
    }

    @Override
    public int eliminar(Integer id) {
        if (id == null) {
            return 0;
        }
        String sql = "DELETE FROM consultas WHERE id = ?";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Eliminar consultas ha fallado", e);
        }
    }

    @Override
    public List<Consulta> buscarPorMascota(Integer idMascota) {
        if (idMascota == null) {
            return List.of();
        }
        String sql = "SELECT id, id_mascota, id_veterinario, fecha, horas, estado FROM consultas WHERE id_mascota = ?";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, idMascota);
            return consultar(ps);
        } catch (SQLException e) {
            throw new DaoException("Buscar consultas por su id ha fallado", e);
        }
    }

    @Override
    public List<Consulta> buscarPorVeterinario(Integer idVeterinario) {
        if (idVeterinario == null) {
            return List.of();
        }
        String sql = "SELECT id, id_mascota, id_veterinario, fecha, horas, estado FROM consultas WHERE id_veterinario = ?";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, idVeterinario);
            return consultar(ps);
        } catch (SQLException e) {
            throw new DaoException("Buscar consultas por su id ha fallado", e);
        }
    }

    @Override
    public List<Consulta> listarProgramadas() {
        String sql = "SELECT id, id_mascota, id_veterinario, fecha, horas, estado FROM consultas WHERE estado LIKE 'PROGRAMADA'";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            return consultar(ps);
        } catch (SQLException e) {
            throw new DaoException("Buscar consultas por su id ha fallado", e);
        }
    }

}
