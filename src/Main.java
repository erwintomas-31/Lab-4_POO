public class Main {
    public static void main(String[] args) {
        ArenaGT arena = new ArenaGT();
        DatosIniciales.cargar(arena);

        ParticipanteController participanteController = new ParticipanteController(arena);
        TorneoController torneoController = new TorneoController(arena);
        CotizacionController cotizacionController = new CotizacionController();
        EventoController eventoController = new EventoController(arena);
        ReporteController reporteController = new ReporteController(arena);

        Consola consola = new Consola();
        CalendarioView calendarioView = new CalendarioView(consola, reporteController);
        TablaPosicionesView tablaView = new TablaPosicionesView(consola, reporteController);
        ReporteFinancieroView financieroView = new ReporteFinancieroView(consola, reporteController);
        ParticipanteView participanteView = new ParticipanteView(consola, participanteController);
        TorneoView torneoView = new TorneoView(consola, torneoController, cotizacionController,reporteController, calendarioView);
        EventoView eventoView = new EventoView(consola, eventoController, torneoController, tablaView);

        new MenuPrincipal(consola, participanteView, torneoView, eventoView,calendarioView, tablaView, financieroView).iniciar();
    }
}
