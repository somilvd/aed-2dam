package es.codelearnacademy.filelab.config;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
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
        try {
            Properties properties = cargar();
            return Optional.ofNullable(properties.getProperty(key));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public String getOrDefault(String key, String defaultValue) {
        return get(key).orElse(defaultValue);
    }

    public Map<String, String> findAll() {
        try {
            Properties properties = cargar();
            Map<String, String> resultado = new HashMap<>();
            for (String clave : properties.stringPropertyNames()) {
                resultado.put(clave, properties.getProperty(clave));
            }
            return resultado;
        } catch (IOException e) {
            return Map.of();
        }
    }

    public boolean put(String key, String value) {
        try {
            Properties properties = cargar();
            properties.setProperty(key, value);
            guardar(properties);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean remove(String key) {
        try {
            Properties properties = cargar();
            properties.remove(key);
            guardar(properties);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private Properties cargar() throws IOException {
        Properties properties = new Properties();
        if (Files.exists(path)) {
            try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                properties.load(reader);
            }
        }
        return properties;
    }

    private void guardar(Properties properties) throws IOException {
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            properties.store(writer, null);
        }
    }
}