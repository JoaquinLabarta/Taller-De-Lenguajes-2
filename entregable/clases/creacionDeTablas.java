package entregable.Clases;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class creacionDeTablas {
    public static void crearTablaMoneda(Connection connection) {
        String query = "CREATE TABLE IF NOT EXISTS Moneda (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nombre VARCHAR(15) NOT NULL, " +
            "sigla VARCHAR(3) NOT NULL, " +
            "valor REAL NOT NULL, " +
            "tipo VARCHAR(12) NOT NULL, " +
            "volatilidad REAL, " +
            "stock REAL NULL)";
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
            System.out.println("Tabla 'Moneda' creada exitosamente.");
        } catch (SQLException ex) {
            System.out.println("Error al crear Moneda");
        }
    }

    public static void crearTablaActivos(Connection connection){
        String query = "CREATE TABLE IF NOT EXISTS Activos (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "sigla VARCHAR(3) NOT NULL, " +
            "cantidad REAL, " +
            "tipo VARCHAR(12) NOT NULL)";
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
            System.out.println("Tabla 'Activos' creada exitosamente.");
        } catch (SQLException ex) {
            System.out.println("Error al crear Activos");
        }
    }

    public static void crearTablaTransaccion(Connection connection) {
        String query = "CREATE TABLE IF NOT EXISTS Transaccion (" +
                       "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                       "resumen VARCHAR(100) NOT NULL, " +
                       "fecha_hora VARCHAR(79) NOT NULL)";
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
            System.out.println("Tabla 'Transaccion' creada exitosamente.");
        } catch (SQLException ex) {
            System.out.println("Error al crear Transaccion: " + ex.getMessage());
        }
    }

    
    public static void insertarDatosActivos(Connection connection) {
        String query = "INSERT INTO Activos (sigla, cantidad, tipo) VALUES " +
                       "('BTC', 2.5, 'Criptomoneda'), " +
                       "('ETH', 10.0, 'Criptomoneda'), " +
                       "('USD', 500, 'Fiat'), " +
                        "('EUR', 200, 'Fiat')";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            System.out.println("Datos insertados en la tabla 'Activos' exitosamente.");
        } catch (SQLException ex) {
            System.out.println("Error al insertar datos en Activos: " + ex.getMessage());
        }
    }

    public static void insertarDatosMoneda(Connection connection) {
        String query = "INSERT INTO Moneda (nombre, sigla, valor, tipo, volatilidad, stock) VALUES " +
                       "('Bitcoin', 'BTC', 45000.0, 'Criptomoneda', 0.05, 100.0), " +
                       "('Ethereum', 'ETH', 3000.0, 'Criptomoneda', 0.03, 200.0), " +
                       "('Dólar', 'USD', 1.0, 'Fiat', 0.01, 1000.0), " +
                       "('Euro', 'EUR', 1.2, 'Fiat', 0.01, 800.0)";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            System.out.println("Datos insertados en la tabla 'Moneda' exitosamente.");
        } catch (SQLException ex) {
            System.out.println("Error al insertar datos en Moneda: " + ex.getMessage());
        }
    }
    
    // Método para crear la tabla Persona
    private static void crearTablaPersona(Connection connection) {
            String sqlPersona = """
                    CREATE TABLE IF NOT EXISTS Persona (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        nombres VARCHAR(100) NOT NULL,
                        apellidos VARCHAR(100) NOT NULL
                    );
                    """;
    
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(sqlPersona);
                System.out.println("Tabla 'Persona' creada correctamente.");
            } catch (SQLException e) {
                System.out.println("Error al crear la tabla Persona: " + e.getMessage());
            }
        }
    
        // Método para crear la tabla Usuario
        private static void crearTablaUsuario(Connection connection) {
            String sqlUsuario = """
                    CREATE TABLE IF NOT EXISTS Usuario (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        id_persona INTEGER NOT NULL,
                        password VARCHAR(100) NOT NULL,
                        acepta_terminos BOOLEAN NOT NULL,
                        FOREIGN KEY (id_persona) REFERENCES Persona(id) ON DELETE CASCADE
                    );
                    """;
    
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(sqlUsuario);
                System.out.println("Tabla 'Usuario' creada correctamente.");
            } catch (SQLException e) {
                System.out.println("Error al crear la tabla Usuario: " + e.getMessage());
            }
        }   
}