package cl.eos.view;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.Prueba;
import cl.eos.view.ots.OTPruebaRendida;

/**
 * Esta clase es la que recibe una carpeta donde debe extraer las imagenes que
 * representan las pruebas.
 *
 */
public class ProcesadorPrueba extends Task<ObservableList<OTPruebaRendida>> {

	/**
	 * El curso al que se le esta procesando la prueba.
	 */
	private Curso curso;
	private Prueba prueba;
	private String carpeta;

	public ProcesadorPrueba(Prueba prueba, Curso curso, String carpeta) {
		this.prueba = prueba;
		this.curso = curso;
		this.carpeta = carpeta;
	}

	@Override
	protected ObservableList<OTPruebaRendida> call() throws Exception {
		updateMessage("Procesando Pruebas");
		File folder =  new File(carpeta);
		if(folder.isDirectory())
		{
			File[] archivos = folder.listFiles();
			
		}
		ObservableList<OTPruebaRendida> results = FXCollections
				.observableArrayList();
		return results;
	}
}
