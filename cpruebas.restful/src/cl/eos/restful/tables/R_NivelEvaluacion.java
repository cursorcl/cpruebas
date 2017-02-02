package cl.eos.restful.tables;

import com.google.gson.annotations.SerializedName;

import cl.eos.persistence.AEntity;

public class R_NivelEvaluacion  extends AEntity{
    
    private static final long serialVersionUID = 1L;
   
    @SerializedName(value = "id", alternate = {"ID"})
    Long id;
    @SerializedName(value = "name", alternate = {"NAME"})
    String name;
    @SerializedName(value = "nrorangos", alternate = {"NRORANGOS"})
    Integer nrorangos;

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
     * @return the nrorangos
     */
    public final Integer getNrorangos() {
        return nrorangos;
    }

    /**
     * @param nrorangos the nrorangos to set
     */
    public final void setNrorangos(Integer nrorangos) {
        this.nrorangos = nrorangos;
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
        private String name;
        private Integer nrorangos;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder nrorangos(Integer nrorangos) {
            this.nrorangos = nrorangos;
            return this;
        }


        public R_NivelEvaluacion build() {
            return new R_NivelEvaluacion(this);
        }
    }

    private R_NivelEvaluacion(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.nrorangos = builder.nrorangos;
    }
}
