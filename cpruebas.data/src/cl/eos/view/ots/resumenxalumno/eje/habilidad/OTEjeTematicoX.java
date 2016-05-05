package cl.eos.view.ots.resumenxalumno.eje.habilidad;

import cl.eos.persistence.models.EjeTematico;

/**
 * Para una prueba, contiene un eje temático y la cantidad de preguntas que hay de dicho eje temático.
 * @author curso_000
 */
public class OTEjeTematicoX {
  private EjeTematico eje;
  private int cantidadPreguntas;

  public OTEjeTematicoX(EjeTematico eje) {
    super();
    this.eje = eje;
  }

  public EjeTematico getEje() {
    return eje;
  }

  public void setEje(EjeTematico eje) {
    this.eje = eje;
  }

  public int getCantidadPreguntas() {
    return cantidadPreguntas;
  }

  public void setCantidadPreguntas(int cantidadPreguntas) {
    this.cantidadPreguntas = cantidadPreguntas;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((eje == null) ? 0 : eje.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    OTEjeTematicoX other = (OTEjeTematicoX) obj;
    if (eje == null) {
      if (other.eje != null)
        return false;
    } else if (!eje.equals(other.eje))
      return false;
    return true;
  }

  public boolean itIsTheSame(EjeTematico eje)
  {
    return this.eje.equals(eje);
  }
  
}
