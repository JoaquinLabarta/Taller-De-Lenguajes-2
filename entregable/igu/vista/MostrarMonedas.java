package entregable.igu.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MostrarMonedas {
    private static JFrame frame;

    public static void main(String[] args) {
        // Crear ventana inicial
        frame = new JFrame("Monedas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Crear panel con botones de opciones
        JPanel menuPanel = new JPanel();
        JButton listarCriptoButton = new JButton("Listar Criptomonedas");
        JButton listarFiatButton = new JButton("Listar Fiat");
        menuPanel.add(listarCriptoButton);
        menuPanel.add(listarFiatButton);

        frame.add(menuPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        // Acción para el botón "Listar Criptomonedas"
        listarCriptoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                mostrarTabla("Criptomoneda");
            }
        });

        // Acción para el botón "Listar Fiat"
        listarFiatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                mostrarTabla("Fiat");
            }
        });
    }

    private static void mostrarTabla(String tipo) {
        frame.getContentPane().removeAll(); // Limpiar ventana

        // Panel con tabla y botón de volver
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        JButton volverButton = new JButton("Volver al Menú");

        // Conectar a la base de datos y cargar datos
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:projectDatabase.db");
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Activos WHERE tipo = ?")) {

            pstmt.setString(1, tipo);
            ResultSet rs = pstmt.executeQuery();

            // Obtener nombres de columnas
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            // Agregar filas
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos: " + e.getMessage());
        }

        // Configurar tabla y botón de volver
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(volverButton, BorderLayout.SOUTH);

        // Acción para el botón "Volver al Menú"
        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                volverAlMenu();
            }
        });

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private static void volverAlMenu() {
        frame.getContentPane().removeAll(); // Limpiar ventana

        // Restaurar el menú inicial
        JPanel menuPanel = new JPanel();
        JButton listarCriptoButton = new JButton("Listar Criptomonedas");
        JButton listarFiatButton = new JButton("Listar Fiat");
        menuPanel.add(listarCriptoButton);
        menuPanel.add(listarFiatButton);

        frame.add(menuPanel, BorderLayout.CENTER);

        // Acción para el botón "Listar Criptomonedas"
        listarCriptoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                mostrarTabla("Criptomoneda");
            }
        });

        // Acción para el botón "Listar Fiat"
        listarFiatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                mostrarTabla("Fiat");
            }
        });

        frame.revalidate();
        frame.repaint();
    }
}
