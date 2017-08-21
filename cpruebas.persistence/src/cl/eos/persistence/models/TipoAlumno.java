package cl.eos.persistence.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import cl.eos.persistence.AEntity;

@Entity(name = "tipoalumno")
@Table( name = "tipoalumno",
schema="cpruebas_comun")

@NamedQueries({ @NamedQuery(name = "TipoAlumno.findAll", query = "SELECT e FROM tipoalumno e order by e.id") })
public class TipoAlumno extends AEntity {

    private static final long serialVersionUID = -5362288224985235697L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public TipoAlumno() {
        // Creado para que pueda ser instanciado por le sistema.
    }

    public TipoAlumno(Long id, String name) {
        super();
        this.id = id;
        this.name = name;
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
