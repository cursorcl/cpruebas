package cl.eos.view.ots.resumenxalumno.eje.habilidad;

import java.util.ArrayList;
import java.util.List;

import cl.eos.persistence.models.SAlumno;

public class OTAlumnoResumen {
    private SAlumno alumno;
    private List<Float> porcentajes = new ArrayList<Float>();

    public OTAlumnoResumen(SAlumno alumno) {
        this.alumno = alumno;
    }

    public SAlumno getAlumno() {
        return alumno;
    }

    public List<Float> getPorcentajes() {
        return porcentajes;
    }

    public void setAlumno(SAlumno alumno) {
        this.alumno = alumno;
    }

    public void setPorcentajes(List<Float> porcentajes) {
        this.porcentajes = porcentajes;
    }

}
