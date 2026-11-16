public class Voleibol extends DisciplinaDeEncuentros {

    public Voleibol() {
        super("Voleibol", 4, 6, 450);
    }

    @Override
    protected void validarMarcador(ResultadoEncuentro r) {
        int mayor = Math.max(r.getMarcador1(), r.getMarcador2());
        int menor = Math.min(r.getMarcador1(), r.getMarcador2());
        if (mayor != 3 || menor > 2) {
            throw new ReglaNegocioException(
                    "En voleibol el ganador siempre tiene 3 sets y el perdedor 0, 1 o 2");
        }
    }

    @Override
    protected int[] calcularPuntosTabla(ResultadoEncuentro r) {
        int menor = Math.min(r.getMarcador1(), r.getMarcador2());
        int[] ganadorPrimero = (menor == 2) ? new int[]{2, 1} : new int[]{3, 0};
        return ganador(r) == 1 ? ganadorPrimero : new int[]{ganadorPrimero[1], ganadorPrimero[0]};
    }
}