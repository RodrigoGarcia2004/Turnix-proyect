package modelo;

public class Turno {

    private int numeroTurno;
    private String cliente;
    private String estado;

    public Turno(int numeroTurno, String cliente) {
        this.numeroTurno = numeroTurno;
        this.cliente = cliente;
        this.estado = "EN_ESPERA";
    }

    public int getNumeroTurno() {
        return numeroTurno;
    }

    public void setNumeroTurno(int numeroTurno) {
        this.numeroTurno = numeroTurno;
    }

    public String getCliente() {
        return cliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Turno " + numeroTurno + " - Cliente: " + cliente + " - Estado: " + estado;
    }
}