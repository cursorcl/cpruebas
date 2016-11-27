package cl.eos.restful.tables;

import cl.eos.persistence.AEntity;

public class RangoEvaluacion  extends AEntity{
    
    private static final long serialVersionUID = 1L;
    
    Long id;
    String abreviacion;
    float minimo;
    String name;
    float maximo;
    Long nivelevaluacion_id;

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
     * @return the abreviacion
     */
    public final String getAbreviacion() {
        return abreviacion;
    }

    /**
     * @param abreviacion the abreviacion to set
     */
    public final void setAbreviacion(String abreviacion) {
        this.abreviacion = abreviacion;
    }

    /**
     * @return the minimo
     */
    public final float getMinimo() {
        return minimo;
    }

    /**
     * @param minimo the minimo to set
     */
    public final void setMinimo(float minimo) {
        this.minimo = minimo;
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
     * @return the maximo
     */
    public final float getMaximo() {
        return maximo;
    }

    /**
     * @param maximo the maximo to set
     */
    public final void setMaximo(float maximo) {
        this.maximo = maximo;
    }

    /**
     * @return the nivelevaluacion_id
     */
    public final Long getNivelevaluacion_id() {
        return nivelevaluacion_id;
    }

    /**
     * @param nivelevaluacion_id the nivelevaluacion_id to set
     */
    public final void setNivelevaluacion_id(Long nivelevaluacion_id) {
        this.nivelevaluacion_id = nivelevaluacion_id;
    }

    public static class Builder {
        private Long id;
        private String abreviacion;
        private float minimo;
        private String name;
        private float maximo;
        private Integer version;
        private Long nivelevaluacion_id;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder abreviacion(String abreviacion) {
            this.abreviacion = abreviacion;
            return this;
        }

        public Builder minimo(float minimo) {
            this.minimo = minimo;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder maximo(float maximo) {
            this.maximo = maximo;
            return this;
        }

        public Builder version(Integer version) {
            this.version = version;
            return this;
        }

        public Builder nivelevaluacion_id(Long nivelevaluacion_id) {
            this.nivelevaluacion_id = nivelevaluacion_id;
            return this;
        }

        public RangoEvaluacion build() {
            return new RangoEvaluacion(this);
        }
    }

    private RangoEvaluacion(Builder builder) {
        this.id = builder.id;
        this.abreviacion = builder.abreviacion;
        this.minimo = builder.minimo;
        this.name = builder.name;
        this.maximo = builder.maximo;
        this.version = builder.version;
        this.nivelevaluacion_id = builder.nivelevaluacion_id;
    }
}
