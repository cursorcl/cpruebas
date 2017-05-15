package cl.eos.restful.tables;

import java.time.LocalDate;

import com.google.gson.annotations.SerializedName;

import cl.eos.persistence.AEntity;

public class R_Prueba extends AEntity {
  private static final long serialVersionUID = 1L;

  public enum Estado {
    CREADA(0), DEFINIDA(1), EVALUADA(2);

    private int id;

    private Estado(int id) {
      this.id = id;
    }

    /**
     * @return the id
     */
    public final int getId() {
      return id;
    }

    /**
     * @param id the id to set
     */
    public final void setId(int id) {
      this.id = id;
    }

    public static Estado getEstado(int estado) {
      return Estado.values()[estado % 3];
    }
  };

  @SerializedName(value = "id", alternate = {"ID"})
  Long id;
  @SerializedName(value = "exigencia", alternate = {"EXIGENCIA"})
  Integer exigencia;
  @SerializedName(value = "alternativas", alternate = {"ALTERNATIVAS"})
  Integer alternativas;
  @SerializedName(value = "fecha", alternate = {"FECHA"})
  Long fecha;
  @SerializedName(value = "puntajebase", alternate = {"PUNTAJEBASE"})
  Integer puntajebase;
  @SerializedName(value = "nroformas", alternate = {"NROFORMAS"})
  Integer nroformas;
  @SerializedName(value = "responses", alternate = {"RESPONSES"})
  String responses;
  @SerializedName(value = "nropreguntas", alternate = {"NROPREGUNTAS"})
  Integer nropreguntas;
  @SerializedName(value = "name", alternate = {"NAME"})
  String name;
  @SerializedName(value = "asignatura_id", alternate = {"ASIGNATURA_ID"})
  Long asignatura_id;
  @SerializedName(value = "tipocurso_id", alternate = {"TIPOCURSO_ID"})
  Long curso_id;
  @SerializedName(value = "tipoprueba_id", alternate = {"TIPOPRUEBA_ID"})
  Long tipoprueba_id;
  @SerializedName(value = "profesor_id", alternate = {"PROFESOR_ID"})
  Long profesor_id;
  @SerializedName(value = "nivelevaluacion_id", alternate = {"NIVELEVALUACION_ID"})
  Long nivelevaluacion_id;

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
   * @return the exigencia
   */
  public final Integer getExigencia() {
    return exigencia;
  }

  /**
   * @param exigencia the exigencia to set
   */
  public final void setExigencia(Integer exigencia) {
    this.exigencia = exigencia;
  }

  /**
   * @return the alternativas
   */
  public final Integer getAlternativas() {
    return alternativas;
  }

  /**
   * @param alternativas the alternativas to set
   */
  public final void setAlternativas(Integer alternativas) {
    this.alternativas = alternativas;
  }

  /**
   * @return the fecha
   */
  public final Long getFecha() {
    return fecha;
  }

  /**
   * @param fecha the fecha to set
   */
  public final void setFecha(Long fecha) {
    this.fecha = fecha;
  }

  /**
   * @return the puntajebase
   */
  public final Integer getPuntajebase() {
    return puntajebase;
  }

  /**
   * @param puntajebase the puntajebase to set
   */
  public final void setPuntajebase(Integer puntajebase) {
    this.puntajebase = puntajebase;
  }

  /**
   * @return the nroformas
   */
  public final Integer getNroformas() {
    return nroformas;
  }

  /**
   * @param nroformas the nroformas to set
   */
  public final void setNroformas(Integer nroformas) {
    this.nroformas = nroformas;
  }

  /**
   * @return the responses
   */
  public final String getResponses() {
    return responses;
  }

  /**
   * @param responses the responses to set
   */
  public final void setResponses(String responses) {
    this.responses = responses;
  }

  /**
   * @return the nropreguntas
   */
  public final Integer getNropreguntas() {
    return nropreguntas;
  }

  /**
   * @param nropreguntas the nropreguntas to set
   */
  public final void setNropreguntas(Integer nropreguntas) {
    this.nropreguntas = nropreguntas;
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
   * @return the asignatura_id
   */
  public final Long getAsignatura_id() {
    return asignatura_id;
  }

  /**
   * @param asignatura_id the asignatura_id to set
   */
  public final void setAsignatura_id(Long asignatura_id) {
    this.asignatura_id = asignatura_id;
  }

  /**
   * @return the curso_id
   */
  public final Long getCurso_id() {
    return curso_id;
  }

  /**
   * @param curso_id the curso_id to set
   */
  public final void setCurso_id(Long curso_id) {
    this.curso_id = curso_id;
  }

  /**
   * @return the tipoprueba_id
   */
  public final Long getTipoprueba_id() {
    return tipoprueba_id;
  }

  /**
   * @param tipoprueba_id the tipoprueba_id to set
   */
  public final void setTipoprueba_id(Long tipoprueba_id) {
    this.tipoprueba_id = tipoprueba_id;
  }

  /**
   * @return the profesor_id
   */
  public final Long getProfesor_id() {
    return profesor_id;
  }

  /**
   * @param profesor_id the profesor_id to set
   */
  public final void setProfesor_id(Long profesor_id) {
    this.profesor_id = profesor_id;
  }

  /**
   * @return the nivelevaluacion_id
   */
  public final Long getNivelevaluacion_id() {
    return nivelevaluacion_id;
  }

  /**
   * @param nivelevaluacion_id the nivelevaluacion_id to set
   */
  public final void setNivelevaluacion_id(Long nivelevaluacion_id) {
    this.nivelevaluacion_id = nivelevaluacion_id;
  }


  public LocalDate getFechaLocal() {
    return LocalDate.ofEpochDay(this.fecha.longValue());
  }


  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return name;
  }



  public static class Builder {
    private Long id;
    private Integer exigencia;
    private Integer alternativas;
    private Long fecha;
    private Integer puntajebase;
    private Integer nroformas;
    private String responses;
    private Integer nropreguntas;
    private String name;
    private Long asignatura_id;
    private Long curso_id;
    private Long tipoprueba_id;
    private Long profesor_id;
    private Long nivelevaluacion_id;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder exigencia(Integer exigencia) {
      this.exigencia = exigencia;
      return this;
    }

    public Builder alternativas(Integer alternativas) {
      this.alternativas = alternativas;
      return this;
    }

    public Builder fecha(Long fecha) {
      this.fecha = fecha;
      return this;
    }

    public Builder puntajebase(Integer puntajebase) {
      this.puntajebase = puntajebase;
      return this;
    }

    public Builder nroformas(Integer nroformas) {
      this.nroformas = nroformas;
      return this;
    }

    public Builder responses(String responses) {
      this.responses = responses;
      return this;
    }

    public Builder nropreguntas(Integer nropreguntas) {
      this.nropreguntas = nropreguntas;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder asignatura_id(Long asignatura_id) {
      this.asignatura_id = asignatura_id;
      return this;
    }

    public Builder curso_id(Long curso_id) {
      this.curso_id = curso_id;
      return this;
    }

    public Builder tipoprueba_id(Long tipoprueba_id) {
      this.tipoprueba_id = tipoprueba_id;
      return this;
    }

    public Builder profesor_id(Long profesor_id) {
      this.profesor_id = profesor_id;
      return this;
    }

    public Builder nivelevaluacion_id(Long nivelevaluacion_id) {
      this.nivelevaluacion_id = nivelevaluacion_id;
      return this;
    }



    public R_Prueba build() {
      return new R_Prueba(this);
    }
  }

  private R_Prueba(Builder builder) {
    this.id = builder.id;
    this.exigencia = builder.exigencia;
    this.alternativas = builder.alternativas;
    this.fecha = builder.fecha;
    this.puntajebase = builder.puntajebase;
    this.nroformas = builder.nroformas;
    this.responses = builder.responses;
    this.nropreguntas = builder.nropreguntas;
    this.name = builder.name;
    this.asignatura_id = builder.asignatura_id;
    this.curso_id = builder.curso_id;
    this.tipoprueba_id = builder.tipoprueba_id;
    this.profesor_id = builder.profesor_id;
    this.nivelevaluacion_id = builder.nivelevaluacion_id;
  }
}
