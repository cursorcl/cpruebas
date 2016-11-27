package cl.eos.restful.tables;

import cl.eos.persistence.AEntity;

public class Profesor  extends AEntity{
    
    private static final long serialVersionUID = 1L;
    
    Long id;
    String name;
    String paterno;
    String rut;
    String materno;

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
     * @return the paterno
     */
    public final String getPaterno() {
        return paterno;
    }

    /**
     * @param paterno the paterno to set
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
     * @param rut the rut to set
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
     * @param materno the materno to set
     */
    public final void setMaterno(String materno) {
        this.materno = materno;
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

        public Profesor build() {
            return new Profesor(this);
        }
    }

    private Profesor(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.paterno = builder.paterno;
        this.rut = builder.rut;
        this.materno = builder.materno;
        this.version = builder.version;
    }
}
