package cl.eos.interfaces.entity;

import java.util.List;

public interface IPersistenceListener {

	void onFindAllFinished(List<IEntity> list);
}
