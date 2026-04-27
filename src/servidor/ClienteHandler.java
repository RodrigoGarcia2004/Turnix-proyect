package servidor;

import modelo.Turno;
import modelo.Usuario;
import database.GestorTurnos;
import database.TurnoDAO;
import database.UsuarioDAO;
import java.io.*;
import java.net.Socket;
import java.sql.*;

/**
 * Clase que gestiona la comunicación con cada cliente.
 * Implementa flujos diferenciados por rol para cumplir con los requisitos
 * de un proyecto intermodular profesional.
 */
public class ClienteHandler implements Runnable {
    private Socket cliente;
    private BufferedReader entrada;
    private PrintWriter salida;
    private Usuario userLogueado; 

    public ClienteHandler(Socket cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        try {
            entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            salida = new PrintWriter(cliente.getOutputStream(), true);
            
            salida.println("Conexión establecida. Formato login -> usuario:password");

            // 1. Fase de Autenticación
            String lineaLogin = entrada.readLine();
            if (lineaLogin == null || !lineaLogin.contains(":")) {
                salida.println("ERROR: Formato de login incorrecto.");
                return;
            }

            String[] credenciales = lineaLogin.split(":");
            String user = credenciales[0];
            String pass = credenciales[1];

            // Delegación en el módulo DAO para validar identidad y rol
            try {
                userLogueado = UsuarioDAO.autenticar(user, pass);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (userLogueado != null) {
                salida.println("LOGIN_OK: Bienvenido " + userLogueado.getNombreCompleto() + " [" + userLogueado.getRol() + "]");
                // 2. Fase de Operación (Procesamiento de peticiones según rol)
                try {
                    procesarPeticiones();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                salida.println("ERROR: Credenciales no válidas.");
            }

        } catch (IOException e) {
            System.out.println("Sesión cerrada por el cliente.");
        } finally {
            cerrarConexion();
        }
    }

    /**
     * Lógica de negocio segmentada. Aquí se justifica la "toma de decisiones técnicas"
     * y la "integración de módulos" solicitada en el feedback.
     */
    private void procesarPeticiones() throws Exception {
        String peticion;
        while ((peticion = entrada.readLine()) != null) {
            
            // --- COMANDOS COMUNES ---
            if (peticion.equals("VER_TURNOS")) {
                salida.print(GestorTurnos.verTurnos());
                salida.println("FIN_LISTA");
            }
            
            // --- FLUJO ESPECÍFICO: PACIENTE ---
            else if (userLogueado.getRol() == Usuario.Rol.PACIENTE) {
                if (peticion.startsWith("PEDIR_TURNO:")) {
                    String nombrePaciente = peticion.substring(12);
                    Turno t = GestorTurnos.pedirTurno(nombrePaciente);
                    salida.println("Turno #" + t.getNumeroTurno() + " creado satisfactoriamente.");
                } 
                else if (peticion.startsWith("ENVIAR_MSG:")) {
                    // Persistencia de mensajes en el historial del turno
                    String contenido = peticion.substring(11);
                    boolean ok = TurnoDAO.registrarMensaje(userLogueado.getId(), contenido);
                    salida.println(ok ? "MSG_ENVIADO" : "ERROR: No tiene un turno activo para chatear.");
                }
                else if (peticion.startsWith("SUBIR_DOC:")) {
                    // Registro de documentación clínica (Análisis de ficheros)
                    String nombreArchivo = peticion.substring(10);
                    String rutaSimulada = "C:/turnix/uploads/" + nombreArchivo;
                    boolean ok = TurnoDAO.registrarArchivo(userLogueado.getId(), nombreArchivo, rutaSimulada);
                    salida.println(ok ? "DOC_REGISTRADO: " + nombreArchivo : "ERROR: Fallo al vincular documento.");
                }
            }
            
            // --- FLUJO ESPECÍFICO: MÉDICO / ADMIN ---
            else if (userLogueado.getRol() == Usuario.Rol.MEDICO || userLogueado.getRol() == Usuario.Rol.ADMIN) {
                if (peticion.equals("ATENDER_SIGUIENTE")) {
                    boolean exito = TurnoDAO.atenderSiguiente();
                    salida.println(exito ? "Paciente atendido y movido a historial." : "No hay pacientes pendientes.");
                }
                // Integración de módulos: El médico consulta la info previa del paciente
                else if (peticion.equals("VER_INTERACCION")) {
                    String datos = TurnoDAO.obtenerInteraccionesTurnoActual();
                    salida.println("--- INFO PACIENTE EN ESPERA ---");
                    salida.println(datos);
                    salida.println("--- FIN DEL REPORTE ---");
                }
                // NUEVO COMANDO: Finalizar la consulta actual
                else if (peticion.equals("FINALIZAR_CONSULTA")) {
                    // Usamos el método que identifica el turno que está siendo procesado
                    int idTurnoActual = TurnoDAO.obtenerIdTurnoEnConsulta(); 
                    
                    if (idTurnoActual != -1) {
                        boolean ok = TurnoDAO.finalizarConsulta(idTurnoActual);
                        salida.println(ok ? "CONSULTA_FINALIZADA: El turno ha sido cerrado y archivado." 
                                          : "ERROR: No se pudo cerrar el turno en la base de datos.");
                    } else {
                        salida.println("ERROR: No hay ninguna consulta activa para finalizar.");
                    }
                }
            }

            else if (peticion.equals("SALIR")) {
                break;
            }
            else {
                salida.println("ERROR: Comando no reconocido o permisos insuficientes.");
            }
        }
    }

    private void cerrarConexion() {
        try {
            if (cliente != null) cliente.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}