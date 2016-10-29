package cl.eos.persistence.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

import cl.eos.persistence.AEntity;

@Entity(name = "imagenes")
@NamedQueries({
        @NamedQuery(name = "Imagenes.findAll", query = "SELECT i FROM imagenes i order by i.respuesta.prueba.id, i.numero "),
        @NamedQuery(name = "Imagenes.findByPrueba", query = "SELECT i FROM imagenes i where i.respuesta.prueba.id = :idPrueba") })
public class Imagenes extends AEntity {

    public static class Builder {
        private Long id;
        private String name;
        private int numero;
        private RespuestasEsperadasPrueba respuesta;
        private boolean eliminada;
        private int version;

        public Imagenes build() {
            final Imagenes imagenes = new Imagenes();
            imagenes.id = id;
            imagenes.name = name;
            imagenes.numero = numero;
            imagenes.respuesta = respuesta;
            imagenes.eliminada = eliminada;
            imagenes.version = version;
            return imagenes;
        }

        public Builder eliminada(boolean eliminada) {
            this.eliminada = eliminada;
            return this;
        }

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

        public Builder respuesta(RespuestasEsperadasPrueba respuesta) {
            this.respuesta = respuesta;
            return this;
        }

        public Builder version(int version) {
            this.version = version;
            return this;
        }
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private int numero;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private RespuestasEsperadasPrueba respuesta;

    @javax.persistence.Transient
    public boolean eliminada;

    @Version
    private int version;

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

    public RespuestasEsperadasPrueba getRespuesta() {
        return respuesta;
    }

    @Override
    public int getVersion() {
        return version;
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

    public void setRespuesta(RespuestasEsperadasPrueba respuesta) {
        this.respuesta = respuesta;
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
