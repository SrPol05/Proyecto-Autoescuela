import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainInterface {
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel alumnoPanel;
    private JPanel profesorPanel;
    private JPanel adminPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new MainInterface().initialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initialize() {
        // Configuración de la ventana principal
        frame = new JFrame("Sistema Educativo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Crear los paneles
        createMainPanel();
        createAlumnoPanel();
        createProfesorPanel();
        createAdminPanel();

        // Mostrar el panel principal inicialmente
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    private void createMainPanel() {
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        // Botón Alumno
        JButton alumnoBtn = new JButton("Alumno");
        alumnoBtn.setPreferredSize(new Dimension(200, 60));
        alumnoBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(alumnoBtn, gbc);

        // Botón Profesor
        JButton profesorBtn = new JButton("Profesor");
        profesorBtn.setPreferredSize(new Dimension(200, 60));
        profesorBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(profesorBtn, gbc);

        // Botón Admin
        JButton adminBtn = new JButton("Admin");
        adminBtn.setPreferredSize(new Dimension(200, 60));
        adminBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(adminBtn, gbc);

        // Action Listeners
        alumnoBtn.addActionListener(e -> frame.setContentPane(alumnoPanel));
        profesorBtn.addActionListener(e -> frame.setContentPane(profesorPanel));
        adminBtn.addActionListener(e -> frame.setContentPane(adminPanel));
    }

    private void createAlumnoPanel() {
        alumnoPanel = new JPanel(new GridBagLayout());
        alumnoPanel.setBackground(new Color(220, 230, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        // Botón Test
        JButton testBtn = new JButton("Test");
        testBtn.setPreferredSize(new Dimension(200, 60));
        testBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        alumnoPanel.add(testBtn, gbc);

        // Botón Historia
        JButton historiaBtn = new JButton("Historia");
        historiaBtn.setPreferredSize(new Dimension(200, 60));
        historiaBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        alumnoPanel.add(historiaBtn, gbc);

        // Botón Calendario
        JButton calendarioBtn = new JButton("Calendario");
        calendarioBtn.setPreferredSize(new Dimension(200, 60));
        calendarioBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        alumnoPanel.add(calendarioBtn, gbc);

        // Botón de regreso
        JButton backBtn = new JButton("<-");
        backBtn.setPreferredSize(new Dimension(50, 30));
        backBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        gbc.fill = GridBagConstraints.NONE;
        alumnoPanel.add(backBtn, gbc);

        backBtn.addActionListener(e -> frame.setContentPane(mainPanel));
    }

    private void createProfesorPanel() {
        profesorPanel = new JPanel(new GridBagLayout());
        profesorPanel.setBackground(new Color(240, 220, 230));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        // Botón Calendario Docente
        JButton calendarioBtn = new JButton("Calendario Docente");
        calendarioBtn.setPreferredSize(new Dimension(250, 60));
        calendarioBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        profesorPanel.add(calendarioBtn, gbc);

        // Botón Notas
        JButton notasBtn = new JButton("Notas");
        notasBtn.setPreferredSize(new Dimension(250, 60));
        notasBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        profesorPanel.add(notasBtn, gbc);

        // Botón de regreso
        JButton backBtn = new JButton("<-");
        backBtn.setPreferredSize(new Dimension(50, 30));
        backBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        gbc.fill = GridBagConstraints.NONE;
        profesorPanel.add(backBtn, gbc);

        backBtn.addActionListener(e -> frame.setContentPane(mainPanel));
    }

    private void createAdminPanel() {
        adminPanel = new JPanel(new GridBagLayout());
        adminPanel.setBackground(new Color(230, 240, 220));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        // Botón Editor de Teoría
        JButton editorBtn = new JButton("Editor de Teoría");
        editorBtn.setPreferredSize(new Dimension(250, 60));
        editorBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        adminPanel.add(editorBtn, gbc);

        // Botón Listado de Alumnos
        JButton alumnosBtn = new JButton("Listado de Alumnos");
        alumnosBtn.setPreferredSize(new Dimension(250, 60));
        alumnosBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        adminPanel.add(alumnosBtn, gbc);

        // Botón Listado de Empleados
        JButton empleadosBtn = new JButton("Listado de Empleados");
        empleadosBtn.setPreferredSize(new Dimension(250, 60));
        empleadosBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        adminPanel.add(empleadosBtn, gbc);

        // Botón de regreso
        JButton backBtn = new JButton("<-");
        backBtn.setPreferredSize(new Dimension(50, 30));
        backBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        gbc.fill = GridBagConstraints.NONE;
        adminPanel.add(backBtn, gbc);

        backBtn.addActionListener(e -> frame.setContentPane(mainPanel));
    }
}
