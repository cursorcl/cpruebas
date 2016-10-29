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

public class UtilReportBuilder {

    /**
     * Establece las columnas fijas del reporte:
     * <ul>
     * <li>Objetivo</li>
     * <li>Preguntas asociadas</li>
     * <li>Ejes Temáticos asociados</li>
     * <li>Habilidades asociadas</li>
     * </ul>
     *
     * @param respEsperadas
     *            Corresponde a lista de respuestas esperadas para la prueba.
     * @return Lista con el contenido de ItemTablaObjetivo asociados al
     *         requerimiento.
     */
    static List<ItemTablaObjetivo> buildFixedColumnsReport(List<RespuestasEsperadasPrueba> respEsperadas) {
        final List<ItemTablaObjetivo> objetivos = new ArrayList<>();
        for (final RespuestasEsperadasPrueba re : respEsperadas) {
            ItemTablaObjetivo ot = new ItemTablaObjetivo.Builder().objetivo(re.getObjetivo()).build();
            final int index = objetivos.indexOf(ot);
            if (index != -1) {
                ot = objetivos.get(index);
            } else {
                objetivos.add(ot);
            }
            String value;
            if (!ot.getEjesAsociados().contains(re.getEjeTematico().getName())) {
                value = ot.getEjesAsociados() + (ot.getEjesAsociados().isEmpty() ? "" : "\n")
                        + re.getEjeTematico().getName().toUpperCase();
                ot.setEjesAsociados(value);
            }
            if (!ot.getHabilidades().contains(re.getHabilidad().getName())) {
                value = ot.getHabilidades() + (ot.getHabilidades().isEmpty() ? "" : "\n")
                        + re.getHabilidad().getName().toUpperCase();
                ot.setHabilidades(value);
            }
            value = ot.getPreguntas() + (ot.getPreguntas().isEmpty() ? "" : "-") + re.getNumero();
            ot.setPreguntas(value);
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
    public static Pair<List<Curso>, List<ItemTablaObjetivo>> buildReportColegio(List<PruebaRendida> pruebas) {

        final Optional<PruebaRendida> first = pruebas.stream().findFirst();
        if (!first.isPresent())
            return null;
        final List<RespuestasEsperadasPrueba> respEsperadas = first.get().getEvaluacionPrueba().getPrueba()
                .getRespuestas();
        final List<ItemTablaObjetivo> objetivos = UtilReportBuilder.buildFixedColumnsReport(respEsperadas);

        final Map<Curso, List<ItemObjetivo>> eval = UtilEvaluations.evaluarColegio(pruebas);

        final List<Curso> cursosOrdenados = eval.keySet().stream().sorted(Comparadores.odenarCurso())
                .collect(Collectors.toList());

        final int size = cursosOrdenados.size();

        objetivos.forEach(o -> o.setItems(Stream.generate(ItemObjetivo::new).limit(size).collect(Collectors.toList())));

        final int n = 0;
        for (final Curso key : cursosOrdenados) {
            final List<ItemObjetivo> values = eval.get(key);
            objetivos.forEach(o -> o.getItems().set(n,
                    values.stream().filter(e -> e.objetivo.equals(o.getObjetivo())).findFirst().orElse(null)));
        }

        return new Pair<List<Curso>, List<ItemTablaObjetivo>>(cursosOrdenados, objetivos);
    }

    public static Pair<List<TipoCurso>, List<ItemTablaObjetivo>> buildReportColegioxNivel(List<PruebaRendida> pruebas) {

        final Optional<PruebaRendida> first = pruebas.stream().findFirst();
        if (!first.isPresent())
            return null;
        final List<RespuestasEsperadasPrueba> respEsperadas = first.get().getEvaluacionPrueba().getPrueba()
                .getRespuestas();
        final List<ItemTablaObjetivo> objetivos = UtilReportBuilder.buildFixedColumnsReport(respEsperadas);

        final Map<TipoCurso, List<ItemObjetivo>> eval = UtilEvaluations.evaluarColegioxNivel(pruebas);

        // Obtengo los tipos de curso ordenados

        final List<TipoCurso> tipoCursosOrdenados = eval.keySet().stream().sorted(Comparadores.comparaTipoCurso())
                .collect(Collectors.toList());

        final int size = tipoCursosOrdenados.size();
        objetivos.forEach(o -> o.setItems(Stream.generate(ItemObjetivo::new).limit(size).collect(Collectors.toList())));
        final int n = 0;
        for (final TipoCurso key : tipoCursosOrdenados) {
            final List<ItemObjetivo> values = eval.get(key);
            objetivos.forEach(o -> o.getItems().set(n,
                    values.stream().filter(e -> e.objetivo.equals(o.getObjetivo())).findFirst().orElse(null)));
        }

        return new Pair<List<TipoCurso>, List<ItemTablaObjetivo>>(tipoCursosOrdenados, objetivos);
    }

    // public static Pair<List<Colegio>, Pair< List<Curso>,
    // List<ItemTablaObjetivo>>> buildReportComunal(List<PruebaRendida> pruebas)
    // {
    // Optional<PruebaRendida> first = pruebas.stream().findFirst();
    // if (!first.isPresent())
    // return null;
    // List<RespuestasEsperadasPrueba> respEsperadas =
    // first.get().getEvaluacionPrueba().getPrueba().getRespuestas();
    // List<ItemTablaObjetivo> objetivos =
    // buildFixedReportClumns(respEsperadas);
    //
    // return objetivos;
    // }
    //
    // public static List<ItemTablaObjetivo>
    // buildReportComunalxCurso(List<PruebaRendida> pruebas) {
    // IResultado evaluacion = evaluador.evaluar(pruebas);
    // List<RespuestasEsperadasPrueba> respEsperadas =
    // pruebas.get(0).getEvaluacionPrueba().getPrueba()
    // .getRespuestas();
    // List<ItemTablaObjetivo> objetivos =
    // buildFixedReportClumns(respEsperadas);
    // List<TitledItemObjetivo> values = evaluacion.getResultados();
    // return objetivos;
    // }
    //
    // public static List<ItemTablaObjetivo>
    // buildReportComunalxNivel(List<PruebaRendida> pruebas) {
    // IResultado evaluacion = evaluador.evaluar(pruebas);
    // List<RespuestasEsperadasPrueba> respEsperadas =
    // pruebas.get(0).getEvaluacionPrueba().getPrueba()
    // .getRespuestas();
    // List<ItemTablaObjetivo> objetivos =
    // buildFixedReportClumns(respEsperadas);
    // List<TitledItemObjetivo> values = evaluacion.getResultados();
    // return objetivos;
    // }

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
    public static List<ItemTablaObjetivo> buildReportCurso(List<PruebaRendida> pruebas) {
        final Optional<PruebaRendida> first = pruebas.stream().findFirst();
        if (!first.isPresent())
            return null;
        final List<RespuestasEsperadasPrueba> respEsperadas = first.get().getEvaluacionPrueba().getPrueba()
                .getRespuestas();
        final List<ItemTablaObjetivo> objetivos = UtilReportBuilder.buildFixedColumnsReport(respEsperadas);
        final List<ItemObjetivo> eval = UtilEvaluations.evaluarCurso(pruebas);
        for (final ItemTablaObjetivo obj : objetivos) {
            for (final ItemObjetivo item : eval) {
                if (item.getObjetivo().equals(obj.getObjetivo())) {
                    obj.getItems().add(item);
                }
            }
        }
        // objetivos.forEach(o -> o.getItems()
        // .add(eval.stream().filter(e ->
        // e.objetivo.equals(o.getObjetivo())).findFirst().orElse(null)));

        return objetivos;
    }
}
