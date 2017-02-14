package cl.eos.restful.tables;

import com.google.gson.annotations.SerializedName;

import cl.eos.persistence.AEntity;

public class R_Objetivo  extends AEntity{
    private static final long serialVersionUID = 1L;
    @SerializedName(value = "id", alternate = {"ID"})
    Long id;
    @SerializedName(value = "name", alternate = {"NAME"})
    String name;
    @SerializedName(value = "descripcion", alternate = {"DESCRIPCION"})
    String descripcion;
    @SerializedName(value = "tipocurso_id", alternate = {"TIPOCURSO_ID"})
    Long tipocurso_id;
    @SerializedName(value = "asignatura_id", alternate = {"ASIGNATURA_ID"})
    Long asignatura_id;

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
     * @return the tipocurso_id
     */
    public final Long getTipocurso_id() {
        return tipocurso_id;
    }

    /**
     * @param tipocurso_id the tipocurso_id to set
     */
    public final void setTipocurso_id(Long tipocurso_id) {
        this.tipocurso_id = tipocurso_id;
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
      R_Objetivo other = (R_Objetivo) obj;
      if (id == null) {
        if (other.id != null)
          return false;
      } else if (!id.equals(other.id))
        return false;
      return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
      return name;
    }



    public static class Builder {
        private Long id;
        private String descripcion;
        private String name;
        private Long tipocurso_id;
        private Long asignatura_id;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder descripcion(String descripcion) {
            this.descripcion = descripcion;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }


        public Builder tipocurso_id(Long tipocurso_id) {
            this.tipocurso_id = tipocurso_id;
            return this;
        }

        public Builder asignatura_id(Long asignatura_id) {
            this.asignatura_id = asignatura_id;
            return this;
        }

        public R_Objetivo build() {
            return new R_Objetivo(this);
        }
    }

    private R_Objetivo(Builder builder) {
        this.id = builder.id;
        this.descripcion = builder.descripcion;
        this.name = builder.name;
        this.tipocurso_id = builder.tipocurso_id;
        this.asignatura_id = builder.asignatura_id;
    }
}
