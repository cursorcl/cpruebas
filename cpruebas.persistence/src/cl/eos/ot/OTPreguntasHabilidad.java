package cl.eos.ot;

import cl.eos.persistence.models.Habilidad;

public class OTPreguntasHabilidad {

	private Habilidad habilidad;
	private Integer buenas;
	private Integer total;

	public Habilidad getHabilidad() {
		return habilidad;
	}

	public void setHabilidad(Habilidad habilidad) {
		this.habilidad = habilidad;
	}

	public Integer getBuenas() {
		return buenas;
	}

	public void setBuenas(Integer buenas) {
		this.buenas = buenas;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
	
	public String getName(){
		return habilidad.getName();
	}
	
	public String getDescripcion(){
		return "Respuestas correctas:" + buenas.toString() +" de " + total.toString();
	}

	public Float getLogrado(){
		return ((float )buenas/(float )total)*100f;
	}
	
	public Float getNologrado(){
		Float valor = (float) total-(float) buenas;
		return (float) ((valor/(float )total)*100f);
	}
}
