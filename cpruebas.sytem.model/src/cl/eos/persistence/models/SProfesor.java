package cl.eos.persistence.models;

import cl.eos.persistence.AEntity;

public class SProfesor extends AEntity {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String rut;
    private String name;
    private String paterno;
    private String materno;

    @Override
    public Long getId() {
        return id;
    }

    public String getMaterno() {
        return materno;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getPaterno() {
        return paterno;
    }

    public String getRut() {
        return rut;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", name, paterno, materno).trim();
    }

    @Override
    public boolean validate() {
        return true;
    }

    public static class Builder {
        private Long id;
        private String rut;
        private String name;
        private String paterno;
        private String materno;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder rut(String rut) {
            this.rut = rut;
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

        public Builder materno(String materno) {
            this.materno = materno;
            return this;
        }

        public SProfesor build() {
            SProfesor sProfesor = new SProfesor();
            sProfesor.id = id;
            sProfesor.rut = rut;
            sProfesor.name = name;
            sProfesor.paterno = paterno;
            sProfesor.materno = materno;
            return sProfesor;
        }
    }
}
