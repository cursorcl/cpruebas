package cl.eos.restful.tables;

import cl.eos.persistence.AEntity;

public class EvaluacionEjetematico  extends AEntity{
    private static final long serialVersionUID = 1L;
    Long id;
    String name;
    float nrorangomin;
    float nrorangomax;

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
     * @return the nrorangomin
     */
    public final float getNrorangomin() {
        return nrorangomin;
    }

    /**
     * @param nrorangomin the nrorangomin to set
     */
    public final void setNrorangomin(float nrorangomin) {
        this.nrorangomin = nrorangomin;
    }

    /**
     * @return the nrorangomax
     */
    public final float getNrorangomax() {
        return nrorangomax;
    }

    /**
     * @param nrorangomax the nrorangomax to set
     */
    public final void setNrorangomax(float nrorangomax) {
        this.nrorangomax = nrorangomax;
    }


    public static class Builder {
        private Long id;
        private String name;
        private float nrorangomin;
        private float nrorangomax;
        private Integer version;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder nrorangomin(float nrorangomin) {
            this.nrorangomin = nrorangomin;
            return this;
        }

        public Builder nrorangomax(float nrorangomax) {
            this.nrorangomax = nrorangomax;
            return this;
        }

        public Builder version(Integer version) {
            this.version = version;
            return this;
        }

        public EvaluacionEjetematico build() {
            return new EvaluacionEjetematico(this);
        }
    }

    private EvaluacionEjetematico(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.nrorangomin = builder.nrorangomin;
        this.nrorangomax = builder.nrorangomax;
        this.version = builder.version;
    }
}
