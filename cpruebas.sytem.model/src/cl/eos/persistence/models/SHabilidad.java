package cl.eos.persistence.models;

import cl.eos.persistence.AEntity;

public class SHabilidad extends AEntity {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String descripcion;

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public void setId(Long id) {
        this.id = id;

    }

    @Override
    public void setName(String name) {
        this.name = name;

    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean validate() {
        return false;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String descripcion;

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

        public SHabilidad build() {
            SHabilidad sHabilidad = new SHabilidad();
            sHabilidad.id = id;
            sHabilidad.name = name;
            sHabilidad.descripcion = descripcion;
            return sHabilidad;
        }
    }
}
