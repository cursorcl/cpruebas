package cl.eos.interfaces.entity;

import java.util.List;

public interface IPersistenceListener {

    void onError(String error);

    void onFindAllFinished(List<Object> list);

    void onFindFinished(List<Object> list);

    void onFound(IEntity entity);
}
