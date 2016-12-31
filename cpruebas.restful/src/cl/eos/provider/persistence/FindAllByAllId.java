package cl.eos.provider.persistence;

import java.util.ArrayList;
import java.util.List;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.restful.RestfulClient;

public class FindAllByAllId extends AOperation {
    Class<? extends IEntity> entityClazz;
    Long[] id;

    @Override
    public List<Object> execute() {
        List<Object> lresult = new ArrayList<>();
        for (Long lId : id) {
            List<? extends IEntity> lList = RestfulClient.get(entityClazz, lId);
            for (Object o : lList) {
                lresult.add(o);
            }
        }

        return lresult;
    }

    public static class Builder {
        private Class<? extends IEntity> entityClazz;
        private Long[] id;

        public Builder entityClazz(Class<? extends IEntity> entityClazz) {
            this.entityClazz = entityClazz;
            return this;
        }

        public Builder id(Long...id) {
            this.id = id;
            return this;
        }
        public FindAllByAllId build() {
            return new FindAllByAllId(this);
        }
    }

    private FindAllByAllId(Builder builder) {
        this.entityClazz = builder.entityClazz;
        this.id = builder.id;
    }
}
