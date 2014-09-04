package cl.eos.ot;

import java.util.Date;

public class OTPrueba {
	private Long id;

	private String name;
	private OTTipoPrueba tipoPrueba;
	private OTCurso curso;
	private OTAsignatura asignatura;
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

	public OTTipoPrueba getTipoPrueba() {
		return tipoPrueba;
	}

	public void setTipoPrueba(OTTipoPrueba tipoPrueba) {
		this.tipoPrueba = tipoPrueba;
	}

	public OTCurso getCurso() {
		return curso;
	}

	public void setCurso(OTCurso curso) {
		this.curso = curso;
	}

	public OTAsignatura getAsignatura() {
		return asignatura;
	}

	public void setAsignatura(OTAsignatura asignatura) {
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
