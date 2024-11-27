package entregable.Clases;

import entregable.comparadores.ComparadorCantidadActivosFiat;
import entregable.comparadores.ComparadorSiglaActivosFiat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Activo_Fiat {

    public Activo_Fiat(String sigla, double cantidad) {
        this.sigla = sigla;
        this.cantidad = cantidad;
    }

    public Activo_Fiat() {
    }

    public static boolean existeActivo(Connection con, String sigla) {
        String query = "SELECT COUNT(*) FROM Activos WHERE sigla = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, sigla);
            pstmt.executeQuery();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al verificar si el activo existe: " + e.getMessage());
            return false;
        }
    }

    public static void agregarFiat(String sigla, double cantidad, Connection con) {
        String verificarQuery = "SELECT cantidad FROM Activos WHERE sigla = ? and tipo = 'FIAT'";
        String actualizarQuery = "UPDATE Activos SET cantidad = cantidad + ? WHERE sigla = ? and tipo = 'FIAT'";
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
                        insertarStmt.setString(3, "FIAT");
                        insertarStmt.executeUpdate();
                        System.out.println("Fiat agregada: " + cantidad + " " + sigla);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar la Fiat: " + e.getMessage());
        }
    }
    

    public static List<Activo_Fiat> listarActivosFiat(Connection con, String ordenarPor) {
    List<Activo_Fiat> fiats = new ArrayList<>();
    String query = "SELECT sigla, cantidad FROM Activos WHERE tipo = 'FIAT'";

    try (PreparedStatement pstmt = con.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
        // Recorrer los resultados de la consulta y agregar monedas fiat a la lista
        while (rs.next()) {
            String sigla = rs.getString("sigla");
            double cantidadF = rs.getInt("cantidad");
    
            // Crear un objeto Fiat con stock
            Activo_Fiat fiat = new Activo_Fiat(sigla,cantidadF);
            fiats.add(fiat);
        }

        // Ordenar las monedas fiat según el criterio
        if (ordenarPor.equalsIgnoreCase("sigla")) {
            fiats.sort(new ComparadorSiglaActivosFiat()); // Ordenar por sigla
        } else if (ordenarPor.equalsIgnoreCase("cantidad")) {
            fiats.sort(new ComparadorCantidadActivosFiat().reversed()); // Ordenar por valor
        } else {
            System.out.println("Criterio de ordenamiento no válido. Se ordenará por cantidad por defecto.");
            fiats.sort(new ComparadorCantidadActivosFiat().reversed()); // Por defecto, ordenar por valor
        }

        // Imprimir las monedas fiat
        return fiats;

    } catch (SQLException e) {
        System.out.println("Error al listar los activos fiat: " + e.getMessage());
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