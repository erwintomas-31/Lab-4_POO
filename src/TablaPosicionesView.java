import java.util.List;
import java.util.Map;

public class TablaPosicionesView {
    private final Consola consola;
    private final ReporteController reportes;

    public TablaPosicionesView(Consola consola, ReporteController reportes) {
        this.consola = consola;
        this.reportes = reportes;
    }

    public void mostrarFormularioTabla() {
        StringBuilder codigos = new StringBuilder();
        for (Torneo t : reportes.listarTorneos()) {
            if (codigos.length() > 0) codigos.append(", ");
            codigos.append(t.getCodigo());
        }
        consola.mostrar("Torneos: " + codigos);
        mostrarTabla(reportes.obtenerTorneo(consola.leerTexto("Código del torneo: ")));
    }

    public void mostrarTabla(Torneo torneo) {
        consola.mostrar("Tabla de posiciones: " + torneo.getCodigo() + " - " + torneo.getNombre()+ " , " + torneo.getDisciplina().getNombre() + ", " + torneo.getEstado().getEtiqueta());
        List<EntradaTabla> tabla = reportes.obtenerTablaPosiciones(torneo.getCodigo());
        if (tabla.isEmpty()) {
            consola.mostrar("El torneo aún no tiene participantes inscritos");
            return;
        }
        consola.mostrar(String.format("%-4s %-8s %-28s %-18s %4s %5s %4s %3s", "Pos", "Código", "Participante", "Empresa", "Pts", "Dif", "Vic", "PJ"));
        int posicion = 1;
        for (EntradaTabla e : tabla) {
            Participante p = e.getParticipante();
            consola.mostrar(String.format("%-4d %-8s %-28.28s %-18.18s %4d %5s %4d %3d",
                    posicion++, p.getCodigo(), p.getNombre(), p.getEmpresa().getNombre(), e.getPuntosTabla(), Formato.diferencia(e.getDiferencia()), e.getVictorias(), e.getEventosJugados()));
        }
        Participante campeon = reportes.obtenerCampeon(torneo.getCodigo());
        if (campeon != null) {
            consola.mostrar("Campeón: " + campeon.getNombre() + " (" + campeon.getEmpresa().getNombre() + ")");
        } else {
            consola.mostrar("El torneo todavía no finaliza, aún no hay campeón");
        }
    }

    public void mostrarRankingEmpresas() {
        consola.mostrar("Ranking de empresas");
        Map<Empresa, Integer> ranking = reportes.generarRankingEmpresas();
        if (ranking.isEmpty()) {
            consola.mostrar("No hay empresas registradas");
            return;
        }
        consola.mostrar(String.format("%-4s %-24s %6s", "Pos", "Empresa", "Puntos"));
        int posicion = 1;
        for (Map.Entry<Empresa, Integer> e : ranking.entrySet()) {
            consola.mostrar(String.format("%-4d %-24.24s %6d", posicion++, e.getKey().getNombre(), e.getValue()));
        }
    }
}