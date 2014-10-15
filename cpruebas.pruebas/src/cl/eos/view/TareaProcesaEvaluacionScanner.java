package cl.eos.view;

import java.io.File;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import cl.eos.detection.ExtractorResultadosPruebas;
import cl.eos.detection.OTResultadoScanner;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RangoEvaluacion;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.util.Utils;

/**
 * Esta clase es la que recibe una carpeta donde debe extraer las imagenes que
 * representan las pruebas.
 *
 */
public class TareaProcesaEvaluacionScanner extends Task<ObservableList<PruebaRendida>> {

	/**
	 * El curso al que se le esta procesando la prueba.
	 */
	private Curso curso;
	private Prueba prueba;
	private List<File> archivos;

	public TareaProcesaEvaluacionScanner(Prueba prueba, Curso curso, List<File> archivos) {
		this.prueba = prueba;
		this.curso = curso;
		this.archivos = archivos;
	}

	@Override
	protected ObservableList<PruebaRendida> call() throws Exception {
		updateMessage("Procesando Pruebas");
		ExtractorResultadosPruebas procesador =  new ExtractorResultadosPruebas();
        ObservableList<PruebaRendida> results = FXCollections
            .observableArrayList();
        int max = archivos.size();
        int n = 1;
		for(File archivo: archivos)
		{
		  OTResultadoScanner resultado = procesador.process(archivo, prueba.getNroPreguntas());
		  if(resultado != null)
		  {
		    String rut = resultado.getRut();
		    Alumno alumno = null;
		    for(Alumno alum : curso.getAlumnos())
		    {
		      if(rut.equals(alum.getRut()))
		      {
		          alumno = alum;
		          break;
		      }
		    }
		    if(alumno != null)
		    {
		      PruebaRendida rendida = new PruebaRendida();
		      rendida.setAlumno(alumno);
		      rendida.setRespuestas(resultado.getRespuestas());
		      evaluar(rendida.getRespuestas(), rendida);
		      results.add(rendida);
		    }
		    updateProgress(n++, max);
		  }
		}
		return results;
	}
	
	protected void evaluar(String respuetas, PruebaRendida pRendida) {
      List<RespuestasEsperadasPrueba> respEsperadas = prueba.getRespuestas();

      int nMax = Math.min(respuetas.length(), respEsperadas.size());
      pRendida.setOmitidas(Math.abs(respuetas.length() - respEsperadas.size()));
      pRendida.setBuenas(0);
      pRendida.setMalas(0);
      for (int n = 0; n < nMax; n++) {
          RespuestasEsperadasPrueba resp = respEsperadas.get(n);
          String userResp = respuetas.substring(n, n + 1);
          String validResp = resp.getRespuesta();
          if (userResp.toUpperCase().equals("*")) {
            pRendida.setOmitidas(pRendida.getOmitidas() + 1);
          } else if (userResp.toUpperCase().equals(validResp.toUpperCase())) {
            pRendida.setBuenas(pRendida.getBuenas() + 1);
          } else {
            pRendida.setMalas(pRendida.getMalas() + 1);
          }
      }
      int nroPreguntas = respEsperadas.size();
      float porcDificultad = prueba.getExigencia() == null ? 60f : prueba
              .getExigencia();
      float notaMinima = 1.0f;
      pRendida.setNota(Utils.getNota(nroPreguntas, porcDificultad,
          pRendida.getBuenas(), notaMinima));

      float total = pRendida.getBuenas() + pRendida.getMalas()
              + pRendida.getOmitidas();
      float porcentaje = ((float) pRendida.getBuenas()) / total * 100f;
      RangoEvaluacion rango = prueba.getNivelEvaluacion()
              .getRango(porcentaje);
      pRendida.setRango(rango);
  }
	
}
