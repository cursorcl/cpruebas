package cl.eos.view;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cl.eos.detection.ExtractorResultadosPrueba;
import cl.eos.detection.OTResultadoScanner;
import cl.eos.exceptions.CPruebasException;
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RangoEvaluacion;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 * Esta clase es la que recibe una carpeta donde debe extraer las imagenes que representan las pruebas.
 *
 */
public class TareaProcesaEvaluacionScanner extends Task<ObservableList<R_PruebaRendida>> {
    /**
     * El colegio al que se le esta procesando la prueba.
     */
    List<R_Alumno> alumnos;
    final R_Prueba prueba;
    final List<File> archivos;
    final List<R_RespuestasEsperadasPrueba> respEsperadas;
    final List<R_RangoEvaluacion> rangos;
    final ExtractorResultadosPrueba procesador;
    public TareaProcesaEvaluacionScanner(R_Prueba prueba, List<R_Alumno> alumnos, List<R_RespuestasEsperadasPrueba> respEsperadas,
            List<R_RangoEvaluacion> rangos, List<File> archivos) throws IOException {
        this.prueba = prueba;
        this.alumnos = alumnos;
        this.archivos = archivos;
        this.respEsperadas = respEsperadas;
        this.rangos = rangos;
        procesador = ExtractorResultadosPrueba.getInstance();
    }
    @Override
    protected ObservableList<R_PruebaRendida> call() {
        // updateMessage("Procesando Pruebas");
        final ObservableList<R_PruebaRendida> results = FXCollections.observableArrayList();
        archivos.size();
        for (final File archivo : archivos) {
            OTResultadoScanner resultado;
            try {
                resultado = procesador.process(archivo, prueba.getNropreguntas());
                final String rut = resultado.getRut();
                R_Alumno alumno = null;
                for (final R_Alumno alum : alumnos) {
                    if (rut.equals(alum.getRut())) {
                        alumno = alum;
                        break;
                    }
                }
                if (alumno != null) {
                    final R_PruebaRendida rendida = new R_PruebaRendida.Builder().id(Utils.getLastIndex()).build();
                    rendida.setAlumno_id(alumno.getColegio_id());
                    rendida.setRespuestas(resultado.getRespuestas());
                    evaluar(rendida.getRespuestas(), rendida, rangos);
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
    protected void evaluar(String respuetas, R_PruebaRendida pRendida, List<R_RangoEvaluacion> rangos) {
        final int nMax = Math.min(respuetas.length(), respEsperadas.size());
        pRendida.setOmitidas(Math.abs(respuetas.length() - respEsperadas.size()));
        pRendida.setBuenas(0);
        pRendida.setMalas(0);
        for (int n = 0; n < nMax; n++) {
            final R_RespuestasEsperadasPrueba resp = respEsperadas.get(n);
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
        final float notaMinima = prueba.getPuntajebase() == null ? 1.0f : prueba.getPuntajebase().floatValue();
        pRendida.setNota(Utils.getNota(nroPreguntas, porcDificultad, pRendida.getBuenas(), notaMinima));
        final float total = pRendida.getBuenas() + pRendida.getMalas() + pRendida.getOmitidas();
        final float porcentaje = (float) pRendida.getBuenas() / total * 100f;
        final R_RangoEvaluacion rango = pRendida.getRango(porcentaje, rangos);
        pRendida.setRango_id(rango.getId());
    }
}
