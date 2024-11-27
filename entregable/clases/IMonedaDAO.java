package entregable.Clases;

import java.sql.Connection;
import java.util.List;

public interface IMonedaDAO {
    void insertarMoneda(Connection connection, Moneda moneda);
    boolean existeMoneda(Connection connection, String sigla);
    List<Moneda> listarMonedas(Connection connection, String ordenarPor);
    double obtenerStock(Connection connection, String sigla);
}
