import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public abstract class Evento {

    public static final Comparator<Evento> ORDEN_CRONOLOGICO = (a, b) -> {
        if (a.fecha != null && b.fecha != null) {
            int c = a.fecha.compareTo(b.fecha);
            if (c != 0) return c;
            c = Integer.compare(a.hora, b.hora);
            if (c != 0) return c;
            c = a.sede.compareToIgnoreCase(b.sede);
            if (c != 0) return c;
        } else if (a.fecha != null) {
            return -1;
        } else if (b.fecha != null) {
            return 1;
        }
        return a.codigo.compareTo(b.codigo);
    };

    private final String codigo;
    private final Torneo torneo;
    private EstadoEvento estado = EstadoEvento.SIN_PROGRAMAR;
    private LocalDate fecha;
    private int hora;
    private String sede;
    private Resultado resultado;

    protected Evento(String codigo, Torneo torneo) {
        this.codigo = codigo;
        this.torneo = torneo;
    }

    public abstract List<Participante> getParticipantes();

    public abstract String describir();

    public String getCodigo() {
        return codigo;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public EstadoEvento getEstado() {
        return estado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getHora() {
        return hora;
    }

    public String getSede() {
        return sede;
    }

    public Resultado getResultado() {
        return resultado;
    }

    public boolean tieneHorario() {
        return fecha != null;
    }

    public boolean estaSinProgramar() {
        return estado == EstadoEvento.SIN_PROGRAMAR;
    }

    public boolean estaProgramado() {
        return estado == EstadoEvento.PROGRAMADO;
    }

    public boolean estaFinalizado() {
        return estado == EstadoEvento.FINALIZADO;
    }

    public boolean involucra(Participante p) {
        return getParticipantes().contains(p);
    }

    void programar(LocalDate fecha, int hora, String sede) {
        if (estaFinalizado()) {
            throw new ReglaNegocioException("El evento '" + codigo + "' ya tiene resultado y no puede reprogramarse");
        }
        if (fecha == null) {
            throw new ReglaNegocioException("La fecha es obligatoria");
        }
        if (hora < 0 || hora > 23) {
            throw new ReglaNegocioException("La hora de inicio debe ser un número entero entre 0 y 23");
        }
        if (sede == null || sede.trim().isEmpty()) {
            throw new ReglaNegocioException("La sede es obligatoria");
        }
        this.fecha = fecha;
        this.hora = hora;
        this.sede = sede.trim();
        this.estado = EstadoEvento.PROGRAMADO;
    }

    /** Lanza excepción si el evento todavía no admite un resultado. */
    public void verificarAdmiteResultado() {
        if (estaFinalizado()) {
            throw new ReglaNegocioException("El evento '" + codigo + "' ya está finalizado y no admite otro resultado");
        }
        if (estaSinProgramar()) {
            throw new ReglaNegocioException("El evento '" + codigo+ "' no está programado, solo se puede registrar resultado a eventos programados");
        }
    }

    List<Puntuacion> registrarResultado(Resultado resultado) {
        verificarAdmiteResultado();
        if (resultado == null) {
            throw new ReglaNegocioException("Debe indicar un resultado");
        }
        Disciplina disciplina = torneo.getDisciplina();
        disciplina.validarResultado(this, resultado);
        List<Puntuacion> puntuaciones = disciplina.calcularPuntuaciones(this, resultado);
        this.resultado = resultado;
        this.estado = EstadoEvento.FINALIZADO;
        return puntuaciones;
    }
}
