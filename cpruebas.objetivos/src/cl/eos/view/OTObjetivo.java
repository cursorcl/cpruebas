package cl.eos.view;

import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Objetivo;
import cl.eos.restful.tables.R_TipoCurso;

public class OTObjetivo {

  Long id;
  String name;
  String descripcion;
  R_TipoCurso tipocurso;
  R_Asignatura asignatura;
  R_Objetivo objetivo;

  /**
   * @return the id
   */
  public final Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public final void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the name
   */
  public final String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public final void setName(String name) {
    this.name = name;
  }

  /**
   * @return the descripcion
   */
  public final String getDescripcion() {
    return descripcion;
  }

  /**
   * @param descripcion the descripcion to set
   */
  public final void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  /**
   * @return the tipocurso
   */
  public final R_TipoCurso getTipoCurso() {
    return tipocurso;
  }

  /**
   * @param tipocurso the tipocurso to set
   */
  public final void setTipoCurso(R_TipoCurso tipocurso) {
    this.tipocurso = tipocurso;
  }

  /**
   * @return the asignatura
   */
  public final R_Asignatura getAsignatura() {
    return asignatura;
  }

  /**
   * @param asignatura the asignatura to set
   */
  public final void setAsignatura(R_Asignatura asignatura) {
    this.asignatura = asignatura;
  }



  /**
   * @return the objetivo
   */
  public final R_Objetivo getObjetivo() {
    return objetivo;
  }

  /**
   * @param objetivo the objetivo to set
   */
  public final void setObjetivo(R_Objetivo objetivo) {
    this.objetivo = objetivo;
  }



  public static class Builder {
    private R_TipoCurso tipocurso;
    private R_Asignatura asignatura;
    private R_Objetivo objetivo;

    public Builder tipocurso(R_TipoCurso tipocurso) {
      this.tipocurso = tipocurso;
      return this;
    }

    public Builder asignatura(R_Asignatura asignatura) {
      this.asignatura = asignatura;
      return this;
    }

    public Builder objetivo(R_Objetivo objetivo) {
      this.objetivo = objetivo;
      return this;
    }

    public OTObjetivo build() {
      OTObjetivo oTObjetivo = new OTObjetivo();
      oTObjetivo.objetivo = objetivo;
      oTObjetivo.id = objetivo.getId();
      oTObjetivo.name = objetivo.getName();
      oTObjetivo.descripcion = objetivo.getDescripcion();
      oTObjetivo.tipocurso = tipocurso;
      oTObjetivo.asignatura = asignatura;
      oTObjetivo.objetivo = objetivo;
      return oTObjetivo;
    }
  }
}
