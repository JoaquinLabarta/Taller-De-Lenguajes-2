package entregable_2;

import java.sql.Connection;

public class Main {
    
    public static void main(String[] args) {
        Connection conn = SQLiteConnection.connect();

        SQLiteConnection.disconnect(conn);
    }
}
