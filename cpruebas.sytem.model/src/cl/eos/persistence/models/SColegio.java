package cl.eos.persistence.models;

import cl.eos.persistence.AEntity;

public class SColegio extends AEntity {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String ciudad;
    private String direccion;
    private STipoColegio tipoColegio;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SColegio other = (SColegio) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (version != other.version)
            return false;
        return true;
    }

    /**
     * @return the ciudad
     */
    public String getCiudad() {
        return ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public STipoColegio getTipoColegio() {
        return tipoColegio;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        result = prime * result + version;
        return result;
    }

    /**
     * @param ciudad
     *            the ciudad to set
     */
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setTipoColegio(STipoColegio tipo) {
        tipoColegio = tipo;
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
