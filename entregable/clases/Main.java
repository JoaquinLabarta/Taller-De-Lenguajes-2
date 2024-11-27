package entregable.Clases;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Conexion con la base de datos
        String url = "jdbc:sqlite:projectDatabase.db";
        Connection con = null;
    
        try (Scanner ent = new Scanner(System.in)) {
            con = DriverManager.getConnection(url);
            System.out.println("Conexión exitosa a la base de datos.");
            Servicio servicio = new Servicio(con);
    
            // Creacion de tablas (Ejecutar una sola vez y comentar)
            creacionDeTablas.crearTablaActivos(con);
            creacionDeTablas.crearTablaMoneda(con);
            creacionDeTablas.crearTablaTransaccion(con);
            creacionDeTablas.insertarDatosActivos(con);
            creacionDeTablas.insertarDatosMoneda(con);
            // Interaccion usando clase servicio 
            boolean salir = false;
            while (!salir) {
                System.out.println("Presiona Enter para continuar...");
                ent.nextLine();
                System.out.println("\n===== Menú de Opciones =====");
                System.out.println("1. Insertar nueva moneda");
                System.out.println("2. Listar todas las monedas");
                System.out.println("3. Listar stock");
                System.out.println("4. Depositar activos");
                System.out.println("5. Listar activos");
                System.out.println("6. Comprar activo");
                System.out.println("7. Realizar SWAP entre activos");
                System.out.println("8. Salir");
                System.out.print("Elige una opción: ");
                int opcion = ent.nextInt();
                ent.nextLine(); // Limpiar buffer
    
                switch (opcion) {
                    case 1 -> servicio.ingresarMoneda(ent);
                    case 2 -> servicio.listarMonedas(ent);
                    case 3 -> servicio.listarStock(ent);
                    case 4 -> servicio.depositar(ent);
                    case 5 -> servicio.listarActivos(ent);
                    case 6 -> servicio.comprarActivo(ent); 
                    case 7 -> servicio.realizarSwap(ent); 
                    //case 9 -> SwingUtilities.invokeLater(() -> new RegistroUsuario(servicio).setVisible(true));
                    case 8 -> {
                        salir = true;
                        System.out.println("Saliendo del programa...");
                    }
                    default -> System.out.println("Opción no válida, por favor intenta de nuevo.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en la conexión a la base de datos: " + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                    System.out.println("Conexión cerrada.");
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar la conexión: " + ex.getMessage());
            }
        }
    }
    
}
