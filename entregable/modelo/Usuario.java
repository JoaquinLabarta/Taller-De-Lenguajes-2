package entregable.modelo;
import java.util.HashMap;
import java.util.Map;

public class Usuario {
    private String username;
    private String password;

    // Almacenará los usuarios registrados
    private static Map<String, String> usuariosRegistrados = new HashMap<>();

    // Constructor
    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Métodos estáticos para manejar los usuarios
    public static boolean verificarCredenciales(String username, String password) {
        return usuariosRegistrados.containsKey(username) && usuariosRegistrados.get(username).equals(password);
    }

    public static boolean registrarUsuario(String username, String password) {
        if (usuariosRegistrados.containsKey(username)) {
            return false; // Usuario ya existe
        }
        usuariosRegistrados.put(username, password);
        return true;
    }

    // Método de inicialización (opcional, para pruebas)
    public static void inicializarUsuariosDePrueba() {
        usuariosRegistrados.put("admin", "admin123");
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
