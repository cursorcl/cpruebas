package cl.eos.persistence.models;

import java.util.Collection;

import cl.eos.persistence.AEntity;

public class SCurso extends AEntity {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private SCiclo ciclo;
    private SColegio colegio;
    private STipoCurso tipoCurso;

    private Collection<SAlumno> alumnos;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SCurso other = (SCurso) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public Collection<SAlumno> getAlumnos() {
        return alumnos;
    }

    public SCiclo getCiclo() {
        return ciclo;
    }

    public SColegio getColegio() {
        return colegio;
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

    public void setAlumnos(Collection<SAlumno> alumnos) {
        this.alumnos = alumnos;
    }

    public void setCiclo(SCiclo ciclo) {
        this.ciclo = ciclo;
    }

    public void setColegio(SColegio colegio) {
        this.colegio = colegio;
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
        private SCiclo ciclo;
        private SColegio colegio;
        private STipoCurso tipoCurso;
        private Collection<SAlumno> alumnos;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder ciclo(SCiclo ciclo) {
            this.ciclo = ciclo;
            return this;
        }

        public Builder colegio(SColegio colegio) {
            this.colegio = colegio;
            return this;
        }

        public Builder tipoCurso(STipoCurso tipoCurso) {
            this.tipoCurso = tipoCurso;
            return this;
        }

        public Builder alumnos(Collection<SAlumno> alumnos) {
            this.alumnos = alumnos;
            return this;
        }

        public SCurso build() {
            SCurso sCurso = new SCurso();
            sCurso.id = id;
            sCurso.name = name;
            sCurso.ciclo = ciclo;
            sCurso.colegio = colegio;
            sCurso.tipoCurso = tipoCurso;
            sCurso.alumnos = alumnos;
            return sCurso;
        }
    }
}
