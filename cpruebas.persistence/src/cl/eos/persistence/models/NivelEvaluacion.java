package cl.eos.persistence.models;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import cl.eos.persistence.AEntity;

@Entity(name = "nivelevaluacion")
@NamedQueries({ @NamedQuery(name = "NivelEvaluacion.findAll", query = "SELECT e FROM nivelevaluacion e") })
public class NivelEvaluacion extends AEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer nroRangos;
    @OneToMany(mappedBy = "nivelEvaluacion", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Collection<RangoEvaluacion> rangos;

    /**
     * Se crea para el manejo de multiusuarios
     */
    @Version
    protected int version;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public Integer getNroRangos() {
        return nroRangos;
    }

    public RangoEvaluacion getRango(float porcentaje) {
        RangoEvaluacion result = null;
        int n = 0;

        for (final RangoEvaluacion rango : rangos) {
            if (n == 0) {
                if (porcentaje < rango.getMaximo()) {
                    result = rango;
                    break;
                }
            } else if (n == nroRangos - 1) {
                if (porcentaje >= rango.getMinimo()) {
                    result = rango;
                    break;
                }
            } else {
                if (porcentaje >= rango.getMinimo() && porcentaje < rango.getMaximo()) {
                    result = rango;
                    break;
                }
            }
            n++;
        }
        return result;
    }

    public Collection<RangoEvaluacion> getRangos() {
        return rangos;
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

    public void setNroRangos(Integer nroRangos) {
        this.nroRangos = nroRangos;
    }

    public void setRangos(Collection<RangoEvaluacion> rangos) {
        this.rangos = rangos;
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
        return false;
    }

}
