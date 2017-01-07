package cl.eos.ot;

import cl.eos.restful.tables.R_RangoEvaluacion;
import cl.eos.restful.tables.R_TipoCurso;

public class OTRangoTipoCurso {

    private R_RangoEvaluacion rango;
    private int total;
    private R_TipoCurso curso;

    public R_TipoCurso getCurso() {
        return curso;
    }

    public R_RangoEvaluacion getRango() {
        return rango;
    }

    public int getTotal() {
        return total;
    }

    public void setCurso(R_TipoCurso curso) {
        this.curso = curso;
    }

    public void setRango(R_RangoEvaluacion rango) {
        this.rango = rango;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
