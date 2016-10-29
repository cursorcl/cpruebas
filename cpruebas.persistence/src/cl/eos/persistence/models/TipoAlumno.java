package cl.eos.persistence.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;

import cl.eos.persistence.AEntity;

@Entity(name = "tipoalumno")
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
@NamedQueries({ @NamedQuery(name = "TipoAlumno.findAll", query = "SELECT e FROM tipoalumno e order by e.id") })
public class TipoAlumno extends AEntity {

    private static final long serialVersionUID = -5362288224985235697L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /**
     * Se crea para el manejo de multiusuarios
     */
    @Version
    protected int version;

    public TipoAlumno() {
        // Creado para que pueda ser instanciado por le sistema.
    }

    public TipoAlumno(Long id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public final int getVersion() {
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
