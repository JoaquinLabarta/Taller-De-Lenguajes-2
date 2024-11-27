package entregable.igu.vista;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class PantallaRegistro extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnRegistrar;
    private JButton btnVolver;

    public PantallaRegistro() {
        // Configuración de la ventana
        setTitle("Registro de Usuario");
        setSize(400, 300);
        setLocationRelativeTo(null); // Centrar ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior: Título
        JLabel lblTitulo = new JLabel("Registro de Usuario", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(0, 102, 204)); // Azul llamativo
        add(lblTitulo, BorderLayout.NORTH);

        // Panel central: Campos de texto
        JPanel panelCentral = new JPanel(new GridLayout(4, 1, 10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Margen interno

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        txtUsuario = new JTextField();

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 14));
        txtPassword = new JPasswordField();

        panelCentral.add(lblUsuario);
        panelCentral.add(txtUsuario);
        panelCentral.add(lblPassword);
        panelCentral.add(txtPassword);

        add(panelCentral, BorderLayout.CENTER);

        // Panel inferior: Botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));

        btnRegistrar = new JButton("Registrar");
        estilizarBoton(btnRegistrar, new Color(0, 153, 76), Color.WHITE); // Verde

        btnVolver = new JButton("Volver");
        estilizarBoton(btnVolver, new Color(255, 153, 51), Color.WHITE); // Naranja

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnVolver);

        add(panelBotones, BorderLayout.SOUTH);
    }

    // Métodos para obtener los datos ingresados
    public String getUsuario() {
        return txtUsuario.getText();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    // Métodos para agregar listeners a los botones
    public void addRegistrarListener(ActionListener listener) {
        btnRegistrar.addActionListener(listener);
    }

    public void addVolverListener(ActionListener listener) {
        btnVolver.addActionListener(listener);
    }

    // Método para estilizar botones
    private void estilizarBoton(JButton boton, Color background, Color foreground) {
        boton.setBackground(background);
        boton.setForeground(foreground);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
}
