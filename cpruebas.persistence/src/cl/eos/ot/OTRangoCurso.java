package cl.eos.ot;

import cl.eos.persistence.models.SCurso;
import cl.eos.persistence.models.SRangoEvaluacion;

public class OTRangoCurso {

    private SRangoEvaluacion rango;
    private int total;
    private SCurso curso;

    public SCurso getCurso() {
        return curso;
    }

    public SRangoEvaluacion getRango() {
        return rango;
    }

    public int getTotal() {
        return total;
    }

    public void setCurso(SCurso curso) {
        this.curso = curso;
    }

    public void setRango(SRangoEvaluacion rango) {
        this.rango = rango;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
