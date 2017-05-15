package cl.eos.restful.tables;

import com.google.gson.annotations.SerializedName;

import cl.eos.persistence.AEntity;

public class R_EstadoPruebaCliente extends AEntity {

  private static final long serialVersionUID = 1L;
  @SerializedName(value = "prueba_id", alternate = {"PRUEBA_ID"})
  Long prueba_id;
  @SerializedName(value = "cliente_id", alternate = {"CLIENTE_ID"})
  Long cliente_id;
  @SerializedName(value = "estado_id", alternate = {"ESTADO_ID"})
  Long estado_id;

  @Override
  public Long getId() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setId(Long id) {
    // TODO Auto-generated method stub

  }

  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setName(String name) {
    // TODO Auto-generated method stub

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

  /**
   * @return the cliente_id
   */
  public final Long getCliente_id() {
    return cliente_id;
  }

  /**
   * @param cliente_id the cliente_id to set
   */
  public final void setCliente_id(Long cliente_id) {
    this.cliente_id = cliente_id;
  }

  /**
   * @return the estado_id
   */
  public final Long getEstado_id() {
    return estado_id;
  }

  /**
   * @param estado_id the estado_id to set
   */
  public final void setEstado_id(Long estado_id) {
    this.estado_id = estado_id;
  }



  public static class Builder {
    private Long prueba_id;
    private Long cliente_id;
    private Long estado_id;

    public Builder prueba_id(Long prueba_id) {
      this.prueba_id = prueba_id;
      return this;
    }

    public Builder cliente_id(Long cliente_id) {
      this.cliente_id = cliente_id;
      return this;
    }

    public Builder estado_id(Long estado_id) {
      this.estado_id = estado_id;
      return this;
    }

    public R_EstadoPruebaCliente build() {
      return new R_EstadoPruebaCliente(this);
    }
  }

  private R_EstadoPruebaCliente(Builder builder) {
    this.prueba_id = builder.prueba_id;
    this.cliente_id = builder.cliente_id;
    this.estado_id = builder.estado_id;
  }
}
