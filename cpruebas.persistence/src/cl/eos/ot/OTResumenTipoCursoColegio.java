package cl.eos.ot;

import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.TipoCurso;
import cl.eos.util.Utils;


public class OTResumenTipoCursoColegio {
	private Colegio colegio;
	private TipoCurso tipoCurso;
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

	public TipoCurso getTipoCurso() {
		return tipoCurso;
	}

	public void setTipoCurso(TipoCurso curso) {
		this.tipoCurso = curso;
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
	}

	public int getTotalAprobados() {
		return totalAprobados;
	}

	public void setTotalAprobados(int totalAprobados) {
		this.totalAprobados = totalAprobados;
	}

	public int getTotalReprobados() {
		return totalReprobados;
	}

	public void setTotalReprobados(int totalReprobados) {
		this.totalReprobados = totalReprobados;
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
	
	
	public void add(OTResumenTipoCursoColegio ot)
	{
	    totalAlumnos += ot.getTotalAlumnos();
	    totalEvaluados += ot.getTotalEvaluados();
	    totalAprobados += ot.getTotalAprobados();
	    totalReprobados += ot.getTotalReprobados();
	    alumnosEvaluados =  Utils.redondeo2Decimales(100f * ((float)totalEvaluados /  (float)totalAlumnos));
	    alumnosAprobados = Utils.redondeo2Decimales(100f * ((float)totalAprobados / (float)totalEvaluados));
	    alumnosReprobados = Utils.redondeo2Decimales(100f * ((float)totalReprobados / (float)totalEvaluados)); 
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((colegio == null) ? 0 : colegio.hashCode());
        result = prime * result + ((tipoCurso == null) ? 0 : tipoCurso.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OTResumenTipoCursoColegio other = (OTResumenTipoCursoColegio) obj;
        if (colegio == null) {
            if (other.colegio != null)
                return false;
        } else if (!colegio.equals(other.colegio))
            return false;
        if (tipoCurso == null) {
            if (other.tipoCurso != null)
                return false;
        } else if (!tipoCurso.equals(other.tipoCurso))
            return false;
        return true;
    }

	
}
