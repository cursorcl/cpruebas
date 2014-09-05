package cl.eos.provider.persistence;

import java.util.List;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;
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
	public void findAll(final Class<? extends IEntity> entityClazz,
			final IPersistenceListener listener) {

		final Task<List<IEntity>> task = new Task<List<IEntity>>() {
			@Override
			protected List<IEntity> call() throws Exception {
				List<IEntity> lresults = null;
				String findAll = entityClazz.getSimpleName() + ".findAll";

				Query query = eManager.createNamedQuery(findAll);

				if (query != null) {
					lresults = (List<IEntity>) query.getResultList();
				}
				return lresults;
			}
		};
		task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent t) {
						listener.onFindAllFinished(task.getValue());
					}
				});
		new Thread(task).start();

		
		
	}

}
