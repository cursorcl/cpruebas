package ot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.models.TipoCurso;
import cl.eos.persistence.util.Comparadores;
import cl.eos.util.Pair;

public class XUtilReportBuilder {

    

    public static Pair<List<TipoCurso>, List<XItemTablaObjetivo>> reporteColegioxNivel(List<PruebaRendida> pruebas, long tipoAlumno) {

        final List<XItemTablaObjetivo> objetivos = XUtilReportBuilder.buildFixedColumnsReport(pruebas);

        final Map<TipoCurso, List<XItemObjetivo>> eval = XUtilEvaluations.evaluarColegioxNivel(pruebas, tipoAlumno);

        // Obtengo los tipos de curso ordenados
        final List<TipoCurso> tipoCursosOrdenados = eval.keySet().stream().sorted(Comparadores.comparaTipoCurso())
                .collect(Collectors.toList());

        final int size = tipoCursosOrdenados.size();
        objetivos.forEach(o -> o.setItems(Stream.generate(XItemObjetivo::new).limit(size).collect(Collectors.toList())));
        int n = 0;
        for (final TipoCurso key : tipoCursosOrdenados) {
            final int idx = n;
            final List<XItemObjetivo> values = eval.get(key);
            objetivos.forEach(o -> o.getItems().set(idx,
                    values.stream().filter(e -> e.objetivo.equals(o.getObjetivo())).findFirst().orElse(null)));
            n++;
        }

        return new Pair<List<TipoCurso>, List<XItemTablaObjetivo>>(tipoCursosOrdenados, objetivos);
    }

    
    /**
     * Establece las columnas que son comunes con los valores calculados de las
     * pruebas rendidas.
     * 
     * @param pRendidas
     *            Lista de pruebas rendidas con las que se debe calcular los
     *            objetivos, preguntas asociadas, ejes y habilidades.
     * @return Lista con los items de tabla asociados.
     */
    static List<XItemTablaObjetivo> buildFixedColumnsReport(List<PruebaRendida> pRendidas) {
        final List<XItemTablaObjetivo> objetivos = new ArrayList<>();

        for(PruebaRendida p : pRendidas )
        {
        	System.out.println(p.getAlumno() + " " + p.getCurso() + " "  + p.getObjCurso());
        }
        final Map<Curso, List<PruebaRendida>> mapCursos = pRendidas.stream()
                .collect(Collectors.groupingBy(PruebaRendida::getObjCurso));

        for (Curso curso : mapCursos.keySet()) {
            List<PruebaRendida> pR = mapCursos.get(curso);
            PruebaRendida p = pR.get(0);
            List<RespuestasEsperadasPrueba> respuestasEsperadas = p.getEvaluacionPrueba().getPrueba().getRespuestas();
            for (final RespuestasEsperadasPrueba re : respuestasEsperadas) {
                XItemTablaObjetivo ot = new XItemTablaObjetivo.Builder().objetivo(re.getObjetivo()).build();
                final int index = objetivos.indexOf(ot);
                if (index != -1) {
                    ot = objetivos.get(index);
                } else {
                    objetivos.add(ot);
                }
            }
        }
        return objetivos;
    }
    
    /**
     * Construye una lista de ItemTablaObjetivo que es la clase que se mapea a
     * la tabla donde se muestra el reporte. Se llena cada item con un objetivo,
     * preguntas, ejes y habilidades asocidas y una lista de
     * <code>ItemObjetivo</code>,
     * <p>
     * el que contiene el objetivo, nro de buenas y cuenta de preguntas
     * asociadas.
     *
     * @param pruebas
     *            Lista de pruebas rendidas.
     * @return Calculo de las buenas de cada objetivo y curso.
     */
    public static List<XItemTablaObjetivo> reporteCurso(List<PruebaRendida> pruebas, long tipoAlumno) {
        final List<XItemTablaObjetivo> objetivos = XUtilReportBuilder.buildFixedColumnsReport(pruebas);
        final List<XItemObjetivo> eval = XUtilEvaluations.evaluarCurso(pruebas, tipoAlumno);

        for (final XItemObjetivo item : eval) {
            Optional<XItemTablaObjetivo> obj = objetivos.stream().filter(o -> o.getObjetivo().equals(item.getObjetivo()))
                    .findFirst();
            if (obj.isPresent()) {
                obj.get().getItems().add(item);
            }
        }

        return objetivos;
    }

    /**
     * Construye una lista de ItemTablaObjetivo que es la clase que se mapea a
     * la tabla donde se muestra el reporte. Se llena cada item con un objetivo,
     * preguntas, ejes y habilidades asocidas y una lista de
     * <code>ItemObjetivo</code>,
     * <p>
     * el que contiene el objetivo, nro de buenas y cuenta de preguntas
     * asociadas.
     * <p>
     * Se crea un ItemObjetivo por curso que participa.
     * 
     * @param pruebas
     *            Lista de pruebas rendidas.
     * @return Calculo de las buenas de cada objetivo y lista de cursos.
     */
    public static Pair<List<Curso>, List<XItemTablaObjetivo>> reporteColegio(List<PruebaRendida> pruebas, long tipoAlumno) {

        final List<XItemTablaObjetivo> objetivos = XUtilReportBuilder.buildFixedColumnsReport(pruebas);

        final Map<Curso, List<XItemObjetivo>> eval = XUtilEvaluations.evaluarColegio(pruebas, tipoAlumno);

        final List<Curso> cursosOrdenados = eval.keySet().stream().sorted(Comparadores.odenarCurso())
                .collect(Collectors.toList());

        final int size = cursosOrdenados.size();

        objetivos.forEach(o -> o.setItems(Stream.generate(XItemObjetivo::new).limit(size).collect(Collectors.toList())));

        int n = 0;
        for (final Curso key : cursosOrdenados) {
            final int idx = n;
            final List<XItemObjetivo> values = eval.get(key);
            objetivos.forEach(o -> o.getItems().set(idx,
                    values.stream().filter(e -> e.objetivo.equals(o.getObjetivo())).findFirst().orElse(null)));
            n++;
        }

        return new Pair<List<Curso>, List<XItemTablaObjetivo>>(cursosOrdenados, objetivos);
    }

}
