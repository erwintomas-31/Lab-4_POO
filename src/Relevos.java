import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Relevos extends Disciplina {
    private static final int[] PUNTOS_POR_LUGAR = {10, 8, 6, 5, 4, 3, 2, 1};

    public Relevos() {
        super("Relevos", 3, 8, 300);
    }

    @Override
    public List<Evento> generarEventos(Torneo torneo) {
        List<Evento> eventos = new ArrayList<>();
        eventos.add(new Carrera(torneo.getCodigo() + "-E1", torneo, torneo.getParticipantes()));
        return eventos;
    }

    @Override
    public void validarResultado(Evento evento, Resultado resultado) {
        if (!(resultado instanceof ResultadoCarrera)) {
            throw new ReglaNegocioException("Los relevos requieren el resultado de una carrera (tiempos)");
        }
        ResultadoCarrera r = (ResultadoCarrera) resultado;
        List<Participante> inscritos = evento.getParticipantes();

        for (Participante p : r.getTiempos().keySet()) {
            if (!inscritos.contains(p)) {
                throw new ReglaNegocioException("El participante '" + p.getCodigo() + "' no compite en esta carrera");
            }
            if (r.getDescalificados().contains(p)) {
                throw new ReglaNegocioException("'" + p.getCodigo() + "' no puede tener tiempo y estar descalificado");
            }
        }
        for (Participante p : r.getDescalificados()) {
            if (!inscritos.contains(p)) {
                throw new ReglaNegocioException("El participante '" + p.getCodigo() + "' no compite en esta carrera");
            }
        }
        for (Participante p : inscritos) {
            if (!r.getTiempos().containsKey(p) && !r.getDescalificados().contains(p)) {
                throw new ReglaNegocioException("Falta el resultado de '" + p.getCodigo()+ "' (tiempo o descalificación)");
            }
        }
        Set<Double> vistos = new HashSet<>();
        for (Map.Entry<Participante, Double> e : r.getTiempos().entrySet()) {
            double t = e.getValue();
            if (Double.isNaN(t) || Double.isInfinite(t) || t <= 0) {
                throw new ReglaNegocioException("El tiempo de '" + e.getKey().getCodigo()+ "' debe ser un número mayor que cero");
            }
            if (!vistos.add(t)) {
                throw new ReglaNegocioException("Hay tiempos repetidos en relevos: " + t+ ", cada participante debe tener un tiempo distinto");
            }
        }
    }

    @Override
    public List<Puntuacion> calcularPuntuaciones(Evento evento, Resultado resultado) {
        ResultadoCarrera r = (ResultadoCarrera) resultado;
        List<Map.Entry<Participante, Double>> ordenados = new ArrayList<>(r.getTiempos().entrySet());
        ordenados.sort(Map.Entry.comparingByValue());
        List<Puntuacion> lista = new ArrayList<>();
        for (int lugar = 0; lugar < ordenados.size(); lugar++) {
            int puntos = lugar < PUNTOS_POR_LUGAR.length ? PUNTOS_POR_LUGAR[lugar] : 0;
            lista.add(new Puntuacion(ordenados.get(lugar).getKey(), puntos, 0, lugar == 0));
        }
        for (Participante p : r.getDescalificados()) {
            lista.add(new Puntuacion(p, 0, 0, false));
        }
        return lista;
    }
}