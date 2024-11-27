package entregable.Clases;

import entregable.comparadores.ComparadorCantidadActivosCripto;
import entregable.comparadores.ComparadorSiglaActivosCripto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Activo_Cripto {

    public Activo_Cripto(String sigla, double cantidad) {
        this.sigla = sigla;
        this.cantidad = cantidad;
    }

    public Activo_Cripto() {
    }

    public static boolean existeActivo(Connection con, String sigla) {
        String query = "SELECT COUNT(*) FROM Activos WHERE sigla = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, sigla);
            pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error al verificar si el activo existe: " + e.getMessage());
        }
        return false;
    }

    public static void agregarCriptomoneda(String sigla, double cantidad, Connection con) {
        String verificarQuery = "SELECT cantidad FROM Activos WHERE sigla = ? and tipo = 'Criptomoneda'";
        String actualizarQuery = "UPDATE Activos SET cantidad = cantidad + ? WHERE sigla = ? and tipo = 'Criptomoneda'";
        String insertarQuery = "INSERT INTO Activos (sigla, cantidad, tipo) VALUES (?, ?, ?)";

        try {
            // Verificar si la moneda ya existe en la tabla Activos
            try (PreparedStatement verificarStmt = con.prepareStatement(verificarQuery)) {
                verificarStmt.setString(1, sigla);
                ResultSet rs = verificarStmt.executeQuery();
    
                if (rs.next()) {
                    // Si existe, actualizar la cantidad sumando la nueva cantidad
                    try (PreparedStatement actualizarStmt = con.prepareStatement(actualizarQuery)) {
                        actualizarStmt.setDouble(1, cantidad);  // Nueva cantidad a sumar
                        actualizarStmt.setString(2, sigla);   // La sigla de la moneda existente
                        int filasActualizadas = actualizarStmt.executeUpdate();
                        if (filasActualizadas > 0) {
                            System.out.println("Cantidad actualizada: " + cantidad + " " + sigla);
                        } else {
                            System.out.println("No se pudo actualizar la cantidad.");
                        }
                    }
                } else {
                    // Si no existe, insertar nuevo registro
                    try (PreparedStatement insertarStmt = con.prepareStatement(insertarQuery)) {
                        insertarStmt.setString(1, sigla);
                        insertarStmt.setDouble(2, cantidad);
                        insertarStmt.setString(3, "Criptomoneda");
                        insertarStmt.executeUpdate();
                        System.out.println("Criptomoneda agregada: " + cantidad + " " + sigla);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar la criptomoneda: " + e.getMessage());
        }
    }

    public static List<Activo_Cripto> listarActivosCripto(Connection con, String ordenarPor) {
        List<Activo_Cripto> activosCripto = new ArrayList<>();
        String query = "SELECT sigla, cantidad FROM Activos WHERE tipo = 'Criptomoneda'";
    
        try (PreparedStatement pstmt = con.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
            // Recorrer los resultados de la consulta y agregar los activos de criptomonedas a la lista
            while (rs.next()) {
                String sigla = rs.getString("sigla");
                double cantidad = rs.getInt("cantidad");

                // Agregar el activo a la lista
                Activo_Cripto activo = new Activo_Cripto(sigla,cantidad);
                activosCripto.add(activo);
            }
    
            // Ordenar los activos según el criterio
            if (ordenarPor.equalsIgnoreCase("sigla")) {
                activosCripto.sort(new ComparadorSiglaActivosCripto()); // Ordenar por sigla
            } else if (ordenarPor.equalsIgnoreCase("cantidad")) {
                activosCripto.sort(new ComparadorCantidadActivosCripto().reversed()); // Ordenar por cantidad
            } else {
                System.out.println("Criterio de ordenamiento no válido. Se ordenará por sigla por defecto.");
                activosCripto.sort(new ComparadorSiglaActivosCripto().reversed()); // Por defecto, ordenar por sigla
            }
    
            // Imprimir los activos de criptomonedas
            return activosCripto;
    
        } catch (SQLException e) {
            System.out.println("Error al listar los activos de criptomonedas: " + e.getMessage());
            return null;
        }
    }
    
    private double cantidad;
    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    private String sigla;
    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }
}