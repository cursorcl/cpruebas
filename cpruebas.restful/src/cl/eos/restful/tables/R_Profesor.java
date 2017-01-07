package cl.eos.restful.tables;

import com.google.gson.annotations.SerializedName;

import cl.eos.persistence.AEntity;

public class R_Profesor extends AEntity {
    private static final long serialVersionUID = 1L;
    @SerializedName(value = "id", alternate = { "ID" }) Long id;
    @SerializedName(value = "name", alternate = { "NAME" }) String name;
    @SerializedName(value = "paterno", alternate = { "PATERNO" }) String paterno;
    @SerializedName(value = "rut", alternate = { "RUT" }) String rut;
    @SerializedName(value = "materno", alternate = { "MATERNO" }) String materno;
    /**
     * @return the id
     */
    public final Long getId() {
        return id;
    }
    /**
     * @param id
     *            the id to set
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
     * @param name
     *            the name to set
     */
    public final void setName(String name) {
        this.name = name;
    }
    /**
     * @return the paterno
     */
    public final String getPaterno() {
        return paterno;
    }
    /**
     * @param paterno
     *            the paterno to set
     */
    public final void setPaterno(String paterno) {
        this.paterno = paterno;
    }
    /**
     * @return the rut
     */
    public final String getRut() {
        return rut;
    }
    /**
     * @param rut
     *            the rut to set
     */
    public final void setRut(String rut) {
        this.rut = rut;
    }
    /**
     * @return the materno
     */
    public final String getMaterno() {
        return materno;
    }
    /**
     * @param materno
     *            the materno to set
     */
    public final void setMaterno(String materno) {
        this.materno = materno;
    }
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s %s %s", getPaterno(), getMaterno(), getName());
    }
    public static class Builder {
        private Long id;
        private String name;
        private String paterno;
        private String rut;
        private String materno;
        private Integer version;
        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        public Builder paterno(String paterno) {
            this.paterno = paterno;
            return this;
        }
        public Builder rut(String rut) {
            this.rut = rut;
            return this;
        }
        public Builder materno(String materno) {
            this.materno = materno;
            return this;
        }
        public Builder version(Integer version) {
            this.version = version;
            return this;
        }
        public R_Profesor build() {
            return new R_Profesor(this);
        }
    }
    private R_Profesor(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.paterno = builder.paterno;
        this.rut = builder.rut;
        this.materno = builder.materno;
        this.version = builder.version;
    }
}
