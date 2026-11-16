public class ResultadoEncuentro extends Resultado {
    private final int marcador1;
    private final int marcador2;
    private final Integer penales1;
    private final Integer penales2;

    public ResultadoEncuentro(int marcador1, int marcador2) {
        this(marcador1, marcador2, null, null);
    }

    public ResultadoEncuentro(int marcador1, int marcador2, Integer penales1, Integer penales2) {
        if ((penales1 == null) != (penales2 == null)) {
            throw new ReglaNegocioException("Los penales deben registrarse para ambos participantes");
        }
        this.marcador1 = marcador1;
        this.marcador2 = marcador2;
        this.penales1 = penales1;
        this.penales2 = penales2;
    }

    public int getMarcador1() {
        return marcador1;
    }

    public int getMarcador2() {
        return marcador2;
    }

    public Integer getPenales1() {
        return penales1;
    }

    public Integer getPenales2() {
        return penales2;
    }

    public boolean tienePenales() {
        return penales1 != null;
    }

    @Override
    public String describir() {
        String texto = marcador1 + " - " + marcador2;
        if (tienePenales()) {
            texto += " penales " + penales1 + " - " + penales2;
        }
        return texto;
    }
}
