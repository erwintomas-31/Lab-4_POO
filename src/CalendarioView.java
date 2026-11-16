import java.util.List;

public class CalendarioView {
    private final Consola consola;
    private final ReporteController reportes;

    public CalendarioView(Consola consola, ReporteController reportes) {
        this.consola = consola;
        this.reportes = reportes;
    }

    public void mostrarCalendario(String titulo, List<Evento> eventos) {
        consola.mostrar(titulo);
        if (eventos.isEmpty()) {
            consola.mostrar("No hay eventos programados");
            return;
        }
        for (Evento e : eventos) {
            consola.mostrar(Formato.evento(e));
        }
    }

    public void mostrarCalendarioGeneral() {
        mostrarCalendario("Calendario general", reportes.obtenerCalendarioGeneral());
    }

    public void mostrarCalendarioTorneo() {
        StringBuilder codigos = new StringBuilder();
        for (Torneo t : reportes.listarTorneos()) {
            if (codigos.length() > 0) codigos.append(", ");
            codigos.append(t.getCodigo());
        }
        consola.mostrar("Torneos: " + codigos);
        String codigo = consola.leerTexto("Código del torneo: ");
        Torneo torneo = reportes.obtenerTorneo(codigo);
        mostrarCalendario("Calendario del torneo " + torneo.getCodigo() + " - " + torneo.getNombre(),reportes.obtenerCalendarioTorneo(codigo));
    }

    public void mostrarCalendarioParticipante() {
        StringBuilder codigos = new StringBuilder();
        for (Participante p : reportes.listarParticipantes()) {
            if (codigos.length() > 0) codigos.append(", ");
            codigos.append(p.getCodigo());
        }
        consola.mostrar("Participantes: " + codigos);
        String codigo = consola.leerTexto("Código del participante: ");
        Participante participante = reportes.obtenerParticipante(codigo);
        mostrarCalendario("Calendario de " + participante.getNombre(),reportes.obtenerCalendarioParticipante(codigo));
    }
}