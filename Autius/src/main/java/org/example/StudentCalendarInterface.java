package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentCalendarInterface
{
    private static final String URL_BASE_DATOS = "jdbc:mysql://autoescuela.cxyekqoc86sq.us-east-1.rds.amazonaws.com:3306/autoescuela";
    private static final String USUARIO = "admin";
    private static final String CONTRASEÑA = "Qwerty2005#";
    private int idAlumno;
    private JFrame ventana;
    private DefaultTableModel tableModel;

    public StudentCalendarInterface(int idAlumno)
    {
        this.idAlumno = idAlumno;
        mostrarInterfazCalendario();
    }

    private void mostrarInterfazCalendario()
    {
        ventana = new JFrame("Reservar Fechas");
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setSize(600, 400);
        ventana.setLocationRelativeTo(null);
        String[] nombresColumnas = {"Fecha", "Profesor", "Acción"};
        tableModel = new DefaultTableModel(nombresColumnas, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return column == 2;
            }
        };

        List<Object[]> datosFechas = obtenerFechasDisponibles();
        for (Object[] fila : datosFechas)
        {
            tableModel.addRow(new Object[]{fila[0], fila[1], fila[2]});
        }

        JTable tabla = new JTable(tableModel);
        tabla.setFillsViewportHeight(true);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.getColumnModel().getColumn(2).setCellRenderer(new RenderizadorBotones());
        tabla.getColumnModel().getColumn(2).setCellEditor(new EditorBotones(new JCheckBox(), this, datosFechas));
        tabla.setDefaultRenderer(Object.class, (table, value, isSelected, hasFocus, row, column) ->
        {
            Component c = new DefaultTableCellRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            Object[] rowData = datosFechas.get(row);
            int idc = (Integer) rowData[4];
            int studentId = (Integer) rowData[5];
            if (idc != 0 && studentId == idAlumno)
            {
                c.setFont(c.getFont().deriveFont(Font.BOLD));
                ((JLabel) c).setText("<html><u>" + value + "</u></html>");
            }
            return c;
        });

        JScrollPane panelDesplazamiento = new JScrollPane(tabla);
        ventana.setLayout(new BorderLayout());
        ventana.add(panelDesplazamiento, BorderLayout.CENTER);
        ventana.setVisible(true);
    }

    private List<Object[]> obtenerFechasDisponibles()
    {
        List<Object[]> datos = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL_BASE_DATOS, USUARIO, CONTRASEÑA))
        {
            String sql = "SELECT c.IDP, c.ID, c.FECHAS_DISPONIBLES, c.IDC, p.NOMBRE, p.APELLIDOS " +
                    "FROM Calendario c " +
                    "JOIN Profesores p ON c.IDP = p.DNI " +
                    "WHERE c.ID IS NULL OR c.ID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idAlumno);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                datos.add(new Object[]
                        {
                                rs.getString("FECHAS_DISPONIBLES"),
                                rs.getString("NOMBRE") + " " + rs.getString("APELLIDOS"),
                                "Reservar",
                                rs.getString("IDP"),
                                rs.getInt("IDC") == 0 ? 0 : rs.getInt("IDC"),
                                rs.getInt("ID") == 0 ? 0 : rs.getInt("ID")
                        });
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(null, "Error al obtener fechas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return datos;
    }

    public void reservarFecha(int row, List<Object[]> datosFechas)
    {
        Object[] rowData = datosFechas.get(row);
        String fecha = (String) rowData[0];
        String idp = (String) rowData[3];
        try (Connection conn = DriverManager.getConnection(URL_BASE_DATOS, USUARIO, CONTRASEÑA))
        {
            String sql = "UPDATE Calendario SET ID = ?, IDC = (SELECT COALESCE(MAX(IDC), 0) + 1 FROM Calendario) " +
                    "WHERE IDP = ? AND FECHAS_DISPONIBLES = ? AND ID IS NULL";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idAlumno);
            stmt.setString(2, idp);
            stmt.setString(3, fecha);
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            if (rowsAffected > 0)
            {
                JOptionPane.showMessageDialog(ventana, "Reserva realizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                refrescarTabla(datosFechas);
            }
            else
            {
                JOptionPane.showMessageDialog(ventana, "No se pudo realizar la reserva.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(ventana, "Error al reservar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void refrescarTabla(List<Object[]> datosFechas)
    {
        tableModel.setRowCount(0);
        datosFechas.clear();
        datosFechas.addAll(obtenerFechasDisponibles());
        for (Object[] fila : datosFechas)
        {
            tableModel.addRow(new Object[]{fila[0], fila[1], fila[2]});
        }
    }

    private static class RenderizadorBotones extends JButton implements TableCellRenderer
    {
        public RenderizadorBotones()
        {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    private static class EditorBotones extends DefaultCellEditor
    {
        private JButton button;
        private String label;
        private boolean isPushed;
        private StudentCalendarInterface parent;
        private List<Object[]> datosFechas;
        private int row;

        public EditorBotones(JCheckBox checkBox, StudentCalendarInterface parent, List<Object[]> datosFechas)
        {
            super(checkBox);
            this.parent = parent;
            this.datosFechas = datosFechas;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
        {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            this.row = row;
            return button;
        }

        @Override
        public Object getCellEditorValue()
        {
            if (isPushed)
            {
                parent.reservarFecha(row, datosFechas);
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing()
        {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped()
        {
            super.fireEditingStopped();
        }
    }
}