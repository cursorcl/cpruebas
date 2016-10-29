package cl.eos.persistence.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;

import cl.eos.persistence.AEntity;

@Entity(name = "formas")
@Cache(type = CacheType.NONE, size = 64000, // Use 64,000 as the initial cache
                                            // size.
        expiry = 360000, // 6 minutes
        coordinationType = CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS // if
                                                                            // cache
                                                                            // coordination
                                                                            // is
                                                                            // used,
                                                                            // only
                                                                            // send
                                                                            // invalidation
                                                                            // messages.
)
@NamedQueries({ @NamedQuery(name = "Formas.findAll", query = "SELECT e FROM formas e"),
        @NamedQuery(name = "Formas.findByPrueba", query = "SELECT e FROM formas e WHERE e.prueba.id = :pruebaId"),
        @NamedQuery(name = "Formas.deleteByPrueba", query = "DELETE FROM formas e WHERE e.prueba.id = :pruebaId") })

public class Formas extends AEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    private Integer forma;
    /**
     * Indica el orden en que se imprimieron las preguntas.
     */
    private String orden;

    @ManyToOne(fetch = FetchType.LAZY)
    private Prueba prueba;

    /**
     * Se crea para el manejo de multiusuarios
     */
    @Version
    protected int version;

    public Integer getForma() {
        return forma;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getOrden() {
        return orden;
    }

    public Prueba getPrueba() {
        return prueba;
    }

    @Override
    public final int getVersion() {
        return version;
    }

    public void setForma(Integer forma) {
        this.forma = forma;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public void setPrueba(Prueba prueba) {
        this.prueba = prueba;
    }

    @Override
    public final void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean validate() {
        return true;
    }

}
