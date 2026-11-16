import java.util.List;
import java.util.Objects;

public class Empresa {
    private final String nombre;

    public Empresa(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public int calcularPuntosTotales(List<Torneo> torneos) {
        int total = 0;
        for (Torneo torneo : torneos) {
            for (EntradaTabla entrada : torneo.getEntradasTabla()) {
                if (entrada.getParticipante().getEmpresa().equals(this)) {
                    total += entrada.getPuntosTabla();
                }
            }
        }
        return total;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Empresa && nombre.equalsIgnoreCase(((Empresa) o).nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre.toLowerCase());
    }

    @Override
    public String toString() {
        return nombre;
    }
}
