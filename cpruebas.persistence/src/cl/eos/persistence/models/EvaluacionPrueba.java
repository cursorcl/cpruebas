package cl.eos.persistence.models;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import cl.eos.interfaces.entity.IEntity;

@Entity(name = "evaluacionprueba")
@NamedQueries({ @NamedQuery(name = "EvaluacionPrueba.findAll", query = "SELECT e FROM evaluacionprueba e") })
public class EvaluacionPrueba implements IEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 100)
	private String name;

	private Prueba prueba;
	private Curso curso;
	private List<PruebaRendida> pruebasRendidas;
	private Long fecha;
	private Profesor profesor;
	private Colegio colegio;

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

	public Prueba getPrueba() {
		return prueba;
	}

	public void setPrueba(Prueba prueba) {
		this.prueba = prueba;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public List<PruebaRendida> getPruebasRendidas() {
		return pruebasRendidas;
	}

	public void setPruebasRendidas(List<PruebaRendida> pruebasRendidas) {
		this.pruebasRendidas = pruebasRendidas;
	}

	public Long getFecha() {
		return fecha;
	}
	
	public LocalDate getFechaLocal() {
		return LocalDate.ofEpochDay(this.fecha.longValue());
	}

	public void setFecha(Long fecha) {
		this.fecha = fecha;
	}

	public Profesor getProfesor() {
		return profesor;
	}

	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
	}

	public Colegio getColegio() {
		return colegio;
	}

	public void setColegio(Colegio colegio) {
		this.colegio = colegio;
	}
	

}