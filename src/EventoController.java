import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class EventoController {
    private final ArenaGT arena;

    public EventoController(ArenaGT arena) {
        this.arena = arena;
    }

    public Evento obtenerEvento(String codigo) {
        return arena.buscarEvento(codigo);
    }

    public Evento obtenerEventoParaResultado(String codigo) {
        Evento evento = arena.buscarEvento(codigo);
        evento.verificarAdmiteResultado();
        return evento;
    }

    public void programarEvento(String codigoEvento, LocalDate fecha, int hora, String sede) {
        arena.programarEvento(arena.buscarEvento(codigoEvento), fecha, hora, sede);
    }

    public void registrarResultadoEvento(Evento evento, Resultado resultado) {
        evento.getTorneo().registrarResultado(evento, resultado);
    }

    public Torneo registrarResultadoEncuentro(String codigoEvento, int marcador1, int marcador2, Integer penales1, Integer penales2) {
        Evento evento = arena.buscarEvento(codigoEvento);
        registrarResultadoEvento(evento, new ResultadoEncuentro(marcador1, marcador2, penales1, penales2));
        return evento.getTorneo();
    }

    public Torneo registrarResultadoCarrera(String codigoEvento, Map<String, Double> tiemposPorCodigo, Set<String> descalificadosPorCodigo) {
        Evento evento = arena.buscarEvento(codigoEvento);
        Map<Participante, Double> tiempos = new LinkedHashMap<>();
        Set<Participante> descalificados = new java.util.LinkedHashSet<>();
        for (Map.Entry<String, Double> e : tiemposPorCodigo.entrySet()) {
            tiempos.put(buscarEnEvento(evento, e.getKey()), e.getValue());
        }
        for (String codigo : descalificadosPorCodigo) {
            descalificados.add(buscarEnEvento(evento, codigo));
        }
        registrarResultadoEvento(evento, new ResultadoCarrera(tiempos, descalificados));
        return evento.getTorneo();
    }

    private Participante buscarEnEvento(Evento evento, String codigo) {
        for (Participante p : evento.getParticipantes()) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) return p;
        }
        throw new ReglaNegocioException("El participante: " + codigo + " no compite en el evento: "+ evento.getCodigo());
    }
}
