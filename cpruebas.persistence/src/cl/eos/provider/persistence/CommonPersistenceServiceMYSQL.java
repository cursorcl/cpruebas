package cl.eos.provider.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import cl.eos.imp.view.ProgressForm;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;
import cl.eos.persistence.IPersistenceService;
import cl.eos.persistence.models.Prueba;
import cl.eos.util.Pair;
import cl.eos.util.Utils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

/**
 * Instancia de servicio para almacenamiento.
 *
 * @author cursor
 */
public class CommonPersistenceServiceMYSQL implements IPersistenceService {

	static final Logger LOG = Logger.getLogger(CommonPersistenceServiceMYSQL.class.getName());
	private final static String NAME_COMUN = "_comun";

	@PersistenceContext(unitName = "_comun")
	private final EntityManagerFactory eFactoryComun;

	private final Lock _mutex = new ReentrantLock(true);

	/**
	 * Constructor de la clase.
	 */
	public CommonPersistenceServiceMYSQL() {

		final Properties props = new Properties();

		props.put("javax.persistence.jdbc.user", "eosorio");
		props.put("javax.persistence.jdbc.password", "_l2j1rs2");
		props.put("javax.persistence.jdbc.url",
				String.format("jdbc:mysql://170.239.86.231:3306/cpruebas_comun"));
		props.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");
		props.put("eclipselink.allow-zero-id", "true");
		props.put("eclipselink.query-results-cache", "false");
		props.put("eclipselink.cache.shared.default", "false");

		eFactoryComun = Persistence.createEntityManagerFactory(CommonPersistenceServiceMYSQL.NAME_COMUN, props);
		eFactoryComun.getCache().evictAll();
	}


	@Override
	public void disconnect() {
		if (eFactoryComun != null && eFactoryComun.isOpen()) {
			eFactoryComun.close();
		}
	}

	@Override
	public int executeUpdate(final String namedQuery, Map<String, Object> parameters) {

		final EntityManager eManager = eFactoryComun.createEntityManager();
		eManager.getTransaction().begin();
		final Query query = eManager.createNamedQuery(namedQuery);
		for (final Entry<String, Object> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		int res = 0;
		try {
			res = query.executeUpdate();
			eManager.getTransaction().commit();
		} catch (final Exception e) {
			CommonPersistenceServiceMYSQL.LOG.severe("Error en el executeUpdate de:" + namedQuery + " / " + e.getMessage());
			eManager.getTransaction().rollback();

		}
		eManager.close();
		return res;
	}

	@Override
	public void find(final String namedQuery, final Map<String, Object> parameters,
			final IPersistenceListener listener) {

		final Task<List<Object>> task = new Task<List<Object>>() {
			@SuppressWarnings("unchecked")
			@Override
			protected List<Object> call() throws Exception {
				List<Object> lresults = null;
				final EntityManager eManager = eFactoryComun.createEntityManager();
				eManager.getTransaction().begin();

				final Query query = eManager.createNamedQuery(namedQuery);
				if (query != null) {
					query.setHint(QueryHints.CACHE_STORE_MODE, HintValues.TRUE);
					if (parameters != null && !parameters.isEmpty()) {
						for (final Entry<String, Object> entry : parameters.entrySet()) {
							query.setParameter(entry.getKey(), entry.getValue());
						}
					}
					try {
						lresults = query.setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();
						eManager.getTransaction().commit();
					} catch (final Exception e) {
						eManager.getTransaction().rollback();
						CommonPersistenceServiceMYSQL.LOG
								.severe("Error en el find del namedQuery:" + namedQuery + " / " + e.getMessage());
					}

				}
				eManager.close();
				return lresults;
			}
		};
		task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, t -> listener.onFindFinished(task.getValue()));
		new Thread(task).start();

	}

	@SuppressWarnings("unchecked")
	@Override
	public void findAll(final Class<? extends IEntity> entityClazz, final IPersistenceListener listener) {

		final Task<List<Object>> task = new Task<List<Object>>() {
			@Override
			protected List<Object> call() throws Exception {
				List<Object> lresults = null;
				final String findAll = entityClazz.getSimpleName() + ".findAll";

				final EntityManager eManager = eFactoryComun.createEntityManager();
				eManager.getTransaction().begin();

				final Query query = eManager.createNamedQuery(findAll);

				if (query != null) {
					query.setHint(QueryHints.CACHE_STORE_MODE, HintValues.TRUE);
					query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
					try {
						lresults = query.getResultList();
						eManager.getTransaction().commit();
					} catch (final Exception e) {
						eManager.getTransaction().rollback();
						CommonPersistenceServiceMYSQL.LOG.severe(
								"Error en el findAll de la entidad:" + entityClazz.getName() + " / " + e.getMessage());
					}
				}

				eManager.close();
				return lresults;
			}
		};
		task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, t -> listener.onFindAllFinished(task.getValue()));
		new Thread(task).start();

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IEntity> List<T> findAllSynchro(Class<T> entityClazz) {

		_mutex.lock();
		List<T> lresults = null;
		final String findAll = entityClazz.getSimpleName() + ".findAll";
		final EntityManager eManager = eFactoryComun.createEntityManager();
		// eManager.getTransaction().begin();
		final Query query = eManager.createNamedQuery(findAll);
		if (query != null) {
			query.setHint(QueryHints.CACHE_STORE_MODE, HintValues.TRUE);
			// lresults =
			// query.setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();
			lresults = query.getResultList();
		}
		eManager.close();
		_mutex.unlock();
		return lresults;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void findByAllId(final Class<? extends IEntity> entityClazz, final Object[] id,
			final IPersistenceListener listener) {
		final Task<List<Object>> task = new Task<List<Object>>() {
			@Override
			protected List<Object> call() throws Exception {
				List<Object> lresult = null;
				final StringBuffer ids = new StringBuffer();
				for (final Object object : id) {
					if (object instanceof Prueba) {
						ids.append(((Prueba) object).getId());
						ids.append(",");
					}
				}
				final int idLast = ids.lastIndexOf(",");
				final String listaIds = ids.substring(0, idLast);
				final String strEntity = entityClazz.getSimpleName();
				final String strQuery = String.format("select c from %s c where c.id in (%s)", strEntity.toLowerCase(),
						listaIds);

				final EntityManager eManager = eFactoryComun.createEntityManager();
				eManager.getTransaction().begin();
				final Query query = eManager.createQuery(strQuery);
				if (query != null) {
					query.setHint(QueryHints.CACHE_STORE_MODE, HintValues.TRUE);
					try {
						lresult = query.setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();
					} catch (final Exception e) {
						eManager.getTransaction().rollback();
						CommonPersistenceServiceMYSQL.LOG.severe("Error en el findByAllId de la entidad:"
								+ entityClazz.getName() + " / " + e.getMessage());
					}
				}
				eManager.close();
				return lresult;
			}
		};
		task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, t -> listener.onFindAllFinished(task.getValue()));
		new Thread(task).start();

	}

	@Override
	public void findById(final Class<? extends IEntity> entityClazz, final Long id,
			final IPersistenceListener listener) {
		final Task<IEntity> task = new Task<IEntity>() {
			@Override
			protected IEntity call() throws Exception {
				IEntity lresult = null;
				final String strEntity = entityClazz.getSimpleName();
				final String strQuery = String.format("select c from %s c where c.id = :id", strEntity.toLowerCase());

				final EntityManager eManager = eFactoryComun.createEntityManager();
				eManager.getTransaction().begin();

				final Query query = eManager.createQuery(strQuery);
				if (query != null) {
					query.setHint(QueryHints.CACHE_STORE_MODE, HintValues.TRUE);
					query.setParameter("id", id);
					try {
						lresult = (IEntity) query.setLockMode(LockModeType.PESSIMISTIC_WRITE).getSingleResult();
						eManager.getTransaction().commit();
					} catch (final Exception e) {
						eManager.getTransaction().rollback();
						CommonPersistenceServiceMYSQL.LOG.severe(
								"Error en el findById de la entidad:" + entityClazz.getName() + " / " + e.getMessage());
					}

				}
				eManager.close();
				return lresult;
			}
		};
		task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, t -> listener.onFound(task.getValue()));
		new Thread(task).start();

	}

	@Override
	public void findByName(final Class<? extends IEntity> entityClazz, final String name,
			final IPersistenceListener listener) {
		final Task<IEntity> task = new Task<IEntity>() {
			@Override
			protected IEntity call() throws Exception {
				IEntity lresult = null;
				final String strEntity = entityClazz.getSimpleName();

				final EntityManager eManager = eFactoryComun.createEntityManager();
				eManager.getTransaction().begin();
				final Query query = eManager
						.createQuery(String.format("select c from %s c where c.name = :name", strEntity));
				if (query != null) {
					query.setHint(QueryHints.CACHE_STORE_MODE, HintValues.TRUE);
					query.setParameter("name", name);
					try {
						lresult = (IEntity) query.setLockMode(LockModeType.PESSIMISTIC_WRITE).getSingleResult();
						eManager.getTransaction().commit();
					} catch (final Exception e) {
						eManager.getTransaction().rollback();
						CommonPersistenceServiceMYSQL.LOG.severe("Error en el findByName de la entidad:"
								+ entityClazz.getName() + " / " + e.getMessage());
					}

				}
				eManager.close();
				return lresult;
			}
		};
		task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, t -> listener.onFound(task.getValue()));
		task.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, t -> {
			throw new RuntimeException(task.getException());
		});
		new Thread(task).start();

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<IEntity> findSynchro(final String namedQuery, final Map<String, Object> parameters) {

		List<IEntity> lresults = null;
		final EntityManager eManager = eFactoryComun.createEntityManager();
		eManager.getTransaction().begin();

		final Query query = eManager.createNamedQuery(namedQuery);
		if (query != null) {
			query.setHint(QueryHints.CACHE_STORE_MODE, HintValues.TRUE);
			if (parameters != null && !parameters.isEmpty()) {
				for (final Entry<String, Object> entry : parameters.entrySet()) {
					query.setParameter(entry.getKey(), entry.getValue());
				}
			}
			try {
				lresults = query.setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();
				eManager.getTransaction().commit();
			} catch (final Exception e) {
				eManager.getTransaction().rollback();
				CommonPersistenceServiceMYSQL.LOG
						.severe("Error en el find del namedQuery:" + namedQuery + " / " + e.getMessage());
			}

		}
		eManager.close();
		return lresults;

	}

	@Override
	public IEntity findSynchroById(Class<? extends IEntity> entityClazz, Long id) {
		IEntity lresult = null;
		final String strEntity = entityClazz.getSimpleName();
		final String strQuery = String.format("select c from %s c where c.id = :id", strEntity.toLowerCase());

		final EntityManager eManager = eFactoryComun.createEntityManager();
		eManager.getTransaction().begin();

		final Query query = eManager.createQuery(strQuery);
		if (query != null) {
			query.setHint(QueryHints.CACHE_STORE_MODE, HintValues.TRUE);
			query.setParameter("id", id);
			try {
				lresult = (IEntity) query.setLockMode(LockModeType.PESSIMISTIC_WRITE).getSingleResult();
				eManager.getTransaction().commit();
			} catch (final Exception e) {
				eManager.getTransaction().rollback();
				CommonPersistenceServiceMYSQL.LOG
						.severe("Error en el findById de la entidad:" + entityClazz.getName() + " / " + e.getMessage());
			}

		}
		eManager.close();
		return lresult;
	}

	@Override
	public void insert(final String entity, final List<Object> list, final IPersistenceListener listener) {

		final ProgressForm dlg = new ProgressForm();
		dlg.title("Importando datos");
		dlg.message("Esto tomará algunos minutos.");

		final Task<Pair<String, Pair<Integer, List<String>>>> task = new Task<Pair<String, Pair<Integer, List<String>>>>() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			protected Pair<String, Pair<Integer, List<String>>> call() {
				final String name = entity;
				final StringBuffer string = new StringBuffer();
				final List<Object> filas = list;
				if (filas.size() > 0) {
					final List columnas = (List) filas.get(0);
					for (int i = 0; i < columnas.size(); i++) {
						string.append("?,");
					}
				}
				final List<String> errores = new ArrayList<String>();
				final Integer size = new Integer(filas.size());
				final Pair<String, Pair<Integer, List<String>>> pair = new Pair<String, Pair<Integer, List<String>>>();
				pair.setFirst(name);
				pair.setSecond(new Pair<Integer, List<String>>(size, errores));
				final int largo = string.lastIndexOf(",");
				final String parametros = string.substring(0, largo);

				final String insert = "INSERT INTO " + entity + " values ( 0, " + parametros + ",?)";

				int row = 1;
				final EntityManager eManager = eFactoryComun.createEntityManager();
				for (final Object fila : filas) {
					int indice = 1;

					final Query query = eManager.createNativeQuery(insert);
					if (query != null) {
						try {
							final StringBuffer strRegister = new StringBuffer();
							eManager.getTransaction().begin();
							final List<Object> columnas = (List<Object>) fila;
							for (int i = 0; i < columnas.size(); i++) {
								query.setParameter(indice++, columnas.get(i));
								strRegister.append(columnas.get(i));
								strRegister.append(" ");
							}
							query.setParameter(indice++, 1);
							query.executeUpdate();
							eManager.getTransaction().commit();

							updateMessage(String.format("Procesado[%d]: %s", row++, strRegister.toString().trim()));
						} catch (final Exception e) {
							updateMessage("Error en la fila " + row);
							errores.add(String.format("Error [%s] en la fila %d", e.getCause(), row++));
							eManager.getTransaction().rollback();
						}
					}
				}
				eManager.close();
				return pair;
			}
		};

		task.setOnSucceeded(arg0 -> {
			final Pair<String, Pair<Integer, List<String>>> pair = task.getValue();
			final List<String> errores = pair.getSecond().getSecond();
			final Runnable r = () -> {

				if (errores.isEmpty()) {
					final Alert alert1 = new Alert(AlertType.INFORMATION);
					alert1.setTitle("Importación desde excel");
					alert1.setHeaderText("Ha finalizado proceso de importación.");
					alert1.setContentText(" Se han importado [" + pair.getSecond().getFirst() + "] registros de ["
							+ pair.getFirst() + "]");
					alert1.show();
				} else {
					final StringBuffer error = new StringBuffer();
					for (final String str : errores) {
						error.append(str);
						error.append("\n");
					}
					try {
						final Alert alert2 = new Alert(AlertType.ERROR);
						alert2.setTitle("Error de importación desde excel");
						alert2.setHeaderText("Se ha presentado algunos problemas");
						alert2.setContentText("Se grabará el archivo de log.");
						alert2.show();

						final FileChooser fileChooser = new FileChooser();
						fileChooser.setInitialFileName("import_" + entity + "_" + System.currentTimeMillis() + ".log");
						fileChooser.setInitialDirectory(Utils.getDefaultDirectory());
						final FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivo de log",
								"*.log");
						fileChooser.getExtensionFilters().add(extFilter);
						final File file = fileChooser.showSaveDialog(null);
						if (file != null) {
							final FileWriter writer = new FileWriter(file);
							writer.write(error.toString());
							writer.close();
						}

					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
				dlg.getDialogStage().hide();
			};
			Platform.runLater(r);
		});
		task.setOnFailed((WorkerStateEvent t) -> {
			dlg.getDialogStage().hide();
			throw new RuntimeException(task.getException());
		});

		dlg.showWorkerProgress(task);
		Executors.newSingleThreadExecutor().execute(task);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * cpruebas.osgi.persistence.IPersistenceService#save(cl.eos.interfaces.
	 * entity.IEntity)
	 */
	@Override
	public IEntity save(IEntity entity) {
		final EntityManager eManager = eFactoryComun.createEntityManager();
		if(eManager == null)
			return null;
		eManager.getTransaction().begin();
		final IEntity eMerged = eManager.merge(entity);
		eManager.persist(eMerged);
		eManager.getTransaction().commit();
		eManager.close();
		return eMerged;
	}

	@Override
	public IEntity update(IEntity entity) {

		final EntityManager eManager = eFactoryComun.createEntityManager();
		if(eManager == null)
			return null;

		eManager.getTransaction().begin();
		final IEntity mEntity = eManager.merge(entity);
		eManager.lock(mEntity, LockModeType.OPTIMISTIC);
		eManager.persist(mEntity);
		eManager.getTransaction().commit();
		eManager.close();
		return mEntity;
	}
	
	@Override
	public IEntity delete(IEntity entity) {
		IEntity mEntity = null;
		try {
			final EntityManager eManager = eFactoryComun.createEntityManager();
			if(eManager == null)
				return null;
			
			eManager.getTransaction().begin();
			mEntity = eManager.merge(entity);
			eManager.lock(mEntity, LockModeType.PESSIMISTIC_WRITE);
			eManager.remove(mEntity);
			eManager.getTransaction().commit();

			eManager.close();
		} catch (final RollbackException exception) {
			mEntity = null;
			CommonPersistenceServiceMYSQL.LOG.severe(exception.getMessage());
		}
		return mEntity;
	}


	
	

}