package cl.eos.persistence.models;

import javax.persistence.Entity;
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

@Entity(name = "alumno")
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
@NamedQueries({
        @NamedQuery(name = "Alumno.findAll", query = "SELECT e FROM alumno e order by e.colegio.name, e.curso.name, e.paterno, e.materno, e.name") })
public class Alumno extends AEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String rut;
    private String name;
    private String paterno;
    private String materno;
    private String direccion;
    private Colegio colegio;

    @ManyToOne
    private TipoAlumno tipoAlumno;

    @ManyToOne
    private Curso curso;


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Alumno other = (Alumno) obj;
        if (rut == null) {
            if (other.rut != null)
                return false;
        } else if (!rut.equals(other.rut))
            return false;
        return true;
    }

    public Colegio getColegio() {
        return colegio;
    }

    public Curso getCurso() {
        return curso;
    }

    public String getDireccion() {
        return direccion;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getMaterno() {
        return materno;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getPaterno() {
        return paterno;
    }

    public String getRut() {
        return rut;
    }

    public final TipoAlumno getTipoAlumno() {
        return tipoAlumno;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (rut == null ? 0 : rut.hashCode());
        return result;
    }

    public void setColegio(Colegio colegio) {
        this.colegio = colegio;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public final void setTipoAlumno(TipoAlumno tipoAlumno) {
        this.tipoAlumno = tipoAlumno;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", paterno, materno, name);
    }

    @Override
    public boolean validate() {
        return true;
    }

}
