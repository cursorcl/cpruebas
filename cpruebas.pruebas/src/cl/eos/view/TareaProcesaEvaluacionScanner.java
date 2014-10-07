package cl.eos.view;

import java.io.File;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import cl.eos.detection.OTResultadoScanner;
import cl.eos.detection.ProcesadorPruebas;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.PruebaRendida;

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
		ProcesadorPruebas procesador = new ProcesadorPruebas();
        ObservableList<PruebaRendida> results = FXCollections
            .observableArrayList();
		for(File archivo: archivos)
		{
		  OTResultadoScanner resultado = procesador.process(archivo);
		  if(resultado != null)
		  {
		    PruebaRendida rendida = new PruebaRendida();
		    //rendida.setAlumno(alumno);
		    rendida.setRespuestas(resultado.getRespuestas());
		  }
		}
		return results;
	}
}
