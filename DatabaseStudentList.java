package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseStudentList
{
    private static final String URL_BASE_DATOS = "jdbc:mysql://autoescuela.cxyekqoc86sq.us-east-1.rds.amazonaws.com:3306/autoescuela";
    private static final String USUARIO = "admin";
    private static final String CONTRASEÑA = "Qwerty2005#";

    public void mostrarListaAlumnos()
    {
        try
        {
            Connection conn = DriverManager.getConnection(URL_BASE_DATOS, USUARIO, CONTRASEÑA);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ID, NOMBRE, APELLIDOS, DNI FROM Alumnos");
            List<String[]> datosAlumnos = new ArrayList<>();
            while (rs.next())
            {
                String[] row =
                        {
                                String.valueOf(rs.getInt("ID")),
                                rs.getString("NOMBRE"),
                                rs.getString("APELLIDOS"),
                                rs.getString("DNI")
                        };
                datosAlumnos.add(row);
            }

            rs.close();
            stmt.close();
            conn.close();
            String[][] datosTabla = datosAlumnos.toArray(new String[0][]);
            String[] nombresColumnas = {"ID", "Nombre", "Apellidos", "DNI"};
            JTable tabla = new JTable(datosTabla, nombresColumnas);
            tabla.setFillsViewportHeight(true);
            tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            JScrollPane panelDesplazamiento = new JScrollPane(tabla);
            panelDesplazamiento.setPreferredSize(new Dimension(500, 400));
            JFrame ventanaAlumnos = new JFrame("Listado de Alumnos");
            ventanaAlumnos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventanaAlumnos.setLayout(new BorderLayout());
            ventanaAlumnos.add(panelDesplazamiento, BorderLayout.CENTER);
            ventanaAlumnos.pack();
            ventanaAlumnos.setLocationRelativeTo(null);
            ventanaAlumnos.setVisible(true);
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}