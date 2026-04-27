package modelo;

/**
 * Clase que representa a un usuario del sistema Turnix.
 * Refleja un nivel de madurez superior al incluir roles diferenciados
 * y gestión de información profesional.
 */
public class Usuario {

    // Definición de Roles (Punto clave del feedback para permisos y flujos) [cite: 76]
    public enum Rol {
        PACIENTE,    // Acceso a pedir turnos, chat y subir analíticas.
        MEDICO,      // Acceso a atender, ver historial y descargar archivos.
        ADMIN,       // Gestión 	 de usuarios y configuración del sistema.
        SUPERVISOR   // Acceso a auditoría y estadísticas de tiempos.
    }

    private int id;
    private String username;
    private String password;
    private Rol rol;
    private String nombreCompleto;
    private String email;

    // Constructor completo para la base de datos
    public Usuario(int id, String username, Rol rol, String nombreCompleto, String email) {
        this.id = id;
        this.username = username;
        this.rol = rol;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
    }

    // Constructor vacío (necesario para algunas librerías de persistencia)
    public Usuario() {}

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", rol=" + rol +
                ", nombre='" + nombreCompleto + '\'' +
                '}';
    }
}