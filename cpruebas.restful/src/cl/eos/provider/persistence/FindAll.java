package cl.eos.provider.persistence;

import java.util.List;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;
import cl.eos.restful.RestFul2Entity;

public class FindAll extends AOperation {

    Class<? extends IEntity> entityClazz;
    IPersistenceListener listener;

    @SuppressWarnings("unchecked")
    @Override
    public List<Object> execute() {
        if(entityClazz == null) return null;
        
        List<? extends IEntity> result = RestFul2Entity.getAll(entityClazz);
        return (List<Object>) result;
    }

    public static class Builder {
        private Class<? extends IEntity> entityClazz;
        private IPersistenceListener listener;

        public Builder entityClazz(Class<? extends IEntity> entityClazz) {
            this.entityClazz = entityClazz;
            return this;
        }

        public Builder listener(IPersistenceListener listener) {
            this.listener = listener;
            return this;
        }

        public FindAll build() {
            return new FindAll(this);
        }
    }

    private FindAll(Builder builder) {
        this.entityClazz = builder.entityClazz;
        this.listener = builder.listener;
    }
}
