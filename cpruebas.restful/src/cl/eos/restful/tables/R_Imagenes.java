package cl.eos.restful.tables;

import com.google.gson.annotations.SerializedName;

import cl.eos.persistence.AEntity;

public class R_Imagenes  extends AEntity{
    private static final long serialVersionUID = 1L;
    @SerializedName(value = "id", alternate = {"ID"})
    Long id;
    @SerializedName(value = "name", alternate = {"NAME"})
    String name;
    @SerializedName(value = "numero", alternate = {"NUMERO"})
    Integer numero;
    @SerializedName(value = "respuesta_id", alternate = {"RESPUESTA_ID"})
    Long respuesta_id;
    @SerializedName(value = "image", alternate = {"IMAGE"})
    String image;

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
     * @return the respuesta_id
     */
    public final Long getRespuesta_id() {
        return respuesta_id;
    }

    /**
     * @param respuesta_id the respuesta_id to set
     */
    public final void setRespuesta_id(Long respuesta_id) {
        this.respuesta_id = respuesta_id;
    }

    /**
     * @return the image
     */
    public final String getImage() {
      return image;
    }

    /**
     * @param image the image to set
     */
    public final void setImage(String image) {
      this.image = image;
    }

    public static class Builder {
        private Long id;
        private String name;
        private Integer numero;
        private Long respuesta_id;
        private String image;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder numero(Integer numero) {
            this.numero = numero;
            return this;
        }


        public Builder respuesta_id(Long respuesta_id) {
            this.respuesta_id = respuesta_id;
            return this;
        }
        
        public Builder image (String image)
        {
          this.image =  image;
          return this;
        }

        public R_Imagenes build() {
            return new R_Imagenes(this);
        }
    }

    private R_Imagenes(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.numero = builder.numero;
        this.respuesta_id = builder.respuesta_id;
        this.image =  builder.image;
    }
}
