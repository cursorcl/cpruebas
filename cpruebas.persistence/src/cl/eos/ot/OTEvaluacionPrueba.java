package cl.eos.ot;

import java.time.LocalDate;

import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_EvaluacionPrueba;

public class OTEvaluacionPrueba {
    LocalDate fechaLocal;
    String name;

    R_Curso curso;

    R_EvaluacionPrueba evaluacionPrueba;
    /**
     * @return the fechaLocal
     */
    public final LocalDate getFechaLocal() {
        return fechaLocal;
    }
    /**
     * @param fechaLocal
     *            the fechaLocal to set
     */
    public final void setFechaLocal(LocalDate fechaLocal) {
        this.fechaLocal = fechaLocal;
    }
    /**
     * @return the curso
     */
    public final R_Curso getCurso() {
        return curso;
    }
    /**
     * @param curso
     *            the curso to set
     */
    public final void setCurso(R_Curso curso) {
        this.curso = curso;
    }
    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }
    /**
     * @param name
     *            the name to set
     */
    public final void setName(String name) {
        this.name = name;
    }
    /**
     * @return the evaluacionPrueba
     */
    public final R_EvaluacionPrueba getEvaluacionPrueba() {
        return evaluacionPrueba;
    }
    /**
     * @param evaluacionPrueba
     *            the evaluacionPrueba to set
     */
    public final void setEvaluacionPrueba(R_EvaluacionPrueba evaluacionPrueba) {
        this.evaluacionPrueba = evaluacionPrueba;
        this.name = evaluacionPrueba.getName();
        this.fechaLocal = LocalDate.ofEpochDay(evaluacionPrueba.getFecha());
    }
    public static class Builder {
        private LocalDate fechaLocal;
        private R_Curso curso;
        private String name;
        private R_EvaluacionPrueba evaluacionPrueba;
        public Builder fechaLocal(LocalDate fechaLocal) {
            this.fechaLocal = fechaLocal;
            return this;
        }
        public Builder curso(R_Curso curso) {
            this.curso = curso;
            return this;
        }
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        public Builder evaluacionPrueba(R_EvaluacionPrueba evaluacionPrueba) {
            this.evaluacionPrueba = evaluacionPrueba;
            this.name = evaluacionPrueba.getName();
            this.fechaLocal = LocalDate.ofEpochDay(evaluacionPrueba.getFecha());
            return this;
        }
        public OTEvaluacionPrueba build() {
            return new OTEvaluacionPrueba(this);
        }
    }
    private OTEvaluacionPrueba(Builder builder) {
        this.fechaLocal = builder.fechaLocal;
        this.curso = builder.curso;
        this.name = builder.name;
        this.evaluacionPrueba = builder.evaluacionPrueba;
    }
}
