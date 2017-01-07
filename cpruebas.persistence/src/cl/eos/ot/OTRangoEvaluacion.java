package cl.eos.ot;

import cl.eos.restful.tables.R_RangoEvaluacion;

public class OTRangoEvaluacion {

    private R_RangoEvaluacion rango;
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

    public R_RangoEvaluacion getRango() {
        return rango;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setLogrado(float logrado) {
        this.logrado = logrado;
    }

    public void setRango(R_RangoEvaluacion rango) {
        this.rango = rango;
    }

}
