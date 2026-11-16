public class Puntuacion {
    private final Participante participante;
    private final int puntosTabla;
    private final int diferencia;
    private final boolean victoria;

    public Puntuacion(Participante participante, int puntosTabla, int diferencia, boolean victoria) {
        this.participante = participante;
        this.puntosTabla = puntosTabla;
        this.diferencia = diferencia;
        this.victoria = victoria;
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

    public boolean esVictoria() {
        return victoria;
    }
}
