package cl.eos.persistence.models;

import java.time.LocalDate;
import java.util.List;

import cl.eos.persistence.AEntity;

/**
 * Esta clase contiene las preguntas asociadas a una prueba. Las preguntas
 * contienen información del tipo de pregunta
 * 
 * @author cursor
 *
 */
public class SPreguntasPrueba extends AEntity {

    public enum Estado {
        CREADA, DEFINIDA, EVALUADA
    };

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private STipoPrueba tipoPrueba;
    private STipoCurso curso;
    private SAsignatura asignatura;
    private SNivelEvaluacion nivelEvaluacion;
    private Long fecha;
    private Integer nroPreguntas;
    private Integer nroFormas;
    private Integer alternativas;
    private SProfesor profesor;
    private Integer puntajeBase;
    private String responses;
    private Integer exigencia;
    private List<SFormas> formas;
    private List<SRespuestasEsperadasPrueba> respuestas;
    private List<SEvaluacionPrueba> evaluaciones;

    public Integer getAlternativas() {
        return alternativas;
    }

    public SAsignatura getAsignatura() {
        return asignatura;
    }

    public STipoCurso getCurso() {
        return curso;
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

    public List<SEvaluacionPrueba> getEvaluaciones() {
        return evaluaciones;
    }

    public Integer getExigencia() {
        return exigencia;
    }

    public Long getFecha() {
        return fecha;
    }

    public LocalDate getFechaLocal() {
        return LocalDate.ofEpochDay(fecha.longValue());
    }

    public List<SFormas> getFormas() {
        return formas;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public SNivelEvaluacion getNivelEvaluacion() {
        return nivelEvaluacion;
    }

    public Integer getNroFormas() {
        return nroFormas;
    }

    public Integer getNroPreguntas() {
        return nroPreguntas;
    }

    public SProfesor getProfesor() {
        return profesor;
    }

    public Integer getPuntajeBase() {
        return puntajeBase;
    }

    public String getResponses() {
        return responses;
    }

    public List<SRespuestasEsperadasPrueba> getRespuestas() {
        return respuestas;
    }

    public STipoPrueba getTipoPrueba() {
        return tipoPrueba;
    }


    public void setAlternativas(Integer alternativas) {
        this.alternativas = alternativas;
    }

    public void setAsignatura(SAsignatura asignatura) {
        this.asignatura = asignatura;
    }

    public void setCurso(STipoCurso curso) {
        this.curso = curso;
    }

    public void setExigencia(Integer exigencia) {
        this.exigencia = exigencia;
    }

    public void setFecha(Long fecha) {
        this.fecha = fecha;
    }

    public void setFormas(List<SFormas> formas) {
        this.formas = formas;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setNivelEvaluacion(SNivelEvaluacion nivelEvaluacion) {
        this.nivelEvaluacion = nivelEvaluacion;
    }

    public void setNroFormas(Integer nroFormas) {
        this.nroFormas = nroFormas;
    }

    public void setNroPreguntas(Integer nroPreguntas) {
        this.nroPreguntas = nroPreguntas;
    }

    public void setProfesor(SProfesor profesor) {
        this.profesor = profesor;
    }

    public void setPuntajeBase(Integer puntajeBase) {
        this.puntajeBase = puntajeBase;
    }

    public void setResponses(String responses) {
        this.responses = responses;
    }

    public void setRespuestas(List<SRespuestasEsperadasPrueba> respuestas) {
        this.respuestas = respuestas;
    }

    public void setTipoPrueba(STipoPrueba tipoPrueba) {
        this.tipoPrueba = tipoPrueba;
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
