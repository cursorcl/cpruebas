package cl.eos.view.ots;

import cl.eos.persistence.models.SAlumno;

public class OTAlumnosEvaluarManual {

    private final String respuestas;
    private final SAlumno alumno;

    public OTAlumnosEvaluarManual(SAlumno alumno, String respuestas) {
        this.alumno = alumno;
        this.respuestas = respuestas;
    }

    public String getMaterno() {
        return alumno.getMaterno();
    }

    public String getNombres() {
        return alumno.getName();
    }

    public String getPaterno() {
        return alumno.getPaterno();
    }

    public String getRespuestas() {
        return respuestas;
    }

    public String getRut() {
        return alumno.getRut();
    }
}
