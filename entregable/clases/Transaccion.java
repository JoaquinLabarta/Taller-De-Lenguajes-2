package entregable.Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Transaccion {


    public void realizarSwap(Connection con, String siglaConvertir, double cantidadConvertir, String siglaEsperada, Scanner ent) {
        // Queries para verificar activos y realizar el swap
        String queryActivos = "SELECT cantidad FROM Activos WHERE sigla = ?";
        String queryMonedaValor = "SELECT valor FROM Moneda WHERE sigla = ?";
        String updateActivoConvertir = "UPDATE Activos SET cantidad = cantidad - ? WHERE sigla = ?";
        String updateActivoEsperado = "UPDATE Activos SET cantidad = cantidad + ? WHERE sigla = ?";
        String insertTransactionQuery = "INSERT INTO Transaccion (resumen, fecha_hora) VALUES (?, ?)";
        
        try (PreparedStatement checkActivoConvertirStmt = con.prepareStatement(queryActivos);
             PreparedStatement checkActivoEsperadoStmt = con.prepareStatement(queryActivos);
             PreparedStatement getValorConvertirStmt = con.prepareStatement(queryMonedaValor);
             PreparedStatement getValorEsperadoStmt = con.prepareStatement(queryMonedaValor);
             PreparedStatement updateConvertirStmt = con.prepareStatement(updateActivoConvertir);
             PreparedStatement updateEsperadoStmt = con.prepareStatement(updateActivoEsperado);
             PreparedStatement insertTransactionStmt = con.prepareStatement(insertTransactionQuery)) {

            // Verifica si la criptomoneda a convertir está en Mis Activos
            checkActivoConvertirStmt.setString(1, siglaConvertir);
            ResultSet rsConvertir = checkActivoConvertirStmt.executeQuery();

            if (!rsConvertir.next() || rsConvertir.getDouble("cantidad") < cantidadConvertir) {
                System.out.println("Swap fallido: No tienes suficiente cantidad de " + siglaConvertir);
                return;
            }

            // Verifica si la criptomoneda esperada está en Mis Activos
            checkActivoEsperadoStmt.setString(1, siglaEsperada);
            ResultSet rsEsperado = checkActivoEsperadoStmt.executeQuery();

            if (!rsEsperado.next()) {
                System.out.println("Swap fallido: No tienes " + siglaEsperada + " en tus activos.");
                return;
            }

            // Obtiene los valores de las criptomonedas involucradas
            getValorConvertirStmt.setString(1, siglaConvertir);
            ResultSet valorConvertirRS = getValorConvertirStmt.executeQuery();

            getValorEsperadoStmt.setString(1, siglaEsperada);
            ResultSet valorEsperadoRS = getValorEsperadoStmt.executeQuery();

            if (valorConvertirRS.next() && valorEsperadoRS.next()) {
                double valorConvertir = valorConvertirRS.getDouble("valor");
                double valorEsperado = valorEsperadoRS.getDouble("valor");

                // Calcula la cantidad esperada en la criptomoneda de destino
                double cantidadEsperada = (cantidadConvertir * valorConvertir) / valorEsperado;

                // Confirmación del swap
                System.out.println("===== Confirmación del SWAP =====");
                System.out.println("Convertir: " + cantidadConvertir + " " + siglaConvertir);
                System.out.println("Recibirás: " + cantidadEsperada + " " + siglaEsperada);
                System.out.println("¿Deseas confirmar el SWAP? (sí/no)");

                String confirmacion = ent.nextLine();

                if (confirmacion.equalsIgnoreCase("sí")) {
                    // Actualiza la cantidad de la criptomoneda a convertir
                    updateConvertirStmt.setDouble(1, cantidadConvertir);
                    updateConvertirStmt.setString(2, siglaConvertir);
                    updateConvertirStmt.executeUpdate();

                    // Actualiza la cantidad de la criptomoneda esperada
                    updateEsperadoStmt.setDouble(1, cantidadEsperada);
                    updateEsperadoStmt.setString(2, siglaEsperada);
                    updateEsperadoStmt.executeUpdate();

                    // Inserta la transacción en la tabla Transaccion
                    String resumen = "Swap de " + cantidadConvertir + " " + siglaConvertir + " a " + cantidadEsperada + " " + siglaEsperada;
                    insertTransactionStmt.setString(1, resumen);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    String fechaHora = LocalDateTime.now().format(formatter);
                    insertTransactionStmt.setString(2, fechaHora);
                    insertTransactionStmt.executeUpdate();

                    System.out.println("SWAP realizado exitosamente.");
                } else {
                    System.out.println("SWAP cancelado por el usuario.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al realizar el SWAP: " + e.getMessage());
        }
    }

    public void comprarActivo2(Connection con, String sigla, String siglaFiat, double cantidadCompraUSD) {
        String checkStockQuery = "SELECT stock, valor FROM Moneda WHERE sigla = ?";
        String checkStockFiatQuery = "SELECT cantidad FROM Activos WHERE sigla = ? AND tipo = 'FIAT'";
        String updateStockQuery = "UPDATE Moneda SET stock = stock - ? WHERE sigla = ?";
        String updateStockFiatQuery = "UPDATE Activos SET cantidad = cantidad - ? WHERE sigla = ?";
        String insertTransactionQuery = "INSERT INTO Transaccion (resumen, fecha_hora) VALUES (?, ?)";
    
        try {
            con.setAutoCommit(false); // Iniciar la transacción
    
            try (PreparedStatement checkStockStmt = con.prepareStatement(checkStockQuery);
                 PreparedStatement checkStockFiatStmt = con.prepareStatement(checkStockFiatQuery);
                 PreparedStatement updateStockStmt = con.prepareStatement(updateStockQuery);
                 PreparedStatement insertTransactionStmt = con.prepareStatement(insertTransactionQuery);
                 PreparedStatement updateStockFiatStmt = con.prepareStatement(updateStockFiatQuery)) {
    
                // Obtener el stock y valor actual de la moneda
                checkStockStmt.setString(1, sigla);
                ResultSet rs = checkStockStmt.executeQuery();
    
                // Configurar y ejecutar la consulta de cantidad Fiat
                checkStockFiatStmt.setString(1, siglaFiat);
                ResultSet rsFiat = checkStockFiatStmt.executeQuery();
    
                if (rs.next() && rsFiat.next()) { 
                    double stockDisponible = rs.getDouble("stock");
                    double valorUnitario = rs.getDouble("valor");
                    double cantidadFiat = rsFiat.getDouble("cantidad");
    
                    // Convertir el monto de compra en USD a la cantidad de criptomoneda
                    double cantidadCompraCripto = cantidadCompraUSD / valorUnitario;
    
                    // Verificar si hay fondos Fiat y suficiente stock
                    if (cantidadFiat < cantidadCompraUSD) {
                        System.out.println("Compra fallida: fondos insuficientes en Fiat.");
                        con.rollback(); // Revertir la transacción
                        return;
                    }
                    if (stockDisponible < cantidadCompraCripto) {
                        System.out.println("Compra fallida: stock insuficiente para " + sigla);
                        con.rollback(); // Revertir la transacción
                        return;
                    }
    
                    // Confirmación de usuario simulada
                    String confirmacion = "sí"; 
                    if (confirmacion.equalsIgnoreCase("sí")) {
                        // Realizar las actualizaciones si el usuario confirma
    
                        // Restar cantidad de criptomoneda en el stock de Moneda
                        updateStockStmt.setDouble(1, cantidadCompraCripto);
                        updateStockStmt.setString(2, sigla);
                        updateStockStmt.executeUpdate();
    
                        // Restar cantidad en Fiat del usuario
                        updateStockFiatStmt.setDouble(1, cantidadCompraUSD);
                        updateStockFiatStmt.setString(2, siglaFiat);
                        updateStockFiatStmt.executeUpdate();
    
                        // Insertar o actualizar el activo en la tabla Activos
                        insertarActivo(con, sigla, cantidadCompraCripto);
    
                        // Insertar la transacción en la tabla Transaccion
                        String resumen = "Compra de " + cantidadCompraCripto + " unidades de " + sigla + " a $" + valorUnitario + " cada una";
                        insertTransactionStmt.setString(1, resumen);
                        // Actualizamos la hora
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                        String fechaHora = LocalDateTime.now().format(formatter);
                        insertTransactionStmt.setString(2, fechaHora);
                        insertTransactionStmt.executeUpdate();
    
                        // Generar la factura de compra
                        generarFactura(con, sigla, cantidadCompraCripto);
    
                        con.commit(); // Confirmar la transacción
                    } else {
                        System.out.println("Compra cancelada por el usuario.");
                        con.rollback(); // Revertir la transacción
                    }
                } else {
                    System.out.println("Error: Moneda o fondos Fiat no disponibles.");
                    con.rollback(); // Revertir la transacción
                }
    
            } catch (SQLException e) {
                System.out.println("Error en la compra: " + e.getMessage());
                con.rollback(); // Revertir la transacción en caso de error
            } finally {
                con.setAutoCommit(true); // Restablecer el modo de auto-commit
            }
    
        } catch (SQLException e) {
            System.out.println("Error al configurar la transacción: " + e.getMessage());
        }
    }
    

    // Método para insertar o actualizar el activo en la tabla Activos
    private void insertarActivo(Connection con, String sigla, double cantidad) {
        String checkActivoQuery = "SELECT cantidad FROM Activos WHERE sigla = ? AND tipo = 'Criptomoneda'";
        String updateActivoQuery = "UPDATE Activos SET cantidad = cantidad + ? WHERE sigla = ? AND tipo = 'Criptomoneda'";
        String insertActivoQuery = "INSERT INTO Activos (sigla, cantidad, tipo) VALUES (?, ?, 'Criptomoneda')";
    
        try (PreparedStatement checkStmt = con.prepareStatement(checkActivoQuery);
             PreparedStatement updateStmt = con.prepareStatement(updateActivoQuery);
             PreparedStatement insertStmt = con.prepareStatement(insertActivoQuery)) {
    
            // Verificar si el activo ya existe
            checkStmt.setString(1, sigla);
            ResultSet rs = checkStmt.executeQuery();
    
            if (rs.next()) {
                // Si existe, actualizar la cantidad
                updateStmt.setDouble(1, cantidad);
                updateStmt.setString(2, sigla);
                updateStmt.executeUpdate();
            } else {
                // Si no existe, insertar el nuevo activo
                insertStmt.setString(1, sigla);
                insertStmt.setDouble(2, cantidad);
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar o actualizar el activo: " + e.getMessage());
        }
    }
    

  public void generarFactura(Connection con, String sigla, Double cantidad) {
    String query = "SELECT nombre, valor FROM Moneda WHERE sigla = ?";

    try (PreparedStatement pstmt = con.prepareStatement(query)) {
        pstmt.setString(1, sigla);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String nombre = rs.getString("nombre");
            double valorUnitario = rs.getDouble("valor");
            double total = valorUnitario * cantidad;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String fechaHora = LocalDateTime.now().format(formatter);

            System.out.println("===== Factura de Compra =====");
            System.out.println("Moneda: " + nombre);
            System.out.println("Sigla: " + sigla);
            System.out.println("Cantidad: " + cantidad);
            System.out.println("Valor Unitario: $" + String.format("%.2f", valorUnitario));
            System.out.println("Total: $" + String.format("%.2f", total));
            System.out.println("Fecha y Hora: " + fechaHora);
            System.out.println("=============================");
        } else {
            System.out.println("Error: La moneda con la sigla " + sigla + " no existe.");
        }
    } catch (SQLException e) {
        System.out.println("Error al generar la factura: " + e.getMessage());
    }
}

}