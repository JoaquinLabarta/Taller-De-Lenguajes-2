package entregable.Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class Moneda implements IMonedaDAO {

    // Metodo para insertar una moneda en la base de datos
    @Override
    public abstract void insertarMoneda(Connection connection, Moneda moneda);

    // Método para verificar si una moneda ya existe por su sigla
    @Override
    public boolean existeMoneda(Connection connection, String sigla) {
        String query = "SELECT COUNT(*) FROM Moneda WHERE sigla = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, sigla);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
        catch (SQLException e) {
            System.out.println("Error al verificar si la moneda existe.");
            return false;
        }
    }
    @Override
    public abstract List<Moneda> listarMonedas(Connection connection, String ordenarPor);
    @Override
    public abstract double obtenerStock(Connection connection, String sigla);

    private String nombre;      // Nombre de la criptomoneda.
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private String sigla;       // Sigla que identifica a la criptomoneda.
    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    private double precioActual;

    public double getPrecioActual() {
        return precioActual;
    }

    public void setPrecioActual(double precioActual) {
        this.precioActual = precioActual;
    } 

    private double cantidad;
    public double getCantidad() {
        return cantidad;
    }
    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    private double volatilidad;
    public double getVolatilidad() {
        return volatilidad;
    }
}