package cl.eos.restful.tables;

import com.google.gson.annotations.SerializedName;

import cl.eos.persistence.AEntity;

public class R_Usuarios extends AEntity {

  private static final long serialVersionUID = 1L;

  @SerializedName(value = "id", alternate = {"ID"})
  Long id;
  @SerializedName(value = "login", alternate = {"LOGIN"})
  String login;
  @SerializedName(value = "name", alternate = {"NAME"})
  String name;
  @SerializedName(value = "password", alternate = {"PASSWORD"})
  String password;
  @SerializedName(value = "cliente", alternate = {"CLIENTE"})
  Long cliente;

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
   * @return the login
   */
  public final String getLogin() {
    return login;
  }

  /**
   * @param login the login to set
   */
  public final void setLogin(String login) {
    this.login = login;
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
   * @return the password
   */
  public final String getPassword() {
    return password;
  }

  /**
   * @param password the password to set
   */
  public final void setPassword(String password) {
    this.password = password;
  }

  /**
   * @return the cliente
   */
  public final Long getCliente() {
    return cliente;
  }

  /**
   * @param cliente the cliente to set
   */
  public final void setCliente(Long cliente) {
    this.cliente = cliente;
  }


  public static class Builder {
    private Long id;
    private String login;
    private String name;
    private String password;
    private Long cliente;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder login(String login) {
      this.login = login;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Builder cliente(Long cliente) {
      this.cliente = cliente;
      return this;
    }

    public R_Usuarios build() {
      return new R_Usuarios(this);
    }
  }

  private R_Usuarios(Builder builder) {
    this.id = builder.id;
    this.login = builder.login;
    this.name = builder.name;
    this.password = builder.password;
    this.cliente = builder.cliente;
  }
}
