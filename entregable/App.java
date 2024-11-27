package entregable;
import entregable.igu.vista.PantallaLogin;
import entregable.igu.vista.PantallaRegistro;
import entregable.modelo.Usuario;
import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // Inicialización de usuarios de prueba
        Usuario.inicializarUsuariosDePrueba();

        // Crear la vista principal (PantallaLogin)
        PantallaLogin pantallaLogin = new PantallaLogin();

        // Configurar las acciones desde la clase App
        pantallaLogin.agregarAccionLogin(e -> login(pantallaLogin));
        pantallaLogin.agregarAccionRegistrar(e -> abrirPantallaRegistro(pantallaLogin));

        // Mostrar la pantalla de login
        pantallaLogin.setVisible(true);
    }

    private static void login(PantallaLogin vista) {
        String usuario = vista.getUsuario();
        String password = vista.getPassword();

        if (Usuario.verificarCredenciales(usuario, password)) {
            JOptionPane.showMessageDialog(vista, "Inicio de sesión exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            vista.dispose();
            // Aquí podrías cargar otra vista o pantalla de la aplicación principal
            // por ejemplo, una pantalla de "inicio" o "dashboard".
        } else {
            JOptionPane.showMessageDialog(vista, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void abrirPantallaRegistro(PantallaLogin vista) {
        // Cierra la ventana actual (PantallaLogin)
        vista.dispose();

        // Crear y mostrar la pantalla de registro
        PantallaRegistro pantallaRegistro = new PantallaRegistro();

        // Configurar acciones del controlador de registro
        pantallaRegistro.addRegistrarListener(e -> registrarUsuario(pantallaRegistro));
        pantallaRegistro.addVolverListener(e -> volverALogin(pantallaRegistro));

        pantallaRegistro.setVisible(true);
    }

    private static void registrarUsuario(PantallaRegistro vista) {
        String usuario = vista.getUsuario();
        String password = vista.getPassword();

        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Complete todos los campos para registrarse.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (Usuario.registrarUsuario(usuario, password)) {
            JOptionPane.showMessageDialog(vista, "Usuario registrado con éxito.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
            volverALogin(vista); // Vuelve al login tras registrar al usuario
        } else {
            JOptionPane.showMessageDialog(vista, "El usuario ya existe. Intente con otro nombre.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void volverALogin(PantallaRegistro vista) {
        // Cierra la ventana de registro
        vista.dispose();

        // Crear y mostrar la pantalla de login nuevamente
        PantallaLogin pantallaLogin = new PantallaLogin();
        pantallaLogin.agregarAccionLogin(e -> login(pantallaLogin));
        pantallaLogin.agregarAccionRegistrar(e -> abrirPantallaRegistro(pantallaLogin));
        pantallaLogin.setVisible(true);
    }
}
