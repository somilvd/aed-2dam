package ies.puesto.peliculas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:sqlite:target/peliculas-test.sqlite3",
        "spring.datasource.driver-class-name=org.sqlite.JDBC",
        "spring.sql.init.mode=always",
        "spring.sql.init.schema-locations=classpath:schema-test.sql"
})
@AutoConfigureMockMvc
@ActiveProfiles("jdbc")
class PeliculaApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void prepararDatos() {
        jdbcTemplate.update("DELETE FROM peliculas");

        jdbcTemplate.update(
                "INSERT INTO peliculas VALUES (?, ?, ?, ?)",
                "PEL001", "El viaje de Chihiro", 2001, 125);

        jdbcTemplate.update(
                "INSERT INTO peliculas VALUES (?, ?, ?, ?)",
                "PEL002", "Mi vecino Totoro", 1988, 86);
    }

    @Test
    void listarDevuelve200() throws Exception {
        mockMvc.perform(get("/api/v1/jdbc/peliculas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void obtenerExistenteDevuelve200() throws Exception {
        mockMvc.perform(get("/api/v1/jdbc/peliculas/PEL001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("PEL001"));
    }

    @Test
    void obtenerInexistenteDevuelve404() throws Exception {
        mockMvc.perform(get("/api/v1/jdbc/peliculas/PEL999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value(
                        "No se encontró la película con código PEL999"));
    }

    @Test
    void crearNuevaDevuelve201() throws Exception {
        mockMvc.perform(post("/api/v1/jdbc/peliculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"codigo":"PEL003","titulo":"La princesa Mononoke",
                                "anioEstreno":1997,"duracionMinutos":134}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigo").value("PEL003"));
    }

    @Test
    void crearDuplicadaDevuelve409() throws Exception {
        mockMvc.perform(post("/api/v1/jdbc/peliculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"codigo":"PEL001","titulo":"Duplicada",
                                "anioEstreno":2020,"duracionMinutos":100}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value(
                        "Ya existe una película con código PEL001"));
    }

    @Test
    void actualizarExistenteDevuelve200() throws Exception {
        mockMvc.perform(put("/api/v1/jdbc/peliculas/PEL001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo":"Chihiro actualizada",
                                "anioEstreno":2001,"duracionMinutos":130}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Chihiro actualizada"));
    }

    @Test
    void actualizarInexistenteDevuelve404() throws Exception {
        mockMvc.perform(put("/api/v1/jdbc/peliculas/PEL999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo":"Inexistente",
                                "anioEstreno":2001,"duracionMinutos":100}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarExistenteDevuelve204() throws Exception {
        mockMvc.perform(delete("/api/v1/jdbc/peliculas/PEL002"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarInexistenteDevuelve404() throws Exception {
        mockMvc.perform(delete("/api/v1/jdbc/peliculas/PEL999"))
                .andExpect(status().isNotFound());
    }
}
