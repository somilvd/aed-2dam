package aula.tareas.repository;

import aula.tareas.entity.TareaEntityXml;
import aula.tareas.modelo.Tarea;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.annotation.JsonRootName;
import tools.jackson.dataformat.xml.XmlMapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Repository
@Qualifier("tareaRepositoryXml")
public class TareaRepositoryXml extends RepositorioTareaFichero {
    private final Path path;
    private final XmlMapper mapper = XmlMapper.builder()
            .defaultUseWrapper(false)
            .build();

    public TareaRepositoryXml() {
        this(Path.of("data", "tarea.xml"));
    }

    public TareaRepositoryXml(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        this.path = path;
    }

    @JsonRootName("tareas")
    public static class DocumentoXml {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "tarea")
        private List<TareaEntityXml> tareas;

        public DocumentoXml() {}

        public DocumentoXml(List<TareaEntityXml> tareas) {
            this.tareas = tareas;
        }

        public List<TareaEntityXml> getTareas() {
            return tareas;
        }

        public void setTareas(List<TareaEntityXml> tareas) {
            this.tareas = tareas;
        }
    }

    @Override
    List<Tarea> readAll() {
        try {
            DocumentoXml documento = mapper.readValue(path.toFile(), DocumentoXml.class);

            return documento.getTareas().stream()
                    .map(entidad -> new Tarea(entidad.getIdTarea(), entidad.getNombreTarea()))
                    .toList();

        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    void writeAll(List<Tarea> tareas) {
        List<TareaEntityXml> entities = tareas.stream()
                .map(tarea -> new TareaEntityXml(tarea.identificador(), tarea.nombre()))
                .toList();

        DocumentoXml documento = new DocumentoXml(entities);
        mapper.writeValue(path.toFile(), documento);
    }
}
