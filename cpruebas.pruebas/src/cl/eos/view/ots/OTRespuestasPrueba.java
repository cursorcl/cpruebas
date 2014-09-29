package cl.eos.view.ots;

public class OTRespuestasPrueba {

  private Integer nroPregunta;
  private String respuesta;
  private Boolean verdaderoFalso;
  private Boolean Calculo;

  public OTRespuestasPrueba() {
  }

  public Integer getNroPregunta() {
    return nroPregunta;
  }

  public void setNroPregunta(Integer nroPregunta) {
    this.nroPregunta = nroPregunta;
  }

  public String getRespuesta() {
    return respuesta;
  }

  public void setRespuesta(String respuesta) {
    this.respuesta = respuesta;
  }

  public Boolean getVerdaderoFalso() {
    return verdaderoFalso;
  }

  public void setVerdaderoFalso(Boolean verdaderoFalso) {
    this.verdaderoFalso = verdaderoFalso;
  }

  public Boolean getCalculo() {
    return Calculo;
  }

  public void setCalculo(Boolean calculo) {
    Calculo = calculo;
  }

}
