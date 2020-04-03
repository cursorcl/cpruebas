package cl.eos.external.files.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import cl.eos.external.files.EvaluadorPruebas;
import cl.eos.external.files.utils.Register;
import cl.eos.external.files.utils.Results;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.PruebaRendida;
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
	 * Encargado de la laectura de archivo excel,
	 */
	ExcelExamsReader reader = new ExcelExamsReader();
	EvaluadorPruebas evaluador = new EvaluadorPruebas();

	public Task<Results> process(File exams) {
		Results results = reader.readExcelFile(exams);
		boolean nextStep = showBadResults(results);
		if (nextStep) {
			return process(results.results);
		}
		return null;
	}
	

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
	private Task<Results> process(List<Register> results) {

		Task<Results> task = new Task<Results>() {
			public Results call() {

				List<Register> goodResults = new ArrayList<>();
				List<Register> badResults = new ArrayList<>();

				Curso ultimoCurso = null;
				Asignatura ultimaAsignatura = null;
				Prueba prueba = null;
				EvaluacionPrueba evaluacion = null;

				int n = 0;
				int max = results.size();
				
				for (Register register : results) {
					updateMessage(register.toString());
					
					updateProgress(++n, max);
					
					Asignatura asignatura = evaluador.getAsignatura(register.getIdAsignatura());
					Curso curso = evaluador.getCurso(register.getIdCurso());
					if (curso == null || asignatura == null) {
						badResults.add(register);
						updateMessage("Cancelled");
					} else {
						// Cuando cambia la asignatura o el curso debo obtener la evaluación
						// correspondiente.
						if (!(curso.equals(ultimoCurso) && asignatura.equals(ultimaAsignatura))) {
							ultimoCurso = curso;
							ultimaAsignatura = asignatura;
							// Tengo que obtner los valores de la prueba y evaluación.
							prueba = evaluador.getPrueba(asignatura, curso);
							evaluacion = evaluador.getEvaluacionPrueba(prueba, curso);
						}

						Alumno alumno = curso.getAlumnos().stream()
								.filter(a -> a.getRut().equalsIgnoreCase(register.getRut())).findFirst().orElse(null);
						if (alumno == null) {
							badResults.add(register);
						} else {

							PruebaRendida pruebaRendida = evaluador.generarPruebaRendida(prueba, alumno,
									register.getRespuestas());
							pruebaRendida.setEvaluacionPrueba(evaluacion);
							pruebaRendida = (PruebaRendida) evaluador.savePruebaRendida(pruebaRendida);
							register.setNota(pruebaRendida.getNota());
							goodResults.add(register);
						}
					}
				}
				return new Results(goodResults, badResults);
			}
		};
		return task;
	}

	/**
	 * Genera archivo con los registros que han fallado.
	 * 
	 * @param register El registro que se almacena.
	 */
	private void registerError(Register register) {

	}

	/**
	 * Este metodo muestra al usuario los resultados incorrectos de la lectura.
	 * 
	 * Pide al operador indicar si continua solo con los valores correctos o abora
	 * el proceso.
	 * 
	 * @param results
	 * @return
	 */
	private boolean showBadResults(Results results) {
		// TODO Auto-generated method stub
		return true;
	}
}
