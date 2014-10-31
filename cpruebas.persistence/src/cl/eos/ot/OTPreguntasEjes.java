package cl.eos.ot;

import cl.eos.persistence.models.EjeTematico;
import cl.eos.util.Utils;

public class OTPreguntasEjes {

	private EjeTematico ejeTematico;
	private Integer buenas;
	private Integer total;
	
	public EjeTematico getEjeTematico() {
		return ejeTematico;
	}

	public void setEjeTematico(EjeTematico ejeTematico) {
		this.ejeTematico = ejeTematico;
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
		return ejeTematico.getName();
	}
	
	public String getDescripcion(){
		return "Respuestas correctas: " + buenas.toString() +" de " + total.toString();
	}

	public Float getLogrado(){
		return ((float )buenas/(float )total)*100f;
	}
	
	public Float getNologrado(){
		Float valor = (float) total-(float) buenas;
		return (float) ((valor/(float )total)*100f);
	}
	
	public Float getSlogrado(){
		float valor = ((float )buenas/(float )total)*100f;
		return Utils.redondeo2Decimales(valor);
	}
	
	public Float getSnlogrado(){
		Float valor = (float) total-(float) buenas;
		float pvalor =  (float) ((valor/(float )total)*100f);
		return  Utils.redondeo2Decimales(pvalor);
	}
}
