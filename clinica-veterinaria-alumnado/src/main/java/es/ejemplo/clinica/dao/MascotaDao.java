package es.ejemplo.clinica.dao;

import es.ejemplo.clinica.modelo.Mascota;

import javax.swing.text.html.HTMLDocument;
import java.sql.*;
import java.util.*;

public final class MascotaDao extends AbstractDao<Mascota> implements IMascotaDao {

    private String url;

    public MascotaDao(String url) {
        super(url);
        this.url = url;

    }

    @Override
    protected Optional<Mascota> mapToClass(ResultSet rs) {
        try {
            return Optional.of(new Mascota(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("especie"),
                    rs.getString("raza"),
                    rs.getString("fecha_nacimiento"),
                    rs.getInt("id_cliente")
            ));
        } catch (SQLException e) {
            throw new DaoException("MapToClass mascotas ha fallado", e);
        }
    }

    @Override
    public List<Mascota> listar() {
        String sql = "SELECT id, nombre, especie, raza, fecha_nacimiento, id_cliente FROM mascotas";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            return consultar(ps);
        } catch (SQLException e) {
            throw new DaoException("Listar mascotas ha fallado", e);
        }
    }

    @Override
    public Optional<Mascota> buscarPorId(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        String sql = "SELECT id, nombre, especie, raza, fecha_nacimiento, id_cliente FROM mascotas WHERE id = ?";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            return consultarUno(ps);
        } catch (SQLException e) {
            throw new DaoException("Buscar mascota por su id ha fallado", e);
        }
    }

    @Override
    public int insertar(Mascota entidad) {
        if (entidad == null) {
            return 0;
        }
        String sql = "INSERT INTO mascotas (nombre, especie, raza, fecha_nacimiento, id_cliente) VALUES (?,?,?,?,?)";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getEspecie());
            ps.setString(3, entidad.getRaza());
            ps.setString(4, entidad.getFechaNacimiento());
            ps.setInt(5, entidad.getIdCliente());

            int filas = ps.executeUpdate();

            if (filas == 0) {
                return 0;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }

        } catch (SQLException e) {
            throw new DaoException("Insertar mascotas ha fallado", e);
        }
    }

    @Override
    public int actualizar(Mascota entidad) {
        if (entidad == null) {
            return 0;
        }
        String sql = "UPDATE mascotas SET nombre = ?, especie = ?, raza = ?, fecha_nacimiento = ?, id_cliente = ? WHERE id = ?";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getEspecie());
            ps.setString(3, entidad.getRaza());
            ps.setString(4, entidad.getFechaNacimiento());
            ps.setInt(5, entidad.getIdCliente());
            ps.setInt(6, entidad.getId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Actualizar mascotas ha fallado", e);
        }
    }

    @Override
    public int eliminar(Integer id) {
        if (id == null) {
            return 0;
        }
        String sql = "DELETE FROM mascotas WHERE id = ?";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Eliminar mascota ha fallado", e);
        }
    }

    @Override
    public List<Mascota> buscarPorCliente(Integer idCliente) {
        if (idCliente == null) {
            return List.of();
        }
        String sql = "SELECT id, nombre, especie, raza, fecha_nacimiento, id_cliente FROM mascotas WHERE id_cliente = ?";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, idCliente);
            return consultar(ps);
        } catch (SQLException e) {
            throw new DaoException("Buscar mascota por el id del cliente ha fallado", e);
        }
    }

    @Override
    public List<Mascota> listarPorEspecie(String especie) {
        if (especie == null || especie.isBlank()) {
            return List.of();
        }
        String sql = "SELECT id, nombre, especie, raza, fecha_nacimiento, id_cliente FROM mascotas WHERE especie = ?";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, especie);
            return consultar(ps);
        } catch (SQLException e) {
            throw new DaoException("Listar mascota por su especie ha fallado", e);
        }
    }
}
