package cl.eos.persistence.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import cl.eos.persistence.AEntity;

@Entity(name = "imagenes")
@NamedQueries({ 
    @NamedQuery(name = "Imagenes.findAll", query = "SELECT i FROM imagenes i order by i.prueba.id, i.numero "),
    @NamedQuery(name = "Imagenes.findByPrueba", query = "SELECT i FROM imagenes i where i.prueba.id = :idPrueba") 
    })
public class Imagenes extends AEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int numero;

    @ManyToOne
    private Prueba prueba;

    @ManyToOne
    private Alternativas alternativa;

    private int version;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Prueba getPrueba() {
        return prueba;
    }

    public void setPrueba(Prueba prueba) {
        this.prueba = prueba;
    }

    public Alternativas getAlternativa() {
        return alternativa;
    }

    public void setAlternativa(Alternativas alternativa) {
        this.alternativa = alternativa;
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

}
