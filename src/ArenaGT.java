import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArenaGT {
    private final List<Disciplina> disciplinas = new ArrayList<>();
    private final List<Empresa> empresas = new ArrayList<>();
    private final List<Participante> participantes = new ArrayList<>();
    private final List<Torneo> torneos = new ArrayList<>();

    public ArenaGT() {
        disciplinas.add(new FutbolSala());
        disciplinas.add(new FutbolSalaConPenales());
        disciplinas.add(new Baloncesto());
        disciplinas.add(new Voleibol());
        disciplinas.add(new Relevos());
    }

    public void registrarDisciplina(Disciplina disciplina) {
        disciplinas.add(disciplina);
    }

    public Participante registrarParticipante(String codigo, String nombre, String nombreEmpresa) {
        String cod = exigirTexto(codigo, "El código del participante");
        String nom = exigirTexto(nombre, "El nombre del participante");
        String emp = exigirTexto(nombreEmpresa, "El nombre de la empresa");
        if (buscarParticipanteOpcional(cod) != null) {
            throw new ReglaNegocioException("Ya existe un participante con el código: " + cod);
        }
        Participante participante = new Participante(cod, nom, obtenerOCrearEmpresa(emp));
        participantes.add(participante);
        return participante;
    }

    public Torneo crearTorneo(String codigo, String nombre, Disciplina disciplina) {
        String cod = exigirTexto(codigo, "El código del torneo");
        String nom = exigirTexto(nombre, "El nombre del torneo");
        if (disciplina == null) {
            throw new ReglaNegocioException("Debe seleccionar una disciplina");
        }
        for (Torneo t : torneos) {
            if (t.getCodigo().equalsIgnoreCase(cod)) {
                throw new ReglaNegocioException("Ya existe un torneo con el código: " + cod);
            }
        }
        Torneo torneo = new Torneo(cod, nom, disciplina);
        torneos.add(torneo);
        return torneo;
    }

    private Empresa obtenerOCrearEmpresa(String nombre) {
        for (Empresa e : empresas) {
            if (e.getNombre().equalsIgnoreCase(nombre)) return e;
        }
        Empresa nueva = new Empresa(nombre);
        empresas.add(nueva);
        return nueva;
    }

    private static String exigirTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ReglaNegocioException(campo + " no puede estar vacío");
        }
        return valor.trim();
    }

    public Torneo buscarTorneo(String codigo) {
        String cod = exigirTexto(codigo, "El código del torneo");
        for (Torneo t : torneos) {
            if (t.getCodigo().equalsIgnoreCase(cod)) return t;
        }
        throw new ReglaNegocioException("No existe el torneo: " + cod);
    }

    public Participante buscarParticipante(String codigo) {
        String cod = exigirTexto(codigo, "El código del participante");
        Participante p = buscarParticipanteOpcional(cod);
        if (p == null) {
            throw new ReglaNegocioException("No existe el participante: " + cod);
        }
        return p;
    }

    private Participante buscarParticipanteOpcional(String codigo) {
        for (Participante p : participantes) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) return p;
        }
        return null;
    }

    public Evento buscarEvento(String codigo) {
        String cod = exigirTexto(codigo, "El código del evento");
        for (Torneo t : torneos) {
            for (Evento e : t.getEventos()) {
                if (e.getCodigo().equalsIgnoreCase(cod)) return e;
            }
        }
        throw new ReglaNegocioException("No existe el evento: " + cod);
    }

    public void programarEvento(Evento evento, LocalDate fecha, int hora, String sede) {
        if (evento == null) {
            throw new ReglaNegocioException("Debe indicar un evento");
        }
        if (evento.estaFinalizado()) {
            throw new ReglaNegocioException("El evento '" + evento.getCodigo()+ "' ya tiene resultado y no puede reprogramarse");
        }
        if (fecha == null) {
            throw new ReglaNegocioException("La fecha es obligatoria");
        }
        if (hora < 0 || hora > 23) {
            throw new ReglaNegocioException("La hora de inicio debe ser un número entero entre 0 y 23");
        }
        String sedeLimpia = exigirTexto(sede, "La sede");

        for (Torneo t : torneos) {
            for (Evento otro : t.getEventos()) {
                boolean mismoHorario = otro != evento && otro.tieneHorario()
                        && otro.getFecha().equals(fecha) && otro.getHora() == hora;
                if (!mismoHorario) continue;
                if (otro.getSede().equalsIgnoreCase(sedeLimpia)) {
                    throw new ReglaNegocioException("Conflicto de sede: " + sedeLimpia+ "' ya está ocupada a esa fecha y hora por el evento " + otro.getCodigo());
                }
                for (Participante p : evento.getParticipantes()) {
                    if (otro.involucra(p)) {
                        throw new ReglaNegocioException("Conflicto de participante: " + p.getNombre()+ ", ya tiene el evento " + otro.getCodigo() + " a esa fecha y hora");
                    }
                }
            }
        }
        evento.programar(fecha, hora, sedeLimpia);
    }

    public List<Evento> calendarioGeneral() {
        List<Evento> todos = new ArrayList<>();
        for (Torneo t : torneos) todos.addAll(t.getEventos());
        return ordenarCalendario(todos);
    }

    public List<Evento> calendarioDeTorneo(Torneo torneo) {
        return ordenarCalendario(torneo.getEventos());
    }

    public List<Evento> calendarioDeParticipante(Participante participante) {
        List<Evento> propios = new ArrayList<>();
        for (Torneo t : torneos) {
            for (Evento e : t.getEventos()) {
                if (e.involucra(participante)) propios.add(e);
            }
        }
        return ordenarCalendario(propios);
    }

    private List<Evento> ordenarCalendario(List<Evento> eventos) {
        List<Evento> calendario = new ArrayList<>();
        for (Evento e : eventos) {
            if (e.tieneHorario()) calendario.add(e);
        }
        calendario.sort(Evento.ORDEN_CRONOLOGICO);
        return calendario;
    }

    public List<Disciplina> getDisciplinas() {
        return Collections.unmodifiableList(disciplinas);
    }

    public List<Empresa> getEmpresas() {
        return Collections.unmodifiableList(empresas);
    }

    public List<Participante> getParticipantes() {
        return Collections.unmodifiableList(participantes);
    }

    public List<Torneo> getTorneos() {
        return Collections.unmodifiableList(torneos);
    }
}