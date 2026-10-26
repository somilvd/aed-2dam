package es.ejemplo.clinica.dao;

import es.ejemplo.clinica.modelo.Tratamiento;

import java.sql.*;
import java.util.*;
import java.math.BigDecimal;

public final class TratamientoDao extends AbstractDao<Tratamiento> implements ITratamientoDao {

    private String url;
    public TratamientoDao(String url) {
        super(url);
        this.url = url;

    }

    @Override
    protected Optional<Tratamiento> mapToClass(ResultSet rs) {
        try {
            return Optional.of(new Tratamiento(
                    rs.getInt("id"),
                    rs.getInt("id_consulta"),
                    rs.getString("descripcion"),
                    rs.getInt("cantidad"),
                    rs.getBigDecimal("precio_unitario")
            ));
        } catch (SQLException e) {
            throw new DaoException("MapToClass tratamientos ha fallado", e);
        }
    }

    @Override
    public List<Tratamiento> listar() {
        String sql = "SELECT id, id_consulta, descripcion, cantidad, precio_unitario FROM tratamientos";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            return consultar(ps);
        } catch (SQLException e) {
            throw new DaoException("Listar tratamientos ha fallado", e);
        }
    }

    @Override
    public Optional<Tratamiento> buscarPorId(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        String sql = "SELECT id, id_consulta, descripcion, cantidad, precio_unitario FROM tratamientos WHERE id = ?";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            return consultarUno(ps);
        } catch (SQLException e) {
            throw new DaoException("Buscar tratamientos por su id ha fallado", e);
        }
    }

    @Override
    public int insertar(Tratamiento entidad) {
        if (entidad == null) {
            return 0;
        }
        String sql = "INSERT INTO tratamientos (id_consulta, descripcion, cantidad, precio_unitario) VALUES (?,?,?,?)";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, entidad.getIdConsulta());
            ps.setString(2, entidad.getDescripcion());
            ps.setInt(3, entidad.getCantidad());
            ps.setBigDecimal(4, entidad.getPrecioUnitario());

            int filas = ps.executeUpdate();

            if (filas == 0) {
                return 0;
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new DaoException("Insertar tratamientos ha fallado", e);
        }
    }

    @Override
    public int actualizar(Tratamiento entidad) {
        if (entidad == null) {
            return 0;
        }
        String sql = "UPDATE tratamientos SET id_consulta = ?, descripcion = ?, cantidad = ?, precio_unitario = ? WHERE id = ?";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, entidad.getIdConsulta());
            ps.setString(2, entidad.getDescripcion());
            ps.setInt(3, entidad.getCantidad());
            ps.setBigDecimal(4, entidad.getPrecioUnitario());
            ps.setInt(5, entidad.getId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Insertar tratamientos ha fallado", e);
        }
    }

    @Override
    public int eliminar(Integer id) {
        if (id == null) {
            return 0;
        }
        String sql = "DELETE FROM tratamientos WHERE id = ?";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Eliminar tratamientos por su id ha fallado", e);
        }
    }

    @Override
    public List<Tratamiento> listarPorConsulta(Integer idConsulta) {
        if (idConsulta == null) {
            return List.of();
        }
        String sql = "SELECT id, id_consulta, descripcion, cantidad, precio_unitario FROM tratamientos WHERE id_consulta = ?";

        try (Connection connection = abrirConexion(url)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, idConsulta);
            return consultar(ps);
        } catch (SQLException e) {
            throw new DaoException("Buscar tratamientos por id de consulta ha fallado", e);
        }
    }

    @Override
    public BigDecimal calcularCosteTratamientos(Integer idConsulta) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calcularCosteTratamientos'");
    }

}
