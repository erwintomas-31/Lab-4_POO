import java.util.List;

public abstract class Disciplina {
    public static final int MAX_PARTICIPANTES_POR_EMPRESA = 2;
    public static final double PORCENTAJE_PREMIO = 0.5;
    private final String nombre;
    private final int minParticipantes;
    private final int maxParticipantes;
    private final double cuotaInscripcion;

    protected Disciplina(String nombre, int minParticipantes, int maxParticipantes, double cuotaInscripcion) {
        this.nombre = nombre;
        this.minParticipantes = minParticipantes;
        this.maxParticipantes = maxParticipantes;
        this.cuotaInscripcion = cuotaInscripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public int getMinParticipantes() {
        return minParticipantes;
    }

    public int getMaxParticipantes() {
        return maxParticipantes;
    }

    public double getCuotaInscripcion() {
        return cuotaInscripcion;
    }

    public void validarInscripcion(Torneo torneo, Participante participante) {
        if (torneo.getParticipantes().size() >= maxParticipantes) {
            throw new ReglaNegocioException("El torneo '" + torneo.getCodigo()+ "' ya alcanzó el máximo de " + maxParticipantes + " participantes");
        }
        if (torneo.estaInscrito(participante)) {
            throw new ReglaNegocioException("El participante '" + participante.getCodigo()+ "' ya está inscrito en el torneo '" + torneo.getCodigo() + "'");
        }
        if (torneo.contarParticipantesDe(participante.getEmpresa()) >= MAX_PARTICIPANTES_POR_EMPRESA) {
            throw new ReglaNegocioException("La empresa '" + participante.getEmpresa().getNombre()+ "' ya tiene " + MAX_PARTICIPANTES_POR_EMPRESA + " participantes inscritos en el torneo '"+ torneo.getCodigo() + "'");
        }
    }

    public double calcularCotizacion(int cantidadParticipantes) {
        if (cantidadParticipantes <= 0) {
            throw new ReglaNegocioException("La cantidad de participantes debe ser un entero positivo");
        }
        if (cantidadParticipantes > maxParticipantes) {
            throw new ReglaNegocioException("La cantidad (" + cantidadParticipantes + ") está fuera de rango: "+ nombre + " admite entre 1 y " + maxParticipantes + " participantes");
        }
        return cuotaInscripcion * cantidadParticipantes;
    }

    public double calcularPremio(int cantidadInscritos) {
        return PORCENTAJE_PREMIO * cuotaInscripcion * cantidadInscritos;
    }

    public abstract List<Evento> generarEventos(Torneo torneo);

    public abstract void validarResultado(Evento evento, Resultado resultado);

    public abstract List<Puntuacion> calcularPuntuaciones(Evento evento, Resultado resultado);

    @Override
    public String toString() {
        return nombre;
    }
}