package cl.eos.restful.tables;

import com.google.gson.annotations.SerializedName;

import cl.eos.persistence.AEntity;

public class R_Preguntas extends AEntity {
  private static final long serialVersionUID = 1L;
  @SerializedName(value = "id", alternate = {"ID"})
  Long id;
  @SerializedName(value = "name", alternate = {"NAME"})
  String name;
  @SerializedName(value = "numero", alternate = {"NUMERO"})
  Integer numero;
  @SerializedName(value = "prueba_id", alternate = {"PRUEBA_ID"})
  Long prueba_id;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the numero
   */
  public final Integer getNumero() {
    return numero;
  }

  /**
   * @param numero the numero to set
   */
  public final void setNumero(Integer numero) {
    this.numero = numero;
  }

  /**
   * @return the prueba_id
   */
  public final Long getPrueba_id() {
    return prueba_id;
  }

  /**
   * @param prueba_id the prueba_id to set
   */
  public final void setPrueba_id(Long prueba_id) {
    this.prueba_id = prueba_id;
  }



  public static class Builder {
    private Long id;
    private String name;
    private Integer numero;
    private Long prueba_id;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder numero(Integer numero) {
      this.numero = numero;
      return this;
    }

    public Builder prueba_id(Long prueba_id) {
      this.prueba_id = prueba_id;
      return this;
    }

    public R_Preguntas build() {
      return new R_Preguntas(this);
    }
  }

  private R_Preguntas(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.numero = builder.numero;
    this.prueba_id = builder.prueba_id;
  }
}
