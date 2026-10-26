package es.ejemplo.clinica.dao;

import es.ejemplo.clinica.infra.ConexionDB;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T> extends ConexionDB implements IAbstractDao<T> {


    public AbstractDao(String url) {
        super(url);
    }

    protected abstract Optional<T> mapToClass(ResultSet rs);

    @Override
    public List<T> consultar(PreparedStatement ps) {
        if (ps == null) {
            return List.of();
        }
        try (ResultSet rs = ps.executeQuery()) {
            List<T> resultado = new ArrayList<>();
            while (rs.next()) {
                Optional<T> entidad = mapToClass(rs);
                if (entidad.isEmpty()) {
                    return List.of();
                }
                resultado.add(entidad.get());
            }
            return resultado;

        } catch (SQLException e) {
            throw new DaoException("Consultar ha fallado",e);
        }
    }

    @Override
    public Optional<T> consultarUno(PreparedStatement ps) {
        if (ps == null) {
            return Optional.empty();
        }
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next() ? mapToClass(rs) : Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Consultar Uno ha fallado", e);
        }
    }

    @Override
    public int consultarEntero(PreparedStatement ps) {
        if (ps == null) {
            return 0;
        }
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DaoException("Consultar Entero ha fallado", e);
        }
    }

    @Override
    public BigDecimal consultarBigDecimal(PreparedStatement ps) {
        if (ps == null) {
            return BigDecimal.ZERO;
        }
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        } catch (SQLException e) {
            throw new DaoException("Consultar Entero ha fallado", e);
        }
    }
}
