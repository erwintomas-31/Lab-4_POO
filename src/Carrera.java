import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Carrera extends Evento {
    private final List<Participante> participantes;

    public Carrera(String codigo, Torneo torneo, List<Participante> participantes) {
        super(codigo, torneo);
        this.participantes = new ArrayList<>(participantes);
    }

    @Override
    public List<Participante> getParticipantes() {
        return Collections.unmodifiableList(participantes);
    }

    @Override
    public String describir() {
        return "Carrera con " + participantes.size() + " participantes";
    }
}
