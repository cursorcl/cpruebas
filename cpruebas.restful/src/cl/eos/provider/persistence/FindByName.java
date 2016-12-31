package cl.eos.provider.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;
import cl.eos.restful.RestfulClient;

public class FindByName extends AOperation {

    @Override
    public List<Object> execute() {
        Class<? extends IEntity> entityClazz;
        String name;
        IPersistenceListener listener;

        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        List<? extends IEntity> result = RestfulClient.getByParameters(entityClazz, params);
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    public static class Builder {
        public FindByName build() {
            return new FindByName(this);
        }
    }

    private FindByName(Builder builder) {
    }
}
