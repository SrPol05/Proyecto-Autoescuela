package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Optional;

public class MainInterface {
    private JFrame ventana;
    private JButton botonAlumno, botonProfesor, botonAdmin;
    private JButton botonTest, botonHistoria, botonCalendarioAlumno;
    private JButton botonCalendarioDocente, botonNotas;
    private JButton botonEditorTeoria, botonListadoAlumnos, botonListadoEmpleados;
    private JButton botonVolver;
    private JLabel etiquetaBienvenida;
    private int idAlumno;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainInterface().initialize());
    }

    private void initialize() {
        ventana = new JFrame("Autoescuela Alpha-0.8");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(600, 600);
        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        etiquetaBienvenida = new JLabel("", SwingConstants.CENTER);
        etiquetaBienvenida.setFont(new Font("Arial", Font.PLAIN, 16));
        etiquetaBienvenida.setVisible(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 10, 10, 10);
        ventana.add(etiquetaBienvenida, gbc);

        createMainButtons(gbc);
        createAlumnoButtons(gbc);
        createProfesorButtons(gbc);
        createAdminButtons(gbc);
        createBackButton(gbc);

        showMainButtons();
        ventana.setVisible(true);
    }

    private void createMainButtons(GridBagConstraints gbc) {
        botonAlumno = createStyledButton("Alumno", 200, 60);
        gbc.gridx = 0;
        gbc.gridy = 1;
        ventana.add(botonAlumno, gbc);

        botonProfesor = createStyledButton("Profesor", 200, 60);
        gbc.gridx = 0;
        gbc.gridy = 2;
        ventana.add(botonProfesor, gbc);

        botonAdmin = createStyledButton("Admin", 200, 60);
        gbc.gridx = 0;
        gbc.gridy = 3;
        ventana.add(botonAdmin, gbc);

        botonAlumno.addActionListener(e -> mostrarDialogoDNI());
        botonProfesor.addActionListener(e -> showProfesorButtons());
        botonAdmin.addActionListener(e -> showAdminButtons());
    }

    private void createAlumnoButtons(GridBagConstraints gbc) {
        botonTest = createStyledButton("Test", 200, 60);
        gbc.gridx = 0;
        gbc.gridy = 1;
        ventana.add(botonTest, gbc);

        botonHistoria = createStyledButton("Historia", 200, 60);
        gbc.gridx = 0;
        gbc.gridy = 2;
        ventana.add(botonHistoria, gbc);

        botonCalendarioAlumno = createStyledButton("Calendario", 200, 60);
        gbc.gridx = 0;
        gbc.gridy = 3;
        ventana.add(botonCalendarioAlumno, gbc);

        botonTest.addActionListener(e -> new TestInterface(idAlumno));
        botonHistoria.addActionListener(e -> new HistoryInterface(idAlumno));
    }

    private void createProfesorButtons(GridBagConstraints gbc) {
        botonCalendarioDocente = createStyledButton("Calendario Docente", 250, 60);
        gbc.gridx = 0;
        gbc.gridy = 1;
        ventana.add(botonCalendarioDocente, gbc);

        botonNotas = createStyledButton("Notas", 250, 60);
        gbc.gridx = 0;
        gbc.gridy = 2;
        ventana.add(botonNotas, gbc);
    }

    private void createAdminButtons(GridBagConstraints gbc) {
        botonEditorTeoria = createStyledButton("Editor de Teoría", 250, 60);
        gbc.gridx = 0;
        gbc.gridy = 1;
        ventana.add(botonEditorTeoria, gbc);

        botonListadoAlumnos = createStyledButton("Listado de Alumnos", 250, 60);
        gbc.gridx = 0;
        gbc.gridy = 2;
        ventana.add(botonListadoAlumnos, gbc);

        botonListadoEmpleados = createStyledButton("Listado de Empleados", 250, 60);
        gbc.gridx = 0;
        gbc.gridy = 3;
        ventana.add(botonListadoEmpleados, gbc);

        botonListadoAlumnos.addActionListener(e -> {
            DatabaseStudentList studentList = new DatabaseStudentList();
            studentList.showStudentList();
        });
    }

    private void createBackButton(GridBagConstraints gbc) {
        botonVolver = new JButton("<-");
        botonVolver.setPreferredSize(new Dimension(50, 30));
        botonVolver.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        gbc.fill = GridBagConstraints.NONE;
        ventana.add(botonVolver, gbc);

        botonVolver.addActionListener(e -> showMainButtons());
    }

    private JButton createStyledButton(String text, int width, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setVisible(false);
        return button;
    }

    private void mostrarDialogoDNI() {
        JDialog dialogoDNI = new JDialog(ventana, "Ingresar DNI", true);
        dialogoDNI.setSize(300, 150);
        dialogoDNI.setLocationRelativeTo(ventana);
        dialogoDNI.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel etiquetaDNI = new JLabel("DNI:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialogoDNI.add(etiquetaDNI, gbc);

        JTextField campoDNI = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        dialogoDNI.add(campoDNI, gbc);

        JButton botonAceptar = new JButton("Aceptar");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        dialogoDNI.add(botonAceptar, gbc);

        botonAceptar.addActionListener(e -> {
            String dni = campoDNI.getText().trim();
            if (dni.isEmpty()) {
                JOptionPane.showMessageDialog(dialogoDNI, "Por favor, ingrese un DNI.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (dni.length() > 10) {
                JOptionPane.showMessageDialog(dialogoDNI, "El DNI no puede exceder los 10 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DatabaseStudentManager manager = new DatabaseStudentManager();
            Optional<String> nombreOpt = manager.buscarNombrePorDNI(dni);

            if (nombreOpt.isPresent()) {
                idAlumno = buscarIdPorDNI(dni);
                dialogoDNI.dispose();
                etiquetaBienvenida.setText("Bienvenid@ de vuelta " + nombreOpt.get());
                showAlumnoButtons();
            } else {
                dialogoDNI.dispose();
                mostrarDialogoNuevoAlumno();
            }
        });

        dialogoDNI.setVisible(true);
    }

    private int buscarIdPorDNI(String dni) {
        try (Connection conn = DriverManager.getConnection(DatabaseStudentManager.URL_BASE_DATOS, DatabaseStudentManager.USUARIO, DatabaseStudentManager.CONTRASEÑA)) {
            String sql = "SELECT ID FROM Alumnos WHERE DNI = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("ID");
                rs.close();
                stmt.close();
                return id;
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void mostrarDialogoNuevoAlumno() {
        JDialog dialogoNuevo = new JDialog(ventana, "Registrar Nuevo Alumno", true);
        dialogoNuevo.setSize(300, 250);
        dialogoNuevo.setLocationRelativeTo(ventana);
        dialogoNuevo.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel etiquetaDNI = new JLabel("DNI:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialogoNuevo.add(etiquetaDNI, gbc);

        JTextField campoDNI = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        dialogoNuevo.add(campoDNI, gbc);

        JLabel etiquetaNombre = new JLabel("Nombre:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        dialogoNuevo.add(etiquetaNombre, gbc);

        JTextField campoNombre = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        dialogoNuevo.add(campoNombre, gbc);

        JLabel etiquetaApellidos = new JLabel("Apellidos:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        dialogoNuevo.add(etiquetaApellidos, gbc);

        JTextField campoApellidos = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        dialogoNuevo.add(campoApellidos, gbc);

        JButton botonRegistrar = new JButton("Registrar");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        dialogoNuevo.add(botonRegistrar, gbc);

        botonRegistrar.addActionListener(e -> {
            String dni = campoDNI.getText().trim();
            String nombre = campoNombre.getText().trim();
            String apellidos = campoApellidos.getText().trim();

            if (dni.isEmpty() || nombre.isEmpty() || apellidos.isEmpty()) {
                JOptionPane.showMessageDialog(dialogoNuevo, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (dni.length() > 10) {
                JOptionPane.showMessageDialog(dialogoNuevo, "El DNI no puede exceder los 10 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (nombre.length() > 133) {
                JOptionPane.showMessageDialog(dialogoNuevo, "El nombre no puede exceder los 133 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (apellidos.length() > 133) {
                JOptionPane.showMessageDialog(dialogoNuevo, "Los apellidos no pueden exceder los 133 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DatabaseStudentManager manager = new DatabaseStudentManager();
            DatabaseStudentManager.ResultadoInsercion resultado = manager.agregarNuevoAlumno(dni, nombre, apellidos);

            if (resultado.esExitoso()) {
                idAlumno = buscarIdPorDNI(dni);
                JOptionPane.showMessageDialog(dialogoNuevo, resultado.getMensaje(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dialogoNuevo.dispose();
                etiquetaBienvenida.setText("Bienvenid@ de vuelta " + nombre);
                showAlumnoButtons();
            } else {
                JOptionPane.showMessageDialog(dialogoNuevo, resultado.getMensaje(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialogoNuevo.setVisible(true);
    }

    private void showMainButtons() {
        setAllButtonsVisible(false);
        botonAlumno.setVisible(true);
        botonProfesor.setVisible(true);
        botonAdmin.setVisible(true);
        botonVolver.setVisible(false);
        etiquetaBienvenida.setVisible(false);
    }

    private void showAlumnoButtons() {
        setAllButtonsVisible(false);
        etiquetaBienvenida.setVisible(true);
        botonTest.setVisible(true);
        botonHistoria.setVisible(true);
        botonCalendarioAlumno.setVisible(true);
        botonVolver.setVisible(true);
    }

    private void showProfesorButtons() {
        setAllButtonsVisible(false);
        botonCalendarioDocente.setVisible(true);
        botonNotas.setVisible(true);
        botonVolver.setVisible(true);
        etiquetaBienvenida.setVisible(false);
    }

    private void showAdminButtons() {
        setAllButtonsVisible(false);
        botonEditorTeoria.setVisible(true);
        botonListadoAlumnos.setVisible(true);
        botonListadoEmpleados.setVisible(true);
        botonVolver.setVisible(true);
        etiquetaBienvenida.setVisible(false);
    }

    private void setAllButtonsVisible(boolean visible) {
        botonAlumno.setVisible(visible);
        botonProfesor.setVisible(visible);
        botonAdmin.setVisible(visible);
        botonTest.setVisible(visible);
        botonHistoria.setVisible(visible);
        botonCalendarioAlumno.setVisible(visible);
        botonCalendarioDocente.setVisible(visible);
        botonNotas.setVisible(visible);
        botonEditorTeoria.setVisible(visible);
        botonListadoAlumnos.setVisible(visible);
        botonListadoEmpleados.setVisible(visible);
        botonVolver.setVisible(visible);
    }
}
