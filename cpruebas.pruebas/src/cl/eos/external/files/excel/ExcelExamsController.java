package cl.eos.external.files.excel;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import cl.eos.external.files.EvaluadorPruebas;
import cl.eos.external.files.excel.ExcelExamsReader.Register;
import cl.eos.external.files.excel.ExcelExamsReader.Results;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.PruebaRendida;

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

	public void process(File exams) {
		Results results = reader.readExcelFile(exams);
		boolean nextStep = showBadResults(results);
		if (nextStep) {
			process(results.results);
		}
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
	private void process(List<Register> results) {

		Curso ultimoCurso = null;
		Asignatura ultimaAsignatura = null;

		Prueba prueba = null;
		EvaluacionPrueba evaluacion = null;

		for (Register register : results) {
			 log.info("Procesando registro:" + register.toString());
			Asignatura asignatura = evaluador.getAsignatura(register.idAsignatura);
			Curso curso = evaluador.getCurso(register.idCurso);
			if (curso == null || asignatura == null) {

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

				Alumno alumno = curso.getAlumnos().stream().filter(a -> a.getRut().equalsIgnoreCase(register.rut))
						.findFirst().orElse(null);
				if (alumno == null) {
					registerError(register);
				} else {
					
					
					PruebaRendida pruebaRendida = evaluador.generarPruebaRendida(prueba, alumno, register.respuestas);
					pruebaRendida.setEvaluacionPrueba(evaluacion);

					// Debo ver si existe una evaluación y recuperarla y cambiarla.
					
					// Aquí debo guardar la prueba.
					
					pruebaRendida = (PruebaRendida) evaluador.savePruebaRendida(pruebaRendida);
				}
			}

		}

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
