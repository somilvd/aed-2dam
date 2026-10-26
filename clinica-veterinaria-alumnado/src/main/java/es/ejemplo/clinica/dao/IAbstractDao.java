package es.ejemplo.clinica.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

public interface IAbstractDao<T> {


    List<T> consultar(PreparedStatement ps);

    Optional<T> consultarUno(PreparedStatement ps);

    int consultarEntero(PreparedStatement ps);

    BigDecimal consultarBigDecimal(PreparedStatement ps);

}
