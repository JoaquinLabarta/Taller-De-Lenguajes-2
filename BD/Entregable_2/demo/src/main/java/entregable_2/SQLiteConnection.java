package entregable_2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {

    public static Connection connect() {
        Connection conn = null;
        try {
            // URL de conexión a la base de datos
            String url = "jdbc:sqlite:BD.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Conexión a SQLite establecida.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void disconnect(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Conexión a SQLite cerrada.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}