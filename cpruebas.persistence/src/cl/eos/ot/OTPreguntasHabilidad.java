package cl.eos.ot;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import cl.eos.persistence.models.Habilidad;

public class OTPreguntasHabilidad {

	NumberFormat formatter = new DecimalFormat("#0.00"); 
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
		return "Respuestas correctas: " + buenas.toString() +" de " + total.toString();
	}

	public Float getLogrado(){
		float valor = ((float )buenas/(float )total)*100f;
		return valor;
	}
	
	public Float getNologrado(){
		Float valor = (float) total-(float) buenas;
		float pvalor = (float) ((valor/(float )total)*100f);
		return pvalor;
	}
	
	public String getSlogrado(){
		float valor = ((float )buenas/(float )total)*100f;
		return formatter.format(valor);
	}
	
	public String getSnlogrado(){
		Float valor = (float) total-(float) buenas;
		float pvalor = (float) ((valor/(float )total)*100f);
		return formatter.format(pvalor);
	}
}
