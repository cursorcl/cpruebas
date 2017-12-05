package cl.eos.restful.tables;

import cl.eos.persistence.AEntity;

public class Alternativas extends AEntity{

    private static final long serialVersionUID = 1L;
    Long id;
    String name;
    Integer numero;
    String texto;
    Long respuesta_id;

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
     * @return the texto
     */
    public final String getTexto() {
        return texto;
    }

    /**
     * @param texto the texto to set
     */
    public final void setTexto(String texto) {
        this.texto = texto;
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

    public static class Builder {
        private Long id;
        private String name;
        private Integer numero;
        private String texto;
        private Integer version;
        private Long respuesta_id;

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

        public Builder texto(String texto) {
            this.texto = texto;
            return this;
        }

        public Builder version(Integer version) {
            this.version = version;
            return this;
        }

        public Builder respuesta_id(Long respuesta_id) {
            this.respuesta_id = respuesta_id;
            return this;
        }

        public Alternativas build() {
            return new Alternativas(this);
        }
    }

    private Alternativas(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.numero = builder.numero;
        this.texto = builder.texto;
        this.version = builder.version;
        this.respuesta_id = builder.respuesta_id;
    }

}