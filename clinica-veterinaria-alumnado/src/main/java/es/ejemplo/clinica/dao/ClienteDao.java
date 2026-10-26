package es.ejemplo.clinica.dao;

import es.ejemplo.clinica.modelo.Cliente;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public final class ClienteDao extends AbstractDao<Cliente> implements IClienteDao {

    private String url;
    public ClienteDao(String url) {
        super(url);
        this.url = url;

    }

    @Override
    protected Optional<Cliente> mapToClass(ResultSet rs) {
        try {
            return Optional.of(new Cliente(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("telefono")));
        } catch (SQLException e) {
            throw new DaoException("MapToClass ha fallado", e);
        }
    }

    @Override
    public List<Cliente> listar() {
        String sql = "SELECT id, nombre, email, telefono FROM clientes ORDER BY id ASC";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            return consultar(ps);
        } catch (SQLException e) {
            throw new DaoException("Listar CLientes ha fallado", e);
        }
    }

    @Override
    public Optional<Cliente> buscarPorId(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        String sql = "SELECT id, nombre, email, telefono FROM clientes WHERE id = ?";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            return consultarUno(ps);
        } catch (SQLException e) {
            throw new DaoException("Buscar cliente por su id ha fallado", e);
        }
    }

    @Override
    public int insertar(Cliente entidad) {
        String sql = "INSERT INTO clientes (nombre, email, telefono) VALUES (?,?,?)";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getEmail());
            ps.setString(3, entidad.getTelefono());

            int filas = ps.executeUpdate();

            if (filas == 0) {
                return 0;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }

        } catch (SQLException e) {
            throw new DaoException("Insertar clientes ha fallado", e);
        }
    }

    @Override
    public int actualizar(Cliente entidad) {
        if (entidad == null || entidad.getId() == null) {
            throw new IllegalArgumentException();
        }
        String sql = "UPDATE clientes SET nombre = ?, email = ?, telefono = ? WHERE id = ?";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getEmail());
            ps.setString(3, entidad.getTelefono());
            ps.setInt(4, entidad.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Actualizar clientes ha fallado", e);
        }
    }

    @Override
    public int eliminar(Integer id) {
        if (id == null) {
            throw new NullPointerException();
        }
        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Eliminar clientes ha fallado", e);
        }
    }

}
