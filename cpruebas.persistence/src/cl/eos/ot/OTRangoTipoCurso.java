package cl.eos.ot;

import cl.eos.persistence.models.SRangoEvaluacion;
import cl.eos.persistence.models.STipoCurso;

public class OTRangoTipoCurso {

    private SRangoEvaluacion rango;
    private int total;
    private STipoCurso curso;

    public STipoCurso getCurso() {
        return curso;
    }

    public SRangoEvaluacion getRango() {
        return rango;
    }

    public int getTotal() {
        return total;
    }

    public void setCurso(STipoCurso curso) {
        this.curso = curso;
    }

    public void setRango(SRangoEvaluacion rango) {
        this.rango = rango;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
