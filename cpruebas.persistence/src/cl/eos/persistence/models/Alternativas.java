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
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;

import cl.eos.persistence.AEntity;

@Entity(name = "alternativas")
@Cache(type = CacheType.NONE, size = 64000,
        expiry = 360000, 
        coordinationType = CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS                                                                            // messages.
)
@NamedQueries({
        @NamedQuery(name = "Alternativas.findAll", query = "SELECT a FROM alternativas a order by a.respuesta.prueba.id, a.numero"),
        @NamedQuery(name = "Alternativas.findByPrueba", query = "SELECT a FROM alternativas a where a.respuesta.prueba.id = :idPrueba") })
public class Alternativas extends AEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int numero;
    private String texto;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private RespuestasEsperadasPrueba respuesta;

    @Version
    private int version;

    @javax.persistence.Transient
    public boolean eliminada;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
        this.name = String.format("%d", numero);
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    public RespuestasEsperadasPrueba getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(RespuestasEsperadasPrueba respuesta) {
        this.respuesta = respuesta;
    }

    @PreUpdate
    public void detachFromPrueba() {
        if (this.eliminada) {
            // prueba.getLstAlternativas().remove(this);
            // prueba = null;
        }
    }

    public static class Builder {
        private Long id;
        private String name;
        private int numero;
        private String texto;
        private RespuestasEsperadasPrueba respuesta;
        private int version;
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

        public Builder texto(String texto) {
            this.texto = texto;
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

        public Builder eliminada(boolean eliminada) {
            this.eliminada = eliminada;
            return this;
        }

        public Alternativas build() {
            Alternativas alternativas = new Alternativas();
            alternativas.id = id;
            alternativas.name = name;
            alternativas.numero = numero;
            alternativas.texto = texto;
            alternativas.respuesta = respuesta;
            alternativas.version = version;
            alternativas.eliminada = eliminada;
            return alternativas;
        }
    }
}
