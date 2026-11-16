import java.util.List;

public class TorneoController {
    private final ArenaGT arena;

    public TorneoController(ArenaGT arena) {
        this.arena = arena;
    }

    public List<Disciplina> obtenerDisciplinas() {
        return arena.getDisciplinas();
    }

    public List<Torneo> listarTorneos() {
        return arena.getTorneos();
    }

    public Torneo obtenerTorneo(String codigo) {
        return arena.buscarTorneo(codigo);
    }

    public Torneo crearTorneo(String codigo, String nombre, Disciplina disciplina) {
        return arena.crearTorneo(codigo, nombre, disciplina);
    }

    public double procesarInscripcion(String codigoTorneo, String codigoParticipante) {
        Torneo torneo = arena.buscarTorneo(codigoTorneo);
        Participante participante = arena.buscarParticipante(codigoParticipante);
        return torneo.inscribirParticipante(participante);
    }

    public List<Evento> cerrarInscripcionesTorneo(String codigoTorneo) {
        Torneo torneo = arena.buscarTorneo(codigoTorneo);
        torneo.cerrarInscripciones();
        return torneo.getEventos();
    }
}
