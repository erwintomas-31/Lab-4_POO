import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;

public class Consola {

    public static class FinDeEntradaException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    private static final DateTimeFormatter FECHA_LATINA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private final Scanner scanner = new Scanner(System.in);

    public void mostrar(String texto) {
        System.out.println(texto);
    }

    public void mostrarError(String texto) {
        System.out.println("! " + texto);
    }

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        if (!scanner.hasNextLine()) {
            throw new FinDeEntradaException();
        }
        return scanner.nextLine().trim();
    }

    public int leerEntero(String mensaje) {
        String texto = leerTexto(mensaje);
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            throw new ReglaNegocioException("Entrada no válida: " + texto + " no es un número entero");
        }
    }

    public Integer leerEnteroOpcional(String mensaje) {
        String texto = leerTexto(mensaje);
        if (texto.isEmpty()) return null;
        try {
            return Integer.valueOf(texto);
        } catch (NumberFormatException e) {
            throw new ReglaNegocioException("Entrada no válida: " + texto + " no es un número entero");
        }
    }

    public double convertirDecimal(String texto) {
        try {
            return Double.parseDouble(texto.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new ReglaNegocioException("Entrada no válida: " + texto + " no es un número");
        }
    }

    public LocalDate leerFecha(String mensaje) {
        String texto = leerTexto(mensaje);
        try {
            return LocalDate.parse(texto);
        } catch (DateTimeParseException e1) {
            try {
                return LocalDate.parse(texto, FECHA_LATINA);
            } catch (DateTimeParseException e2) {
                throw new ReglaNegocioException("Fecha no válida: " + texto+ " Use el formato día, mes, año");
            }
        }
    }
}