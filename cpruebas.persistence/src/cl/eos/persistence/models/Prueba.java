package cl.eos.persistence.models;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Version;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;

import cl.eos.persistence.AEntity;

@Entity(name = "prueba")
@Cache(type = CacheType.NONE, size = 64000, // Use 64,000 as the initial cache
                                            // size.
        expiry = 360000, // 6 minutes
        coordinationType = CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS // if
                                                                            // cache
                                                                            // coordination
                                                                            // is
                                                                            // used,
                                                                            // only
                                                                            // send
                                                                            // invalidation
                                                                            // messages.
)
@NamedQueries({ @NamedQuery(name = "Prueba.findAll", query = "SELECT e FROM prueba e") })
public class Prueba extends AEntity {

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

    @OneToMany(mappedBy = "prueba", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    private List<Formas> formas;

    @OneToMany(mappedBy = "prueba", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    @OrderBy("numero ASC")
    private List<RespuestasEsperadasPrueba> respuestas;

    @OneToMany(mappedBy = "prueba", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    private List<EvaluacionPrueba> evaluaciones;

    /**
     * Se crea para el manejo de multiusuarios
     */
    @Version
    protected int version;

    public Integer getAlternativas() {
        return alternativas;
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }

    public TipoCurso getCurso() {
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

    public List<EvaluacionPrueba> getEvaluaciones() {
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

    public List<Formas> getFormas() {
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

    public NivelEvaluacion getNivelEvaluacion() {
        return nivelEvaluacion;
    }

    public Integer getNroFormas() {
        return nroFormas;
    }

    public Integer getNroPreguntas() {
        return nroPreguntas;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public Integer getPuntajeBase() {
        return puntajeBase;
    }

    public String getResponses() {
        return responses;
    }

    public List<RespuestasEsperadasPrueba> getRespuestas() {
        return respuestas;
    }

    public TipoPrueba getTipoPrueba() {
        return tipoPrueba;
    }

    @Override
    public final int getVersion() {
        return version;
    }

    public void setAlternativas(Integer alternativas) {
        this.alternativas = alternativas;
    }

    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    public void setCurso(TipoCurso curso) {
        this.curso = curso;
    }

    public void setExigencia(Integer exigencia) {
        this.exigencia = exigencia;
    }

    public void setFecha(Long fecha) {
        this.fecha = fecha;
    }

    public void setFormas(List<Formas> formas) {
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

    public void setNivelEvaluacion(NivelEvaluacion nivelEvaluacion) {
        this.nivelEvaluacion = nivelEvaluacion;
    }

    public void setNroFormas(Integer nroFormas) {
        this.nroFormas = nroFormas;
    }

    public void setNroPreguntas(Integer nroPreguntas) {
        this.nroPreguntas = nroPreguntas;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public void setPuntajeBase(Integer puntajeBase) {
        this.puntajeBase = puntajeBase;
    }

    public void setResponses(String responses) {
        this.responses = responses;
    }

    public void setRespuestas(List<RespuestasEsperadasPrueba> respuestas) {
        this.respuestas = respuestas;
    }

    public void setTipoPrueba(TipoPrueba tipoPrueba) {
        this.tipoPrueba = tipoPrueba;
    }

    @Override
    public final void setVersion(int version) {
        this.version = version;
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
