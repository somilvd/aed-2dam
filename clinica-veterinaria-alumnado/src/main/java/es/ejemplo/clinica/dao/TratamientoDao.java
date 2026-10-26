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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listar'");
    }

    @Override
    public Optional<Tratamiento> buscarPorId(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorId'");
    }

    @Override
    public int insertar(Tratamiento entidad) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insertar'");
    }

    @Override
    public int actualizar(Tratamiento entidad) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizar'");
    }

    @Override
    public int eliminar(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminar'");
    }

    @Override
    public List<Tratamiento> listarPorConsulta(Integer idConsulta) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listarPorConsulta'");
    }

    @Override
    public BigDecimal calcularCosteTratamientos(Integer idConsulta) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calcularCosteTratamientos'");
    }

}
