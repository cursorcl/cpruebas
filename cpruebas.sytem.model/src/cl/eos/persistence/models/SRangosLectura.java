package cl.eos.persistence.models;

import cl.eos.persistence.AEntity;

public class SRangosLectura extends AEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    private STipoCurso tipoCurso;
    private SVelocidadLectora velocidadLectora;
    int value;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return null;
    }

    public STipoCurso getTipoCurso() {
        return tipoCurso;
    }

    public int getValue() {
        return value;
    }

    public SVelocidadLectora getVelocidadLectora() {
        return velocidadLectora;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {

    }

    public void setTipoCurso(STipoCurso tipoCurso) {
        this.tipoCurso = tipoCurso;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setVelocidadLectora(SVelocidadLectora velocidadLectora) {
        this.velocidadLectora = velocidadLectora;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
