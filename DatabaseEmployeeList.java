package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseEmployeeList
{
    private static final String URL_BASE_DATOS = "jdbc:mysql://autoescuela.cxyekqoc86sq.us-east-1.rds.amazonaws.com:3306/autoescuela";
    private static final String USUARIO = "admin";
    private static final String CONTRASEÑA = "Qwerty2005#";

    public void mostrarListaEmpleados()
    {
        try
        {
            Connection conn = DriverManager.getConnection(URL_BASE_DATOS, USUARIO, CONTRASEÑA);
            Statement stmt = conn.createStatement();
            String sql = "SELECT DNI, NOMBRE, APELLIDOS, 'Administrador' AS TIPO FROM Administracion " +
                    "UNION " +
                    "SELECT DNI, NOMBRE, APELLIDOS, 'Profesor' AS TIPO FROM Profesores";
            ResultSet rs = stmt.executeQuery(sql);
            List<String[]> datosEmpleados = new ArrayList<>();
            while (rs.next())
            {
                String[] row =
                        {
                                rs.getString("DNI"),
                                rs.getString("NOMBRE"),
                                rs.getString("APELLIDOS"),
                                rs.getString("TIPO")
                        };
                datosEmpleados.add(row);
            }

            rs.close();
            stmt.close();
            conn.close();
            String[][] datosTabla = datosEmpleados.toArray(new String[0][]);
            String[] nombresColumnas = {"DNI", "Nombre", "Apellidos", "Tipo"};
            DefaultTableModel model = new DefaultTableModel(datosTabla, nombresColumnas)
            {
                @Override
                public boolean isCellEditable(int row, int column)
                {
                    return true;
                }
            };

            JTable tabla = new JTable(model);
            tabla.setFillsViewportHeight(true);
            tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            JScrollPane panelDesplazamiento = new JScrollPane(tabla);
            panelDesplazamiento.setPreferredSize(new Dimension(500, 400));
            JFrame ventanaEmpleados = new JFrame("Listado de Empleados");
            ventanaEmpleados.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventanaEmpleados.setLayout(new BorderLayout());
            ventanaEmpleados.add(panelDesplazamiento, BorderLayout.CENTER);
            ventanaEmpleados.pack();
            ventanaEmpleados.setLocationRelativeTo(null);
            ventanaEmpleados.setVisible(true);
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}