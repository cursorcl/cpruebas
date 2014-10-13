package cl.eos.detection;

public class OTResultadoScanner {

  private String rut;
  private Integer forma;
  private String respuestas;
  
  public OTResultadoScanner() {
  }

  
  public OTResultadoScanner(String rut, Integer forma, String respuestas) {
    super();
    this.rut = rut;
    this.forma = forma;
    this.respuestas = respuestas;
  }


  public String getRut() {
    return rut;
  }
  public void setRut(String rut) {
    this.rut = rut;
  }
  public Integer getForma() {
    return forma;
  }
  public void setForma(Integer forma) {
    this.forma = forma;
  }
  public String getRespuestas() {
    return respuestas;
  }
  public void setRespuestas(String respuestas) {
    this.respuestas = respuestas;
  }


  @Override
  public String toString() {
    return "OTResultadoScanner [rut=" + rut + ", forma=" + forma + ", respuestas=" + respuestas
        + "]";
  }
  
  
}
