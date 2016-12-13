package cl.eos.imp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import cl.eos.persistence.AEntity;

@Entity(name = "formas")
@NamedQueries({ @NamedQuery(name = "R_Formas.findAll", query = "SELECT e FROM formas e"),
        @NamedQuery(name = "R_Formas.findByPrueba", query = "SELECT e FROM formas e WHERE e.prueba.id = :pruebaId"),
        @NamedQuery(name = "R_Formas.deleteByPrueba", query = "DELETE FROM formas e WHERE e.prueba.id = :pruebaId") })

public class Formas extends AEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    private Integer forma;
    /**
     * Indica el orden en que se imprimieron las preguntas.
     */
    private String orden;

    @ManyToOne(fetch = FetchType.LAZY)
    private Prueba prueba;

    public Integer getForma() {
        return forma;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getOrden() {
        return orden;
    }

    public Prueba getPrueba() {
        return prueba;
    }

    public void setForma(Integer forma) {
        this.forma = forma;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public void setPrueba(Prueba prueba) {
        this.prueba = prueba;
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
