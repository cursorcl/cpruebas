package cl.eos.persistence.models;

import cl.eos.persistence.AEntity;

public class SEjeTematico extends AEntity {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private STipoPrueba tipoprueba;
    private SAsignatura asignatura;

    public SAsignatura getAsignatura() {
        return asignatura;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public STipoPrueba getTipoprueba() {
        return tipoprueba;
    }

    public void setAsignatura(SAsignatura asignatura) {
        this.asignatura = asignatura;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setTipoprueba(STipoPrueba tipoprueba) {
        this.tipoprueba = tipoprueba;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean validate() {
        return true;
    }

    public static class Builder {
        private Long id;
        private String name;
        private STipoPrueba tipoprueba;
        private SAsignatura asignatura;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder tipoprueba(STipoPrueba tipoprueba) {
            this.tipoprueba = tipoprueba;
            return this;
        }

        public Builder asignatura(SAsignatura asignatura) {
            this.asignatura = asignatura;
            return this;
        }

        public SEjeTematico build() {
            SEjeTematico sEjeTematico = new SEjeTematico();
            sEjeTematico.id = id;
            sEjeTematico.name = name;
            sEjeTematico.tipoprueba = tipoprueba;
            sEjeTematico.asignatura = asignatura;
            return sEjeTematico;
        }
    }
}
