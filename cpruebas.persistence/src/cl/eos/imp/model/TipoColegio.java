package cl.eos.imp.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import cl.eos.persistence.AEntity;

@Entity(name = "tipocolegio")
@NamedQueries({ @NamedQuery(name = "STipoColegio.findAll", query = "SELECT e FROM tipocolegio e order by e.name") })

public class TipoColegio extends AEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "tipoColegio", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Collection<Colegio> colegios;

    public TipoColegio() {
        // TODO Auto-generated constructor stub
    }
    
    public TipoColegio(Long id, String name) {
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
        final TipoColegio other = (TipoColegio) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public Collection<Colegio> getColegios() {
        return colegios;
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
        return result;
    }

    public void setColegios(Collection<Colegio> colegios) {
        this.colegios = colegios;
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
