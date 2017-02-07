package cl.eos.restful.tables;

import com.google.gson.annotations.SerializedName;

import cl.eos.persistence.AEntity;

public class R_RespuestasEsperadasPrueba  extends AEntity implements Comparable<R_RespuestasEsperadasPrueba>{
    
    private static final long serialVersionUID = 1L;
    
    @SerializedName(value = "id", alternate = {"ID"})
    Long id;
    @SerializedName(value = "numero", alternate = {"NUMERO"})
    Integer numero;
    @SerializedName(value = "name", alternate = {"NAME"})
    String name;
    @SerializedName(value = "verdaderofalso", alternate = {"VERDADEROFALSO"})
    Boolean verdaderofalso;
    @SerializedName(value = "respuesta", alternate = {"RESPUESTA"})
    String respuesta;
    @SerializedName(value = "mental", alternate = {"MENTAL"})
    Boolean mental;
    @SerializedName(value = "prueba_id", alternate = {"PRUEBA_ID"})
    Long prueba_id;
    @SerializedName(value = "habilidad_id", alternate = {"HABILIDAD_ID"})
    Long habilidad_id;
    @SerializedName(value = "ejetematico_id", alternate = {"EJETEMATICO_ID"})
    Long ejetematico_id;
    @SerializedName(value = "anulada", alternate = {"ANULADA"})
    Boolean anulada;
    @SerializedName(value = "objetivo_id", alternate = {"OBJETIVO_ID"})
    Long objetivo_id;

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
     * @return the numero
     */
    public final Integer getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public final void setNumero(Integer numero) {
        this.numero = numero;
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
     * @return the verdaderofalso
     */
    public final Boolean getVerdaderofalso() {
        return verdaderofalso;
    }

    /**
     * @param verdaderofalso the verdaderofalso to set
     */
    public final void setVerdaderofalso(Boolean verdaderofalso) {
        this.verdaderofalso = verdaderofalso;
    }

    /**
     * @return the respuesta
     */
    public final String getRespuesta() {
        return respuesta;
    }

    /**
     * @param respuesta the respuesta to set
     */
    public final void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    /**
     * @return the mental
     */
    public final Boolean getMental() {
        return mental;
    }

    /**
     * @param mental the mental to set
     */
    public final void setMental(Boolean mental) {
        this.mental = mental;
    }

    /**
     * @return the prueba_id
     */
    public final Long getPrueba_id() {
        return prueba_id;
    }

    /**
     * @param prueba_id the prueba_id to set
     */
    public final void setPrueba_id(Long prueba_id) {
        this.prueba_id = prueba_id;
    }

    /**
     * @return the habilidad_id
     */
    public final Long getHabilidad_id() {
        return habilidad_id;
    }

    /**
     * @param habilidad_id the habilidad_id to set
     */
    public final void setHabilidad_id(Long habilidad_id) {
        this.habilidad_id = habilidad_id;
    }

    /**
     * @return the ejetematico_id
     */
    public final Long getEjetematico_id() {
        return ejetematico_id;
    }

    /**
     * @param ejetematico_id the ejetematico_id to set
     */
    public final void setEjetematico_id(Long ejetematico_id) {
        this.ejetematico_id = ejetematico_id;
    }

    /**
     * @return the anulada
     */
    public final Boolean getAnulada() {
        return anulada;
    }

    /**
     * @param anulada the anulada to set
     */
    public final void setAnulada(Boolean anulada) {
        this.anulada = anulada;
    }

    /**
     * @return the objetivo_id
     */
    public final Long getObjetivo_id() {
        return objetivo_id;
    }

    /**
     * @param objetivo_id the objetivo_id to set
     */
    public final void setObjetivo_id(Long objetivo_id) {
        this.objetivo_id = objetivo_id;
    }

    public static class Builder {
        private Long id;
        private Integer numero;
        private String name;
        private Boolean verdaderofalso;
        private String respuesta;
        private Boolean mental;
        private Long prueba_id;
        private Long habilidad_id;
        private Long ejetematico_id;
        private Boolean anulada;
        private Long objetivo_id;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder numero(Integer numero) {
            this.numero = numero;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder verdaderofalso(Boolean verdaderofalso) {
            this.verdaderofalso = verdaderofalso;
            return this;
        }

        public Builder respuesta(String respuesta) {
            this.respuesta = respuesta;
            return this;
        }

        public Builder mental(Boolean mental) {
            this.mental = mental;
            return this;
        }

        public Builder prueba_id(Long prueba_id) {
            this.prueba_id = prueba_id;
            return this;
        }

        public Builder habilidad_id(Long habilidad_id) {
            this.habilidad_id = habilidad_id;
            return this;
        }

        public Builder ejetematico_id(Long ejetematico_id) {
            this.ejetematico_id = ejetematico_id;
            return this;
        }

        public Builder anulada(Boolean anulada) {
            this.anulada = anulada;
            return this;
        }


        public Builder objetivo_id(Long objetivo_id) {
            this.objetivo_id = objetivo_id;
            return this;
        }

        public R_RespuestasEsperadasPrueba build() {
            return new R_RespuestasEsperadasPrueba(this);
        }
    }

    private R_RespuestasEsperadasPrueba(Builder builder) {
        this.id = builder.id;
        this.numero = builder.numero;
        this.name = builder.name;
        this.verdaderofalso = builder.verdaderofalso;
        this.respuesta = builder.respuesta;
        this.mental = builder.mental;
        this.prueba_id = builder.prueba_id;
        this.habilidad_id = builder.habilidad_id;
        this.ejetematico_id = builder.ejetematico_id;
        this.anulada = builder.anulada;
        this.objetivo_id = builder.objetivo_id;
    }

    @Override
    public int compareTo(R_RespuestasEsperadasPrueba other) {
      return this.numero - other.numero;
    }
}
