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

    private void calculate() {
        setPorcAlumnosEvaluados(Utils.redondeo2Decimales((float) totalEvaluados / (float) totalAlumnos * 100f));
        setPorcAlumnosAprobados(Utils.redondeo2Decimales((float) totalAprobados / (float) totalEvaluados * 100f));
        setPorcAlumnosReprobados(Utils.redondeo2Decimales((float) totalReprobados / (float) totalEvaluados * 100f));
    }

    public Colegio getColegio() {
        return colegio;
    }

    public Curso getCurso() {
        return curso;
    }

    public String getName() {
        return name;
    }

    public float getPorcAlumnosAprobados() {
        return porcAlumnosAprobados;
    }

    public float getPorcAlumnosEvaluados() {
        return porcAlumnosEvaluados;
    }

    public float getPorcAlumnosReprobados() {
        return porcAlumnosReprobados;
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

    public void setColegio(Colegio colegio) {
        this.colegio = colegio;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPorcAlumnosAprobados(float alumnosAprobados) {
        porcAlumnosAprobados = alumnosAprobados;
    }

    public void setPorcAlumnosEvaluados(float alumnosEvaluados) {
        porcAlumnosEvaluados = alumnosEvaluados;
    }

    public void setPorcAlumnosReprobados(float alumnosReprobados) {
        porcAlumnosReprobados = alumnosReprobados;
    }

    public void setTotalAlumnos(int totalAlumnos) {
        this.totalAlumnos = totalAlumnos;
    }

    public void setTotalAprobados(int totalAprobados) {
        this.totalAprobados = totalAprobados;
        calculate();
    }

    public void setTotalEvaluados(int totalEvaluados) {
        this.totalEvaluados = totalEvaluados;
        calculate();
    }

    public void setTotalReprobados(int totalReprobados) {
        this.totalReprobados = totalReprobados;
        calculate();
    }

}
