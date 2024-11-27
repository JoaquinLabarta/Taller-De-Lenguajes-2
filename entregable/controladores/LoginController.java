package entregable.controladores;
import entregable.igu.vista.PantallaLogin;
import entregable.igu.vista.PantallaRegistro;
import entregable.modelo.Usuario;
import javax.swing.*;

public class LoginController {
    private PantallaLogin pantallaLogin;

    public LoginController(PantallaLogin pantallaLogin) {
        this.pantallaLogin = pantallaLogin;

        // Asocia los oyentes (listeners) de los botones con métodos del controlador
        this.pantallaLogin.agregarAccionLogin(e -> iniciarSesion());
        this.pantallaLogin.agregarAccionRegistrar(e -> abrirPantallaRegistro());
    }

    private void iniciarSesion() {
        String username = pantallaLogin.getUsuario();
        String password = pantallaLogin.getPassword();

        // Verifica credenciales utilizando el modelo Usuario
        if (Usuario.verificarCredenciales(username, password)) {
            JOptionPane.showMessageDialog(pantallaLogin, "Inicio de sesión exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            pantallaLogin.dispose(); // Cierra la ventana de login si es necesario
            // Aquí puedes cargar la siguiente vista de la aplicación si lo deseas.
        } else {
            JOptionPane.showMessageDialog(pantallaLogin, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirPantallaRegistro() {
        pantallaLogin.dispose(); // Cierra la ventana de login

        // Abre la pantalla de registro
        PantallaRegistro pantallaRegistro = new PantallaRegistro();
        new RegistroController(pantallaRegistro);
        pantallaRegistro.setVisible(true);
    }
}

