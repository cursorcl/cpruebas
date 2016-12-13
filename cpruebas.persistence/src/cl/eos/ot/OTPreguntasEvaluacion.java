package cl.eos.ot;

import cl.eos.persistence.models.SEvaluacionEjeTematico;

public class OTPreguntasEvaluacion {

    private SEvaluacionEjeTematico evaluacion;
    private Integer alumnos = 0;

    public Integer getAlumnos() {
        return alumnos;
    }

    public SEvaluacionEjeTematico getEvaluacion() {
        return evaluacion;
    }

    public void setAlumnos(Integer alumnos) {
        this.alumnos = alumnos;
    }

    public void setEvaluacion(SEvaluacionEjeTematico evaluacion) {
        this.evaluacion = evaluacion;
    }

}
