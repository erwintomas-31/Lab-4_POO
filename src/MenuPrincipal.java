public class MenuPrincipal {
    private final Consola consola;
    private final ParticipanteView participanteView;
    private final TorneoView torneoView;
    private final EventoView eventoView;
    private final CalendarioView calendarioView;
    private final TablaPosicionesView tablaView;
    private final ReporteFinancieroView financieroView;

    public MenuPrincipal(Consola consola, ParticipanteView participanteView, TorneoView torneoView, EventoView eventoView, CalendarioView calendarioView, TablaPosicionesView tablaView, ReporteFinancieroView financieroView) {
        this.consola = consola;
        this.participanteView = participanteView;
        this.torneoView = torneoView;
        this.eventoView = eventoView;
        this.calendarioView = calendarioView;
        this.tablaView = tablaView;
        this.financieroView = financieroView;
    }

    public void iniciar() {
        consola.mostrar("   ArenaGT - Copas deportivas");
        try {
            ejecutarMenu("MENÚ PRINCIPAL", "Salir",
                    new String[]{
                            "Cotizar e inscribir participantes en un torneo",
                            "Registrar participantes y crear torneos",
                            "Cerrar inscripciones",
                            "Programar y reprogramar eventos",
                            "Registrar resultados",
                            "Consultar torneos, participantes y calendarios",
                            "Generar reportes"},
                    new Runnable[]{
                            this::menuInscripciones,
                            this::menuRegistro,
                            torneoView::mostrarFormularioCierre,
                            eventoView::mostrarFormularioProgramacion,
                            eventoView::mostrarFormularioResultado,
                            this::menuConsultas,
                            this::menuReportes});
            consola.mostrar("Cerrando Programa");
        } catch (Consola.FinDeEntradaException e) {
            consola.mostrar("Fin de la entrada");
        }
    }

    private void menuInscripciones() {
        ejecutarMenu("COTIZAR E INSCRIBIR", "Volver",
                new String[]{"Cotizar una inscripción", "Inscribir un participante en un torneo"},
                new Runnable[]{torneoView::mostrarFormularioCotizacion, torneoView::mostrarFormularioInscripcion});
    }

    private void menuRegistro() {
        ejecutarMenu("REGISTRO", "Volver",
                new String[]{"Registrar un participante", "Crear un torneo"},
                new Runnable[]{participanteView::mostrarFormularioParticipante, torneoView::mostrarFormularioTorneo});
    }

    private void menuConsultas() {
        ejecutarMenu("CONSULTAS", "Volver",
                new String[]{"Ver torneos", "Ver detalle de un torneo", "Ver participantes","Calendario general", "Calendario de un torneo", "Calendario de un participante"},
                new Runnable[]{torneoView::mostrarTorneos, torneoView::mostrarDetalleTorneo, participanteView::mostrarParticipantes, calendarioView::mostrarCalendarioGeneral, calendarioView::mostrarCalendarioTorneo, calendarioView::mostrarCalendarioParticipante});
    }

    private void menuReportes() {
        ejecutarMenu("REPORTES", "Volver",
                new String[]{"Tabla de posiciones y campeón de un torneo", "Calendario general", "Calendario de un torneo", "Calendario de un participante", "Ingresos por inscripciones y premios entregados", "Ranking de empresas"},
                new Runnable[]{tablaView::mostrarFormularioTabla, calendarioView::mostrarCalendarioGeneral, calendarioView::mostrarCalendarioTorneo, calendarioView::mostrarCalendarioParticipante, financieroView::mostrarReporteFinanciero, tablaView::mostrarRankingEmpresas});
    }

    private void ejecutarMenu(String titulo, String etiquetaSalida, String[] opciones, Runnable[] acciones) {
        boolean terminar = false;
        while (!terminar) {
            consola.mostrar( titulo);
            for (int i = 0; i < opciones.length; i++) {
                consola.mostrar((i + 1) + ". " + opciones[i]);
            }
            consola.mostrar("0. " + etiquetaSalida);
            try {
                int opcion = consola.leerEntero("Seleccione una opción: ");
                if (opcion == 0) {
                    terminar = true;
                } else if (opcion >= 1 && opcion <= acciones.length) {
                    acciones[opcion - 1].run();
                } else {
                    consola.mostrarError("Opción fuera de rango: elija un número entre 0 y " + acciones.length);
                }
            } catch (ReglaNegocioException e) {
                consola.mostrarError(e.getMessage());
            } catch (Consola.FinDeEntradaException e) {
                throw e;
            } catch (RuntimeException e) {
                consola.mostrarError("Ocurrió un error inesperado: " + e.getClass().getSimpleName() + ", La operación se canceló y los datos no cambiaron");
            }
        }
    }
}
