import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Encuentro extends Evento {
    private final Participante participante1;
    private final Participante participante2;

    public Encuentro(String codigo, Torneo torneo, Participante participante1, Participante participante2) {
        super(codigo, torneo);
        this.participante1 = participante1;
        this.participante2 = participante2;
    }

    public Participante getParticipante1() {
        return participante1;
    }

    public Participante getParticipante2() {
        return participante2;
    }

    @Override
    public List<Participante> getParticipantes() {
        return Collections.unmodifiableList(Arrays.asList(participante1, participante2));
    }

    @Override
    public String describir() {
        return participante1.getNombre() + " vs " + participante2.getNombre();
    }
}