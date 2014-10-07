package cl.eos.ot;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class OTResultado {
	private String nombre;
	private double logrado;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getLogrado() {
		NumberFormat formatter = new DecimalFormat("#0.00");     
		return formatter.format(logrado);
	}

	public void setLogrado(double logrado) {
		this.logrado = logrado;
	}

}
