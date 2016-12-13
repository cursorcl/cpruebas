package cl.eos.restful.tables;

import cl.eos.persistence.AEntity;

public class R_EvaluacionPrueba extends AEntity {
    private static final long serialVersionUID = 1L;
    Long id;
    String name;
    Long fecha;
    Long curso_id;
    Long profesor_id;
    Long colegio_id;
    Long prueba_id;

    /**
     * @return the id
     */
    public final Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public final void setId(Long id) {
        this.id = id;
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
     * @return the fecha
     */
    public final Long getFecha() {
        return fecha;
    }

    /**
     * @param fecha
     *            the fecha to set
     */
    public final void setFecha(Long fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the curso_id
     */
    public final Long getCurso_id() {
        return curso_id;
    }

    /**
     * @param curso_id
     *            the curso_id to set
     */
    public final void setCurso_id(Long curso_id) {
        this.curso_id = curso_id;
    }

    /**
     * @return the profesor_id
     */
    public final Long getProfesor_id() {
        return profesor_id;
    }

    /**
     * @param profesor_id
     *            the profesor_id to set
     */
    public final void setProfesor_id(Long profesor_id) {
        this.profesor_id = profesor_id;
    }

    /**
     * @return the colegio_id
     */
    public final Long getColegio_id() {
        return colegio_id;
    }

    /**
     * @param colegio_id
     *            the colegio_id to set
     */
    public final void setColegio_id(Long colegio_id) {
        this.colegio_id = colegio_id;
    }

    /**
     * @return the prueba_id
     */
    public final Long getPrueba_id() {
        return prueba_id;
    }

    /**
     * @param prueba_id
     *            the prueba_id to set
     */
    public final void setPrueba_id(Long prueba_id) {
        this.prueba_id = prueba_id;
    }

    public static class Builder {
        private Long id;
        private String name;
        private Long fecha;
        private Long curso_id;
        private Long profesor_id;
        private Long colegio_id;
        private Long prueba_id;
        private Integer version;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder fecha(Long fecha) {
            this.fecha = fecha;
            return this;
        }

        public Builder curso_id(Long curso_id) {
            this.curso_id = curso_id;
            return this;
        }

        public Builder profesor_id(Long profesor_id) {
            this.profesor_id = profesor_id;
            return this;
        }

        public Builder colegio_id(Long colegio_id) {
            this.colegio_id = colegio_id;
            return this;
        }

        public Builder prueba_id(Long prueba_id) {
            this.prueba_id = prueba_id;
            return this;
        }

        public Builder version(Integer version) {
            this.version = version;
            return this;
        }

        public R_EvaluacionPrueba build() {
            return new R_EvaluacionPrueba(this);
        }
    }

    private R_EvaluacionPrueba(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.fecha = builder.fecha;
        this.curso_id = builder.curso_id;
        this.profesor_id = builder.profesor_id;
        this.colegio_id = builder.colegio_id;
        this.prueba_id = builder.prueba_id;
        this.version = builder.version;
    }
}
