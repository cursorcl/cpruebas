package cl.eos.view.ots.resumenxalumno.eje.habilidad;

import java.util.ArrayList;
import java.util.List;

import cl.eos.persistence.models.Alumno;

public class OTAlumnoResumen {
    private Alumno alumno;
    private List<Float> porcentajes = new ArrayList<Float>();

    public OTAlumnoResumen(Alumno alumno) {
        this.alumno = alumno;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public List<Float> getPorcentajes() {
        return porcentajes;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public void setPorcentajes(List<Float> porcentajes) {
        this.porcentajes = porcentajes;
    }

}
