package cl.eos.persistence.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import cl.eos.persistence.AEntity;

@Entity(name = "dl_rangolecturas")
@NamedQueries({
        @NamedQuery(name = "RangoLecturas.findAll", query = "SELECT r FROM dl_velocidadlectora r order by r.name") })
public class RangosLectura extends AEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private TipoCurso tipoCurso;
    private VelocidadLectora velocidadLectora;
    int value;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return null;
    }

    public TipoCurso getTipoCurso() {
        return tipoCurso;
    }

    public int getValue() {
        return value;
    }

    public VelocidadLectora getVelocidadLectora() {
        return velocidadLectora;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {

    }

    public void setTipoCurso(TipoCurso tipoCurso) {
        this.tipoCurso = tipoCurso;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setVelocidadLectora(VelocidadLectora velocidadLectora) {
        this.velocidadLectora = velocidadLectora;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
