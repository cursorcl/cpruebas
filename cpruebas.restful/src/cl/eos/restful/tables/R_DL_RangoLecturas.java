package cl.eos.restful.tables;

import com.google.gson.annotations.SerializedName;

import cl.eos.persistence.AEntity;

public class R_DL_RangoLecturas  extends AEntity{
    private static final long serialVersionUID = 1L;
    
    @SerializedName(value = "id", alternate = {"ID"})
    Long id;
    @SerializedName(value = "tipocurso", alternate = {"TIPOCURSO"})
    Long tipocurso;
    @SerializedName(value = "value", alternate = {"VALUE"})
    Integer value;
    @SerializedName(value = "velocidadlectora_id", alternate = {"VELOCIDADLECTORA_ID"})
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

        public R_DL_RangoLecturas build() {
            return new R_DL_RangoLecturas(this);
        }
    }

    private R_DL_RangoLecturas(Builder builder) {
        this.id = builder.id;
        this.tipocurso = builder.tipocurso;
        this.value = builder.value;
        this.velocidadlectora_id = builder.velocidadlectora_id;
        this.version = builder.version;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {
        
    }
}
