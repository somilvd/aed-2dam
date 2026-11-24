package ies.puesto.peliculas.repository.jdbc;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PeliculaRowMapperTest {

    private final PeliculaRowMapper mapper = new PeliculaRowMapper();

    @Test
    void convierteFilaSqlEnEntity() throws SQLException {
        ResultSet rs = mock(ResultSet.class);

        when(rs.getString("pelicula_codigo")).thenReturn("PEL003");
        when(rs.getString("titulo_texto")).thenReturn("La princesa Mononoke");
        when(rs.getInt("estreno_anio")).thenReturn(1997);
        when(rs.getInt("duracion_minutos")).thenReturn(134);

        PeliculaEntity entidad = mapper.mapRow(rs, 0);

        assertEquals("PEL003", entidad.getCodigoFila());
        assertEquals("La princesa Mononoke", entidad.getTituloFila());
        assertEquals(1997, entidad.getAnioFila());
        assertEquals(134, entidad.getMinutosFila());
    }
}