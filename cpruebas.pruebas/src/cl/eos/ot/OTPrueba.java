package cl.eos.ot;

import java.util.Date;

import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.TipoPrueba;

public class OTPrueba {
	private Long id;

	private String name;

	private TipoPrueba tipoPrueba;
	private Curso curso;
	private Asignatura asignatura;

	private Date fecha;
	private Integer nroPreguntas;
	private Integer formas;
	private Integer alternativas;

	private String responses;

	public OTPrueba() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public String getResponses() {
		return responses;
	}

	public void setResponses(String responses) {
		this.responses = responses;
	}
	
	
	
}
