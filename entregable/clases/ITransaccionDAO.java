package entregable.Clases;

import java.sql.Connection;

public interface ITransaccionDAO {
    void realizarSwap(Connection con, String siglaConvertir, double cantidadConvertir, String siglaEsperada);
    void comprarActivo2(Connection con, String sigla, int cantidad);
    void insertarActivo(Connection con, String sigla, int cantidad);
    void generarFactura(Connection con, String sigla, int cantidad);   
}
