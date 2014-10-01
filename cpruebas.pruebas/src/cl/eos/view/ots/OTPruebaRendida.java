package cl.eos.view.ots;

import cl.eos.persistence.models.PruebaRendida;

public class OTPruebaRendida {

	private PruebaRendida pruebaRendida;

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
		return pruebaRendida.getNota();
	}
	public Float getPuntaje()
	{
		return 0F;
	}
	public String getNivel()
	{
		return "NN";
	}

	public PruebaRendida getPruebaRendida() {
		return pruebaRendida;
	}

	public void setPruebaRendida(PruebaRendida pruebaRendida) {
		this.pruebaRendida = pruebaRendida;
	}
	
	
	

}
