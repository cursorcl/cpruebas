package cl.eos.persistence;

import cl.eos.interfaces.entity.IEntity;

public abstract class AEntity implements IEntity {

    private static final long serialVersionUID = -4501591109826791876L;
    
    /* (non-Javadoc)
     * @see cl.eos.interfaces.entity.IEntity#validate()
     */
    @Override
    public boolean validate() {
        return true;
    }
    
    
}
