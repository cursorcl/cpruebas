package cl.eos.ot;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import cl.eos.util.Utils;

public class OTResumenGeneral {

	NumberFormat formatter = null;
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
		char sep = Utils.getDecimalSeparator();
		formatter = new DecimalFormat("#0" + sep + "00");
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

	public String getPbuenas() {
		return formatter.format(pbuenas * 100f);
	}

	public void setPbuenas(Float pBuenas) {
		this.pbuenas = pBuenas;
	}

	public String getPpuntaje() {
		return formatter.format(ppuntaje);
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
