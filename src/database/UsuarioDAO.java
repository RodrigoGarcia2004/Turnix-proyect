package database;

import java.sql.*;
import servidor.DBConnection; // Mantenemos tu conexión original
import modelo.Usuario;

public class UsuarioDAO {

    /**
     * Registra un nuevo paciente. Mantenemos tu lógica original.
     */
    public static boolean registrar(String username, String password, String nombreReal) throws Exception {
        String sql = "INSERT INTO usuarios (nombre, usuario, password, rol, nombre_completo) VALUES (?, ?, ?, 'PACIENTE', ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nombreReal);     
            ps.setString(2, username);       
            ps.setString(3, password);       
            ps.setString(4, nombreReal);     
            
            int filas = ps.executeUpdate();
            return filas > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ ERROR SQL EN REGISTRO:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Autentica al usuario. Mantenemos tu lógica original.
     */
    public static Usuario autenticar(String username, String password) throws Exception {
        String sql = "SELECT id, nombre, rol, email FROM usuarios WHERE usuario = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getInt("id"),
                        username,
                        Usuario.Rol.valueOf(rs.getString("rol").toUpperCase()),
                        rs.getString("nombre"), 
                        rs.getString("email")
                    );
                }
            }
        }
        return null;
    }

    // ============================================================
    // NUEVAS FUNCIONALIDADES DE VALORACIÓN (ESTRELLAS)
    // ============================================================

    /**
     * Actualiza la suma de estrellas y el contador de votos, 
     * luego calcula y devuelve la nueva media.
     * @throws Exception 
     */
    public static double[] obtenerReputacionActual(String username) throws Exception {
        double media = 0.0;
        int totalVotos = 0;
        String sql = "SELECT suma_valoraciones, total_votos FROM usuarios WHERE usuario = ?";
        
        try (Connection conn = servidor.DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double suma = rs.getDouble("suma_valoraciones");
                    totalVotos = rs.getInt("total_votos");
                    if (totalVotos > 0) media = Math.round((suma / totalVotos) * 10.0) / 10.0;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return new double[]{media, totalVotos};
    }
    
    
    
    public static double[] actualizarYObtenerReputacion(String username, double nuevaNota) {
        double media = 0.0;
        int totalVotos = 0;
        
        // SQL para actualizar: suma la nota a la columna existente e incrementa votos
        String sqlUpdate = "UPDATE usuarios SET suma_valoraciones = suma_valoraciones + ?, total_votos = total_votos + 1 WHERE usuario = ?";
        
        // SQL para leer: obtenemos los nuevos valores para calcular la media
        String sqlSelect = "SELECT suma_valoraciones, total_votos FROM usuarios WHERE usuario = ?";
        
        try (Connection conn = DBConnection.getConnection()) {
            
            // 1. Actualizar datos
            try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                psUpdate.setDouble(1, nuevaNota);
                psUpdate.setString(2, username);
                psUpdate.executeUpdate();
            }
            
            // 2. Consultar totales actualizados
            try (PreparedStatement psSelect = conn.prepareStatement(sqlSelect)) {
                psSelect.setString(1, username);
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        double sumaTotal = rs.getDouble("suma_valoraciones");
                        totalVotos = rs.getInt("total_votos");
                        
                        if (totalVotos > 0) {
                            // Calculamos la media y redondeamos a 1 decimal
                            media = Math.round((sumaTotal / totalVotos) * 10.0) / 10.0;
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ ERROR AL ACTUALIZAR REPUTACIÓN:");
            e.printStackTrace();
        }
        
        // Retornamos un array: [0] es la media, [1] es el total de votos
        return new double[]{media, (double)totalVotos};
    }
}