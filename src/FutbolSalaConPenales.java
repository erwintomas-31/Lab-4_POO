public class FutbolSalaConPenales extends FutbolSala {
    public static final double RECARGO = 150;

    public FutbolSalaConPenales() {
        super("Fútbol sala con penales", CUOTA + RECARGO);
    }

    @Override
    protected boolean admitePenales() {
        return true;
    }

    @Override
    protected void validarMarcador(ResultadoEncuentro r) {
        if (r.getMarcador1() == r.getMarcador2()) {
            if (!r.tienePenales()) {
                throw new ReglaNegocioException("En fútbol sala con penales no se aceptan empates, si los goles son iguales debe registrar los penales convertidos");
            }
            if (r.getPenales1() < 0 || r.getPenales2() < 0) {
                throw new ReglaNegocioException("Los penales convertidos no pueden ser negativos");
            }
            if (r.getPenales1().equals(r.getPenales2())) {
                throw new ReglaNegocioException("Los penales convertidos por ambos equipos no pueden ser iguales");
            }
        } else if (r.tienePenales()) {
            throw new ReglaNegocioException("Si los goles son distintos no se registran penales");
        }
    }

    @Override
    protected int ganador(ResultadoEncuentro r) {
        if (r.getMarcador1() == r.getMarcador2() && r.tienePenales()) {
            return r.getPenales1() > r.getPenales2() ? 1 : 2;
        }
        return super.ganador(r);
    }

    @Override
    protected int[] calcularPuntosTabla(ResultadoEncuentro r) {
        if (r.getMarcador1() == r.getMarcador2()) {
            return ganador(r) == 1 ? new int[]{2, 1} : new int[]{1, 2};
        }
        return super.calcularPuntosTabla(r);
    }
}
