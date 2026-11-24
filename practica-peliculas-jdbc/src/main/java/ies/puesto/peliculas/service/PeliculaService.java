package ies.puesto.peliculas.service;

import ies.puesto.peliculas.model.Pelicula;
import ies.puesto.peliculas.repository.PeliculaRepository;
import ies.puesto.peliculas.service.mapper.PeliculaDominioMapper;
import ies.puesto.peliculas.service.modelo.DatosCreacionPelicula;
import ies.puesto.peliculas.service.modelo.DatosEdicionPelicula;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PeliculaService {
    private final PeliculaRepository peliculaRepository;
    private final PeliculaDominioMapper peliculaDominioMapper;

    public PeliculaService(PeliculaRepository peliculaRepository, PeliculaDominioMapper peliculaDominioMapper) {

        this.peliculaRepository = peliculaRepository;
        this.peliculaDominioMapper = peliculaDominioMapper;
    }

    public List<Pelicula> listar() {
        return peliculaRepository.buscarTodas();
    }

    public Optional<Pelicula> obtener(String codigo) {
        return peliculaRepository.buscarPorCodigo(codigo);
    }

    public Optional<Pelicula> crear(DatosCreacionPelicula datos) {

        Pelicula pelicula = peliculaDominioMapper.aPelicula(datos);

        boolean insertada = peliculaRepository.insertarSiAusente(pelicula);

        if (insertada) {
            return Optional.of(pelicula);
        }
        return Optional.empty();
    }

    public Optional<Pelicula> actualizar(String codigo, DatosEdicionPelicula datos) {

        Pelicula pelicula = peliculaDominioMapper.aPelicula(codigo, datos);

        boolean actualizada = peliculaRepository.actualizarSiExiste(pelicula);

        if (actualizada) {
            return Optional.of(pelicula);
        }
        return Optional.empty();
    }

    public boolean eliminar(String codigo) {
        return peliculaRepository.eliminar(codigo);
    }
}
