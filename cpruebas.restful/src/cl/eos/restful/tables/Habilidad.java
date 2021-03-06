package cl.eos.restful.tables;

import cl.eos.persistence.AEntity;

public class Habilidad  extends AEntity{
    
    private static final long serialVersionUID = 1L;
    Long id;
    String name;
    String descripcion;

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

    public static class Builder {
        private Long id;
        private String name;
        private String descripcion;
        private Integer version;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder descripcion(String descripcion) {
            this.descripcion = descripcion;
            return this;
        }

        public Builder version(Integer version) {
            this.version = version;
            return this;
        }

        public Habilidad build() {
            return new Habilidad(this);
        }
    }

    private Habilidad(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.descripcion = builder.descripcion;
        this.version = builder.version;
    }
}
