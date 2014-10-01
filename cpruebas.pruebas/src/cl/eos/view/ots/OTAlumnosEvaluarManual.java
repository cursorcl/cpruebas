package cl.eos.view.ots;

import cl.eos.persistence.models.Alumno;

public class OTAlumnosEvaluarManual {

  private String respuestas;
  private Alumno alumno;

  public OTAlumnosEvaluarManual(Alumno alumno, String respuestas) {
    this.alumno = alumno;
    this.respuestas = respuestas;
  }

  public String getRut() {
    return alumno.getRut();
  }

  public String getPaterno() {
    return alumno.getPaterno();
  }

  public String getMaterno() {
    return alumno.getMaterno();
  }

  public String getNombres() {
    return alumno.getName();
  }

  public String getRespuestas() {
    return respuestas;
  }
}
