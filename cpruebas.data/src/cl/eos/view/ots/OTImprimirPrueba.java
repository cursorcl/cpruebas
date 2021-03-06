package cl.eos.view.ots;

import java.time.LocalDate;

import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.Profesor;
import cl.eos.persistence.models.Prueba;

public class OTImprimirPrueba {

    private Prueba prueba;
    private Colegio colegio;
    private Curso curso;
    private Profesor profesor;
    private Long fecha;
    private Integer nroAlumnos;

    public OTImprimirPrueba(Prueba prueba) {
        this.prueba = prueba;
    }

    public Colegio getColegio() {
        return colegio;
    }

    public Curso getCurso() {
        return curso;
    }

    public Long getFecha() {
        return fecha;
    }

    public String getFechaLocal() {
        final LocalDate date = LocalDate.ofEpochDay(fecha.longValue());
        return date.toString();
    }

    public Integer getNroAlumnos() {
        return nroAlumnos;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public Prueba getPrueba() {
        return prueba;
    }

    public void setColegio(Colegio colegio) {
        this.colegio = colegio;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
        setNroAlumnos(curso.getAlumnos().size());
    }

    public void setFecha(Long fecha) {
        this.fecha = fecha;
    }

    public void setNroAlumnos(Integer nroAlumnos) {
        this.nroAlumnos = nroAlumnos;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public void setPrueba(Prueba prueba) {
        this.prueba = prueba;
    }

}
