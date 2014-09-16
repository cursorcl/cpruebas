package cl.eos.ot;

public class OTResumenGeneral {

	private String name;
	private Float nota;
	private Float pBuenas;
	private Float pPuntaje;
	private Integer puntaje;

	public OTResumenGeneral(String name, Float nota, Float pBuenas,
			Float pPuntaje, Integer puntaje) {
		super();
		this.name = name;
		this.nota = nota;
		this.pBuenas = pBuenas;
		this.pPuntaje = pPuntaje;
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

	public Float getpBuenas() {
		return pBuenas;
	}

	public void setpBuenas(Float pBuenas) {
		this.pBuenas = pBuenas;
	}

	public Float getpPuntaje() {
		return pPuntaje;
	}

	public void setpPuntaje(Float pPuntaje) {
		this.pPuntaje = pPuntaje;
	}

	public Integer getPuntaje() {
		return puntaje;
	}

	public void setPuntaje(Integer puntaje) {
		this.puntaje = puntaje;
	}

}
