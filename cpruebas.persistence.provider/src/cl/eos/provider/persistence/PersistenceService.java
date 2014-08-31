package cl.eos.provider.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.IPersistenceService;

/**
 * Instancia de servicio para almacenamiento.
 * 
 * @author cursor
 */
public class PersistenceService implements IPersistenceService {

	private EntityManager eManager;
	private EntityManagerFactory eFactory;

	/**
	 * Constructor de la clase.
	 */
	public PersistenceService() {
		eFactory = Persistence.createEntityManagerFactory("cpruebas");
		eManager = eFactory.createEntityManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cpruebas.osgi.persistence.IPersistenceService#save(cl.eos.interfaces.
	 * entity.IEntity)
	 */
	@Override
	public void save(IEntity entity) {
		eManager.getTransaction().begin();
		eManager.persist(entity);
		eManager.getTransaction().commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cpruebas.osgi.persistence.IPersistenceService#delete(cl.eos.interfaces
	 * .entity.IEntity)
	 */
	@Override
	public void delete(IEntity entity) {
		eManager.getTransaction().begin();
		eManager.remove(entity);
		eManager.getTransaction().commit();
	}

	@Override
	public void disconnect() {
		if (eManager != null && eManager.isOpen()) {
			eManager.close();
		}
		if (eFactory != null && eFactory.isOpen()) {
			eFactory.close();
		}
	}

	@Override
	public void update(IEntity entity) {
		eManager.getTransaction().begin();
		eManager.persist(entity);
		eManager.getTransaction().commit();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IEntity> getAll(IEntity entity) {
		List<IEntity> results = null;
		String findAll = entity.getClass().getSimpleName() + ".findAll";

		Query query = eManager.createNamedQuery(findAll);

		if (query != null) {
			results = (List<IEntity>) query.getResultList();
		}
		return results;
	}

}
