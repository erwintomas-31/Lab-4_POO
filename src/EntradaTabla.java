import java.util.Comparator;

public class EntradaTabla {
    public static final Comparator<EntradaTabla> ORDEN_POSICIONES = (a, b) -> {
        int c = Integer.compare(b.puntosTabla, a.puntosTabla);
        if (c != 0) return c;
        c = Integer.compare(b.diferencia, a.diferencia);
        if (c != 0) return c;
        c = Integer.compare(b.victorias, a.victorias);
        if (c != 0) return c;
        c = a.participante.getNombre().compareToIgnoreCase(b.participante.getNombre());
        if (c != 0) return c;
        return a.participante.getCodigo().compareToIgnoreCase(b.participante.getCodigo());
    };

    private final Participante participante;
    private int puntosTabla;
    private int diferencia;
    private int victorias;
    private int eventosJugados;

    public EntradaTabla(Participante participante) {
        this.participante = participante;
    }

    void acumular(Puntuacion p) {
        puntosTabla += p.getPuntosTabla();
        diferencia += p.getDiferencia();
        if (p.esVictoria()) victorias++;
        eventosJugados++;
    }

    public Participante getParticipante() {
        return participante;
    }

    public int getPuntosTabla() {
        return puntosTabla;
    }

    public int getDiferencia() {
        return diferencia;
    }

    public int getVictorias() {
        return victorias;
    }

    public int getEventosJugados() {
        return eventosJugados;
    }
}
