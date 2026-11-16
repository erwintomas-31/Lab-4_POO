public class FutbolSala extends DisciplinaDeEncuentros {
    public static final double CUOTA = 600;

    public FutbolSala() {
        this("Fútbol sala", CUOTA);
    }

    protected FutbolSala(String nombre, double cuota) {
        super(nombre, 4, 8, cuota);
    }

    @Override
    protected void validarMarcador(ResultadoEncuentro r) {
    }

    @Override
    protected int[] calcularPuntosTabla(ResultadoEncuentro r) {
        switch (ganador(r)) {
            case 1:
                return new int[]{3, 0};
            case 2:
                return new int[]{0, 3};
            default:
                return new int[]{1, 1};
        }
    }
}
