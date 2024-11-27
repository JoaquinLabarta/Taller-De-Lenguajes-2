package entregable.Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entregable.comparadores.ComparadorSigla;
import entregable.comparadores.ComparadorStock;
import entregable.comparadores.ComparadorValor;


public class Criptomoneda extends Moneda {
    // Atributos privados
    private String nombre;     
    private String sigla;       
    private double precioActual;
    private int stockAleatorio;
    private double volatilidad;

    public Criptomoneda(){}

    public Criptomoneda(String nombre, String sigla, double precioActual,  double volatilidadAleatoria, int stockAleatorio) {
        this.nombre = nombre;
        this.sigla = sigla;
        this.precioActual = precioActual;
        this.stockAleatorio = stockAleatorio;
        this.volatilidad = volatilidadAleatoria;
    }

    // Implementación del método abstracto insertarMoneda
    @Override
    public void insertarMoneda(Connection con, Moneda moneda) {
        if (super.existeMoneda(con, moneda.getSigla())) {
            System.out.println("La criptomoneda ya existe en la base de datos.");
            return;
        }

        String sql = "INSERT INTO Moneda (nombre, sigla, valor, tipo, volatilidad, stock) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, this.nombre);
            pstmt.setString(2, this.sigla);
            pstmt.setDouble(3, this.precioActual);
            pstmt.setString(4, "Criptomoneda");
            pstmt.setDouble(5, this.volatilidad);
            pstmt.setInt(6, this.stockAleatorio);
            pstmt.executeUpdate();
            System.out.println("Criptomoneda insertada exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al insertar la criptomoneda en la base de datos.");
        }
    }

    // Método para listar monedas
    @Override
    public List<Moneda> listarMonedas(Connection connection, String ordenarPor) {
        List<Moneda> monedas = new ArrayList<>();
        String query = "SELECT nombre, sigla, valor, tipo, volatilidad, stock FROM Moneda WHERE tipo = 'Criptomoneda'";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String nombre2 = rs.getString("nombre");
                String sigla2 = rs.getString("sigla");
                double valor = rs.getDouble("valor");
                double volatilidad2 = rs.getDouble("volatilidad");
                int stock = rs.getInt("stock");

                monedas.add(new Criptomoneda(nombre2, sigla2, valor, volatilidad2, stock));
            }

            // Ordenar según el criterio usando comparadores
            if (ordenarPor.equalsIgnoreCase("sigla")) {
                monedas.sort(new ComparadorSigla());
            } 
            else if (ordenarPor.equalsIgnoreCase("valor")) {
                monedas.sort(new ComparadorValor().reversed());
            } 
            else {
                System.out.println("Criterio de ordenamiento no válido. Se ordenará por valor por defecto.");
                monedas.sort(new ComparadorValor().reversed());
            }

            return monedas;

        } catch (SQLException e) {
            System.out.println("Error al listar las criptomonedas: " + e.getMessage());
            return null;    
        }
    }

    // Método para listar el stock disponible (criptomonedas con su stock)
    public static List<Criptomoneda> listarStock(Connection connection, String ordenarPor) {
        List<Criptomoneda> criptomonedas = new ArrayList<>();
        String query = "SELECT nombre, sigla, valor, volatilidad, stock FROM Moneda WHERE tipo = 'Criptomoneda'";
    
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            // Recorrer los resultados de la consulta y agregar solo criptomonedas
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String sigla = rs.getString("sigla");
                double valor = rs.getDouble("valor");
                double volatilidad = rs.getDouble("volatilidad");
                int stock = rs.getInt("stock");
    
                // Agregar criptomonedas a la lista
                criptomonedas.add(new Criptomoneda(nombre, sigla, valor, volatilidad, stock));
            }
    
            // Ordenar usando Comparators definidos anteriormente
            if (ordenarPor.equalsIgnoreCase("sigla")) {
                criptomonedas.sort(new ComparadorSigla()); // Ordenar por sigla
            } else if (ordenarPor.equalsIgnoreCase("stock")) {
                criptomonedas.sort(new ComparadorStock().reversed()); // Ordenar por stock descendente
            } else {
                System.out.println("Criterio de ordenamiento no válido. Se ordenará por stock descendente por defecto.");
                criptomonedas.sort(new ComparadorStock().reversed()); // Ordenar por stock descendente por defecto
            }
    
            return criptomonedas;
    
        } catch (SQLException e) {
            System.out.println("Error al listar el stock de criptomonedas: " + e.getMessage());
            return null;
        }
    }
     
    @Override
    public double obtenerStock(Connection con, String sigla) {
        String query = "SELECT stock FROM Moneda WHERE sigla = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, sigla);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("stock");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el stock: " + e.getMessage());
        }
        return 0;
    }


    // Getters y Setters
    public double getVolatilidadAleatoria() {
        return volatilidad;
    }
    
    public double getStock() {
        return this.stockAleatorio;
    }
    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public String getSigla() {
        return sigla;
    }

    @Override
    public double getPrecioActual() {
        return precioActual;
    }
}
