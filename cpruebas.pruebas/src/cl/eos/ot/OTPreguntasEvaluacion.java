package cl.eos.ot;

import cl.eos.persistence.models.EvaluacionEjeTematico;

public class OTPreguntasEvaluacion {

	private EvaluacionEjeTematico evaluacion;
	private Integer alumnos;

	public EvaluacionEjeTematico getEvaluacion() {
		return evaluacion;
	}

	public void setEvaluacion(EvaluacionEjeTematico evaluacion) {
		this.evaluacion = evaluacion;
	}

	public Integer getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(Integer alumnos) {
		this.alumnos = alumnos;
	}

}
