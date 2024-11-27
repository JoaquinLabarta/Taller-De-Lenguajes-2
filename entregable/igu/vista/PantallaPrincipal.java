package entregable.igu.vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PantallaPrincipal {
    private static JFrame frame;

    public static void main(String[] args) {
        // Crear el marco principal
        frame = new JFrame("Billetera Virtual - Mis Activos");
        frame.setSize(900, 700);

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel userLabel = new JLabel("Usuario");
        JButton logoutButton = new JButton("Cerrar sesión");
        estilizarBoton(logoutButton, Color.RED, Color.WHITE); // Rojo con letras blancas

        // Ubicacion de los botones
        topPanel.add(userLabel, BorderLayout.WEST);
        topPanel.add(logoutButton, BorderLayout.EAST);

        // Panel central
        JPanel centerPanel = new JPanel(new BorderLayout());
        JLabel balanceLabel = new JLabel("Balance (USD): ", JLabel.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 28));
        balanceLabel.setForeground(new Color(0, 102, 204)); // Azul 
        balanceLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTable table = new JTable();
        cargarDatosYCalcularBalance(table, balanceLabel);

        JScrollPane tableScrollPane = new JScrollPane(table);

        // Ubicacion de los elementos
        centerPanel.add(balanceLabel, BorderLayout.NORTH);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Panel inferior
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton cotizacionesButton = new JButton("Cotizacion tiempo real");
        JButton transaccionesRealizadasButton = new JButton("Transacciones Realizadas");
        JButton comprarButton = new JButton("Comprar");
        JButton exportButton = new JButton("Exportar activos como CSV");

        estilizarBoton(cotizacionesButton, new Color(0, 153, 76), Color.WHITE);
        estilizarBoton(transaccionesRealizadasButton, new Color(0, 153, 76), Color.WHITE);
        estilizarBoton(comprarButton, new Color(0, 153, 76), Color.WHITE);
        estilizarBoton(exportButton, new Color(255, 153, 51), Color.WHITE);

        // Agregar botones al panel inferior
        bottomPanel.add(cotizacionesButton);
        bottomPanel.add(transaccionesRealizadasButton);
        bottomPanel.add(comprarButton);
        bottomPanel.add(exportButton);

        // Agregar paneles al marco
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Acción del botón "Cerrar sesión"
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Sesión cerrada exitosamente.");
                // Aca debe volver a la pantalla de login
                System.exit(0);
            }
        });

        // Acción del botón "Exportar como CSV"
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportarTablaACSV(table);
            }
        });

        // Mostrar la ventana
        frame.setVisible(true);
    }

    private static void cargarDatosYCalcularBalance(JTable table, JLabel balanceLabel) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };

        model.addColumn("Activo");
        model.addColumn("Monto (USD)");
        model.addColumn("Tipo"); 

        double balanceTotal = 0;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:projectDatabase.db");
             PreparedStatement pstmt = conn.prepareStatement("SELECT sigla, cantidad, tipo FROM Activos"); PreparedStatement pstmt2 = conn.prepareStatement("SELECT valor FROM Moneda WHERE sigla = ?")) {
            
            ResultSet rs2 = pstmt2.executeQuery();
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String sigla = rs.getString("sigla");
                double cantidad = rs.getDouble("cantidad");
                String tipo = rs.getString("tipo");
                
                pstmt2.setString(1, sigla);
                double valor = rs2.getDouble("valor");

                balanceTotal += cantidad/valor;
                String monto = String.format("%.2f", cantidad/valor);

                model.addRow(new Object[]{sigla, monto, tipo});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos: " + ex.getMessage());
        }

        // Actualizar tabla
        table.setModel(model);
        estilizarTabla(table);
        balanceLabel.setText(String.format("Balance: USD %.2f", balanceTotal));
    }

    private static void estilizarTabla(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(40);

        // Cambiar encabezados
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setFont(new Font("Arial", Font.BOLD, 16));
        headerRenderer.setBackground(Color.GRAY);
        table.getTableHeader().setDefaultRenderer(headerRenderer);

        // Centrar contenido
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, cellRenderer);
    }

    private static void estilizarBoton(JButton button, Color background, Color foreground) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private static void exportarTablaACSV(JTable table) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como...");
        int userSelection = fileChooser.showSaveDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (java.io.FileWriter writer = new java.io.FileWriter(fileChooser.getSelectedFile() + ".csv")) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                for (int i = 0; i < model.getColumnCount(); i++) {
                    writer.write(model.getColumnName(i) + ",");
                }
                writer.write("\n");

                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        writer.write(model.getValueAt(i, j).toString() + ",");
                    }
                    writer.write("\n");
                }

                writer.flush();
                JOptionPane.showMessageDialog(frame, "Datos exportados exitosamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error al exportar datos: " + ex.getMessage());
            }
        }
    }
}
