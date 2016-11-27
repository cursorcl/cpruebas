package cl.eos.persistence;

import javax.persistence.Version;

import cl.eos.interfaces.entity.IEntity;

public abstract class AEntity implements IEntity {

    private static final long serialVersionUID = -4501591109826791876L;
    
    @Version
    protected Integer version;
    
    @Override
    public Integer getVersion() {
        return version;
    }
    
    @Override
    public void setVersion(Integer version) {
        this.version = version;
    }

    /* (non-Javadoc)
     * @see cl.eos.interfaces.entity.IEntity#validate()
     */
    @Override
    public boolean validate() {
        return true;
    }
    
    
}
