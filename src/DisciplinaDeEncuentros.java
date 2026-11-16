import java.util.ArrayList;
import java.util.List;

public abstract class DisciplinaDeEncuentros extends Disciplina {
    protected DisciplinaDeEncuentros(String nombre, int min, int max, double cuota) {
        super(nombre, min, max, cuota);
    }

    @Override
    public List<Evento> generarEventos(Torneo torneo) {
        List<Participante> inscritos = torneo.getParticipantes();
        List<Evento> eventos = new ArrayList<>();
        int numero = 1;
        for (int i = 0; i < inscritos.size(); i++) {
            for (int j = i + 1; j < inscritos.size(); j++) {
                eventos.add(new Encuentro(torneo.getCodigo() + "-E" + numero++, torneo,
                        inscritos.get(i), inscritos.get(j)));
            }
        }
        return eventos;
    }

    @Override
    public void validarResultado(Evento evento, Resultado resultado) {
        if (!(resultado instanceof ResultadoEncuentro)) {
            throw new ReglaNegocioException("Esta disciplina requiere el resultado de un encuentro");
        }
        ResultadoEncuentro r = (ResultadoEncuentro) resultado;
        if (r.getMarcador1() < 0 || r.getMarcador2() < 0) {
            throw new ReglaNegocioException("Los marcadores deben ser enteros no negativos");
        }
        if (r.tienePenales() && !admitePenales()) {
            throw new ReglaNegocioException(getNombre() + " no admite penales");
        }
        validarMarcador(r);
    }

    @Override
    public List<Puntuacion> calcularPuntuaciones(Evento evento, Resultado resultado) {
        ResultadoEncuentro r = (ResultadoEncuentro) resultado;
        List<Participante> ps = evento.getParticipantes();
        int[] puntos = calcularPuntosTabla(r);
        int diferencia = calcularDiferencia(r);
        int ganador = ganador(r);
        List<Puntuacion> resultadoFinal = new ArrayList<>();
        resultadoFinal.add(new Puntuacion(ps.get(0), puntos[0], diferencia, ganador == 1));
        resultadoFinal.add(new Puntuacion(ps.get(1), puntos[1], -diferencia, ganador == 2));
        return resultadoFinal;
    }

    protected boolean admitePenales() {
        return false;
    }

    protected abstract void validarMarcador(ResultadoEncuentro r);

    protected abstract int[] calcularPuntosTabla(ResultadoEncuentro r);

    protected int calcularDiferencia(ResultadoEncuentro r) {
        return r.getMarcador1() - r.getMarcador2();
    }

    protected int ganador(ResultadoEncuentro r) {
        if (r.getMarcador1() > r.getMarcador2()) return 1;
        if (r.getMarcador2() > r.getMarcador1()) return 2;
        return 0;
    }
}
