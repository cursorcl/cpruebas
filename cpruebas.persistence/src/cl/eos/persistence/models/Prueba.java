package cl.eos.persistence.models;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import cl.eos.interfaces.entity.IEntity;

@Entity(name = "prueba")
@NamedQueries({ @NamedQuery(name = "Prueba.findAll", query = "SELECT e FROM prueba e") })
public class Prueba implements IEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100)
	private String name; 

	private TipoPrueba tipoPrueba;
	private TipoCurso curso;
	private Asignatura asignatura;
	private NivelEvaluacion nivelEvaluacion;

	private Long fecha;
	private Integer nroPreguntas;
	private Integer formas;
	private Integer alternativas;
	private Profesor profesor;
	private String responses;
	
	private Integer puntajeBase;

	public TipoPrueba getTipoPrueba() {
		return tipoPrueba;
	}

	public void setTipoPrueba(TipoPrueba tipoPrueba) {
		this.tipoPrueba = tipoPrueba;
	}

	public TipoCurso getCurso() {
		return curso;
	}

	public void setCurso(TipoCurso curso) {
		this.curso = curso;
	}

	public Asignatura getAsignatura() {
		return asignatura;
	}

	public void setAsignatura(Asignatura asignatura) {
		this.asignatura = asignatura;
	}

	public Long getFecha() {
		return fecha;
	}

	public void setFecha(Long fecha) {
		this.fecha = fecha;
	}

	public Integer getNroPreguntas() {
		return nroPreguntas;
	}

	public void setNroPreguntas(Integer nroPreguntas) {
		this.nroPreguntas = nroPreguntas;
	}

	public Integer getFormas() {
		return formas;
	}

	public void setFormas(Integer formas) {
		this.formas = formas;
	}

	public Integer getAlternativas() {
		return alternativas;
	}

	public void setAlternativas(Integer alternativas) {
		this.alternativas = alternativas;
	}

	public NivelEvaluacion getNivelEvaluacion() {
		return nivelEvaluacion;
	}

	public void setNivelEvaluacion(NivelEvaluacion nivelEvaluacion) {
		this.nivelEvaluacion = nivelEvaluacion;
	}

	public Integer getPuntajeBase() {
		return puntajeBase;
	}

	public void setPuntajeBase(Integer puntajeBase) {
		this.puntajeBase = puntajeBase;
	}

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

	public Profesor getProfesor() {
		return profesor;
	}

	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
	}

	public LocalDate getFechaLocal() {
		return LocalDate.ofEpochDay(this.fecha.longValue());
	}

	@Override
	public String toString() {
		return name;
	}

	public String getResponses() {
		return responses;
	}

	public void setResponses(String responses) {
		this.responses = responses;
	}

	
}
