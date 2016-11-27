package cl.eos.restful.tables;

import cl.eos.persistence.AEntity;

public class DL_RangoLecturas  extends AEntity{
    private static final long serialVersionUID = 1L;
    
    Long id;
    Long tipocurso;
    Integer value;
    Long velocidadlectora_id;

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
     * @return the tipocurso
     */
    public final Long getTipocurso() {
        return tipocurso;
    }

    /**
     * @param tipocurso the tipocurso to set
     */
    public final void setTipocurso(Long tipocurso) {
        this.tipocurso = tipocurso;
    }

    /**
     * @return the value
     */
    public final Integer getValue() {
        return value;
    }

    /**
     * @param velocidadlectora_id the velocidadlectora_id to set
     */
    public final void setVelocidadlectora_id(Long velocidadlectora_id) {
        this.velocidadlectora_id = velocidadlectora_id;
    }

    /**
     * @return the version
     */
    public final Integer getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public final void setVersion(Integer version) {
        this.version = version;
    }

    public static class Builder {
        private Long id;
        private Long tipocurso;
        private Integer value;
        private Long velocidadlectora_id;
        private Integer version;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder tipocurso(Long tipocurso) {
            this.tipocurso = tipocurso;
            return this;
        }

        public Builder value(Integer value) {
            this.value = value;
            return this;
        }

        public Builder velocidadlectora_id(Long velocidadlectora_id) {
            this.velocidadlectora_id = velocidadlectora_id;
            return this;
        }

        public Builder version(Integer version) {
            this.version = version;
            return this;
        }

        public DL_RangoLecturas build() {
            return new DL_RangoLecturas(this);
        }
    }

    private DL_RangoLecturas(Builder builder) {
        this.id = builder.id;
        this.tipocurso = builder.tipocurso;
        this.value = builder.value;
        this.velocidadlectora_id = builder.velocidadlectora_id;
        this.version = builder.version;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setName(String name) {
        // TODO Auto-generated method stub
        
    }
}
