package entregable.Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entregable.comparadores.ComparadorSigla;
import entregable.comparadores.ComparadorValor;

public class Fiat extends Moneda {
    // Atributos privados
    private String nombre;     
    private String sigla;       
    private double precioActual; 

    public Fiat(){}

    public Fiat(String nombre, String sigla, double precioActual) {
        this.nombre = nombre;
        this.sigla = sigla;
        this.precioActual = precioActual;   
    }

    // Implementación del método abstracto insertarMoneda
    @Override
    public void insertarMoneda(Connection con, Moneda moneda) {
        if (super.existeMoneda(con, moneda.getSigla())) {
            System.out.println("La moneda FIAT ya existe en la base de datos.");
            return;
        }
        String sql = "INSERT INTO Moneda (nombre, sigla, valor, tipo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, this.nombre);
            pstmt.setString(2, this.sigla);
            pstmt.setDouble(3, this.precioActual);
            pstmt.setString(4, "FIAT");
            pstmt.executeUpdate();
            System.out.println("Moneda FIAT insertada exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al insertar la fiat en la base de datos." + e.getMessage());
        }
    }

    // Método para listar monedas (puede ser llamado desde el Main)
    @Override
    public List<Moneda> listarMonedas(Connection connection, String ordenarPor) {
        List<Moneda> monedas = new ArrayList<>();
        String query = "SELECT nombre, sigla, valor, tipo FROM Moneda WHERE tipo = 'FIAT'";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String nombre2 = rs.getString("nombre");
                String sigla2 = rs.getString("sigla");
                double valor = rs.getDouble("valor");

                monedas.add(new Fiat(nombre2, sigla2, valor));
            }
            
            // Ordenar según el criterio usando comparadores
            if (ordenarPor.equalsIgnoreCase("sigla")) {
                monedas.sort(new ComparadorSigla());
            } else if (ordenarPor.equalsIgnoreCase("valor")) {
                monedas.sort(new ComparadorValor());
            } else {
                System.out.println("Criterio de ordenamiento no válido. Se ordenará por valor por defecto.");
                monedas.sort(new ComparadorValor());
            }

            return monedas;

        } catch (SQLException e) {
            System.out.println("Error al listar las monedas: " + e.getMessage());
            return null;
        }
    }

    public static void listarStock(Connection connection, String ordenarPor){
        System.out.println("Las monedas fiat no tienen stock");
    }

    @Override
    public double obtenerStock(Connection connection, String sigla){
        return 0;
    }

    public static double getPrecioActual(Connection con, String siglaFiat) {
        String query = "SELECT valor FROM Moneda WHERE sigla = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, siglaFiat);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("valor");
            } else {
                System.out.println("No se encontró el valor para la sigla: " + siglaFiat);
                return 0.0;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el precio actual: " + e.getMessage());
            return 0.0;
        }
    }
    
    // Getters y Setters

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getSigla() {
        return sigla;
    }

    @Override
    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    @Override
    public double getPrecioActual() {
        return precioActual;
    }

    @Override
    public void setPrecioActual(double precioActual) {
        this.precioActual = precioActual;
    }
}