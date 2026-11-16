import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReporteController {
    private final ArenaGT arena;

    public ReporteController(ArenaGT arena) {
        this.arena = arena;
    }

    public List<Torneo> listarTorneos() {
        return arena.getTorneos();
    }

    public List<Participante> listarParticipantes() {
        return arena.getParticipantes();
    }

    public Torneo obtenerTorneo(String codigo) {
        return arena.buscarTorneo(codigo);
    }

    public Participante obtenerParticipante(String codigo) {
        return arena.buscarParticipante(codigo);
    }

    public List<EntradaTabla> obtenerTablaPosiciones(String codigoTorneo) {
        return arena.buscarTorneo(codigoTorneo).obtenerTablaPosiciones();
    }

    public Participante obtenerCampeon(String codigoTorneo) {
        Torneo torneo = arena.buscarTorneo(codigoTorneo);
        return torneo.getEstado() == EstadoTorneo.FINALIZADO ? torneo.getCampeon() : null;
    }

    public List<Evento> obtenerCalendarioGeneral() {
        return arena.calendarioGeneral();
    }

    public List<Evento> obtenerCalendarioTorneo(String codigoTorneo) {
        return arena.calendarioDeTorneo(arena.buscarTorneo(codigoTorneo));
    }

    public List<Evento> obtenerCalendarioParticipante(String codigoParticipante) {
        return arena.calendarioDeParticipante(arena.buscarParticipante(codigoParticipante));
    }

    public double calcularIngresosTotales() {
        double total = 0;
        for (Torneo t : arena.getTorneos()) total += t.getIngresos();
        return total;
    }

    public Map<String, Double> calcularIngresosPorDisciplina() {
        Map<String, Double> resultado = new LinkedHashMap<>();
        for (Disciplina d : arena.getDisciplinas()) {
            resultado.put(d.getNombre(), 0.0);
        }
        for (Torneo t : arena.getTorneos()) {
            resultado.merge(t.getDisciplina().getNombre(), t.getIngresos(), Double::sum);
        }
        return resultado;
    }

    public double calcularPremiosEntregados() {
        double total = 0;
        for (Torneo t : arena.getTorneos()) total += t.getPremioEntregado();
        return total;
    }

    public List<Torneo> obtenerTorneosFinalizados() {
        List<Torneo> lista = new ArrayList<>();
        for (Torneo t : arena.getTorneos()) {
            if (t.getEstado() == EstadoTorneo.FINALIZADO) lista.add(t);
        }
        return lista;
    }

    public Map<Empresa, Integer> generarRankingEmpresas() {
        List<Torneo> torneos = arena.getTorneos();
        List<Empresa> ordenadas = new ArrayList<>(arena.getEmpresas());
        Map<Empresa, Integer> puntos = new HashMap<>();
        for (Empresa e : ordenadas) {
            puntos.put(e, e.calcularPuntosTotales(torneos));
        }
        ordenadas.sort((a, b) -> {
            int c = Integer.compare(puntos.get(b), puntos.get(a));
            return c != 0 ? c : a.getNombre().compareToIgnoreCase(b.getNombre());
        });
        Map<Empresa, Integer> ranking = new LinkedHashMap<>();
        for (Empresa e : ordenadas) {
            ranking.put(e, puntos.get(e));
        }
        return ranking;
    }
}