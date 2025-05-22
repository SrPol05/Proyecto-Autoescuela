package org.example;

import java.sql.*;
import java.util.Optional;

public class DatabaseStudentManager
{
    static final String URL_BASE_DATOS = "jdbc:mysql://autoescuela.cxyekqoc86sq.us-east-1.rds.amazonaws.com:3306/autoescuela";
    static final String USUARIO = "admin";
    static final String CONTRASEÑA = "Qwerty2005#";

    public Optional<String> buscarNombrePorDNI(String dni)
    {
        try
        {
            Connection conn = DriverManager.getConnection(URL_BASE_DATOS, USUARIO, CONTRASEÑA);
            String sql = "SELECT NOMBRE FROM Alumnos WHERE DNI = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                String nombre = rs.getString("NOMBRE");
                rs.close();
                stmt.close();
                conn.close();
                return Optional.of(nombre);
            }

            rs.close();
            stmt.close();
            conn.close();
            return Optional.empty();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static class ResultadoInsercion
    {
        private final boolean exito;
        private final String mensaje;

        public ResultadoInsercion(boolean exito, String mensaje)
        {
            this.exito = exito;
            this.mensaje = mensaje;
        }

        public boolean esExitoso()
        {
            return exito;
        }

        public String obtenerMensaje()
        {
            return mensaje;
        }
    }

    public ResultadoInsercion agregarNuevoAlumno(String dni, String nombre, String apellidos)
    {
        if (dni.length() > 10)
        {
            return new ResultadoInsercion(false, "El DNI no puede exceder los 10 caracteres.");
        }
        if (nombre.length() > 133)
        {
            return new ResultadoInsercion(false, "El nombre no puede exceder los 133 caracteres.");
        }
        if (apellidos.length() > 133)
        {
            return new ResultadoInsercion(false, "Los apellidos no pueden exceder los 133 caracteres.");
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = DriverManager.getConnection(URL_BASE_DATOS, USUARIO, CONTRASEÑA);
            String sqlMaxId = "SELECT MAX(ID) AS max_id FROM Alumnos";
            stmt = conn.prepareStatement(sqlMaxId);
            rs = stmt.executeQuery();
            int nuevoId = 1;
            if (rs.next() && rs.getInt("max_id") > 0)
            {
                nuevoId = rs.getInt("max_id") + 1;
            }
            rs.close();
            stmt.close();
            String sqlInsert = "INSERT INTO Alumnos (ID, DNI, NOMBRE, APELLIDOS) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sqlInsert);
            stmt.setInt(1, nuevoId);
            stmt.setString(2, dni);
            stmt.setString(3, nombre);
            stmt.setString(4, apellidos);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0)
            {
                return new ResultadoInsercion(true, "Alumno registrado con éxito con ID: " + nuevoId);
            }
            else
            {
                return new ResultadoInsercion(false, "No se pudo registrar el alumno.");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            if (e.getErrorCode() == 1062)
            {
                return new ResultadoInsercion(false, "El DNI ya está registrado en la base de datos.");
            }
            return new ResultadoInsercion(false, "Error al registrar el alumno: " + e.getMessage());
        }
        finally
        {
            try
            {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
}