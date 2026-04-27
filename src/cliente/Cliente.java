package cliente;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        // Asegúrate de que el puerto (12345) coincida con el de tu Servidor.java
        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             Scanner sc = new Scanner(System.in)) {

            System.out.println(entrada.readLine()); // Bienvenida del servidor

            // --- PROCESO DE LOGIN ---
            System.out.print("Usuario: ");
            String user = sc.nextLine();
            System.out.print("Password: ");
            String pass = sc.nextLine();
            salida.println(user + ":" + pass);

            String respuestaLogin = entrada.readLine();
            System.out.println("Servidor: " + respuestaLogin);

            if (respuestaLogin.contains("ERROR")) return;

            // Detectamos el rol para mostrar el menú correspondiente
            boolean esPaciente = respuestaLogin.contains("PACIENTE");
            boolean esMedicoOAdmin = respuestaLogin.contains("MEDICO") || respuestaLogin.contains("ADMIN");

            while (true) {
                System.out.println("\n--- MENÚ TURNIX (" + 
                    (esPaciente ? "PACIENTE" : (esMedicoOAdmin ? "PERSONAL SANITARIO" : "USUARIO")) + ") ---");
                
                if (esPaciente) {
                    System.out.println("1. Pedir turno");
                    System.out.println("2. Enviar mensaje (Chat)");
                    System.out.println("3. Subir documento (.pdf, .jpg)");
                    System.out.println("4. Ver lista de turnos");
                    System.out.println("5. Salir");
                } else if (esMedicoOAdmin) {
                    System.out.println("1. Atender siguiente turno");
                    System.out.println("2. Ver mensajes y documentos del paciente");
                    System.out.println("3. FINALIZAR CONSULTA ACTUAL"); // Nueva funcionalidad
                    System.out.println("4. Ver lista de turnos");
                    System.out.println("5. Salir");
                }

                System.out.print("Seleccione opción: ");
                String opcion = sc.nextLine();

                // --- LÓGICA PARA PACIENTE ---
                if (esPaciente) {
                    if (opcion.equals("1")) {
                        System.out.print("Nombre para el turno: ");
                        salida.println("PEDIR_TURNO:" + sc.nextLine());
                    } else if (opcion.equals("2")) {
                        System.out.print("Escribe tu mensaje: ");
                        salida.println("ENVIAR_MSG:" + sc.nextLine());
                    } else if (opcion.equals("3")) {
                        System.out.print("Nombre del archivo a subir: ");
                        salida.println("SUBIR_DOC:" + sc.nextLine());
                    } else if (opcion.equals("4")) {
                        verTurnos(salida, entrada);
                        continue;
                    } else if (opcion.equals("5")) {
                        salida.println("SALIR");
                        break;
                    }
                } 
                
                // --- LÓGICA PARA MÉDICO / ADMIN ---
                else if (esMedicoOAdmin) {
                    if (opcion.equals("1")) {
                        salida.println("ATENDER_SIGUIENTE");
                    } else if (opcion.equals("2")) {
                        salida.println("VER_INTERACCION");
                        // El médico recibe varias líneas de historial
                        String linea;
                        while (!(linea = entrada.readLine()).contains("--- FIN")) {
                            System.out.println(linea);
                        }
                        System.out.println(linea); // Imprime el final del reporte
                        continue;
                    } else if (opcion.equals("3")) {
                        salida.println("FINALIZAR_CONSULTA");
                    } else if (opcion.equals("4")) {
                        verTurnos(salida, entrada);
                        continue;
                    } else if (opcion.equals("5")) {
                        salida.println("SALIR");
                        break;
                    }
                }

                // Respuesta general del servidor tras cada comando
                System.out.println("\nServidor: " + entrada.readLine());
            }
        } catch (IOException e) {
            System.err.println("Conexión perdida con el servidor.");
        }
    }

    // Método auxiliar para no repetir código de ver turnos
    private static void verTurnos(PrintWriter salida, BufferedReader entrada) throws IOException {
        salida.println("VER_TURNOS");
        String linea;
        while (!(linea = entrada.readLine()).equals("FIN_LISTA")) {
            System.out.println(linea);
        }
    }
}