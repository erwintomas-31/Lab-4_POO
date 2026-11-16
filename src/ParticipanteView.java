import java.util.List;

public class ParticipanteView {
    private final Consola consola;
    private final ParticipanteController controlador;

    public ParticipanteView(Consola consola, ParticipanteController controlador) {
        this.consola = consola;
        this.controlador = controlador;
    }

    public void mostrarFormularioParticipante() {
        consola.mostrar("Registrar participante");
        String codigo = consola.leerTexto("Código del participante: ");
        String nombre = consola.leerTexto("Nombre (equipo, relevo o cuarteto): ");
        String empresa = consola.leerTexto("Empresa que representa: ");
        Participante p = controlador.registrarParticipante(codigo, nombre, empresa);
        consola.mostrar("Participante registrado: " + p);
    }

    public void mostrarParticipantes() {
        consola.mostrar("Participantes");
        List<Participante> lista = controlador.listarParticipantes();
        if (lista.isEmpty()) {
            consola.mostrar("No hay participantes registrados");
            return;
        }
        consola.mostrar(String.format("%-8s %-30s %-20s", "Código", "Nombre", "Empresa"));
        for (Participante p : lista) {
            consola.mostrar(String.format("%-8s %-30.30s %-20.20s",
                    p.getCodigo(), p.getNombre(), p.getEmpresa().getNombre()));
        }
    }
}
