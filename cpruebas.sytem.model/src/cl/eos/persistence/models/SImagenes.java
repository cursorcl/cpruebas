package cl.eos.persistence.models;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import cl.eos.persistence.AEntity;

public class SImagenes extends AEntity {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private int numero;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private SRespuestasEsperadasPrueba respuesta;

    @javax.persistence.Transient
    public boolean eliminada;

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
    }

    public void setRespuesta(SRespuestasEsperadasPrueba respuesta) {
        this.respuesta = respuesta;
    }

    @Override
    public boolean validate() {
        return true;
    }

    public static class Builder {
        private Long id;
        private String name;
        private int numero;
        private SRespuestasEsperadasPrueba respuesta;
        private boolean eliminada;

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

        public Builder respuesta(SRespuestasEsperadasPrueba respuesta) {
            this.respuesta = respuesta;
            return this;
        }

        public Builder eliminada(boolean eliminada) {
            this.eliminada = eliminada;
            return this;
        }

        public SImagenes build() {
            SImagenes sImagenes = new SImagenes();
            sImagenes.id = id;
            sImagenes.name = name;
            sImagenes.numero = numero;
            sImagenes.respuesta = respuesta;
            sImagenes.eliminada = eliminada;
            return sImagenes;
        }
    }
}
