public class CotizacionController {
    public double calcularCotizacion(Disciplina disciplina, int cantidadParticipantes) {
        if (disciplina == null) {
            throw new ReglaNegocioException("Debe seleccionar una disciplina");
        }
        return disciplina.calcularCotizacion(cantidadParticipantes);
    }
}
