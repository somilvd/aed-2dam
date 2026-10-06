package es.codelearnacademy.filelab.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class PropertiesConfig {

    private final Path path;

    public PropertiesConfig(Path path) {
        this.path = path;
    }

    public Optional<String> get(String key) {
        if (key == null || key.isBlank()) {
            return Optional.empty();
        }

        try {
            Properties propiedades = new Properties();

            InputStream entrada = Files.newInputStream(path);
            propiedades.load(entrada);
            entrada.close();

            String valor = propiedades.getProperty(key);

            if (valor == null) {
                return Optional.empty();
            }

            return Optional.of(valor);

        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public String getOrDefault(String key, String defaultValue) {
        Optional<String> valor = get(key);

        if (valor.isPresent()) {
            return valor.get();
        }

        return defaultValue;
    }

    public Map<String, String> findAll() {
        try {
            Properties propiedades = new Properties();
            try (InputStream entrada = Files.newInputStream(path)) {
                propiedades.load(entrada);
            }
            Map<String, String> resultado = new HashMap<>();
            for (String clave : propiedades.stringPropertyNames()) {
                resultado.put(clave, propiedades.getProperty(clave));
            } return resultado;
        } catch (IOException e) {
            return Map.of();
        }
    }

    public boolean put(String key, String value) {
        if (key == null || key.isBlank()) {
            return false;
        }

        Properties propiedades = new Properties();

        try {
            if (Files.exists(path)) {
                InputStream entrada = Files.newInputStream(path);
                propiedades.load(entrada);
                entrada.close();
            }

            propiedades.setProperty(key, value);

            OutputStream salida = Files.newOutputStream(path);
            propiedades.store(salida, null);
            salida.close();

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    public boolean remove(String key) {
        if (key == null || key.isBlank()) {
            return false;
        }

        Properties propiedades = new Properties();

        try {
            if (Files.exists(path)) {
                InputStream entrada = Files.newInputStream(path);
                propiedades.load(entrada);
                entrada.close();
            }

            propiedades.remove(key);

            OutputStream salida = Files.newOutputStream(path);
            propiedades.store(salida, null);
            salida.close();

            return true;

        } catch (IOException e) {
            return false;
        }
    }
}
