package cl.eos.restful.tables;

import com.google.gson.annotations.SerializedName;

import cl.eos.persistence.AEntity;

public class R_RangoEvaluacion  extends AEntity{
    
    private static final long serialVersionUID = 1L;
    
    @SerializedName(value = "id", alternate = {"ID"})
    Long id;
    @SerializedName(value = "abreviacion", alternate = {"ABREVIACION"})
    String abreviacion;
    @SerializedName(value = "minimo", alternate = {"MINIMO"})
    float minimo;
    @SerializedName(value = "name", alternate = {"NAME"})
    String name;
    @SerializedName(value = "maximo", alternate = {"MAXIMO"})
    float maximo;
    @SerializedName(value = "nivelevaluacion_id", alternate = {"NIVELEVALUACION_ID"})
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


        public Builder nivelevaluacion_id(Long nivelevaluacion_id) {
            this.nivelevaluacion_id = nivelevaluacion_id;
            return this;
        }

        public R_RangoEvaluacion build() {
            return new R_RangoEvaluacion(this);
        }
    }

    private R_RangoEvaluacion(Builder builder) {
        this.id = builder.id;
        this.abreviacion = builder.abreviacion;
        this.minimo = builder.minimo;
        this.name = builder.name;
        this.maximo = builder.maximo;
        this.nivelevaluacion_id = builder.nivelevaluacion_id;
    }
    
    /**
     * Indica si el valor esta dentro del rango.
     * @param porcentaje Porcentaje de logro
     * @return Verdadero si esta dentro.
     */
    public boolean isInside(float porcentaje)
    {
      boolean res = false;
      if(porcentaje > getMinimo() && porcentaje <= getMaximo() )
      {
          res = true;
      }
      else if(getMinimo() == 0 && porcentaje == 0)
      {
        res = true;
      }
      return res;
    }
}
