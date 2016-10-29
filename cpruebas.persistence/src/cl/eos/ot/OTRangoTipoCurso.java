package cl.eos.ot;

import cl.eos.persistence.models.RangoEvaluacion;
import cl.eos.persistence.models.TipoCurso;

public class OTRangoTipoCurso {

    private RangoEvaluacion rango;
    private int total;
    private TipoCurso curso;

    public TipoCurso getCurso() {
        return curso;
    }

    public RangoEvaluacion getRango() {
        return rango;
    }

    public int getTotal() {
        return total;
    }

    public void setCurso(TipoCurso curso) {
        this.curso = curso;
    }

    public void setRango(RangoEvaluacion rango) {
        this.rango = rango;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
