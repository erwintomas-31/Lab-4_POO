import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class Formato {
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Formato() {
    }

    public static String monto(double monto) {
        return String.format(Locale.US, "Q%,.2f", monto);
    }

    public static String fecha(LocalDate fecha) {
        return FECHA.format(fecha);
    }

    public static String hora(int hora) {
        return String.format("%02d:00", hora);
    }

    public static String diferencia(int diferencia) {
        return diferencia > 0 ? "+" + diferencia : String.valueOf(diferencia);
    }

    public static String evento(Evento e) {
        StringBuilder sb = new StringBuilder();
        if (e.tieneHorario()) {
            sb.append(fecha(e.getFecha())).append(' ').append(hora(e.getHora())).append(" , ").append(e.getSede());
        } else {
            sb.append("Sin programar");
        }
        sb.append(" , ").append(e.getCodigo()).append(" , ").append(e.describir()).append(" , ").append(e.getEstado().getEtiqueta());
        if (e.getResultado() != null) {
            sb.append(" , Resultado: ").append(e.getResultado().describir());
        }
        return sb.toString();
    }
}
