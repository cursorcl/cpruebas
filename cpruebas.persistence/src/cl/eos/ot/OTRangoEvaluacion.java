package cl.eos.ot;

import cl.eos.persistence.models.RangoEvaluacion;

public class OTRangoEvaluacion {

    private RangoEvaluacion rango;
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

    public RangoEvaluacion getRango() {
        return rango;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setLogrado(float logrado) {
        this.logrado = logrado;
    }

    public void setRango(RangoEvaluacion rango) {
        this.rango = rango;
    }

}
