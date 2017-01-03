package cl.eos.view.ots.resumenxalumno.eje.habilidad;

import java.util.ArrayList;
import java.util.List;

import cl.eos.restful.tables.R_Alumno;

public class OTAlumnoResumen {
    private R_Alumno alumno;
    private List<Float> porcentajes = new ArrayList<Float>();

    public OTAlumnoResumen(R_Alumno alumno) {
        this.alumno = alumno;
    }

    public R_Alumno getAlumno() {
        return alumno;
    }

    public List<Float> getPorcentajes() {
        return porcentajes;
    }

    public void setAlumno(R_Alumno alumno) {
        this.alumno = alumno;
    }

    public void setPorcentajes(List<Float> porcentajes) {
        this.porcentajes = porcentajes;
    }

}
