package cl.eos.restful.tables;

import cl.eos.persistence.AEntity;

public class R_Formas  extends AEntity{
    
    private static final long serialVersionUID = 1L;
    
    Long id;
    Integer forma;
    String orden;
    String name;
    Long prueba_id;

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
     * @return the forma
     */
    public final Integer getForma() {
        return forma;
    }

    /**
     * @param forma the forma to set
     */
    public final void setForma(Integer forma) {
        this.forma = forma;
    }

    /**
     * @return the orden
     */
    public final String getOrden() {
        return orden;
    }

    /**
     * @param orden the orden to set
     */
    public final void setOrden(String orden) {
        this.orden = orden;
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
     * @return the prueba_id
     */
    public final Long getPrueba_id() {
        return prueba_id;
    }

    /**
     * @param prueba_id the prueba_id to set
     */
    public final void setPrueba_id(Long prueba_id) {
        this.prueba_id = prueba_id;
    }

    public static class Builder {
        private Long id;
        private Integer forma;
        private String orden;
        private String name;
        private Long prueba_id;
        private Integer version;

        public Builder id(Long id) {
            this.id = id;
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

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder prueba_id(Long prueba_id) {
            this.prueba_id = prueba_id;
            return this;
        }

        public Builder version(Integer version) {
            this.version = version;
            return this;
        }

        public R_Formas build() {
            return new R_Formas(this);
        }
    }

    private R_Formas(Builder builder) {
        this.id = builder.id;
        this.forma = builder.forma;
        this.orden = builder.orden;
        this.name = builder.name;
        this.prueba_id = builder.prueba_id;
        this.version = builder.version;
    }
}
