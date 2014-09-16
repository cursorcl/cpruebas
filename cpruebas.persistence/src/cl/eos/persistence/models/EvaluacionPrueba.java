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
@NamedQueries({
		@NamedQuery(name = "EvaluacionPrueba.findAll", query = "SELECT e FROM evaluacionprueba e"),
		@NamedQuery(name = "EvaluacionPrueba.findByPrueba", query = "SELECT e FROM evaluacionprueba e where e.prueba.id = :idPrueba") })
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
	
	public String getAsignatura(){
		return prueba.getAsignatura().getName();
	}

	public Integer getFormas(){
		return prueba.getFormas();
	}
	
	public Integer getNroPreguntas(){
		return prueba.getNroPreguntas();
	}
	
	private volatile Float NotaMin = 7f;
	private volatile Float NotaMax = 0f;
	private volatile Float PBuenasMin = 100f;
	private volatile Float PBuenasMax = 0f;
	private volatile Float ppuntajeMin = 100f;
	private volatile Float ppuntajeMax = 0f;
	private volatile Integer puntajeMin = 100;
	private volatile Integer puntajeMax = 0;

	
	public void obtenerValoresMinMax() {
		for (PruebaRendida pruebaRendida : pruebasRendidas) {
			if (pruebaRendida.getNota() < NotaMin) {
				NotaMin = pruebaRendida.getNota();
			}
			if (pruebaRendida.getNota() > NotaMax) {
				NotaMax = pruebaRendida.getNota();
			}

			if (pruebaRendida.getPbuenas() < PBuenasMin) {
				PBuenasMin = pruebaRendida.getPbuenas();
			}
			if (pruebaRendida.getPbuenas() > PBuenasMax) {
				PBuenasMax = pruebaRendida.getPbuenas();
			}

			if (pruebaRendida.getPpuntaje() < ppuntajeMin) {
				ppuntajeMin = pruebaRendida.getPpuntaje();
			}
			if (pruebaRendida.getPpuntaje() > ppuntajeMax) {
				ppuntajeMax = pruebaRendida.getPpuntaje();
			}

			if (pruebaRendida.getPuntaje() < puntajeMin) {
				puntajeMin = pruebaRendida.getPuntaje();
			}
			if (pruebaRendida.getPuntaje() > puntajeMax) {
				puntajeMax = pruebaRendida.getPuntaje();
			}
		}

	}

	public Float getNotaMin() {
		return NotaMin;
	}

	public void setNotaMin(Float notaMin) {
		NotaMin = notaMin;
	}

	public Float getNotaMax() {
		return NotaMax;
	}

	public void setNotaMax(Float notaMax) {
		NotaMax = notaMax;
	}

	public Float getPBuenasMin() {
		return PBuenasMin;
	}

	public void setPBuenasMin(Float pBuenasMin) {
		PBuenasMin = pBuenasMin;
	}

	public Float getPBuenasMax() {
		return PBuenasMax;
	}

	public void setPBuenasMax(Float pBuenasMax) {
		PBuenasMax = pBuenasMax;
	}

	public Float getPpuntajeMin() {
		return ppuntajeMin;
	}

	public void setPpuntajeMin(Float ppuntajeMin) {
		this.ppuntajeMin = ppuntajeMin;
	}

	public Float getPpuntajeMax() {
		return ppuntajeMax;
	}

	public void setPpuntajeMax(Float ppuntajeMax) {
		this.ppuntajeMax = ppuntajeMax;
	}

	public Integer getPuntajeMin() {
		return puntajeMin;
	}

	public void setPuntajeMin(Integer puntajeMin) {
		this.puntajeMin = puntajeMin;
	}

	public Integer getPuntajeMax() {
		return puntajeMax;
	}

	public void setPuntajeMax(Integer puntajeMax) {
		this.puntajeMax = puntajeMax;
	}

}
