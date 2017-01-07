package cl.eos.ot;

import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_RangoEvaluacion;

public class OTRangoCurso {

    private R_RangoEvaluacion rango;
    private int total;
    private R_Curso curso;

    public R_Curso getCurso() {
        return curso;
    }

    public R_RangoEvaluacion getRango() {
        return rango;
    }

    public int getTotal() {
        return total;
    }

    public void setCurso(R_Curso curso) {
        this.curso = curso;
    }

    public void setRango(R_RangoEvaluacion rango) {
        this.rango = rango;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
