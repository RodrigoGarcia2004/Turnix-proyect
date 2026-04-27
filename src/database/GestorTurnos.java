package database;

import modelo.Turno;

import java.util.LinkedList;
import java.util.Queue;

public class GestorTurnos {

    private static final Queue<Turno> colaTurnos = new LinkedList<>();

    public static synchronized Turno pedirTurno(String cliente) {
        Turno turno = new Turno(0, cliente);
        turno.setEstado("EN_ESPERA");

        turno = TurnoDAO.guardarTurno(turno);
        colaTurnos.add(turno);

        System.out.println("Turno creado: " + turno);
        return turno;
    }

    public static synchronized String verTurnos() {
        return TurnoDAO.obtenerTurnos();
    }
}