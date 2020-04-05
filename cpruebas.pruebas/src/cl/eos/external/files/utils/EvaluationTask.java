package cl.eos.external.files.utils;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import cl.eos.external.files.EvaluadorPruebasDataBridge;
import cl.eos.external.files.excel.ExcelExams;
import cl.eos.external.files.excel.ReaderListener;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class EvaluationTask extends Task<Results>  {


	Logger log = Logger.getLogger(EvaluationTask.class.getName());
	
	ObservableList<RegisterForView> badResults = FXCollections.observableArrayList();
	ObservableList<RegisterForView> goodResults = FXCollections.observableArrayList();
	private File exams;
	/**
	 * Encargado de la laectura de archivo excel,
	 */
	ExcelExams reader = new ExcelExams();
	EvaluadorPruebasDataBridge dataAccess = new EvaluadorPruebasDataBridge();

	public EvaluationTask(File exams) {
		this.exams =  exams;
	}

	@Override
	protected Results call() throws Exception {

		List<Register> results = reader.readExcelFile(exams, new ReaderListener() {
			
			@Override
			public void onReadRegister(Register register) {
				updateMessage(register.toString());
			}
			
			@Override
			public void onReadFile(String message) {
				updateMessage(message);
			}
		});
		
		Curso ultimoCurso = null;
		Asignatura ultimaAsignatura = null;
		Prueba prueba = null;
		EvaluacionPrueba evaluacion = null;

		int n = 0;
		int max = results.size();

		for (Register register : results) {
			updateMessage(register.toString());
			updateProgress(++n, max);
			boolean isValidRut = Utils.validarRut(register.getRut());
			
			if (!isValidRut) {
				RegisterForView rView = new RegisterForView(register);
				rView.setStatus(RegisterStatus.RUT_INCORRECTO);
				badResults.add(rView);
				notifyRegister(rView);
				updateMessage("Rut Incorrecto.");
			}
			
			Colegio colegio = dataAccess.getColegio(register.getIdColegio());
			if (colegio == null) {
				RegisterForView rView = new RegisterForView(register);
				rView.setStatus(RegisterStatus.NO_EXISTE_COLEGIO);
				badResults.add(rView);
				notifyRegister(rView);
				updateMessage("No existe el colegio indicado.");
			}
			
			Asignatura asignatura = dataAccess.getAsignatura(register.getIdAsignatura());
			if (asignatura == null) {
				RegisterForView rView = new RegisterForView(register);
				rView.setStatus(RegisterStatus.NO_EXISTE_ASIGNATURA);
				badResults.add(rView);
				notifyRegister(rView);
				updateMessage("No existe la asignatura indicada.");
			}
			Curso curso = dataAccess.getCurso(register.getIdCurso());
			if (curso == null) {
				RegisterForView rView = new RegisterForView(register);
				rView.setStatus(RegisterStatus.NO_EXISTE_CURSO);
				badResults.add(rView);
				notifyRegister(rView);
				updateMessage("No existe el curso indicado.");

			}

			if (curso != null && asignatura != null && isValidRut) {
				// Cuando cambia la asignatura o el curso debo obtener la evaluación
				// correspondiente.
				if (!(curso.equals(ultimoCurso) && asignatura.equals(ultimaAsignatura))) {
					ultimoCurso = curso;
					ultimaAsignatura = asignatura;
					// Tengo que obtner los valores de la prueba y evaluación.
					prueba = dataAccess.getPrueba(asignatura, curso);
					evaluacion = dataAccess.getEvaluacionPrueba(prueba, curso);
				}

				Alumno alumno = curso.getAlumnos().stream().filter(a -> a.getRut().equalsIgnoreCase(register.getRut()))
						.findFirst().orElse(null);
				if (alumno == null) {
					RegisterForView rView = new RegisterForView(register);
					rView.setStatus(RegisterStatus.NO_EXISTE_ALUMNO);
					rView.curso.set(curso);
					rView.asignatura.set(asignatura);
					rView.colegio.set(colegio);
					badResults.add(rView);
					notifyRegister(rView);
					updateMessage("No existe el alumno indicado.");
					log.info(rView.toString());
					
				} else {

					PruebaRendida pruebaRendida = dataAccess.generarPruebaRendida(prueba, alumno,
							register.getRespuestas());
					pruebaRendida.setEvaluacionPrueba(evaluacion);
					pruebaRendida = (PruebaRendida) dataAccess.savePruebaRendida(pruebaRendida);
					register.setNota(pruebaRendida.getNota());
					
					RegisterForView rView = new RegisterForView(register);
					rView.setStatus(RegisterStatus.BUENO);
					rView.curso.set(curso);
					rView.asignatura.set(asignatura);
					rView.colegio.set(colegio);
					rView.alumno.set(alumno);
					goodResults.add(rView);
					notifyRegister(rView);
				}
				
			}
		}
		return new Results(goodResults, badResults);
	}

	private void notifyRegister(RegisterForView register) {

	}

}
