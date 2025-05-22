package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TheoryEditor
{
    private static final String URL_BASE_DATOS = "jdbc:mysql://autoescuela.cxyekqoc86sq.us-east-1.rds.amazonaws.com:3306/autoescuela";
    private static final String USUARIO = "admin";
    private static final String CONTRASEÑA = "Qwerty2005#";

    public void mostrarEditorTeoria()
    {
        mostrarVentanaPreguntas();
        mostrarVentanaRespuestas();
    }

    private void mostrarVentanaPreguntas()
    {
        try
        {
            Connection conn = DriverManager.getConnection(URL_BASE_DATOS, USUARIO, CONTRASEÑA);
            Statement stmt = conn.createStatement();
            String sql = "SELECT IDPREGUNTA, RESPUESTAS FROM Ejercicios";
            ResultSet rs = stmt.executeQuery(sql);
            List<String[]> datosPreguntas = new ArrayList<>();
            while (rs.next())
            {
                String[] row =
                        {
                                rs.getString("IDPREGUNTA"),
                                rs.getString("RESPUESTAS")
                        };
                datosPreguntas.add(row);
            }

            rs.close();
            stmt.close();
            conn.close();
            String[][] datosTabla = datosPreguntas.toArray(new String[0][]);
            String[] nombresColumnas = {"ID Pregunta", "Pregunta"};
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
            JFrame ventanaPreguntas = new JFrame("Editor de Preguntas");
            ventanaPreguntas.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventanaPreguntas.setLayout(new BorderLayout());
            ventanaPreguntas.add(panelDesplazamiento, BorderLayout.CENTER);
            ventanaPreguntas.pack();
            ventanaPreguntas.setLocation(100, 100);
            ventanaPreguntas.setVisible(true);
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos (Preguntas): " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void mostrarVentanaRespuestas()
    {
        try
        {
            Connection conn = DriverManager.getConnection(URL_BASE_DATOS, USUARIO, CONTRASEÑA);
            Statement stmt = conn.createStatement();
            String sql = "SELECT IDPREGUNTA, CORRECCION FROM Historia";
            ResultSet rs = stmt.executeQuery(sql);
            List<String[]> datosRespuestas = new ArrayList<>();
            while (rs.next())
            {
                String[] row =
                        {
                                rs.getString("IDPREGUNTA"),
                                rs.getString("CORRECCION")
                        };
                datosRespuestas.add(row);
            }

            rs.close();
            stmt.close();
            conn.close();
            String[][] datosTabla = datosRespuestas.toArray(new String[0][]);
            String[] nombresColumnas = {"ID Pregunta", "Respuesta"};
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
            JFrame ventanaRespuestas = new JFrame("Editor de Respuestas");
            ventanaRespuestas.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventanaRespuestas.setLayout(new BorderLayout());
            ventanaRespuestas.add(panelDesplazamiento, BorderLayout.CENTER);
            ventanaRespuestas.pack();
            ventanaRespuestas.setLocation(650, 100);
            ventanaRespuestas.setVisible(true);
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos (Respuestas): " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}