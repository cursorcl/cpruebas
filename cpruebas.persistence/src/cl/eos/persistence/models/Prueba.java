package cl.eos.persistence.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import cl.eos.interfaces.entity.IEntity;

@Entity(name = "prueba")
@NamedQueries({ @NamedQuery(name = "Prueba.findAll", query = "SELECT e FROM prueba e") })
public class Prueba implements IEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 100)
	private String name;

	private TipoPrueba tipoPrueba;
	private Curso curso;
	private Asignatura asignatura;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;
	private Integer nroPreguntas;
	private Integer formas;
	private Integer alternativas;

	private String responses;

	public TipoPrueba getTipoPrueba() {
		return tipoPrueba;
	}

	public void setTipoPrueba(TipoPrueba tipoPrueba) {
		this.tipoPrueba = tipoPrueba;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Asignatura getAsignatura() {
		return asignatura;
	}

	public void setAsignatura(Asignatura asignatura) {
		this.asignatura = asignatura;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
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

	public Integer getNivelEvaluacion() {
		return nivelEvaluacion;
	}

	public void setNivelEvaluacion(Integer nivelEvaluacion) {
		this.nivelEvaluacion = nivelEvaluacion;
	}

	public Integer getPuntajeBase() {
		return puntajeBase;
	}

	public void setPuntajeBase(Integer puntajeBase) {
		this.puntajeBase = puntajeBase;
	}

	private Integer nivelEvaluacion;
	private Integer puntajeBase;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getResponses() {
		return responses;
	}

	public void setResponses(String responses) {
		this.responses = responses;
	}

	
	@Override
	public String toString() {
		return name;
	}

}
