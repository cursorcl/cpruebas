package cl.eos.restful.tables;

import com.google.gson.annotations.SerializedName;

import cl.eos.persistence.AEntity;

public class R_Asignatura extends AEntity {
    
    private static final long serialVersionUID = 1L;
    @SerializedName(value = "id", alternate = {"ID"})
    Long id;
    @SerializedName(value = "name", alternate = {"NAME"})
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
    
    

    @Override
    public String toString() {
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

        public R_Asignatura build() {
            return new R_Asignatura(this);
        }
    }

    private R_Asignatura(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.version = builder.version;
    }
}
