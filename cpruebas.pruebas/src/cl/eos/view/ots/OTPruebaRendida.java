package cl.eos.view.ots;

import cl.eos.persistence.models.PruebaRendida;
import cl.eos.util.Utils;

public class OTPruebaRendida {

	private PruebaRendida pruebaRendida;
	private String nivel;

	public OTPruebaRendida(PruebaRendida pruebaRendida) {
		this.pruebaRendida = pruebaRendida;
	}

	public String getPaterno() {
		return pruebaRendida.getAlumno().getPaterno();
	}

	public String getMaterno() {
		return pruebaRendida.getAlumno().getMaterno();
	}

	public String getNombres() {
		return pruebaRendida.getAlumno().getName();
	}

	public String getRespuestas() {
		return pruebaRendida.getRespuestas();
	}

	public Integer getBuenas() {
		return pruebaRendida.getBuenas();
	}

	public Integer getMalas() {
		return pruebaRendida.getMalas();
	}

	public Integer getOmitidas() {
		return pruebaRendida.getOmitidas();
	}
	public Float getNota()
	{
		return pruebaRendida.getNota() != null ? pruebaRendida.getNota() : 0F;
	}
	public Integer getPuntaje()
	{
		return Utils.getPuntaje(pruebaRendida.getNota().floatValue());
	}
	public String getNivel()
	{
		return  nivel;
	}
	public void setNivel(String nivel)
	{
		this.nivel = nivel;
	}
	public PruebaRendida getPruebaRendida() {
		return pruebaRendida;
	}

	public void setPruebaRendida(PruebaRendida pruebaRendida) {
		this.pruebaRendida = pruebaRendida;
	}
	
	
	

}
