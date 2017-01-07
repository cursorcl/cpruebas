package cl.eos.ot;

import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_RangoEvaluacion;

public class OTReporte {

    private R_RangoEvaluacion rango;
    private int total;
    private R_Ejetematico eje;

    public R_Ejetematico getEje() {
        return eje;
    }

    public R_RangoEvaluacion getRango() {
        return rango;
    }

    public int getTotal() {
        return total;
    }

    public void setEje(R_Ejetematico eje) {
        this.eje = eje;
    }

    public void setRango(R_RangoEvaluacion rango) {
        this.rango = rango;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
