package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TeacherCalendarInterface
{
    private static final String URL_BASE_DATOS = "jdbc:mysql://autoescuela.cxyekqoc86sq.us-east-1.rds.amazonaws.com:3306/autoescuela";
    private static final String USUARIO = "admin";
    private static final String CONTRASEÑA = "Qwerty2005#";
    private JFrame ventana;

    public TeacherCalendarInterface()
    {
        seleccionarProfesorYMostrarInterfaz();
    }

    private void seleccionarProfesorYMostrarInterfaz()
    {
        JDialog dialogoProfesor = new JDialog((Frame) null, "Seleccionar Profesor", true);
        dialogoProfesor.setSize(400, 200);
        dialogoProfesor.setLocationRelativeTo(null);
        dialogoProfesor.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        List<String[]> profesores = obtenerProfesores();
        if (profesores.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "No hay profesores disponibles.", "Error", JOptionPane.ERROR_MESSAGE);
            dialogoProfesor.dispose();
            return;
        }

        JLabel etiqueta = new JLabel("Seleccione un profesor:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        dialogoProfesor.add(etiqueta, gbc);
        int row = 1;
        for (String[] profesor : profesores)
        {
            String dni = profesor[0];
            String nombre = profesor[1] + " " + profesor[2];
            JButton botonProfesor = new JButton(nombre);
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.gridwidth = 2;
            dialogoProfesor.add(botonProfesor, gbc);
            botonProfesor.addActionListener(e ->
            {
                dialogoProfesor.dispose();
                mostrarInterfazIngresoFechas(dni);
            });
            row++;
        }

        dialogoProfesor.setVisible(true);
    }

    private List<String[]> obtenerProfesores()
    {
        List<String[]> profesores = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL_BASE_DATOS, USUARIO, CONTRASEÑA))
        {
            String sql = "SELECT DNI, NOMBRE, APELLIDOS FROM Profesores";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                profesores.add(new String[]
                        {
                                rs.getString("DNI"),
                                rs.getString("NOMBRE"),
                                rs.getString("APELLIDOS")
                        });
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(null, "Error al obtener profesores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return profesores;
    }

    private void mostrarInterfazIngresoFechas(String professorDni)
    {
        ventana = new JFrame("Ingresar Fechas Disponibles");
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setSize(300, 200);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel etiquetaFecha = new JLabel("Fecha (YYYY-MM-DD):");
        gbc.gridx = 0;
        gbc.gridy = 0;
        ventana.add(etiquetaFecha, gbc);
        JTextField campoFecha = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        ventana.add(campoFecha, gbc);
        JButton botonGuardar = new JButton("Guardar");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        ventana.add(botonGuardar, gbc);
        botonGuardar.addActionListener(e ->
        {
            String fecha = campoFecha.getText().trim();
            if (!esFormatoFechaValido(fecha))
            {
                JOptionPane.showMessageDialog(ventana, "Formato de fecha inválido. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (guardarFecha(professorDni, fecha))
            {
                JOptionPane.showMessageDialog(ventana, "Fecha guardada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                campoFecha.setText("");
            }
            else
            {
                JOptionPane.showMessageDialog(ventana, "Error al guardar la fecha.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        ventana.setVisible(true);
    }

    private boolean esFormatoFechaValido(String date)
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(date);
            return date.matches("\\d{4}-\\d{2}-\\d{2}");
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private boolean guardarFecha(String professorDni, String fecha)
    {
        try (Connection conn = DriverManager.getConnection(URL_BASE_DATOS, USUARIO, CONTRASEÑA))
        {
            String sql = "INSERT INTO Calendario (IDP, FECHAS_DISPONIBLES) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, professorDni);
            stmt.setString(2, fecha);
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            return rowsAffected > 0;
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(ventana, "Error al guardar en la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
}