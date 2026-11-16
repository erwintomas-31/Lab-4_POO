import java.time.LocalDate;

public final class DatosIniciales {
    private DatosIniciales() {
    }

    public static void cargar(ArenaGT arena) {
        Participante tec1 = arena.registrarParticipante("TEC-01", "Tecnosoft Titans", "Tecnosoft");
        Participante tec2 = arena.registrarParticipante("TEC-02", "Tecnosoft Bits", "Tecnosoft");
        Participante ban1 = arena.registrarParticipante("BAN-01", "Banco Austral Leones", "Banco Austral");
        Participante ban2 = arena.registrarParticipante("BAN-02", "Banco Austral Condores", "Banco Austral");
        Participante caf1 = arena.registrarParticipante("CAF-01", "Cafe Volcan Aroma", "Cafe Volcan");
        Participante caf2 = arena.registrarParticipante("CAF-02", "Cafe Volcan Grano", "Cafe Volcan");
        Participante log1 = arena.registrarParticipante("LOG-01", "Logistica Express Rayos", "Logistica Express");
        Participante log2 = arena.registrarParticipante("LOG-02", "Logistica Express Cargo", "Logistica Express");

        Torneo fs = arena.crearTorneo("FS-2026", "Copa Apertura Futsal", disciplina(arena, FutbolSala.class));
        inscribir(fs, tec1, ban1, caf1, log1);
        fs.cerrarInscripciones();
        programar(arena, fs, 1, 2, 18, "Cancha Norte");
        resultado(fs, 1, new ResultadoEncuentro(3, 1));   
        programar(arena, fs, 6, 3, 18, "Cancha Norte");
        resultado(fs, 6, new ResultadoEncuentro(1, 0));  
        programar(arena, fs, 5, 4, 18, "Cancha Norte");
        resultado(fs, 5, new ResultadoEncuentro(2, 2));  
        programar(arena, fs, 2, 18, 19, "Cancha Norte");  
        programar(arena, fs, 3, 19, 19, "Cancha Norte");  

        Torneo fp = arena.crearTorneo("FP-2026", "Copa Penales", disciplina(arena, FutbolSalaConPenales.class));
        inscribir(fp, log2, ban2, caf2, tec2);
        fp.cerrarInscripciones();
        programar(arena, fp, 1, 2, 20, "Cancha Sur");
        resultado(fp, 1, new ResultadoEncuentro(1, 1, 5, 4)); 
        programar(arena, fp, 2, 3, 20, "Cancha Sur");
        resultado(fp, 2, new ResultadoEncuentro(3, 0));     
        programar(arena, fp, 3, 20, 20, "Cancha Sur");

        Torneo ba = arena.crearTorneo("BA-2026", "Copa Aro de Oro", disciplina(arena, Baloncesto.class));
        inscribir(ba, tec2, tec1, caf2, ban2);
        ba.cerrarInscripciones();
        programar(arena, ba, 1, 2, 16, "Gimnasio Central");
        resultado(ba, 1, new ResultadoEncuentro(70, 80));
        programar(arena, ba, 2, 3, 16, "Gimnasio Central");
        resultado(ba, 2, new ResultadoEncuentro(100, 60));
        programar(arena, ba, 3, 4, 16, "Gimnasio Central");
        resultado(ba, 3, new ResultadoEncuentro(70, 80));
        programar(arena, ba, 4, 5, 16, "Gimnasio Central");
        resultado(ba, 4, new ResultadoEncuentro(70, 60));
        programar(arena, ba, 5, 21, 16, "Gimnasio Central");

        Torneo vo = arena.crearTorneo("VO-2026", "Copa Red Alta", disciplina(arena, Voleibol.class));
        inscribir(vo, tec1, ban1, caf2, log2);
        vo.cerrarInscripciones();
        programar(arena, vo, 1, 2, 14, "Gimnasio Central");
        resultado(vo, 1, new ResultadoEncuentro(3, 0));
        programar(arena, vo, 4, 3, 14, "Gimnasio Central");
        resultado(vo, 4, new ResultadoEncuentro(3, 2));  
        programar(arena, vo, 6, 4, 14, "Gimnasio Central");
        resultado(vo, 6, new ResultadoEncuentro(1, 3));  
        programar(arena, vo, 2, 22, 14, "Gimnasio Central");

        Torneo re = arena.crearTorneo("RE-2026", "Relevos Empresariales", disciplina(arena, Relevos.class));
        inscribir(re, tec1, ban2, caf1, log1);
        re.cerrarInscripciones();
        programar(arena, re, 1, 23, 10, "Pista de Atletismo");
        Torneo fs2 = arena.crearTorneo("FS-2027", "Copa Clausura Futsal", disciplina(arena, FutbolSala.class));
        inscribir(fs2, tec2, log1);
    }

    private static Disciplina disciplina(ArenaGT arena, Class<? extends Disciplina> tipo) {
        for (Disciplina d : arena.getDisciplinas()) {
            if (d.getClass().equals(tipo)) return d;
        }
        throw new IllegalStateException("Disciplina no registrada: " + tipo.getSimpleName());
    }

    private static void inscribir(Torneo torneo, Participante... participantes) {
        for (Participante p : participantes) {
            torneo.cargarInscritoInicial(p);
        }
    }

    private static void programar(ArenaGT arena, Torneo torneo, int numeroEvento, int diaNoviembre, int hora, String sede) {
        arena.programarEvento(torneo.getEventos().get(numeroEvento - 1),
                LocalDate.of(2026, 11, diaNoviembre), hora, sede);
    }

    private static void resultado(Torneo torneo, int numeroEvento, Resultado resultado) {
        torneo.registrarResultado(torneo.getEventos().get(numeroEvento - 1), resultado);
    }
}
