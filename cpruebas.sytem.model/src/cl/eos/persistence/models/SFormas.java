package cl.eos.persistence.models;

import cl.eos.persistence.AEntity;

public class SFormas extends AEntity {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private Integer forma;
    private String orden;
    private SPrueba prueba;

    public Integer getForma() {
        return forma;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getOrden() {
        return orden;
    }

    public SPrueba getPrueba() {
        return prueba;
    }

    public void setForma(Integer forma) {
        this.forma = forma;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public void setPrueba(SPrueba prueba) {
        this.prueba = prueba;
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
        private Integer forma;
        private String orden;
        private SPrueba prueba;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder forma(Integer forma) {
            this.forma = forma;
            return this;
        }

        public Builder orden(String orden) {
            this.orden = orden;
            return this;
        }

        public Builder prueba(SPrueba prueba) {
            this.prueba = prueba;
            return this;
        }

        public SFormas build() {
            SFormas sFormas = new SFormas();
            sFormas.id = id;
            sFormas.name = name;
            sFormas.forma = forma;
            sFormas.orden = orden;
            sFormas.prueba = prueba;
            return sFormas;
        }
    }
}
