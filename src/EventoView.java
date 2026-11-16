import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class EventoView {
    private final Consola consola;
    private final EventoController eventos;
    private final TorneoController torneos;
    private final TablaPosicionesView tablaView;

    public EventoView(Consola consola, EventoController eventos, TorneoController torneos, TablaPosicionesView tablaView) {
        this.consola = consola;
        this.eventos = eventos;
        this.torneos = torneos;
        this.tablaView = tablaView;
    }

    public void mostrarFormularioProgramacion() {
        consola.mostrar("Programar / reprogramar evento");
        Torneo torneo = pedirTorneoEnJuego();
        consola.mostrar("Eventos que admiten programación:");
        boolean hay = false;
        for (Evento e : torneo.getEventos()) {
            if (!e.estaFinalizado()) {
                consola.mostrar("  " + Formato.evento(e));
                hay = true;
            }
        }
        if (!hay) {
            throw new ReglaNegocioException("El torneo no tiene eventos pendientes de programar");
        }
        String codigoEvento = consola.leerTexto("Código del evento: ");
        LocalDate fecha = consola.leerFecha("Fecha día, mes, año: ");
        int hora = consola.leerEntero("Hora de inicio (0 a 23): ");
        String sede = consola.leerTexto("Sede: ");
        eventos.programarEvento(codigoEvento, fecha, hora, sede);
        consola.mostrar("Evento programado: " + Formato.evento(eventos.obtenerEvento(codigoEvento)));
    }

    public void mostrarFormularioResultado() {
        consola.mostrar("Registrar resultado");
        Torneo torneo = pedirTorneoEnJuego();
        consola.mostrar("Eventos programados, pendientes de resultado:");
        boolean hay = false;
        for (Evento e : torneo.getEventos()) {
            if (e.estaProgramado()) {
                consola.mostrar("  " + Formato.evento(e));
                hay = true;
            }
        }
        if (!hay) {
            throw new ReglaNegocioException("El torneo no tiene eventos programados pendientes de resultado");
        }
        String codigoEvento = consola.leerTexto("Código del evento: ");
        Evento evento = eventos.obtenerEventoParaResultado(codigoEvento);

        Torneo actualizado;
        if (evento instanceof Encuentro) {
            actualizado = pedirResultadoEncuentro((Encuentro) evento);
        } else if (evento instanceof Carrera) {
            actualizado = pedirResultadoCarrera((Carrera) evento);
        } else {
            throw new ReglaNegocioException("Este tipo de evento aún no se puede registrar");
        }
        consola.mostrar("Resultado registrado. El evento " + evento.getCodigo() + " quedó finalizado");
        tablaView.mostrarTabla(actualizado);
        if (actualizado.getEstado() == EstadoTorneo.FINALIZADO) {
            consola.mostrar("El torneo finalizó. Campeón: " + actualizado.getCampeon().getNombre() + ". Premio entregado: " + Formato.monto(actualizado.getPremioEntregado()));
        }
    }

    private Torneo pedirTorneoEnJuego() {
        consola.mostrar("Torneos en juego:");
        boolean hay = false;
        for (Torneo t : torneos.listarTorneos()) {
            if (t.getEstado() == EstadoTorneo.EN_JUEGO) {
                consola.mostrar("  " + t.getCodigo() + " - " + t.getNombre());
                hay = true;
            }
        }
        if (!hay) {
            throw new ReglaNegocioException("No hay torneos en juego");
        }
        Torneo torneo = torneos.obtenerTorneo(consola.leerTexto("Código del torneo: "));
        if (torneo.getEstado() != EstadoTorneo.EN_JUEGO) {
            throw new ReglaNegocioException("El torneo '" + torneo.getCodigo() + "' no está en juego, estado: "+ torneo.getEstado().getEtiqueta());
        }
        return torneo;
    }

    private Torneo pedirResultadoEncuentro(Encuentro e) {
        Participante p1 = e.getParticipante1();
        Participante p2 = e.getParticipante2();
        consola.mostrar("Marcador según la disciplina (goles, puntos o sets ganados)");
        int m1 = consola.leerEntero("Marcador de " + p1.getNombre() + ": ");
        int m2 = consola.leerEntero("Marcador de " + p2.getNombre() + ": ");
        Integer pen1 = consola.leerEnteroOpcional("Penales convertidos por " + p1.getNombre()+ " (Enter si no hubo penales): ");
        Integer pen2 = null;
        if (pen1 != null) {
            pen2 = consola.leerEntero("Penales convertidos por " + p2.getNombre() + ": ");
        }
        return eventos.registrarResultadoEncuentro(e.getCodigo(), m1, m2, pen1, pen2);
    }

    private Torneo pedirResultadoCarrera(Carrera c) {
        Map<String, Double> tiempos = new LinkedHashMap<>();
        Set<String> descalificados = new LinkedHashSet<>();
        consola.mostrar("Para cada participante indique su tiempo en segundos, o D si fue descalificado");
        for (Participante p : c.getParticipantes()) {
            String entrada = consola.leerTexto("  " + p.getNombre() + ", " + p.getCodigo() + " : ");
            if (entrada.equalsIgnoreCase("D")) {
                descalificados.add(p.getCodigo());
            } else {
                tiempos.put(p.getCodigo(), consola.convertirDecimal(entrada));
            }
        }
        return eventos.registrarResultadoCarrera(c.getCodigo(), tiempos, descalificados);
    }
}
