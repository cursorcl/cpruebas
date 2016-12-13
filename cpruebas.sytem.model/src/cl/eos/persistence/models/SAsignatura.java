package cl.eos.persistence.models;

import cl.eos.persistence.AEntity;

public class SAsignatura extends AEntity {

    private static final long serialVersionUID = 1L;
    private Long id;

    private String name;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
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
        return true;
    }

    public static class Builder {
        private Long id;
        private String name;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public SAsignatura build() {
            SAsignatura sAsignatura = new SAsignatura();
            sAsignatura.id = id;
            sAsignatura.name = name;
            return sAsignatura;
        }
    }
}
