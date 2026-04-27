package servidor;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.Queue;
import database.UsuarioDAO;
import modelo.Usuario;

public class ServidorWeb extends WebSocketServer {

    private Queue<WebSocket> colaEspera = new LinkedList<>();

    public ServidorWeb(int puerto) {
        super(new InetSocketAddress(puerto));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("✅ Nueva conexión: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("📥 Mensaje: " + message);

        // ==================== LOGIN ====================
        if (message.startsWith("login:")) {
            String[] datos = message.split(":");
            if (datos.length >= 3) {
                try {
                    Usuario u = UsuarioDAO.autenticar(datos[1], datos[2]);
                    if (u != null) {
                        String nombre = (u.getNombreCompleto() != null && !u.getNombreCompleto().equals("null"))
                                ? u.getNombreCompleto() : u.getUsername();
                        conn.setAttachment(u);
                        conn.send("LOGIN_OK:" + u.getRol() + ":" + nombre);
                    } else {
                        conn.send("ERROR: Usuario o clave incorrectos");
                    }
                } catch (Exception e) {
                    conn.send("ERROR: Fallo en autenticación");
                }
            }
        }

        // ==================== REGISTRO ====================
        else if (message.startsWith("registro:")) {
            String[] datos = message.split(":");
            if (datos.length >= 4) {
                try {
                    boolean exito = UsuarioDAO.registrar(datos[1], datos[2], datos[3]);
                    conn.send(exito ? "REGISTRO_OK" : "REGISTRO_ERROR");
                } catch (Exception e) {
                    conn.send("REGISTRO_ERROR");
                }
            }
        }

        // ==================== PEDIR TURNO ====================
        else if (message.equals("PEDIR_TURNO")) {
            Usuario u = (Usuario) conn.getAttachment();
            if (u != null) {
                boolean yaTiene = false;
                for (WebSocket ws : colaEspera) {
                    Usuario usr = ws.getAttachment();
                    if (usr != null && usr.getId() == u.getId()) { yaTiene = true; break; }
                }
                if (yaTiene) {
                    conn.send("ERROR: Ya tienes un turno en espera.");
                    return;
                }

                String nombre = (u.getNombreCompleto() != null && !u.getNombreCompleto().equals("null"))
                        ? u.getNombreCompleto() : u.getUsername();
                modelo.Turno t = database.GestorTurnos.pedirTurno(nombre);
                if (t != null) {
                    colaEspera.add(conn);
                    String msg = "TURNO_ASIGNADO: " + nombre + " (Turno #" + t.getNumeroTurno() + ")";
                    for (WebSocket c : getConnections()) c.send(msg);
                }
            }
        }

        // ==================== LLAMAR SIGUIENTE TURNO (CORREGIDO) ====================
     // ==================== LLAMAR SIGUIENTE TURNO (VERSIÓN DEFINITIVA) ====================
     // ==================== LLAMAR SIGUIENTE TURNO (CON APERTURA AUTOMÁTICA EN MÉDICO) ====================
        else if (message.equals("LLAMAR_SIGUIENTE")) {
            if (!colaEspera.isEmpty()) {
                WebSocket pacienteConn = colaEspera.poll();
                actualizarPosicionesCola();

                Usuario medico = (Usuario) conn.getAttachment();
                String nombreMedico = (medico != null) ? medico.getUsername() : "Médico";

                // 1. Avisamos al paciente
                if (pacienteConn != null && pacienteConn.isOpen()) {
                    Usuario pacienteUser = (Usuario) pacienteConn.getAttachment();
                    String nombrePaciente = (pacienteUser != null) ? pacienteUser.getUsername() : "Paciente";

                    pacienteConn.send("SISTEMA: LLAMADA_A_CONSULTA");
                    pacienteConn.send("COMANDO:ENTRAR_CONSULTA:" + nombreMedico);

                    // 2. Avisamos al médico para que abra el chat automáticamente
                    conn.send("COMANDO:ABRIR_CHAT:" + nombrePaciente);
                }
            } else {
                conn.send("ERROR: No hay pacientes en espera.");
            }
        }
        // ==================== CHAT ====================
        else if (message.startsWith("CHAT_PRIVADO:")) {
            String[] partes = message.split(":", 3);
            if (partes.length >= 3) {
                String destino = partes[1];
                String texto = partes[2];
                for (WebSocket c : getConnections()) {
                    Usuario u = c.getAttachment();
                    if (u != null) {
                        String n = (u.getNombreCompleto() != null && !u.getNombreCompleto().equals("null"))
                                ? u.getNombreCompleto() : u.getUsername();
                        if (n.equals(destino)) {
                            c.send("MEDICO_DICE:" + texto);
                            break;
                        }
                    }
                }
            }
        }

        else if (message.startsWith("ENVIAR_AL_MEDICO:")) {
            String texto = message.substring("ENVIAR_AL_MEDICO:".length()).trim();
            for (WebSocket c : getConnections()) {
                Usuario u = c.getAttachment();
                if (u != null && (u.getRol() == Usuario.Rol.MEDICO || "MEDICO".equals(u.getRol().toString()))) {
                    c.send("CHAT_DE_PACIENTE:" + texto);
                }
            }
        }

        // ==================== ATENDER (BOTÓN MANUAL) ====================
        else if (message.startsWith("INICIAR_CONSULTA_MANUAL:")) {
            String nombrePaciente = message.substring("INICIAR_CONSULTA_MANUAL:".length()).trim();
            Usuario medico = (Usuario) conn.getAttachment();
            String nombreMedico = (medico != null) ? medico.getUsername() : "Médico";

            for (WebSocket client : getConnections()) {
                Usuario u = client.getAttachment();
                if (u != null && u.getUsername().equals(nombrePaciente)) {
                    client.send("SISTEMA: LLAMADA_A_CONSULTA");
                    client.send("COMANDO:ENTRAR_CONSULTA:" + nombreMedico);
                    break;
                }
            }
            conn.send("CHAT_DE_PACIENTE: 🟢 Has iniciado la consulta con " + nombrePaciente);
        }

        // ==================== FINALIZAR CONSULTA ====================
        else if (message.startsWith("FINALIZAR_CONSULTA:")) {
            String nombrePaciente = message.substring("FINALIZAR_CONSULTA:".length()).trim();
            Usuario medico = (Usuario) conn.getAttachment();
            String nombreMedico = (medico != null) ? medico.getUsername() : "Médico";

            for (WebSocket client : getConnections()) {
                if (client.isOpen()) {
                    client.send("SISTEMA: El médico ha finalizado la consulta.");
                    client.send("COMANDO:CONSULTA_FINALIZADA:" + nombreMedico);
                }
            }
            System.out.println("🛑 Consulta terminada por " + nombreMedico + " para: " + nombrePaciente);
        }

        // ==================== VALORACIÓN ====================
        else if (message.startsWith("VALORACION_MEDICO:")) {
            String[] partes = message.split(":");
            if (partes.length >= 3) {
                try {
                    double nota = Double.parseDouble(partes[1]);
                    String nombreMedico = partes[2].trim();

                    if (!nombreMedico.isEmpty() && !nombreMedico.equals("undefined")) {
                        double[] stats = database.UsuarioDAO.actualizarYObtenerReputacion(nombreMedico, nota);
                        double nuevaMedia = stats[0];
                        int totalVotos = (int) stats[1];

                        for (WebSocket c : getConnections()) {
                            Usuario u = c.getAttachment();
                            if (u != null && u.getUsername().equalsIgnoreCase(nombreMedico)) {
                                c.send("VALORACION_ACTUALIZADA:" + nuevaMedia + ":" + totalVotos);
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void actualizarPosicionesCola() {
        int pos = 1;
        for (WebSocket ws : colaEspera) {
            if (ws.isOpen()) {
                ws.send("COLA_UPDATE:" + pos + ":" + colaEspera.size());
                pos++;
            }
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        colaEspera.remove(conn);
        actualizarPosicionesCola();
    }

    @Override
    public void onError(WebSocket conn, Exception ex) { ex.printStackTrace(); }

    @Override
    public void onStart() {
        System.out.println("🚀 Servidor Turnix iniciado en puerto 8887");
    }

    public static void main(String[] args) {
        new ServidorWeb(8887).start();
    }
}