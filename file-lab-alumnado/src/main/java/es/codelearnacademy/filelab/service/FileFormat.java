package es.codelearnacademy.filelab.service;

public enum FileFormat {
    CSV,
    JSON,
    XML;

    public static FileFormat from(String value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        for (FileFormat formato : FileFormat.values()) {
            if (formato.name().equalsIgnoreCase(value)) {
                return formato;
            }
        }
        throw new IllegalArgumentException();
    }
}
