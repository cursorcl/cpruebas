package cl.eos.restful.tables;

import com.google.gson.annotations.SerializedName;

import cl.eos.persistence.AEntity;

public class R_Habilidad  extends AEntity{
    
    private static final long serialVersionUID = 1L;
    @SerializedName(value = "id", alternate = {"ID"})
    Long id;
    @SerializedName(value = "name", alternate = {"NAME"})
    String name;
    @SerializedName(value = "descripcion", alternate = {"DESCRIPCION"})
    String descripcion;

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
     * @return the descripcion
     */
    public final String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public final void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
      return name;
    }



    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      R_Habilidad other = (R_Habilidad) obj;
      if (id == null) {
        if (other.id != null)
          return false;
      } else if (!id.equals(other.id))
        return false;
      return true;
    }



    public static class Builder {
        private Long id;
        private String name;
        private String descripcion;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder descripcion(String descripcion) {
            this.descripcion = descripcion;
            return this;
        }


        public R_Habilidad build() {
            return new R_Habilidad(this);
        }
    }

    private R_Habilidad(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.descripcion = builder.descripcion;
    }
}
