package cl.eos.imp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import cl.eos.persistence.AEntity;

@Entity(name = "objetivo")
@NamedQueries({ @NamedQuery(name = "R_Objetivo.findAll", query = "SELECT e FROM objetivo e order by e.name"),
        @NamedQuery(name = "R_Objetivo.findByTipoCurso", query = "SELECT e FROM objetivo e where e.tipoCurso.id = :tipoCursoId order by e.name "),
        @NamedQuery(name = "R_Objetivo.findByTipoCursoAsignatura", query = "SELECT e FROM objetivo e where e.tipoCurso.id = :tipoCursoId and e.asignatura.id = :asignaturaId order by e.name ") })
public class Objetivo extends AEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Lob
    private String descripcion;

    @ManyToOne
    private TipoCurso tipoCurso;

    @ManyToOne
    private Asignatura asignatura;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Objetivo other = (Objetivo) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }

    public String getDescripcion() {
        return descripcion;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }

    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
    public String toString() {
        return name;
    }

    @Override
    public boolean validate() {
        return true;
    }

}
