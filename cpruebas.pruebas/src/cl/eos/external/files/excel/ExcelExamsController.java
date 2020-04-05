package cl.eos.external.files.excel;

import java.io.File;
import java.util.logging.Logger;

import cl.eos.external.files.utils.EvaluationTask;
import cl.eos.external.files.utils.Results;
import javafx.concurrent.Task;

/**
 * Esta clase coordina todas las acciones relacionadas con el procesamiento de
 * las pruebas que se leen desde un excel.
 * 
 * @author eosorio
 *
 */
public class ExcelExamsController {

	static Logger log = Logger.getLogger(ExcelExamsController.class.getName());

	/**
	 * Procesa la lista de elementos que vienen en la lista.
	 * 
	 * <li>Verificar que existe prueba</li>
	 * <li>Verificar que el alumno peretenece al curso</li>
	 * <li>Verificar que existe prueba rendida (sino crearla)</li>
	 * <li>Veridicar si el alumno tiene una prueba rendida</li>
	 * 
	 * @param results Lista de registro de alumnos con sus respuestas.
	 */
	public Task<Results> process(File exams) {
		return new EvaluationTask(exams); 
	}
}
