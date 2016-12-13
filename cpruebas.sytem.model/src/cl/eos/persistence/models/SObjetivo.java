package cl.eos.persistence.models;

import cl.eos.persistence.AEntity;

public class SObjetivo extends AEntity {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String descripcion;
    private STipoCurso tipoCurso;
    private SAsignatura asignatura;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SObjetivo other = (SObjetivo) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public SAsignatura getAsignatura() {
        return asignatura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public STipoCurso getTipoCurso() {
        return tipoCurso;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }

    public void setAsignatura(SAsignatura asignatura) {
        this.asignatura = asignatura;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setTipoCurso(STipoCurso tipoCurso) {
        this.tipoCurso = tipoCurso;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean validate() {
        return true;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String descripcion;
        private STipoCurso tipoCurso;
        private SAsignatura asignatura;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder descripcion(String descripcion) {
            this.descripcion = descripcion;
            return this;
        }

        public Builder tipoCurso(STipoCurso tipoCurso) {
            this.tipoCurso = tipoCurso;
            return this;
        }

        public Builder asignatura(SAsignatura asignatura) {
            this.asignatura = asignatura;
            return this;
        }

        public SObjetivo build() {
            SObjetivo sObjetivo = new SObjetivo();
            sObjetivo.id = id;
            sObjetivo.name = name;
            sObjetivo.descripcion = descripcion;
            sObjetivo.tipoCurso = tipoCurso;
            sObjetivo.asignatura = asignatura;
            return sObjetivo;
        }
    }
}
