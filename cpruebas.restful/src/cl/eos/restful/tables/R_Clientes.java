package cl.eos.restful.tables;

import com.google.gson.annotations.SerializedName;

import cl.eos.persistence.AEntity;

public class R_Clientes extends AEntity {

  private static final long serialVersionUID = 1L;

  @SerializedName(value = "id", alternate = {"ID"})
  Long id;
  @SerializedName(value = "name", alternate = {"NAME"})
  String name;
  @SerializedName(value = "rut", alternate = {"RUT"})
  String rut;
  @SerializedName(value = "direccion", alternate = {"DIRECCION"})
  String direccion;

  @SerializedName(value = "fono", alternate = {"FONO"})
  private String fono;
  @SerializedName(value = "email", alternate = {"EMAIL"})
  private String email;
  @SerializedName(value = "activo", alternate = {"ACTIVO"})
  private int activo;

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
   * @return the direccion
   */
  public final String getDireccion() {
    return direccion;
  }

  /**
   * @param direccion the direccion to set
   */
  public final void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  /**
   * @return the rut del cliente
   */
  public final String getRut() {
    return rut;
  }

  /**
   * @param valor the rut
   */
  public final void setRut(String rut) {
    this.rut = rut;
  }

  /**
   * @return the fono
   */
  public final String getFono() {
    return fono;
  }

  /**
   * @param fono the fono to set
   */
  public final void setFono(String fono) {
    this.fono = fono;
  }

  /**
   * @return the email
   */
  public final String getEmail() {
    return email;
  }

  /**
   * @param email the email to set
   */
  public final void setEmail(String email) {
    this.email = email;
  }

  /**
   * @return the activo
   */
  public final int getActivo() {
    return activo;
  }

  /**
   * @param activo the activo to set
   */
  public final void setActivo(int activo) {
    this.activo = activo;
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
    private String name;
    private String direccion;
    private String rut;
    private String fono;
    private String email;
    private int activo;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder direccion(String direccion) {
      this.direccion = direccion;
      return this;
    }

    public Builder rut(String rut) {
      this.rut = rut;
      return this;
    }

    public Builder fono(String fono) {
      this.fono = fono;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder activo(int activo) {
      this.activo = activo;
      return this;
    }

    public R_Clientes build() {
      return new R_Clientes(this);
    }


  }

  private R_Clientes(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.direccion = builder.direccion;
    this.rut = builder.rut;
    this.fono = builder.fono;
    this.email = builder.email;
    this.activo = builder.activo;
  }
}
