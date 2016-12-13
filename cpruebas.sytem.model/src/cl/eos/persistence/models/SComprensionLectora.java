package cl.eos.persistence.models;

import cl.eos.persistence.AEntity;

public class SComprensionLectora extends AEntity {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;


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
    public boolean validate() {
        return true;
    }

}
