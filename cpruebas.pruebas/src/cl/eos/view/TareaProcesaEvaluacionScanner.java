package cl.eos.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import cl.eos.detection.OTResultadoScanner;
import cl.eos.detection.ProcesadorPruebas;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.Prueba;
import cl.eos.view.ots.OTPruebaRendida;

/**
 * Esta clase es la que recibe una carpeta donde debe extraer las imagenes que
 * representan las pruebas.
 *
 */
public class TareaProcesaEvaluacionScanner extends Task<ObservableList<OTPruebaRendida>> {

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
	protected ObservableList<OTPruebaRendida> call() throws Exception {
		updateMessage("Procesando Pruebas");
		ProcesadorPruebas procesador = new ProcesadorPruebas();
        ObservableList<OTPruebaRendida> results = FXCollections
            .observableArrayList();
		for(File archivo: archivos)
		{
		  OTResultadoScanner resultado = procesador.process(archivo);
		  if(resultado != null)
		  {
		    
		  }
		}
		return results;
	}
}
