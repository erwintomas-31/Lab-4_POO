public class Baloncesto extends DisciplinaDeEncuentros {
    public Baloncesto() {
        super("Baloncesto", 4, 6, 500);
    }

    @Override
    protected void validarMarcador(ResultadoEncuentro r) {
        if (r.getMarcador1() == r.getMarcador2()) {
            throw new ReglaNegocioException("En baloncesto no se aceptan marcadores iguales");
        }
    }

    @Override
    protected int[] calcularPuntosTabla(ResultadoEncuentro r) {
        return ganador(r) == 1 ? new int[]{2, 1} : new int[]{1, 2};
    }
}
