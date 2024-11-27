package entregable.controladores;
import entregable.igu.vista.PantallaLogin;
import entregable.igu.vista.PantallaRegistro;
import entregable.modelo.Usuario;

import javax.swing.*;

public class RegistroController {
    private PantallaRegistro pantallaRegistro;

    public RegistroController(PantallaRegistro pantallaRegistro) {
        this.pantallaRegistro = pantallaRegistro;

        // Asocia los oyentes (listeners) de los botones con métodos del controlador
        this.pantallaRegistro.addRegistrarListener(e -> registrarUsuario());
        this.pantallaRegistro.addVolverListener(e -> volverALogin());
    }

    private void registrarUsuario() {
        String username = pantallaRegistro.getUsuario();
        String password = pantallaRegistro.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(pantallaRegistro, "Complete todos los campos para registrarse.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Registra un nuevo usuario utilizando el modelo Usuario
        if (Usuario.registrarUsuario(username, password)) {
            JOptionPane.showMessageDialog(pantallaRegistro, "Usuario registrado con éxito.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
            volverALogin();
        } else {
            JOptionPane.showMessageDialog(pantallaRegistro, "El usuario ya existe. Intente con otro nombre.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverALogin() {
        pantallaRegistro.dispose(); // Cierra la ventana de registro

        // Abre la pantalla de login
        PantallaLogin pantallaLogin = new PantallaLogin();
        new LoginController(pantallaLogin);
        pantallaLogin.setVisible(true);
    }
}