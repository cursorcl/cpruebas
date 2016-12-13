package cl.eos.view.ots;

import java.time.LocalDate;

import cl.eos.persistence.models.SColegio;
import cl.eos.persistence.models.SCurso;
import cl.eos.persistence.models.SProfesor;
import cl.eos.persistence.models.SPrueba;

public class OTImprimirPrueba {

    private SPrueba prueba;
    private SColegio colegio;
    private SCurso curso;
    private SProfesor profesor;
    private Long fecha;
    private Integer nroAlumnos;

    public OTImprimirPrueba(SPrueba prueba) {
        this.prueba = prueba;
    }

    public SColegio getColegio() {
        return colegio;
    }

    public SCurso getCurso() {
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

    public SProfesor getProfesor() {
        return profesor;
    }

    public SPrueba getPrueba() {
        return prueba;
    }

    public void setColegio(SColegio colegio) {
        this.colegio = colegio;
    }

    public void setCurso(SCurso curso) {
        this.curso = curso;
        setNroAlumnos(curso.getAlumnos().size());
    }

    public void setFecha(Long fecha) {
        this.fecha = fecha;
    }

    public void setNroAlumnos(Integer nroAlumnos) {
        this.nroAlumnos = nroAlumnos;
    }

    public void setProfesor(SProfesor profesor) {
        this.profesor = profesor;
    }

    public void setPrueba(SPrueba prueba) {
        this.prueba = prueba;
    }

}
