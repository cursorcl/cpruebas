package cl.eos.view;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cl.eos.detection.ExtractorResultadosPrueba;
import cl.eos.detection.OTResultadoScanner;
import cl.eos.exceptions.CPruebasException;
import cl.eos.persistence.models.SAlumno;
import cl.eos.persistence.models.SCurso;
import cl.eos.persistence.models.SPrueba;
import cl.eos.persistence.models.SPruebaRendida;
import cl.eos.persistence.models.SRangoEvaluacion;
import cl.eos.persistence.models.SRespuestasEsperadasPrueba;
import cl.eos.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 * Esta clase es la que recibe una carpeta donde debe extraer las imagenes que
 * representan las pruebas.
 *
 */
public class TareaProcesaEvaluacionScanner extends Task<ObservableList<SPruebaRendida>> {

    /**
     * El colegio al que se le esta procesando la prueba.
     */
    private final SCurso curso;
    private final SPrueba prueba;
    private final List<File> archivos;
    private final ExtractorResultadosPrueba procesador;

    public TareaProcesaEvaluacionScanner(SPrueba prueba, SCurso curso, List<File> archivos) throws IOException {
        this.prueba = prueba;
        this.curso = curso;
        this.archivos = archivos;
        procesador = ExtractorResultadosPrueba.getInstance();
    }

    @Override
    protected ObservableList<SPruebaRendida> call() {
        // updateMessage("Procesando Pruebas");

        final ObservableList<SPruebaRendida> results = FXCollections.observableArrayList();
        archivos.size();
        for (final File archivo : archivos) {
            OTResultadoScanner resultado;
            try {

                resultado = procesador.process(archivo, prueba.getNroPreguntas());
                final String rut = resultado.getRut();
                SAlumno alumno = null;
                for (final SAlumno alum : curso.getAlumnos()) {
                    if (rut.equals(alum.getRut())) {
                        alumno = alum;
                        break;
                    }
                }
                if (alumno != null) {
                    final SPruebaRendida rendida = new SPruebaRendida();
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

    protected void evaluar(String respuetas, SPruebaRendida pRendida) {
        final List<SRespuestasEsperadasPrueba> respEsperadas = prueba.getRespuestas();

        final int nMax = Math.min(respuetas.length(), respEsperadas.size());
        pRendida.setOmitidas(Math.abs(respuetas.length() - respEsperadas.size()));
        pRendida.setBuenas(0);
        pRendida.setMalas(0);
        for (int n = 0; n < nMax; n++) {
            final SRespuestasEsperadasPrueba resp = respEsperadas.get(n);
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
        final SRangoEvaluacion rango = prueba.getNivelEvaluacion().getRango(porcentaje);
        pRendida.setRango(rango);
    }

}
