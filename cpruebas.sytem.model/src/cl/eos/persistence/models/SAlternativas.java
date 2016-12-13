package cl.eos.persistence.models;

import cl.eos.persistence.AEntity;

public class SAlternativas extends AEntity {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private int numero;
    private String texto;

    private SRespuestasEsperadasPrueba respuesta;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getNumero() {
        return numero;
    }

    public SRespuestasEsperadasPrueba getRespuesta() {
        return respuesta;
    }

    public String getTexto() {
        return texto;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setNumero(int numero) {
        this.numero = numero;
        name = String.format("%d", numero);
    }

    public void setRespuesta(SRespuestasEsperadasPrueba respuesta) {
        this.respuesta = respuesta;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public boolean validate() {
        return true;
    }

    public static class Builder {
        private Long id;
        private String name;
        private int numero;
        private String texto;
        private SRespuestasEsperadasPrueba respuesta;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder numero(int numero) {
            this.numero = numero;
            return this;
        }

        public Builder texto(String texto) {
            this.texto = texto;
            return this;
        }

        public Builder respuesta(SRespuestasEsperadasPrueba respuesta) {
            this.respuesta = respuesta;
            return this;
        }

        public SAlternativas build() {
            SAlternativas sAlternativas = new SAlternativas();
            sAlternativas.id = id;
            sAlternativas.name = name;
            sAlternativas.numero = numero;
            sAlternativas.texto = texto;
            sAlternativas.respuesta = respuesta;
            return sAlternativas;
        }
    }
}
