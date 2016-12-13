package cl.eos.ot;

import cl.eos.persistence.models.SRangoEvaluacion;

public class OTRangoEvaluacion {

    private SRangoEvaluacion rango;
    private int cantidad;
    private float logrado;

    public int getCantidad() {
        return cantidad;
    }

    public float getLogrado() {
        return logrado * 100;
    }

    public String getName() {
        return rango.getName();
    }

    public SRangoEvaluacion getRango() {
        return rango;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setLogrado(float logrado) {
        this.logrado = logrado;
    }

    public void setRango(SRangoEvaluacion rango) {
        this.rango = rango;
    }

}
