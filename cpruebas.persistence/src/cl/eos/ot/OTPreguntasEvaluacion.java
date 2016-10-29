package cl.eos.ot;

import cl.eos.persistence.models.EvaluacionEjeTematico;

public class OTPreguntasEvaluacion {

    private EvaluacionEjeTematico evaluacion;
    private Integer alumnos = 0;

    public Integer getAlumnos() {
        return alumnos;
    }

    public EvaluacionEjeTematico getEvaluacion() {
        return evaluacion;
    }

    public void setAlumnos(Integer alumnos) {
        this.alumnos = alumnos;
    }

    public void setEvaluacion(EvaluacionEjeTematico evaluacion) {
        this.evaluacion = evaluacion;
    }

}
