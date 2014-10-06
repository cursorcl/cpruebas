package cl.eos.ot;

import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.RangoEvaluacion;

public class OTReporte {

	private RangoEvaluacion rango;
	private int total;
	private EjeTematico eje;

	public RangoEvaluacion getRango() {
		return rango;
	}

	public void setRango(RangoEvaluacion rango) {
		this.rango = rango;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public EjeTematico getEje() {
		return eje;
	}

	public void setEje(EjeTematico eje) {
		this.eje = eje;
	}

}
