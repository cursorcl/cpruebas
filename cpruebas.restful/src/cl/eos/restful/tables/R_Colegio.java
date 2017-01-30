package cl.eos.restful.tables;

import com.google.gson.annotations.SerializedName;

import cl.eos.persistence.AEntity;

public class R_Colegio extends AEntity{
    
    private static final long serialVersionUID = 1L;
    
    @SerializedName(value = "id", alternate = {"ID"})
    Long id;
    @SerializedName(value = "name", alternate = {"NAME"})
    String name;
    @SerializedName(value = "direccion", alternate = {"DIRECCION"})
    String direccion;
    @SerializedName(value = "tipocolegio_id", alternate = {"TIPOCOLEGIO_ID"})
    Long tipocolegio_id;
    @SerializedName(value = "ciudad", alternate = {"CIUDAD"})
    String ciudad;

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
     * @return the tipocolegio_id
     */
    public final Long getTipocolegio_id() {
        return tipocolegio_id;
    }

    /**
     * @param tipocolegio_id the tipocolegio_id to set
     */
    public final void setTipocolegio_id(Long tipocolegio_id) {
        this.tipocolegio_id = tipocolegio_id;
    }

    /**
     * @return the ciudad
     */
    public final String getCiudad() {
        return ciudad;
    }

    /**
     * @param ciudad the ciudad to set
     */
    public final void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    /* (non-Javadoc)
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
        private Long tipocolegio_id;
        private Integer version;
        private String ciudad;

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

        public Builder tipocolegio_id(Long tipocolegio_id) {
            this.tipocolegio_id = tipocolegio_id;
            return this;
        }

        public Builder version(Integer version) {
            this.version = version;
            return this;
        }

        public Builder ciudad(String ciudad) {
            this.ciudad = ciudad;
            return this;
        }

        public R_Colegio build() {
            return new R_Colegio(this);
        }
    }

    private R_Colegio(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.direccion = builder.direccion;
        this.tipocolegio_id = builder.tipocolegio_id;
        this.version = builder.version;
        this.ciudad = builder.ciudad;
    }
}
