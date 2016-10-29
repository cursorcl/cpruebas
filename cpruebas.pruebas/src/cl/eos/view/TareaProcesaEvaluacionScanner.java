package cl.eos.view;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cl.eos.detection.ExtractorResultadosPrueba;
import cl.eos.detection.OTResultadoScanner;
import cl.eos.exceptions.CPruebasException;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RangoEvaluacion;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 * Esta clase es la que recibe una carpeta donde debe extraer las imagenes que
 * representan las pruebas.
 *
 */
public class TareaProcesaEvaluacionScanner extends Task<ObservableList<PruebaRendida>> {

    /**
     * El colegio al que se le esta procesando la prueba.
     */
    private final Curso curso;
    private final Prueba prueba;
    private final List<File> archivos;
    private final ExtractorResultadosPrueba procesador;

    public TareaProcesaEvaluacionScanner(Prueba prueba, Curso curso, List<File> archivos) throws IOException {
        this.prueba = prueba;
        this.curso = curso;
        this.archivos = archivos;
        procesador = ExtractorResultadosPrueba.getInstance();
    }

    @Override
    protected ObservableList<PruebaRendida> call() {
        // updateMessage("Procesando Pruebas");

        final ObservableList<PruebaRendida> results = FXCollections.observableArrayList();
        archivos.size();
        for (final File archivo : archivos) {
            OTResultadoScanner resultado;
            try {

                resultado = procesador.process(archivo, prueba.getNroPreguntas());
                final String rut = resultado.getRut();
                Alumno alumno = null;
                for (final Alumno alum : curso.getAlumnos()) {
                    if (rut.equals(alum.getRut())) {
                        alumno = alum;
                        break;
                    }
                }
                if (alumno != null) {
                    final PruebaRendida rendida = new PruebaRendida();
                    rendida.setAlumno(alumno);
                    rendida.setRespuestas(resultado.getRespuestas());
                    evaluar(rendida.getRespuestas(), rendida);
                    results.add(rendida);
                }
                // updateProgress(n++, max);
            } catch (final CPruebasException e) {
                e.printStackTrace();

            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    protected void evaluar(String respuetas, PruebaRendida pRendida) {
        final List<RespuestasEsperadasPrueba> respEsperadas = prueba.getRespuestas();

        final int nMax = Math.min(respuetas.length(), respEsperadas.size());
        pRendida.setOmitidas(Math.abs(respuetas.length() - respEsperadas.size()));
        pRendida.setBuenas(0);
        pRendida.setMalas(0);
        for (int n = 0; n < nMax; n++) {
            final RespuestasEsperadasPrueba resp = respEsperadas.get(n);
            final String userResp = respuetas.substring(n, n + 1);
            final String validResp = resp.getRespuesta();
            if (userResp.toUpperCase().equals("*")) {
                pRendida.setOmitidas(pRendida.getOmitidas() + 1);
            } else if (userResp.toUpperCase().equals(validResp.toUpperCase())) {
                pRendida.setBuenas(pRendida.getBuenas() + 1);
            } else {
                pRendida.setMalas(pRendida.getMalas() + 1);
            }
        }
        final int nroPreguntas = respEsperadas.size();
        final float porcDificultad = prueba.getExigencia() == null ? 60f : prueba.getExigencia();
        final float notaMinima = prueba.getPuntajeBase() == null ? 1.0f : prueba.getPuntajeBase().floatValue();
        pRendida.setNota(Utils.getNota(nroPreguntas, porcDificultad, pRendida.getBuenas(), notaMinima));

        final float total = pRendida.getBuenas() + pRendida.getMalas() + pRendida.getOmitidas();
        final float porcentaje = (float) pRendida.getBuenas() / total * 100f;
        final RangoEvaluacion rango = prueba.getNivelEvaluacion().getRango(porcentaje);
        pRendida.setRango(rango);
    }

}
