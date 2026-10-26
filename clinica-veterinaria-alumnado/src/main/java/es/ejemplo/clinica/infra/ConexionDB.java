package es.ejemplo.clinica.infra;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static javax.swing.DropMode.ON;

public class ConexionDB {
    private final String url;

    public ConexionDB(String url) {
        this.url = url;
    }

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
}
