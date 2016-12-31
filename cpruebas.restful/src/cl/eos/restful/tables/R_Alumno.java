package cl.eos.restful.tables;

import com.google.gson.annotations.SerializedName;

import cl.eos.persistence.AEntity;

public class R_Alumno extends AEntity {

    private static final long serialVersionUID = 1L;

    @SerializedName(value = "id", alternate = { "ID" })
    Long id;
    @SerializedName(value = "paterno", alternate = { "PATERNO" })
    String paterno;
    @SerializedName(value = "materno", alternate = { "MATERNO" })
    String materno;
    @SerializedName(value = "rut", alternate = { "RUT" })
    String rut;
    @SerializedName(value = "direccion", alternate = { "DIRECCION" })
    String direccion;
    @SerializedName(value = "name", alternate = { "NAME" })
    String name;
    @SerializedName(value = "curso_id", alternate = { "CURSO_ID" })
    Long curso_id;
    @SerializedName(value = "colegio_id", alternate = { "COLEGIO_ID" })
    Long colegio_id;
    @SerializedName(value = "tipoalumno_id", alternate = { "TIPOALUMNO_ID" })
    Long tipoalumno_id;

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
     * @return the direccion
     */
    public final String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion
     *            the direccion to set
     */
    public final void setDireccion(String direccion) {
        this.direccion = direccion;
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
     * @return the curso_id
     */
    public final Long getCurso_id() {
        return curso_id;
    }

    /**
     * @param curso_id
     *            the curso_id to set
     */
    public final void setCurso_id(Long curso_id) {
        this.curso_id = curso_id;
    }

    /**
     * @return the colegio_id
     */
    public final Long getColegio_id() {
        return colegio_id;
    }

    /**
     * @param colegio_id
     *            the colegio_id to set
     */
    public final void setColegio_id(Long colegio_id) {
        this.colegio_id = colegio_id;
    }

    /**
     * @return the tipoalumno_id
     */
    public final Long getTipoalumno_id() {
        return tipoalumno_id;
    }

    /**
     * @param tipoalumno_id
     *            the tipoalumno_id to set
     */
    public final void setTipoalumno_id(Long tipoalumno_id) {
        this.tipoalumno_id = tipoalumno_id;
    }

    public static class Builder {
        private Long id;
        private String paterno;
        private String materno;
        private String rut;
        private String direccion;
        private String name;
        private Long curso_id;
        private Long colegio_id;
        private Long tipoalumno_id;
        private Integer version;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder paterno(String paterno) {
            this.paterno = paterno;
            return this;
        }

        public Builder materno(String materno) {
            this.materno = materno;
            return this;
        }

        public Builder rut(String rut) {
            this.rut = rut;
            return this;
        }

        public Builder direccion(String direccion) {
            this.direccion = direccion;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder curso_id(Long curso_id) {
            this.curso_id = curso_id;
            return this;
        }

        public Builder colegio_id(Long colegio_id) {
            this.colegio_id = colegio_id;
            return this;
        }

        public Builder tipoalumno_id(Long tipoalumno_id) {
            this.tipoalumno_id = tipoalumno_id;
            return this;
        }

        public Builder version(Integer version) {
            this.version = version;
            return this;
        }

        public R_Alumno build() {
            return new R_Alumno(this);
        }
    }

    private R_Alumno(Builder builder) {
        this.id = builder.id;
        this.paterno = builder.paterno;
        this.materno = builder.materno;
        this.rut = builder.rut;
        this.direccion = builder.direccion;
        this.name = builder.name;
        this.curso_id = builder.curso_id;
        this.colegio_id = builder.colegio_id;
        this.tipoalumno_id = builder.tipoalumno_id;
        this.version = builder.version;
    }
}
