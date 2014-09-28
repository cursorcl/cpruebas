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

import cl.eos.interfaces.entity.IEntity;

@Entity(name = "curso")
@NamedQueries({
		@NamedQuery(name = "Curso.findAll", query = "SELECT e FROM curso e"),
		@NamedQuery(name = "Curso.findByTipo", query = "SELECT e FROM curso e where e.tipoCurso.id = :tcursoId"),
		@NamedQuery(name = "Curso.findByColegio", query = "SELECT e FROM curso e where e.colegio.id = :coelgioId"),
		@NamedQuery(name = "Curso.findByTipoColegio", query = "SELECT e FROM curso e where e.colegio.id = :colegioId and e.tipoCurso.id = :tcursoId")
		})
public class Curso implements IEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private Ciclo ciclo;
	private Colegio colegio;

	@OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	private Collection<Alumno> alumnos;
	private TipoCurso tipoCurso;

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

	@Override
	public boolean validate() {
		return true;
	}

	public Ciclo getCiclo() {
		return ciclo;
	}

	public void setCiclo(Ciclo ciclo) {
		this.ciclo = ciclo;
	}

	public Collection<Alumno> getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(Collection<Alumno> alumnos) {
		this.alumnos = alumnos;
	}

	public TipoCurso getTipoCurso() {
		return tipoCurso;
	}

	public void setTipoCurso(TipoCurso tipoCurso) {
		this.tipoCurso = tipoCurso;
	}

	public Colegio getColegio() {
		return colegio;
	}

	public void setColegio(Colegio colegio) {
		this.colegio = colegio;
	}

	@Override
	public String toString() {
		return name;
	}

}
