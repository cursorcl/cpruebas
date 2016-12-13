package cl.eos.restful.tables;

import cl.eos.persistence.AEntity;

public class R_TipoAlumno  extends AEntity{
    
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
    
    @Override
    public void setName(String name) {
        this.name =   name;
    }

    /**
     * @return the name
     */
    public final String getName() {
        return name;
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

        public R_TipoAlumno build() {
            return new R_TipoAlumno(this);
        }
    }

    private R_TipoAlumno(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.version = builder.version;
    }
}
