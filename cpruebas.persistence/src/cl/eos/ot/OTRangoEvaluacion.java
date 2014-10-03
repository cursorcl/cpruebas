package cl.eos.ot;

import cl.eos.persistence.models.RangoEvaluacion;

public class OTRangoEvaluacion {

	private RangoEvaluacion rango;
	private int cantidad;
	private float logrado;
	public RangoEvaluacion getRango() {
		return rango;
	}
	public void setRango(RangoEvaluacion rango) {
		this.rango = rango;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public float getLogrado() {
		return logrado;
	}
	public void setLogrado(float logrado) {
		this.logrado = logrado;
	}
	
	
}
