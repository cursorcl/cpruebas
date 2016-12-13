package cl.eos.imp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import cl.eos.persistence.AEntity;

@Entity(name = "habilidad")
@NamedQueries({ @NamedQuery(name = "R_Habilidad.findAll", query = "SELECT e FROM habilidad e order by e.name") })
public class Habilidad extends AEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String descripcion;


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

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean validate() {
        return false;
    }

}
