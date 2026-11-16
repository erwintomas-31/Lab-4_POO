import java.util.Map;

public class ReporteFinancieroView {
    private final Consola consola;
    private final ReporteController reportes;

    public ReporteFinancieroView(Consola consola, ReporteController reportes) {
        this.consola = consola;
        this.reportes = reportes;
    }

    public void mostrarReporteFinanciero() {
        consola.mostrar("Ingresos por inscripciones");
        consola.mostrar("Total: " + Formato.monto(reportes.calcularIngresosTotales()));
        consola.mostrar("Por disciplina:");
        for (Map.Entry<String, Double> e : reportes.calcularIngresosPorDisciplina().entrySet()) {
            consola.mostrar(String.format("  %-26s %s", e.getKey(), Formato.monto(e.getValue())));
        }
        consola.mostrar("Premios entregados");
        consola.mostrar("Total: " + Formato.monto(reportes.calcularPremiosEntregados()));
        if (reportes.obtenerTorneosFinalizados().isEmpty()) {
            consola.mostrar("Todavía no hay torneos finalizados");
        }
        for (Torneo t : reportes.obtenerTorneosFinalizados()) {
            consola.mostrar("  " + t.getCodigo() + " - " + t.getNombre() + ": " + Formato.monto(t.getPremioEntregado())+ " para " + t.getCampeon().getNombre());
        }
    }
}