import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Torneo {
    private final String codigo;
    private final String nombre;
    private final Disciplina disciplina;
    private EstadoTorneo estado = EstadoTorneo.EN_INSCRIPCION;
    private final List<Participante> participantes = new ArrayList<>();
    private final Map<Participante, EntradaTabla> tabla = new LinkedHashMap<>();
    private final List<Evento> eventos = new ArrayList<>();
    private double ingresos;
    private double premioEntregado;
    private Participante campeon;

    public Torneo(String codigo, String nombre, Disciplina disciplina) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.disciplina = disciplina;
    }

    public double inscribirParticipante(Participante participante) {
        agregar(participante);
        ingresos += disciplina.getCuotaInscripcion();
        return disciplina.getCuotaInscripcion();
    }

    public void cargarInscritoInicial(Participante participante) {
        agregar(participante);
    }

    private void agregar(Participante participante) {
        if (participante == null) {
            throw new ReglaNegocioException("Debe indicar un participante");
        }
        if (estado != EstadoTorneo.EN_INSCRIPCION) {
            throw new ReglaNegocioException("El torneo '" + codigo + "' no está en inscripción, estado: "+ estado.getEtiqueta());
        }
        disciplina.validarInscripcion(this, participante);
        participantes.add(participante);
        tabla.put(participante, new EntradaTabla(participante));
    }

    public void cerrarInscripciones() {
        if (estado != EstadoTorneo.EN_INSCRIPCION) {
            throw new ReglaNegocioException("El torneo '" + codigo + "' no está en inscripción, estado: "+ estado.getEtiqueta());
        }
        if (participantes.size() < disciplina.getMinParticipantes()) {
            throw new ReglaNegocioException("No se pueden cerrar las inscripciones: hay " + participantes.size()+ " participantes y el mínimo de " + disciplina.getNombre() + " es "+ disciplina.getMinParticipantes());
        }
        List<Evento> generados = disciplina.generarEventos(this);
        eventos.addAll(generados);
        estado = EstadoTorneo.EN_JUEGO;
    }

    public void registrarResultado(Evento evento, Resultado resultado) {
        if (estado != EstadoTorneo.EN_JUEGO) {
            throw new ReglaNegocioException("El torneo '" + codigo + "' no está en juego, estado: "+ estado.getEtiqueta());
        }
        if (evento == null || !eventos.contains(evento)) {
            throw new ReglaNegocioException("El evento no pertenece al torneo " + codigo);
        }
        List<Puntuacion> puntuaciones = evento.registrarResultado(resultado); 
        for (Puntuacion p : puntuaciones) {
            tabla.get(p.getParticipante()).acumular(p);
        }
        verificarFinalizacion();
    }

    private void verificarFinalizacion() {
        for (Evento e : eventos) {
            if (!e.estaFinalizado()) return;
        }
        estado = EstadoTorneo.FINALIZADO;
        premioEntregado = calcularPremio();
        campeon = obtenerTablaPosiciones().get(0).getParticipante();
    }

    public double calcularPremio() {
        return disciplina.calcularPremio(participantes.size());
    }

    public List<EntradaTabla> obtenerTablaPosiciones() {
        List<EntradaTabla> lista = new ArrayList<>(tabla.values());
        lista.sort(EntradaTabla.ORDEN_POSICIONES);
        return lista;
    }

    public boolean estaInscrito(Participante p) {
        return participantes.contains(p);
    }

    public int contarParticipantesDe(Empresa empresa) {
        int total = 0;
        for (Participante p : participantes) {
            if (p.getEmpresa().equals(empresa)) total++;
        }
        return total;
    }

    public List<Evento> getEventosSinProgramar() {
        List<Evento> lista = new ArrayList<>();
        for (Evento e : eventos) {
            if (e.estaSinProgramar()) lista.add(e);
        }
        return lista;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public EstadoTorneo getEstado() {
        return estado;
    }

    public List<Participante> getParticipantes() {
        return Collections.unmodifiableList(participantes);
    }

    public List<Evento> getEventos() {
        return Collections.unmodifiableList(eventos);
    }

    public Collection<EntradaTabla> getEntradasTabla() {
        return Collections.unmodifiableCollection(tabla.values());
    }

    public double getIngresos() {
        return ingresos;
    }

    public double getPremioEntregado() {
        return premioEntregado;
    }

    public Participante getCampeon() {
        return campeon;
    }
}
