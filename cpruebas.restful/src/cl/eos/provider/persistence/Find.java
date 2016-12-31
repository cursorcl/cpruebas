package cl.eos.provider.persistence;

import java.util.List;
import java.util.Map;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;
import cl.eos.restful.RestFul2Entity;
import cl.eos.restful.RestfulClient;

public class Find extends AOperation {

    String namedQuery;
    Map<String, Object> parameters;
    IPersistenceListener listener;

    @SuppressWarnings("unchecked")
    @Override
    public List<Object> execute() {
        List<Object> lresults = null;
        Class<? extends IEntity> clazz = getClazz(namedQuery);
        if (clazz == null)
            return null;

        if (namedQuery.contains("findAll")) {
            lresults = (List<Object>) RestFul2Entity.getAll(clazz);
        } else {
            lresults = (List<Object>) RestfulClient.getByParameters(clazz, parameters);
        }

        return lresults;
    }

    public static class Builder {
        private String namedQuery;
        private Map<String, Object> parameters;
        private IPersistenceListener listener;

        public Builder namedQuery(String namedQuery) {
            this.namedQuery = namedQuery;
            return this;
        }

        public Builder parameters(Map<String, Object> parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder listener(IPersistenceListener listener) {
            this.listener = listener;
            return this;
        }

        public Find build() {
            return new Find(this);
        }
    }

    private Find(Builder builder) {
        this.namedQuery = builder.namedQuery;
        this.parameters = builder.parameters;
        this.listener = builder.listener;
    }
}
