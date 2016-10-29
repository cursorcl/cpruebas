package cl.eos.persistence.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import cl.eos.persistence.AEntity;

/**
 * Corresponde a la evaluación de un {@link Curso}. Tiene asociada lista de
 * {@link PruebaRendida} del {@link Curso}.
 *
 * @author curso
 *
 */
@Entity(name = "evaluacionprueba")
@NamedQueries({ @NamedQuery(name = "EvaluacionPrueba.findAll", query = "SELECT e FROM evaluacionprueba e"),
        @NamedQuery(name = "EvaluacionPrueba.findEvaluacionByColegioAsig", query = "SELECT e FROM evaluacionprueba e where e.colegio.id = :idColegio and e.prueba.asignatura.id = :idAsignatura"),
        @NamedQuery(name = "EvaluacionPrueba.findByPrueba", query = "SELECT e FROM evaluacionprueba e where e.prueba.id = :idPrueba") })
public class EvaluacionPrueba extends AEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Prueba prueba;

    private Curso curso;
    @OneToMany(mappedBy = "evaluacionPrueba", cascade = { CascadeType.REMOVE,
            CascadeType.REFRESH }, orphanRemoval = true)
    private List<PruebaRendida> pruebasRendidas;
    private Long fecha;
    private Profesor profesor;
    private Colegio colegio;

    /**
     * Se crea para el manejo de multiusuarios
     */
    @Version
    protected int version;

    public EvaluacionPrueba() {
        pruebasRendidas = new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final EvaluacionPrueba other = (EvaluacionPrueba) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public String getAsignatura() {
        return prueba.getAsignatura().getName();
    }

    public Colegio getColegio() {
        return colegio;
    }

    public String getColegiocurso() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(colegio.getName());
        buffer.append("\n");
        if (curso != null)
            buffer.append(curso.getName());
        return buffer.toString();
    }

    public String getColegioTipoCurso() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(colegio.getName());
        buffer.append("\n");
        if (curso != null)
            buffer.append(curso.getTipoCurso().getName());
        return buffer.toString();
    }

    public Curso getCurso() {
        return curso;
    }

    public Integer getExigencia() {
        return prueba.getExigencia();
    }

    public Long getFecha() {
        return fecha;
    }

    public LocalDate getFechaLocal() {
        return LocalDate.ofEpochDay(fecha.longValue());
    }

    public Integer getFormas() {
        return prueba.getNroFormas();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public Integer getNroPreguntas() {
        return prueba.getNroPreguntas();
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public Prueba getPrueba() {
        return prueba;
    }

    public List<PruebaRendida> getPruebasRendidas() {
        return pruebasRendidas;
    }

    public String getResponses() {
        return prueba.getResponses();
    }

    public TipoPrueba getTipo() {
        return prueba.getTipoPrueba();
    }

    public String getTipoCurso() {
        return curso.getTipoCurso().getName();
    }

    @Override
    public final int getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }

    public void setColegio(Colegio colegio) {
        this.colegio = colegio;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public void setFecha(Long fecha) {
        this.fecha = fecha;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public void setPrueba(Prueba prueba) {
        this.prueba = prueba;
    }

    //
    public void setPruebasRendidas(List<PruebaRendida> pruebasRendidas) {
        this.pruebasRendidas = pruebasRendidas;
    }

    @Override
    public final void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean validate() {
        return true;
    }

}
