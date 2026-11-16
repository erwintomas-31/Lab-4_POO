import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResultadoCarrera extends Resultado {
    private final Map<Participante, Double> tiempos;
    private final Set<Participante> descalificados;

    public ResultadoCarrera(Map<Participante, Double> tiempos, Set<Participante> descalificados) {
        this.tiempos = new LinkedHashMap<>(tiempos);
        this.descalificados = new LinkedHashSet<>(descalificados);
    }

    public Map<Participante, Double> getTiempos() {
        return Collections.unmodifiableMap(tiempos);
    }

    public Set<Participante> getDescalificados() {
        return Collections.unmodifiableSet(descalificados);
    }

    @Override
    public String describir() {
        List<Map.Entry<Participante, Double>> ordenados = new ArrayList<>(tiempos.entrySet());
        ordenados.sort(Map.Entry.comparingByValue());
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Participante, Double> e : ordenados) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(e.getKey().getNombre()).append(' ').append(e.getValue());
        }
        for (Participante p : descalificados) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(p.getNombre()).append(" (descalificado)");
        }
        return sb.toString();
    }
}
