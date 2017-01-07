package cl.eos.ot;

import cl.eos.restful.tables.R_EvaluacionEjetematico;

public class OTPreguntasEvaluacion {

    private R_EvaluacionEjetematico evaluacion;
    private Integer alumnos = 0;

    public Integer getAlumnos() {
        return alumnos;
    }

    public R_EvaluacionEjetematico getEvaluacion() {
        return evaluacion;
    }

    public void setAlumnos(Integer alumnos) {
        this.alumnos = alumnos;
    }

    public void setEvaluacion(R_EvaluacionEjetematico evaluacion) {
        this.evaluacion = evaluacion;
    }

}
