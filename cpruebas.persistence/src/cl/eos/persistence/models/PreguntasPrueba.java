package cl.eos.persistence.models;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Version;

import cl.eos.persistence.AEntity;

/**
 * Esta clase contiene las preguntas asociadas a una prueba.
 * Las preguntas contienen información del tipo de pregunta
 * @author cursor
 *
 */
@Entity(name = "preguntasprueba")
@NamedQueries({ @NamedQuery(name = "PreuntasPrueba.findAll", query = "SELECT e FROM preguntasprueba e"),
	 			@NamedQuery(name = "PreuntasPrueba.findByPrueba", query = "SELECT e FROM PreuntasPrueba e WHERE e.prueba.id = :pruebaId order by e.numero")
			})
public class PreguntasPrueba extends AEntity {

	public enum Estado {
		CREADA, DEFINIDA, EVALUADA
	};

	private static final long serialVersionUID = 1L;

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
	private Integer nroFormas;
	private Integer alternativas;
	private Profesor profesor;
	private Integer puntajeBase;
	private String responses;
	private Integer exigencia;

	@OneToMany(mappedBy = "prueba", cascade = CascadeType.ALL)
	private List<Formas> formas;

	@OneToMany(mappedBy = "prueba", cascade = CascadeType.ALL)
	@OrderBy("numero ASC")
	private List<RespuestasEsperadasPrueba> respuestas;

	@OneToMany(mappedBy = "prueba", cascade = CascadeType.ALL)
	private List<EvaluacionPrueba> evaluaciones;

	/**
	 * Se crea para el manejo de multiusuarios
	 */
	@Version 
	protected int version;
	
	
	public final int getVersion() {
		return version;
	}

	public final void setVersion(int version) {
		this.version = version;
	}
	
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

	public Integer getNroFormas() {
		return nroFormas;
	}

	public void setNroFormas(Integer nroFormas) {
		this.nroFormas = nroFormas;
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

	public List<Formas> getFormas() {
		return formas;
	}

	public Integer getExigencia() {
		return exigencia;
	}

	public void setExigencia(Integer exigencia) {
		this.exigencia = exigencia;
	}

	public void setFormas(List<Formas> formas) {
		this.formas = formas;
	}

	public List<RespuestasEsperadasPrueba> getRespuestas() {
		return respuestas;
	}

	public void setRespuestas(List<RespuestasEsperadasPrueba> respuestas) {
		this.respuestas = respuestas;
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

	public Estado getEstado() {
		Estado estado = Estado.CREADA;
		if (respuestas != null && !respuestas.isEmpty()) {
			estado = Estado.DEFINIDA;
		}
		if (evaluaciones != null && !evaluaciones.isEmpty()) {
			estado = Estado.EVALUADA;
		}
		return estado;
	}

	public List<EvaluacionPrueba> getEvaluaciones() {
		return evaluaciones;
	}

}
