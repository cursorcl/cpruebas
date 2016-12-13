package cl.eos.persistence.models;

import cl.eos.persistence.AEntity;

public class SAlumno extends AEntity {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String rut;
    private String name;
    private String paterno;
    private String materno;
    private String direccion;

    private STipoAlumno tipoAlumno;
    private SCurso curso;
    private SColegio colegio;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SAlumno other = (SAlumno) obj;
        if (rut == null) {
            if (other.rut != null)
                return false;
        } else if (!rut.equals(other.rut))
            return false;
        return true;
    }

    public SColegio getColegio() {
        return colegio;
    }

    public SCurso getCurso() {
        return curso;
    }

    public String getDireccion() {
        return direccion;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getMaterno() {
        return materno;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getPaterno() {
        return paterno;
    }

    public String getRut() {
        return rut;
    }

    public final STipoAlumno getTipoAlumno() {
        return tipoAlumno;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (rut == null ? 0 : rut.hashCode());
        return result;
    }

    public void setColegio(SColegio colegio) {
        this.colegio = colegio;
    }

    public void setCurso(SCurso curso) {
        this.curso = curso;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public final void setTipoAlumno(STipoAlumno tipoAlumno) {
        this.tipoAlumno = tipoAlumno;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", paterno, materno, name);
    }

    @Override
    public boolean validate() {
        return true;
    }

    public static class Builder {
        private Long id;
        private String rut;
        private String name;
        private String paterno;
        private String materno;
        private String direccion;
        private STipoAlumno tipoAlumno;
        private SCurso curso;
        private SColegio colegio;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder rut(String rut) {
            this.rut = rut;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder paterno(String paterno) {
            this.paterno = paterno;
            return this;
        }

        public Builder materno(String materno) {
            this.materno = materno;
            return this;
        }

        public Builder direccion(String direccion) {
            this.direccion = direccion;
            return this;
        }

        public Builder tipoAlumno(STipoAlumno tipoAlumno) {
            this.tipoAlumno = tipoAlumno;
            return this;
        }

        public Builder curso(SCurso curso) {
            this.curso = curso;
            return this;
        }

        public Builder colegio(SColegio colegio) {
            this.colegio = colegio;
            return this;
        }

        public SAlumno build() {
            SAlumno sAlumno = new SAlumno();
            sAlumno.id = id;
            sAlumno.rut = rut;
            sAlumno.name = name;
            sAlumno.paterno = paterno;
            sAlumno.materno = materno;
            sAlumno.direccion = direccion;
            sAlumno.tipoAlumno = tipoAlumno;
            sAlumno.curso = curso;
            sAlumno.colegio = colegio;
            return sAlumno;
        }
    }
}
