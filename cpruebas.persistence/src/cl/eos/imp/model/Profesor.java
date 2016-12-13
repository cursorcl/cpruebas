package cl.eos.imp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import cl.eos.persistence.AEntity;

@Entity(name = "profesor")
@NamedQueries({ @NamedQuery(name = "R_Profesor.findAll", query = "SELECT e FROM profesor e order by e.name") })
public class Profesor extends AEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String rut;
    private String name;
    private String paterno;
    private String materno;

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

    @Override
    public String toString() {
        return String.format("%s %s %s", name, paterno, materno).trim();
    }

    @Override
    public boolean validate() {
        return true;
    }

}
