package cl.eos.imp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import cl.eos.persistence.AEntity;

@Entity(name = "ejetematico")
@NamedQueries({ @NamedQuery(name = "SEjeTematico.findAll", query = "SELECT e FROM ejetematico e order by e.name"),
        @NamedQuery(name = "SEjeTematico.findByAsigntura", query = "Select e FROM ejetematico e WHERE e.asignatura.id = :idAsignatura") })
public class EjeTematico extends AEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private TipoPrueba tipoprueba;
    private Asignatura asignatura;

    public Asignatura getAsignatura() {
        return asignatura;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public TipoPrueba getTipoprueba() {
        return tipoprueba;
    }

    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setTipoprueba(TipoPrueba tipoprueba) {
        this.tipoprueba = tipoprueba;
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
