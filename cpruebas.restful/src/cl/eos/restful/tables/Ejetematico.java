package cl.eos.restful.tables;

import cl.eos.persistence.AEntity;

public class Ejetematico  extends AEntity{
    private static final long serialVersionUID = 1L;
    Long id;
    String name;
    Long asignatura_id;
    Long tipoprueba_id;

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
     * @return the asignatura_id
     */
    public final Long getAsignatura_id() {
        return asignatura_id;
    }

    /**
     * @param asignatura_id the asignatura_id to set
     */
    public final void setAsignatura_id(Long asignatura_id) {
        this.asignatura_id = asignatura_id;
    }

    /**
     * @return the tipoprueba_id
     */
    public final Long getTipoprueba_id() {
        return tipoprueba_id;
    }

    /**
     * @param tipoprueba_id the tipoprueba_id to set
     */
    public final void setTipoprueba_id(Long tipoprueba_id) {
        this.tipoprueba_id = tipoprueba_id;
    }

    public static class Builder {
        private Long id;
        private String name;
        private Long asignatura_id;
        private Long tipoprueba_id;
        private Integer version;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder asignatura_id(Long asignatura_id) {
            this.asignatura_id = asignatura_id;
            return this;
        }

        public Builder tipoprueba_id(Long tipoprueba_id) {
            this.tipoprueba_id = tipoprueba_id;
            return this;
        }

        public Builder version(Integer version) {
            this.version = version;
            return this;
        }

        public Ejetematico build() {
            return new Ejetematico(this);
        }
    }

    private Ejetematico(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.asignatura_id = builder.asignatura_id;
        this.tipoprueba_id = builder.tipoprueba_id;
        this.version = builder.version;
    }
}
