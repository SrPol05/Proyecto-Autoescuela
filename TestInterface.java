package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestInterface
{
    private static final String URL_BASE_DATOS = "jdbc:mysql://autoescuela.cxyekqoc86sq.us-east-1.rds.amazonaws.com:3306/autoescuela";
    private static final String USUARIO = "admin";
    private static final String CONTRASEÑA = "Qwerty2005#";
    private JFrame ventana;
    private JLabel etiquetaPregunta;
    private JButton[] botonesRespuesta;
    private List<Pregunta> preguntas;
    private int preguntaActual;
    private int idAlumno;
    private int idTest;
    private List<String> respuestasSeleccionadas;

    public TestInterface(int idAlumno)
    {
        this.idAlumno = idAlumno;
        preguntas = new ArrayList<>();
        respuestasSeleccionadas = new ArrayList<>();
        inicializar();
    }

    private void inicializar()
    {
        ventana = new JFrame("Test de Autoescuela");
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setSize(600, 400);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        etiquetaPregunta = new JLabel("", SwingConstants.CENTER);
        etiquetaPregunta.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        ventana.add(etiquetaPregunta, gbc);
        botonesRespuesta = new JButton[3];
        for (int i = 0; i < 3; i++)
        {
            botonesRespuesta[i] = new JButton();
            botonesRespuesta[i].setFont(new Font("Arial", Font.PLAIN, 14));
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            gbc.gridwidth = 3;
            ventana.add(botonesRespuesta[i], gbc);
            final int index = i;
            botonesRespuesta[i].addActionListener(e -> responderPregunta(index));
        }

        cargarPreguntas();
        if (!preguntas.isEmpty())
        {
            idTest = crearNuevoTest();
            mostrarPregunta();
        }
        else
        {
            JOptionPane.showMessageDialog(ventana, "No hay preguntas disponibles.", "Error", JOptionPane.ERROR_MESSAGE);
            ventana.dispose();
        }

        ventana.setVisible(true);
    }

    private void cargarPreguntas()
    {
        try (Connection conn = DriverManager.getConnection(URL_BASE_DATOS, USUARIO, CONTRASEÑA))
        {
            Statement debugStmt = conn.createStatement();
            ResultSet debugRs = debugStmt.executeQuery("DESCRIBE Historia");
            System.out.println("Historia columns:");
            while (debugRs.next())
            {
                System.out.println("- " + debugRs.getString("Field"));
            }
            debugRs.close();
            debugStmt.close();
            String sqlQuestions = "SELECT IDPREGUNTA, RESPUESTAS FROM Ejercicios WHERE ID IS NULL AND IDT IS NULL ORDER BY RAND() LIMIT 30";
            Statement stmtQuestions = conn.createStatement();
            ResultSet rsQuestions = stmtQuestions.executeQuery(sqlQuestions);

            while (rsQuestions.next())
            {
                int idPregunta = rsQuestions.getInt("IDPREGUNTA");
                String preguntaTexto = rsQuestions.getString("RESPUESTAS");
                String sqlAnswers = "SELECT CORRECCION FROM Historia WHERE IDPREGUNTA = ? AND ID IS NULL AND IDT IS NULL";
                PreparedStatement stmtAnswers = conn.prepareStatement(sqlAnswers);
                stmtAnswers.setInt(1, idPregunta);
                ResultSet rsAnswers = stmtAnswers.executeQuery();
                List<String> respuestas = new ArrayList<>();
                List<String> correccionesCompletas = new ArrayList<>();

                while (rsAnswers.next())
                {
                    String correccion = rsAnswers.getString("CORRECCION");
                    String respuestaLimpia = correccion.replaceAll("^(CORRECTA|INCORRECTA):\\s*", "");
                    respuestas.add(respuestaLimpia);
                    correccionesCompletas.add(correccion);
                }

                rsAnswers.close();
                stmtAnswers.close();

                if (respuestas.size() == 3)
                {
                    preguntas.add(new Pregunta(idPregunta, preguntaTexto, respuestas, correccionesCompletas));
                }
            }

            rsQuestions.close();
            stmtQuestions.close();
            Collections.shuffle(preguntas);
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(ventana, "Error al cargar preguntas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private int crearNuevoTest()
    {
        try (Connection conn = DriverManager.getConnection(URL_BASE_DATOS, USUARIO, CONTRASEÑA))
        {
            String sqlMaxId = "SELECT MAX(IDT) AS max_id FROM Tests";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlMaxId);
            int nuevoId = 1;
            if (rs.next() && rs.getInt("max_id") > 0)
            {
                nuevoId = rs.getInt("max_id") + 1;
            }
            rs.close();
            stmt.close();
            String sqlInsert = "INSERT INTO Tests (IDT, ID) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sqlInsert);
            pstmt.setInt(1, nuevoId);
            pstmt.setInt(2, idAlumno);
            pstmt.executeUpdate();
            pstmt.close();
            System.out.println("Created test with IDT: " + nuevoId);
            return nuevoId;
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(ventana, "Error al crear test: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return -1;
        }
    }

    private void mostrarPregunta()
    {
        if (preguntaActual >= preguntas.size())
        {
            guardarResultados();
            JOptionPane.showMessageDialog(ventana, "Test finalizado.", "Fin", JOptionPane.INFORMATION_MESSAGE);
            ventana.dispose();
            return;
        }

        Pregunta pregunta = preguntas.get(preguntaActual);
        etiquetaPregunta.setText("Pregunta " + (preguntaActual + 1) + ": " + pregunta.textoPregunta);
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < pregunta.respuestas.size(); i++)
        {
            indices.add(i);
        }
        Collections.shuffle(indices);

        for (int i = 0; i < 3; i++)
        {
            int idx = indices.get(i);
            botonesRespuesta[i].setText(pregunta.respuestas.get(idx));
            botonesRespuesta[i].putClientProperty("index", idx);
        }
    }

    private void responderPregunta(int botonIndex)
    {
        Pregunta pregunta = preguntas.get(preguntaActual);
        int respuestaIndex = (int) botonesRespuesta[botonIndex].getClientProperty("index");
        respuestasSeleccionadas.add(pregunta.correccionesCompletas.get(respuestaIndex));
        preguntaActual++;
        mostrarPregunta();
    }

    private void guardarResultados()
    {
        try (Connection conn = DriverManager.getConnection(URL_BASE_DATOS, USUARIO, CONTRASEÑA))
        {
            String sqlMaxId = "SELECT MAX(CorrectID) AS max_id FROM Historia";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlMaxId);
            int nextCorrectId = 1;
            if (rs.next() && rs.getInt("max_id") > 0)
            {
                nextCorrectId = rs.getInt("max_id") + 1;
            }
            rs.close();
            stmt.close();
            String sqlInsert = "INSERT INTO Historia (CorrectID, IDPREGUNTA, CORRECCION, ID, IDT) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sqlInsert);

            for (int i = 0; i < preguntas.size(); i++)
            {
                Pregunta pregunta = preguntas.get(i);
                String correccion = respuestasSeleccionadas.get(i);
                pstmt.setInt(1, nextCorrectId + i);
                pstmt.setInt(2, pregunta.idPregunta);
                pstmt.setString(3, correccion);
                pstmt.setInt(4, idAlumno);
                pstmt.setInt(5, idTest);
                pstmt.addBatch();
                System.out.println("Saving: CorrectID=" + (nextCorrectId + i) + ", IDPREGUNTA=" + pregunta.idPregunta + ", CORRECCION=" + correccion + ", ID=" + idAlumno + ", IDT=" + idTest);
            }

            int[] results = pstmt.executeBatch();
            System.out.println("Batch insert completed, rows affected: " + results.length);
            pstmt.close();
        }
        catch (SQLException e)
        {
            JOptionPane.showMessageDialog(ventana, "Error al guardar resultados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private static class Pregunta
    {
        int idPregunta;
        String textoPregunta;
        List<String> respuestas;
        List<String> correccionesCompletas;

        Pregunta(int idPregunta, String textoPregunta, List<String> respuestas, List<String> correccionesCompletas)
        {
            this.idPregunta = idPregunta;
            this.textoPregunta = textoPregunta;
            this.respuestas = new ArrayList<>(respuestas);
            this.correccionesCompletas = new ArrayList<>(correccionesCompletas);
        }
    }
}