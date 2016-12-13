package cl.eos.persistence.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import cl.eos.persistence.AEntity;

/**
 * Corresponde a la evaluación de un {@link R_Curso}. Tiene asociada lista de
 * {@link R_PruebaRendida} del {@link R_Curso}.
 *
 * @author curso
 *
 */
public class SEvaluacionPrueba extends AEntity {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private SPrueba prueba;
    private SCurso curso;
    private List<SPruebaRendida> pruebasRendidas;
    private Long fecha;
    private SProfesor profesor;
    private SColegio colegio;

    public SEvaluacionPrueba() {
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
        final SEvaluacionPrueba other = (SEvaluacionPrueba) obj;
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

    public SColegio getColegio() {
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

    public SCurso getCurso() {
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

    public SProfesor getProfesor() {
        return profesor;
    }

    public SPrueba getPrueba() {
        return prueba;
    }

    public List<SPruebaRendida> getPruebasRendidas() {
        return pruebasRendidas;
    }

    public String getResponses() {
        return prueba.getResponses();
    }

    public STipoPrueba getTipo() {
        return prueba.getTipoPrueba();
    }

    public String getTipoCurso() {
        return curso.getTipoCurso().getName();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }

    public void setColegio(SColegio colegio) {
        this.colegio = colegio;
    }

    public void setCurso(SCurso curso) {
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

    public void setProfesor(SProfesor profesor) {
        this.profesor = profesor;
    }

    public void setPrueba(SPrueba prueba) {
        this.prueba = prueba;
    }

    //
    public void setPruebasRendidas(List<SPruebaRendida> pruebasRendidas) {
        this.pruebasRendidas = pruebasRendidas;
    }

    @Override
    public boolean validate() {
        return true;
    }

    public static class Builder {
        private Long id;
        private String name;
        private SPrueba prueba;
        private SCurso curso;
        private List<SPruebaRendida> pruebasRendidas;
        private Long fecha;
        private SProfesor profesor;
        private SColegio colegio;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder prueba(SPrueba prueba) {
            this.prueba = prueba;
            return this;
        }

        public Builder curso(SCurso curso) {
            this.curso = curso;
            return this;
        }

        public Builder pruebasRendidas(List<SPruebaRendida> pruebasRendidas) {
            this.pruebasRendidas = pruebasRendidas;
            return this;
        }

        public Builder fecha(Long fecha) {
            this.fecha = fecha;
            return this;
        }

        public Builder profesor(SProfesor profesor) {
            this.profesor = profesor;
            return this;
        }

        public Builder colegio(SColegio colegio) {
            this.colegio = colegio;
            return this;
        }

        public SEvaluacionPrueba build() {
            SEvaluacionPrueba sEvaluacionPrueba = new SEvaluacionPrueba();
            sEvaluacionPrueba.id = id;
            sEvaluacionPrueba.name = name;
            sEvaluacionPrueba.prueba = prueba;
            sEvaluacionPrueba.curso = curso;
            sEvaluacionPrueba.pruebasRendidas = pruebasRendidas;
            sEvaluacionPrueba.fecha = fecha;
            sEvaluacionPrueba.profesor = profesor;
            sEvaluacionPrueba.colegio = colegio;
            return sEvaluacionPrueba;
        }
    }
}
