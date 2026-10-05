package es.codelearnacademy.filelab.io;

import java.nio.file.Path;

public class PathService {

    public Path crear(String primero, String partes) {
        return Path.of(primero, partes);
    }

    public String nombre(Path path) {
        return path.getFileName().toString();
    }

    public Path padre(Path path) {
        return path.getParent();
    }

    public Path absoluto(Path path) {
        if (path == null || !path.isAbsolute()) {
            throw new IllegalArgumentException();
        }
        return path;
    }

    public Path normalizar(Path path) {
        return path.normalize();
    }

    public boolean esAbsoluto(Path path) {
        return path.isAbsolute();
    }

    public Path resolver(Path base, String otro) {
        return base.resolve(otro);
    }

    public Path relativizar(Path base, Path destino) {
        return base.relativize(destino);
    }

    public String extension(Path path) {
        String nombre = path.getFileName().toString();
        int punto = nombre.lastIndexOf(".");
        if (punto <= 0 || punto == nombre.length() - 1){
            return "";
        }
        return nombre.substring(punto + 1);
    }
}
