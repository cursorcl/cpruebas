package cl.eos.ot;

import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_TipoCurso;
import cl.eos.util.Utils;

public class OTResumenTipoCursoColegio {
    private R_Colegio colegio;
    private R_TipoCurso tipoCurso;
    private String name;
    private int totalAlumnos;
    private int totalEvaluados;
    private int totalAprobados;
    private int totalReprobados;
    private float alumnosEvaluados;
    private float alumnosAprobados;
    private float alumnosReprobados;

    public void add(OTResumenTipoCursoColegio ot) {
        totalAlumnos += ot.getTotalAlumnos();
        totalEvaluados += ot.getTotalEvaluados();
        totalAprobados += ot.getTotalAprobados();
        totalReprobados += ot.getTotalReprobados();
        alumnosEvaluados = Utils.redondeo2Decimales(100f * ((float) totalEvaluados / (float) totalAlumnos));
        alumnosAprobados = Utils.redondeo2Decimales(100f * ((float) totalAprobados / (float) totalEvaluados));
        alumnosReprobados = Utils.redondeo2Decimales(100f * ((float) totalReprobados / (float) totalEvaluados));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final OTResumenTipoCursoColegio other = (OTResumenTipoCursoColegio) obj;
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

    public float getAlumnosAprobados() {
        return alumnosAprobados;
    }

    public float getAlumnosEvaluados() {
        return alumnosEvaluados;
    }

    public float getAlumnosReprobados() {
        return alumnosReprobados;
    }

    public R_Colegio getColegio() {
        return colegio;
    }

    public String getName() {
        return name;
    }

    public R_TipoCurso getTipoCurso() {
        return tipoCurso;
    }

    public int getTotalAlumnos() {
        return totalAlumnos;
    }

    public int getTotalAprobados() {
        return totalAprobados;
    }

    public int getTotalEvaluados() {
        return totalEvaluados;
    }

    public int getTotalReprobados() {
        return totalReprobados;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (colegio == null ? 0 : colegio.hashCode());
        result = prime * result + (tipoCurso == null ? 0 : tipoCurso.hashCode());
        return result;
    }

    public void setAlumnosAprobados(float alumnosAprobados) {
        this.alumnosAprobados = alumnosAprobados;
    }

    public void setAlumnosEvaluados(float alumnosEvaluados) {
        this.alumnosEvaluados = alumnosEvaluados;
    }

    public void setAlumnosReprobados(float alumnosReprobados) {
        this.alumnosReprobados = alumnosReprobados;
    }

    public void setColegio(R_Colegio colegio) {
        this.colegio = colegio;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTipoCurso(R_TipoCurso curso) {
        tipoCurso = curso;
    }

    public void setTotalAlumnos(int totalAlumnos) {
        this.totalAlumnos = totalAlumnos;
    }

    public void setTotalAprobados(int totalAprobados) {
        this.totalAprobados = totalAprobados;
    }

    public void setTotalEvaluados(int totalEvaluados) {
        this.totalEvaluados = totalEvaluados;
    }

    public void setTotalReprobados(int totalReprobados) {
        this.totalReprobados = totalReprobados;
    }

}
