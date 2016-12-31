package cl.eos.provider.persistence;

import cl.eos.interfaces.entity.IEntity;

public abstract class AOperation implements IOperation  {

    
    @SuppressWarnings("unchecked")
    protected static Class<? extends IEntity> getClazz(String namedQuery) {
        String[] parts = namedQuery.split(".");
        Class<?> clazz;
        try {
            clazz = Class.forName("cl.eos.persistence.models." + parts[0]);
        } catch (ClassNotFoundException e) {
            return null;
        }
        return (Class<? extends IEntity>) clazz;
    }
}
