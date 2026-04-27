package database;

import java.sql.*;
import modelo.Turno;
import servidor.DBConnection;

public class TurnoDAO {

    /**
     * Guarda un nuevo turno en la base de datos calculando el siguiente número.
     */
	public static Turno guardarTurno(Turno turno) {
	    // Añadimos 'fecha' a la consulta INSERT usando NOW() de MySQL
	    String queryMax = "SELECT IFNULL(MAX(numero_turno), 0) + 1 FROM turnos";
	    String insert = "INSERT INTO turnos (numero_turno, cliente, estado, fecha) VALUES (?, ?, ?, NOW())";

	    try (Connection conn = DBConnection.getConnection()) {
	        try (Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(queryMax)) {
	            if (rs.next()) {
	                turno.setNumeroTurno(rs.getInt(1));
	            }
	        }

	        try (PreparedStatement ps = conn.prepareStatement(insert)) {
	            ps.setInt(1, turno.getNumeroTurno());
	            ps.setString(2, turno.getCliente());
	            ps.setString(3, "EN_ESPERA"); // Lo forzamos a mayúsculas aquí
	            ps.executeUpdate();
	        }
	        System.out.println("DB: Turno #" + turno.getNumeroTurno() + " creado con éxito.");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return turno;
	}

    /**
     * Obtiene una cadena con todos los turnos en espera.
     */
    public static String obtenerTurnos() {
        StringBuilder resultado = new StringBuilder();
        String query = "SELECT numero_turno, cliente, estado FROM turnos WHERE estado = 'EN_ESPERA' ORDER BY numero_turno ASC";        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                resultado.append("Turno ")
                         .append(rs.getInt("numero_turno"))
                         .append(" | Cliente: ")
                         .append(rs.getString("cliente"))
                         .append(" | Estado: ")
                         .append(rs.getString("estado"))
                         .append("\n");
            }
            
            if (resultado.length() == 0) {
                resultado.append("No hay turnos en la cola.\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error al conectar con la base de datos.\n";
        }
        return resultado.toString();
    }

    /**
     * Lógica para el Administrador: Atiende el primer turno, lo registra en historial y lo borra de la cola.
     */
    public static boolean atenderSiguiente() {
        // 1. Buscamos el primer turno que esté EN_ESPERA
        String sel = "SELECT id, numero_turno, cliente FROM turnos WHERE UPPER(TRIM(estado)) = 'EN_ESPERA' ORDER BY numero_turno ASC LIMIT 1";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sel)) {
            
            if (rs.next()) {
                int idFila = rs.getInt("id");
                int numTurno = rs.getInt("numero_turno");

                // 2. Insertar en historial (Esto funciona porque el turno aún existe)
                String sqlHistorial = "INSERT INTO historial (id_turno, fecha_atendido) VALUES (?, NOW())";
                try (PreparedStatement psHist = conn.prepareStatement(sqlHistorial)) {
                    psHist.setInt(1, idFila); // Usamos el ID de la fila para la relación
                    psHist.executeUpdate();
                }

                // 3. EN LUGAR DE DELETE, HACEMOS UPDATE
                // Cambiamos el estado para que no vuelva a salir en la cola
                String sqlUpdate = "UPDATE turnos SET estado = 'ATENDIDO' WHERE id = ?";
                try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                    psUpdate.setInt(1, idFila);
                    psUpdate.executeUpdate();
                }
                
                System.out.println("✅ DEBUG: Turno " + numTurno + " marcado como ATENDIDO.");
                return true;
            }
        } catch (Exception e) {
            System.err.println("❌ ERROR en atenderSiguiente: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Registra un mensaje vinculado a la atención actual.
     * Mejora la versión anterior gestionando la ausencia de turnos activos.
     * @throws Exception 
     */
    public static boolean registrarMensaje(int idEmisor, String contenido) throws Exception {
        // SQL optimizado con subconsultas para vincular el mensaje al turno 'EN_ESPERA' del usuario
        String sql = "INSERT INTO mensajes (id_turno, emisor_id, contenido, fecha_envio) VALUES " +
                     "((SELECT id FROM turnos WHERE cliente = (SELECT usuario FROM usuarios WHERE id = ?) " +
                     "AND estado = 'EN_ESPERA' LIMIT 1), ?, ?, NOW())";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idEmisor);
            ps.setInt(2, idEmisor);
            ps.setString(3, contenido);
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            // Log técnico para el desarrollador (cumple con 'gestión de errores')
            System.err.println("Error de persistencia en mensajes: " + e.getMessage());
            // No lanzamos la excepción hacia arriba para evitar que el hilo del ClienteHandler muera
            return false;
        }
    }
    public static boolean registrarArchivo(int idEmisor, String nombreFichero, String ruta) throws Exception {
        String sql = "INSERT INTO documentos (id_turno, nombre_original, ruta_servidor) VALUES " +
                     "((SELECT id FROM turnos WHERE cliente = (SELECT usuario FROM usuarios WHERE id = ?) " +
                     "AND estado = 'EN_ESPERA' LIMIT 1), ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmisor);
            ps.setString(2, nombreFichero);
            ps.setString(3, ruta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static String obtenerInteraccionesTurnoActual() throws Exception {
        StringBuilder sb = new StringBuilder();
        // Consulta compleja para unir mensajes y documentos del turno más antiguo 'EN_ESPERA'
        String sql = "SELECT 'MSG' as tipo, m.contenido as detalle, m.fecha_envio as fecha " +
                     "FROM mensajes m WHERE m.id_turno = (SELECT id FROM turnos WHERE estado = 'EN_ESPERA' ORDER BY id ASC LIMIT 1) " +
                     "UNION " +
                     "SELECT 'DOC' as tipo, d.nombre_original as detalle, d.subido_en as fecha " +
                     "FROM documentos d WHERE d.id_turno = (SELECT id FROM turnos WHERE estado = 'EN_ESPERA' ORDER BY id ASC LIMIT 1) " +
                     "ORDER BY fecha ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                sb.append("[").append(rs.getString("tipo")).append("] ")
                  .append(rs.getString("detalle")).append(" (")
                  .append(rs.getTimestamp("fecha")).append(")\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "ERROR: No se pudieron recuperar las interacciones.";
        }
        return sb.length() > 0 ? sb.toString() : "No hay mensajes ni documentos para este turno.";
    }
    public static boolean asignarTurnoAMedico(int idMedico) throws Exception {
        // Buscamos el primer turno en espera y lo asignamos a este médico cambiando su estado a 'EN_CONSULTA'
        String sql = "UPDATE turnos SET estado = 'EN_CONSULTA', atendido_por = ? " +
                     "WHERE estado = 'EN_ESPERA' ORDER BY id ASC LIMIT 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMedico);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static int obtenerIdTurnoEnConsulta() throws Exception {
        // Buscamos el turno más antiguo que aún no ha sido completado
        String sql = "SELECT id FROM turnos WHERE estado = 'EN_ESPERA' ORDER BY id ASC LIMIT 1";
        
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ID de turno: " + e.getMessage());
        }
        return -1; // Retorna -1 si no hay turnos pendientes
    }

    /**
     * Cambia el estado del turno a 'COMPLETADO'.
     * Esto permite que el sistema sepa que ese paciente ya fue atendido.
     * @throws Exception 
     */
    public static boolean finalizarConsulta(int idTurno) throws Exception {
        String sql = "UPDATE turnos SET estado = 'COMPLETADO' WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idTurno);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al finalizar consulta: " + e.getMessage());
            return false;
        }
    }
}