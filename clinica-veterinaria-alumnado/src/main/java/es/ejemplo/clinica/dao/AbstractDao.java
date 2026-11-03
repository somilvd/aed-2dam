package es.ejemplo.clinica.dao;

import es.ejemplo.clinica.infra.ConexionDB;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;

public abstract class AbstractDao<T> implements IAbstractDao<T> {


    private final String url;
    public AbstractDao(String url) {
        this.url = url;
    }

    protected abstract Optional<T> mapToClass(ResultSet rs);

    public static Connection abrirConexion(String url) {
        try {
            if (url == null || !url.startsWith("jdbc:sqlite:"))
                throw new IllegalArgumentException("URL SQLite no válida");
            String ruta = url.substring("jdbc:sqlite:".length());
            if (!ruta.equals(":memory:") && !ruta.startsWith("file:") && !Files.isRegularFile(Path.of(ruta)))
                throw new IllegalArgumentException("La base de datos no existe: " + ruta);
            Connection cn = DriverManager.getConnection(url);
            try (Statement st = cn.createStatement()) {
                st.execute("PRAGMA foreign_keys = ON");
            } catch (SQLException e) {
                cn.close();
                throw e;
            }
            return cn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


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

    protected Map<String, Integer> consultarAgrupado(PreparedStatement ps) {
        if (ps == null) return Map.of();
        try (ResultSet rs = ps.executeQuery()) {
            Map<String, Integer> mapa = new LinkedHashMap<>();
            while (rs.next()) mapa.put(rs.getString(1), rs.getInt(2));
            return mapa;
        } catch (SQLException | RuntimeException e) {
            throw new  DaoException("consultarAgrupado", e);
        }
    }

}
