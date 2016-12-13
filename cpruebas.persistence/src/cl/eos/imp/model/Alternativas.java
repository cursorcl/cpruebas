package cl.eos.imp.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;

import cl.eos.persistence.AEntity;

@Entity(name = "alternativas")
@Cache(type = CacheType.NONE, size = 64000, expiry = 360000, coordinationType = CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS // messages.
)
@NamedQueries({
        @NamedQuery(name = "R_Alternativas.findAll", query = "SELECT a FROM alternativas a order by a.respuesta.prueba.id, a.numero"),
        @NamedQuery(name = "R_Alternativas.findByPrueba", query = "SELECT a FROM alternativas a where a.respuesta.prueba.id = :idPrueba") })
public class Alternativas extends AEntity {


    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int numero;

    private String texto;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private RespuestasEsperadasPrueba respuesta;

    
    
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

    public void setRespuesta(RespuestasEsperadasPrueba respuesta) {
        this.respuesta = respuesta;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }


    @Override
    public boolean validate() {
        return true;
    }
}
