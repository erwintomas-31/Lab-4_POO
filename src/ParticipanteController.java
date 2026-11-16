import java.util.List;

public class ParticipanteController {
    private final ArenaGT arena;

    public ParticipanteController(ArenaGT arena) {
        this.arena = arena;
    }

    public Participante registrarParticipante(String codigo, String nombre, String empresa) {
        return arena.registrarParticipante(codigo, nombre, empresa);
    }

    public List<Participante> listarParticipantes() {
        return arena.getParticipantes();
    }

    public List<Empresa> listarEmpresas() {
        return arena.getEmpresas();
    }
}
