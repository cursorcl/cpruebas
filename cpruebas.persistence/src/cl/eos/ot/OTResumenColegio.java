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
	private float alumnosEvaluados;
	private float alumnosAprobados;
	private float alumnosReprobados;
	
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

	public float getAlumnosEvaluados() {
		return alumnosEvaluados;
	}

	public void setAlumnosEvaluados(float alumnosEvaluados) {
		this.alumnosEvaluados = alumnosEvaluados;
	}

	public float getAlumnosAprobados() {
		return alumnosAprobados;
	}

	public void setAlumnosAprobados(float alumnosAprobados) {
		this.alumnosAprobados = alumnosAprobados;
	}

	public float getAlumnosReprobados() {
		return alumnosReprobados;
	}

	public void setAlumnosReprobados(float alumnosReprobados) {
		this.alumnosReprobados = alumnosReprobados;
	}
	
	private void calculate()
	{
	    setAlumnosEvaluados(
                Utils.redondeo2Decimales((((float) totalEvaluados / (float) totalAlumnos) * 100f)));
        setAlumnosAprobados(
                Utils.redondeo2Decimales((((float) totalAprobados / (float) totalEvaluados) * 100f)));
        setAlumnosReprobados(
                Utils.redondeo2Decimales((((float) totalReprobados / (float) totalEvaluados) * 100f)));
	}

}
