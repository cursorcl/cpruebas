package cl.eos.ot;

import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.RangoEvaluacion;

public class OTRangoCurso {

	private RangoEvaluacion rango;
	private int total;
	private Curso curso;

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

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

}