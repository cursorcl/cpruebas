package cl.eos.ot;

import cl.eos.persistence.models.SEjeTematico;
import cl.eos.persistence.models.SRangoEvaluacion;

public class OTReporte {

    private SRangoEvaluacion rango;
    private int total;
    private SEjeTematico eje;

    public SEjeTematico getEje() {
        return eje;
    }

    public SRangoEvaluacion getRango() {
        return rango;
    }

    public int getTotal() {
        return total;
    }

    public void setEje(SEjeTematico eje) {
        this.eje = eje;
    }

    public void setRango(SRangoEvaluacion rango) {
        this.rango = rango;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
