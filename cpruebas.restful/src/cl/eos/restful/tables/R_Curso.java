package cl.eos.restful.tables;

import com.google.gson.annotations.SerializedName;

import cl.eos.persistence.AEntity;

public class R_Curso extends AEntity{

    private static final long serialVersionUID = 1L;
    
    @SerializedName(value = "id", alternate = {"ID"})
    Long id;
    @SerializedName(value = "name", alternate = {"NAME"})
    String name;
    @SerializedName(value = "tipocurso_id", alternate = {"TIPOCURSO_ID"})
    Long tipocurso_id;
    @SerializedName(value = "ciclo_id", alternate = {"CICLO_ID"})
    Long ciclo_id;
    @SerializedName(value = "colegio_id", alternate = {"COLEGIO_ID"})
    Long colegio_id;

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
     * @return the ciclo_id
     */
    public final Long getCiclo_id() {
        return ciclo_id;
    }

    /**
     * @param ciclo_id the ciclo_id to set
     */
    public final void setCiclo_id(Long ciclo_id) {
        this.ciclo_id = ciclo_id;
    }

    /**
     * @return the colegio_id
     */
    public final Long getColegio_id() {
        return colegio_id;
    }

    /**
     * @param colegio_id the colegio_id to set
     */
    public final void setColegio_id(Long colegio_id) {
        this.colegio_id = colegio_id;
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
      R_Curso other = (R_Curso) obj;
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
        private Long tipocurso_id;
        private Long ciclo_id;
        private Long colegio_id;

        public Builder id(Long id) {
            this.id = id;
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

        public Builder ciclo_id(Long ciclo_id) {
            this.ciclo_id = ciclo_id;
            return this;
        }

        public Builder colegio_id(Long colegio_id) {
            this.colegio_id = colegio_id;
            return this;
        }

        public R_Curso build() {
            return new R_Curso(this);
        }
    }

    private R_Curso(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.tipocurso_id = builder.tipocurso_id;
        this.ciclo_id = builder.ciclo_id;
        this.colegio_id = builder.colegio_id;
    }
}
