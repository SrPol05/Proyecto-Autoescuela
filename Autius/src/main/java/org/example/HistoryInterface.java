package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryInterface
{
    private static final String URL_BASE_DATOS = "jdbc:mysql://autoescuela.cxyekqoc86sq.us-east-1.rds.amazonaws.com:3306/autoescuela";
    private static final String USUARIO = "admin";
    private static final String CONTRASEÑA = "Qwerty2005#";
    private JFrame ventana;
    private int idAlumno;

    public HistoryInterface(int idAlumno)
    {
        this.idAlumno = idAlumno;
        inicializar();
    }

    private void inicializar()
    {
        ventana = new JFrame("Historial de Tests");
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setSize(800, 500);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new BorderLayout());
        List<String[]> datosHistoria = cargarHistorial();
        String[][] datosTabla = datosHistoria.toArray(new String[0][]);
        String[] nombresColumnas = {"ID Test", "Pregunta", "Respuesta Seleccionada", "Corrección"};
        JTable tabla = new JTable(datosTabla, nombresColumnas);
        tabla.setFillsViewportHeight(true);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane panelDesplazamiento = new JScrollPane(tabla);
        ventana.add(panelDesplazamiento, BorderLayout.CENTER);
        ventana.setVisible(true);
    }

    private List<String[]> cargarHistorial()
    {
        List<String[]> datos = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL_BASE_DATOS, USUARIO, CONTRASEÑA))
        {
            String sql = "SELECT h.IDT, h.CORRECCION, e.RESPUESTAS " +
                    "FROM Historia h " +
                    "JOIN Ejercicios e ON h.IDPREGUNTA = e.IDPREGUNTA " +
                    "WHERE h.ID = ? AND h.IDT IS NOT NULL " +
                    "ORDER BY h.IDT";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idAlumno);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                String correccion = rs.getString("CORRECCION");
                String correccionStatus = correccion.startsWith("CORRECTA") ? "CORRECTA" : "INCORRECTA";
                String[] row =
                        {
                                String.valueOf(rs.getInt("IDT")),
                                rs.getString("RESPUESTAS"),
                                correccion.replaceAll("^(CORRECTA|INCORRECTA):\\s*", ""),
                                correccionStatus
                        };
                datos.add(row);
            }

            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(ventana, "Error al cargar historial: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return datos;
    }
}