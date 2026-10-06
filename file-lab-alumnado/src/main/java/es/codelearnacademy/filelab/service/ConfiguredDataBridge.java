package es.codelearnacademy.filelab.service;

import es.codelearnacademy.filelab.config.PropertiesConfig;

import java.nio.file.Path;

public class ConfiguredDataBridge {

    private final PropertiesConfig config;
    private final DataBridgeService bridge;

    public ConfiguredDataBridge(PropertiesConfig config, DataBridgeService bridge) {
        this.config = config;
        this.bridge = bridge;
    }

    public int execute() {
        try {
            String origenFormato = config.get("input.format").orElse(null);
            String origenArchivo = config.get("input.file").orElse(null);
            String destinoFormato = config.get("output.format").orElse(null);
            String destinoArchivo = config.get("output.file").orElse(null);

            if (origenFormato == null || origenArchivo == null
                    || destinoFormato == null || destinoArchivo == null) {
                return 0;
            }

            FileFormat formatoOrigen = FileFormat.from(origenFormato);
            FileFormat formatoDestino = FileFormat.from(destinoFormato);

            Path pathOrigen = Path.of(origenArchivo);
            Path pathDestino = Path.of(destinoArchivo);

            return bridge.convert(
                    formatoOrigen,
                    pathOrigen,
                    formatoDestino,
                    pathDestino
            );

        } catch (Exception e) {
            return 0;
        }
    }
}
