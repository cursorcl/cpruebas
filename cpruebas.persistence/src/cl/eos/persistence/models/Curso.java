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

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;

import cl.eos.persistence.AEntity;

@Entity(name = "curso")
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
@NamedQueries({ @NamedQuery(name = "Curso.findAll", query = "SELECT e FROM curso e order by e.name"),
        @NamedQuery(name = "Curso.findByTipo", query = "SELECT e FROM curso e where e.tipoCurso.id = :tcursoId"),
        @NamedQuery(name = "Curso.findByColegio", query = "SELECT e FROM curso e where e.colegio.id = :colegioId"),
        @NamedQuery(name = "Curso.findByTipoColegio", query = "SELECT e FROM curso e where e.colegio.id = :colegioId and e.tipoCurso.id = :tcursoId") })
public class Curso extends AEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Ciclo ciclo;
    private Colegio colegio;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Collection<Alumno> alumnos;
    private TipoCurso tipoCurso;

    /**
     * Se crea para el manejo de multiusuarios
     */
    @Version
    protected int version;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Curso other = (Curso) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public Collection<Alumno> getAlumnos() {
        return alumnos;
    }

    public Ciclo getCiclo() {
        return ciclo;
    }

    public Colegio getColegio() {
        return colegio;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public TipoCurso getTipoCurso() {
        return tipoCurso;
    }

    @Override
    public final int getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }

    public void setAlumnos(Collection<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    public void setCiclo(Ciclo ciclo) {
        this.ciclo = ciclo;
    }

    public void setColegio(Colegio colegio) {
        this.colegio = colegio;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setTipoCurso(TipoCurso tipoCurso) {
        this.tipoCurso = tipoCurso;
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
