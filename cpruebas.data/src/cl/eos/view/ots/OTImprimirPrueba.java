package cl.eos.view.ots;

import java.time.LocalDate;

import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_Profesor;
import cl.eos.restful.tables.R_Prueba;

public class OTImprimirPrueba {

    private R_Prueba prueba;
    private R_Colegio colegio;
    private R_Curso curso;
    private R_Profesor profesor;
    private Long fecha;

    public OTImprimirPrueba(R_Prueba prueba) {
        this.prueba = prueba;
    }

    public R_Colegio getColegio() {
        return colegio;
    }

    public R_Curso getCurso() {
        return curso;
    }

    public Long getFecha() {
        return fecha;
    }

    public String getFechaLocal() {
        final LocalDate date = LocalDate.ofEpochDay(fecha.longValue());
        return date.toString();
    }

    public R_Profesor getProfesor() {
        return profesor;
    }

    public R_Prueba getPrueba() {
        return prueba;
    }

    public void setColegio(R_Colegio colegio) {
        this.colegio = colegio;
    }

    public void setCurso(R_Curso curso) {
        this.curso = curso;
    }

    public void setFecha(Long fecha) {
        this.fecha = fecha;
    }

    public void setProfesor(R_Profesor profesor) {
        this.profesor = profesor;
    }

    public void setPrueba(R_Prueba prueba) {
        this.prueba = prueba;
    }

}
