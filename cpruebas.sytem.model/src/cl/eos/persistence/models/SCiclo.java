package cl.eos.persistence.models;

import cl.eos.persistence.AEntity;

public class SCiclo extends AEntity {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;

    public SCiclo() {
        // TODO Auto-generated constructor stub
    }
    
    
    
    public SCiclo(Long id, String name) {
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
        return false;
    }

}
