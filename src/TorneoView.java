import java.util.List;

public class TorneoView {
    private final Consola consola;
    private final TorneoController torneos;
    private final CotizacionController cotizaciones;
    private final ReporteController reportes;
    private final CalendarioView calendarioView;

    public TorneoView(Consola consola, TorneoController torneos, CotizacionController cotizaciones, ReporteController reportes, CalendarioView calendarioView) {
        this.consola = consola;
        this.torneos = torneos;
        this.cotizaciones = cotizaciones;
        this.reportes = reportes;
        this.calendarioView = calendarioView;
    }

    public void mostrarMensaje(String mensaje) {
        consola.mostrar(mensaje);
    }

    public void mostrarFormularioTorneo() {
        consola.mostrar("Crear torneo");
        String codigo = consola.leerTexto("Código del torneo: ");
        String nombre = consola.leerTexto("Nombre del torneo: ");
        Disciplina disciplina = pedirDisciplina();
        Torneo torneo = torneos.crearTorneo(codigo, nombre, disciplina);
        consola.mostrar("Torneo creado: " + torneo.getCodigo() + " - " + torneo.getNombre()+ ", " + torneo.getDisciplina().getNombre() + ", " + torneo.getEstado().getEtiqueta());
    }

    public void mostrarFormularioCotizacion() {
        consola.mostrar("Cotización de inscripción");
        Disciplina disciplina = pedirDisciplina();
        int cantidad = consola.leerEntero("Cantidad de participantes: ");
        double total = cotizaciones.calcularCotizacion(disciplina, cantidad);
        consola.mostrar("Cotización: " + cantidad + " x " + Formato.monto(disciplina.getCuotaInscripcion())+ " (" + disciplina.getNombre() + ") = " + Formato.monto(total));
    }

    public void mostrarFormularioInscripcion() {
        consola.mostrar("Inscribir participante en un torneo");
        mostrarListaTorneos();
        mostrarCodigosParticipantes();
        String codigoTorneo = consola.leerTexto("Código del torneo: ");
        String codigoParticipante = consola.leerTexto("Código del participante: ");
        double cobrado = torneos.procesarInscripcion(codigoTorneo, codigoParticipante);
        consola.mostrar("Inscripción registrada. Cuota cobrada: " + Formato.monto(cobrado));
    }

    public void mostrarFormularioCierre() {
        consola.mostrar("Cerrar inscripciones");
        mostrarListaTorneos();
        String codigo = consola.leerTexto("Código del torneo: ");
        List<Evento> eventos = torneos.cerrarInscripcionesTorneo(codigo);
        consola.mostrar("Inscripciones cerradas. El torneo pasó a 'En juego' y se generaron "+ eventos.size() + " evento(s) sin programar");
        for (Evento e : eventos) {
            consola.mostrar("  " + e.getCodigo() + " , " + e.describir());
        }
    }

    public void mostrarTorneos() {
        consola.mostrar("Torneos");
        mostrarListaTorneos();
    }

    public void mostrarDetalleTorneo() {
        mostrarListaTorneos();
        Torneo torneo = torneos.obtenerTorneo(consola.leerTexto("Código del torneo: "));
        Disciplina d = torneo.getDisciplina();
        consola.mostrar( torneo.getCodigo() + " - " + torneo.getNombre());
        consola.mostrar("Disciplina: " + d.getNombre() + " , " + d.getMinParticipantes() + " a "+ d.getMaxParticipantes() + " participantes, cuota " + Formato.monto(d.getCuotaInscripcion()));
        consola.mostrar("Estado: " + torneo.getEstado().getEtiqueta());
        consola.mostrar("Ingresos por inscripciones: " + Formato.monto(torneo.getIngresos()));
        consola.mostrar("Participantes inscritos (" + torneo.getParticipantes().size() + "):");
        for (Participante p : torneo.getParticipantes()) {
            consola.mostrar("  " + p);
        }
        if (!torneo.getEventos().isEmpty()) {
            calendarioView.mostrarCalendario("Eventos programados (orden cronológico)",
                    reportes.obtenerCalendarioTorneo(torneo.getCodigo()));
            List<Evento> pendientes = torneo.getEventosSinProgramar();
            if (!pendientes.isEmpty()) {
                consola.mostrar("Eventos sin programar:");
                for (Evento e : pendientes) {
                    consola.mostrar("  " + Formato.evento(e));
                }
            }
        }
    }

    private Disciplina pedirDisciplina() {
        List<Disciplina> disciplinas = torneos.obtenerDisciplinas();
        consola.mostrar("Disciplinas:");
        for (int i = 0; i < disciplinas.size(); i++) {
            Disciplina d = disciplinas.get(i);
            consola.mostrar("  " + (i + 1) + ". " + d.getNombre() + " - " + d.getMinParticipantes() + " a "+ d.getMaxParticipantes() + " participantes - cuota " + Formato.monto(d.getCuotaInscripcion()));
        }
        int numero = consola.leerEntero("Disciplina (número): ");
        if (numero < 1 || numero > disciplinas.size()) {
            throw new ReglaNegocioException("Disciplina fuera de rango: elija un número entre 1 y "+ disciplinas.size());
        }
        return disciplinas.get(numero - 1);
    }

    private void mostrarListaTorneos() {
        List<Torneo> lista = torneos.listarTorneos();
        if (lista.isEmpty()) {
            consola.mostrar("No hay torneos creados");
            return;
        }
        consola.mostrar(String.format("%-9s %-26s %-24s %-14s %s", "Código", "Nombre", "Disciplina", "Estado", "Inscritos"));
        for (Torneo t : lista) {
            consola.mostrar(String.format("%-9s %-26.26s %-24.24s %-14s %d/%d", t.getCodigo(), t.getNombre(),
                    t.getDisciplina().getNombre(), t.getEstado().getEtiqueta(),
                    t.getParticipantes().size(), t.getDisciplina().getMaxParticipantes()));
        }
    }

    private void mostrarCodigosParticipantes() {
        StringBuilder sb = new StringBuilder();
        for (Participante p : reportes.listarParticipantes()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(p.getCodigo());
        }
        consola.mostrar("Participantes registrados: " + (sb.length() == 0 ? "ninguno" : sb));
    }
}