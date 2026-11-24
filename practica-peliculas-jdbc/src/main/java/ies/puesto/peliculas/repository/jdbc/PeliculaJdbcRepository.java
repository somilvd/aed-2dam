package ies.puesto.peliculas.repository.jdbc;

import ies.puesto.peliculas.model.Pelicula;
import ies.puesto.peliculas.repository.PeliculaRepository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PeliculaJdbcRepository implements PeliculaRepository {

    private final JdbcTemplate jdbcTemplate;
    private final PeliculaRowMapper peliculaRowMapper;
    private final PeliculaEntityMapper peliculaEntityMapper;

    public PeliculaJdbcRepository(
            JdbcTemplate jdbcTemplate,
            PeliculaRowMapper peliculaRowMapper,
            PeliculaEntityMapper peliculaEntityMapper) {

        this.jdbcTemplate = jdbcTemplate;
        this.peliculaRowMapper = peliculaRowMapper;
        this.peliculaEntityMapper = peliculaEntityMapper;
    }


    @Override
    public List<Pelicula> buscarTodas() {
        String sql = "SELECT pelicula_codigo, titulo_texto, estreno_anio, duracion_minutos FROM peliculas ORDER BY pelicula_codigo";

        return jdbcTemplate.query(sql, peliculaRowMapper).stream().map(peliculaEntityMapper::aPelicula).toList();
    }

    @Override
    public Optional<Pelicula> buscarPorCodigo(String identificador) {
        String sql = "SELECT pelicula_codigo, titulo_texto, estreno_anio, duracion_minutos FROM peliculas WHERE pelicula_codigo = ?";

        return jdbcTemplate.query(sql, peliculaRowMapper, identificador).stream().findFirst().map(peliculaEntityMapper::aPelicula);
    }

    @Override
    public boolean insertarSiAusente(Pelicula pelicula) {
        String sql = "INSERT INTO peliculas (pelicula_codigo, titulo_texto, estreno_anio, duracion_minutos) VALUES (?,?,?,?)"+
                " ON CONFLICT(pelicula_codigo) DO NOTHING";

        PeliculaEntity entidad = peliculaEntityMapper.aEntity(pelicula);

        int filasAfectadas = jdbcTemplate.update(
                sql,
                entidad.getCodigoFila(),
                entidad.getTituloFila(),
                entidad.getAnioFila(),
                entidad.getMinutosFila()
        );

        return filasAfectadas == 1;
    }

    @Override
    public boolean actualizarSiExiste(Pelicula pelicula) {
        String sql = "UPDATE peliculas SET titulo_texto = ?, estreno_anio = ?, duracion_minutos = ? WHERE pelicula_codigo = ?";

        PeliculaEntity entidad = peliculaEntityMapper.aEntity(pelicula);

        int filasAfectadas = jdbcTemplate.update(
                sql,
                entidad.getTituloFila(),
                entidad.getAnioFila(),
                entidad.getMinutosFila(),
                entidad.getCodigoFila()
        );

        return filasAfectadas == 1;
    }

    @Override
    public boolean eliminar(String identificador) {
        String sql =  "DELETE FROM peliculas WHERE pelicula_codigo = ?";

        int filasAfectadas = jdbcTemplate.update(sql, identificador);

        return filasAfectadas == 1;
    }
}