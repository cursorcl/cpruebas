package cl.eos.restful.tables;

import cl.eos.persistence.AEntity;

public class TipoColegio  extends AEntity{
    
    private static final long serialVersionUID = 1L;
    
    Long id;
    String name;

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

    public static class Builder {
        private Long id;
        private String name;
        private Integer version;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder version(Integer version) {
            this.version = version;
            return this;
        }

        public TipoColegio build() {
            return new TipoColegio(this);
        }
    }

    private TipoColegio(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.version = builder.version;
    }
}
