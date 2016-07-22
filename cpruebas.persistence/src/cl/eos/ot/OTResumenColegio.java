package cl.eos.ot;

import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.util.Utils;


public class OTResumenColegio {
	private Colegio colegio;
	private Curso curso;
	private String name;
	private int totalAlumnos;
	private int totalEvaluados;
	private int totalAprobados;
	private int totalReprobados;
	private float porcAlumnosEvaluados;
	private float porcAlumnosAprobados;
	private float porcAlumnosReprobados;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Colegio getColegio() {
		return colegio;
	}

	public void setColegio(Colegio colegio) {
		this.colegio = colegio;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public int getTotalAlumnos() {
		return totalAlumnos;
	}

	public void setTotalAlumnos(int totalAlumnos) {
		this.totalAlumnos = totalAlumnos;
	}

	public int getTotalEvaluados() {
		return totalEvaluados;
	}

	public void setTotalEvaluados(int totalEvaluados) {
		this.totalEvaluados = totalEvaluados;
		calculate();
	}

	public int getTotalAprobados() {
		return totalAprobados;
	}

	public void setTotalAprobados(int totalAprobados) {
		this.totalAprobados = totalAprobados;
		calculate();
	}

	public int getTotalReprobados() {
		return totalReprobados;
	}

	public void setTotalReprobados(int totalReprobados) {
		this.totalReprobados = totalReprobados;
		calculate();
	}

	public float getPorcAlumnosEvaluados() {
		return porcAlumnosEvaluados;
	}

	public void setPorcAlumnosEvaluados(float alumnosEvaluados) {
		this.porcAlumnosEvaluados = alumnosEvaluados;
	}

	public float getPorcAlumnosAprobados() {
		return porcAlumnosAprobados;
	}

	public void setPorcAlumnosAprobados(float alumnosAprobados) {
		this.porcAlumnosAprobados = alumnosAprobados;
	}

	public float getPorcAlumnosReprobados() {
		return porcAlumnosReprobados;
	}

	public void setPorcAlumnosReprobados(float alumnosReprobados) {
		this.porcAlumnosReprobados = alumnosReprobados;
	}
	
	private void calculate()
	{
	    setPorcAlumnosEvaluados(
                Utils.redondeo2Decimales((((float) totalEvaluados / (float) totalAlumnos) * 100f)));
        setPorcAlumnosAprobados(
                Utils.redondeo2Decimales((((float) totalAprobados / (float) totalEvaluados) * 100f)));
        setPorcAlumnosReprobados(
                Utils.redondeo2Decimales((((float) totalReprobados / (float) totalEvaluados) * 100f)));
	}

}
