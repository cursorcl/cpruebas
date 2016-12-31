package cl.eos.provider.persistence;

import java.util.List;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.restful.RestfulClient;

public class FindById extends AOperation {
    Class<? extends IEntity> entityClazz;
    Long id;

    @Override
    public List<Object> execute() {
        List<? extends IEntity> result = RestfulClient.get(entityClazz, id);
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    public static class Builder {
        private Class<? extends IEntity> entityClazz;
        private Long id;

        public Builder entityClazz(Class<? extends IEntity> entityClazz) {
            this.entityClazz = entityClazz;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public FindById build() {
            return new FindById(this);
        }
    }

    private FindById(Builder builder) {
        this.entityClazz = builder.entityClazz;
        this.id = builder.id;
    }
}
