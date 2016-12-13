package cl.eos.persistence.models;

import cl.eos.persistence.AEntity;

public class STipoCurso extends AEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;

    
    public STipoCurso() {
        // TODO Auto-generated constructor stub
    }
    public STipoCurso(Long id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final STipoCurso other = (STipoCurso) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        result = prime * result + (version == null ? 0 : version.intValue());
        return result;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
