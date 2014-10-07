package cl.eos.interfaces.entity;

import java.util.List;

public abstract class PersistenceListenerAdapter implements IPersistenceListener {

	@Override
	public void onFindAllFinished(List<Object> list) {

	}

	@Override
	public void onFindFinished(List<Object> list) {

	}

	@Override
	public void onFound(IEntity entity) {

	}

	@Override
	public void onError(String error) {

	}

}
