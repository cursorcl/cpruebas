package cl.eos.ot;

public class OTResumenGeneral {

	private String name;
	private Float nota;
	private Float pbuenas;
	private Float ppuntaje;
	private Integer puntaje;

	public OTResumenGeneral(String name, Float nota, Float pBuenas,
			Float pPuntaje, Integer puntaje) {
		super();
		this.name = name;
		this.nota = nota;
		this.pbuenas = pBuenas;
		this.ppuntaje = pPuntaje;
		this.puntaje = puntaje;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getNota() {
		return nota;
	}

	public void setNota(Float nota) {
		this.nota = nota;
	}

	public Float getPbuenas() {
		return pbuenas;
	}

	public void setPbuenas(Float pBuenas) {
		this.pbuenas = pBuenas;
	}

	public Float getPpuntaje() {
		return ppuntaje;
	}

	public void setPpuntaje(Float pPuntaje) {
		this.ppuntaje = pPuntaje;
	}

	public Integer getPuntaje() {
		return puntaje;
	}

	public void setPuntaje(Integer puntaje) {
		this.puntaje = puntaje;
	}

}
