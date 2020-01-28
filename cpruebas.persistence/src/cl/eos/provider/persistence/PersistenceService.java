package cl.eos.provider.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;
import cl.eos.interfaces.view.FXDialogs;
import cl.eos.persistence.IPersistenceService;
import cl.eos.persistence.models.Prueba;
import cl.eos.util.Pair;
import cl.eos.util.Utils;

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
		try {
			eManager.getTransaction().begin();
			eManager.remove(entity);
			eManager.getTransaction().commit();
		} catch (RollbackException exception) {
			exception.getLocalizedMessage();
		}
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

		final Task<List<Object>> task = new Task<List<Object>>() {
			@Override
			protected List<Object> call() throws Exception {
				List<Object> lresults = null;
				String findAll = entityClazz.getSimpleName() + ".findAll";

				Query query = eManager.createNamedQuery(findAll);

				if (query != null) {
					lresults = query.getResultList();
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

	@Override
	public void find(final String namedQuery,
			final Map<String, Object> parameters,
			final IPersistenceListener listener) {

		final Task<List<Object>> task = new Task<List<Object>>() {
			@SuppressWarnings("unchecked")
			@Override
			protected List<Object> call() throws Exception {
				List<Object> lresults = null;
				Query query = eManager.createNamedQuery(namedQuery);
				if (query != null) {
					if (parameters != null && !parameters.isEmpty()) {
						for (Entry<String, Object> entry : parameters
								.entrySet()) {
							try {
								query.setParameter(entry.getKey(),
										entry.getValue());
							} catch (Exception exc) {
								exc.printStackTrace();
							}
						}
					}
					lresults = query.getResultList();

				}
				return lresults;
			}
		};
		task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent t) {
						listener.onFindFinished(task.getValue());
					}
				});
		new Thread(task).start();

	}

	@Override
	public void findById(final Class<? extends IEntity> entityClazz,
			final Long id, final IPersistenceListener listener) {
		final Task<IEntity> task = new Task<IEntity>() {
			@Override
			protected IEntity call() throws Exception {
				IEntity lresult = null;
				String strEntity = entityClazz.getSimpleName();
				String strQuery = String.format(
						"select c from %s c where c.id = :id",
						strEntity.toLowerCase());
				Query query = eManager.createQuery(strQuery);
				if (query != null) {
					query.setParameter("id", id);
					lresult = (IEntity) query.getSingleResult();
				}
				return lresult;
			}
		};
		task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent t) {
						listener.onFound(task.getValue());
					}
				});
		new Thread(task).start();

	}

	@Override
	public void findByAllId(final Class<? extends IEntity> entityClazz,
			final Object[] id, final IPersistenceListener listener) {
		final Task<List> task = new Task<List>() {
			@Override
			protected List call() throws Exception {
				List<IEntity> lresult = null;
				StringBuffer ids = new StringBuffer();
				for (Object object : id) {
					if (object instanceof Prueba) {
						ids.append(((Prueba) object).getId());
						ids.append(",");
					}
				}
				if (ids != null) {
					int idLast = ids.lastIndexOf(",");
					String listaIds = ids.substring(0, idLast);
					String strEntity = entityClazz.getSimpleName();
					String strQuery = String.format(
							"select c from %s c where c.id in ("
									+ listaIds.toString() + ")",
							strEntity.toLowerCase());
					Query query = eManager.createQuery(strQuery);
					if (query != null) {

						lresult = query.getResultList();
					}
				}
				return lresult;
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

	@Override
	public void findByName(final Class<? extends IEntity> entityClazz,
			final String name, final IPersistenceListener listener) {
		final Task<IEntity> task = new Task<IEntity>() {
			@Override
			protected IEntity call() throws Exception {
				IEntity lresult = null;
				String strEntity = entityClazz.getSimpleName();
				Query query = eManager.createQuery(String.format(
						"select c from %s c where c.name = :name", strEntity));
				if (query != null) {
					query.setParameter("name", name);
					lresult = (IEntity) query.getSingleResult();
				}
				return lresult;
			}
		};
		task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
				new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent t) {
						listener.onFound(task.getValue());
					}
				});
		new Thread(task).start();

	}

	@Override
	public int executeUpdate(final String namedQuery,
			Map<String, Object> parameters) {

		Query query = eManager.createNamedQuery(namedQuery);
		for (Entry<String, Object> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query.executeUpdate();
	}

	@Override
	public void insert(final String entity, final List<Object> list,
			final IPersistenceListener listener) {

		final Task<Pair<String, Pair<Integer, List<String>>>> task = new Task<Pair<String, Pair<Integer, List<String>>>>() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			protected Pair<String, Pair<Integer, List<String>>> call() {
				String name = entity;
				StringBuffer string = new StringBuffer();
				List<Object> filas = list;
				if (filas.size() > 0) {
					List columnas = (List) filas.get(0);
					for (int i = 0; i < columnas.size(); i++) {
						string.append("?,");
					}
				}
				List<String> errores = new ArrayList<String>();
				Integer size = new Integer(filas.size());
				Pair<String, Pair<Integer, List<String>>> pair = new Pair<String, Pair<Integer, List<String>>>();
				pair.setFirst(name);
				pair.setSecond(new Pair<Integer, List<String>>(size, errores));
				int largo = string.lastIndexOf(",");
				String parametros = string.substring(0, largo);

				String insert = "INSERT INTO " + entity + " values ( 0, "
						+ parametros + ")";

				int row = 1;
				for (Object fila : filas) {
					int indice = 1;
					Query query = eManager.createNativeQuery(insert);
					if (query != null) {
						try {
							StringBuffer strRegister = new StringBuffer();
							eManager.getTransaction().begin();
							List<Object> columnas = ((List<Object>) fila);
							for (int i = 0; i < columnas.size(); i++) {
								query.setParameter(indice++, columnas.get(i));
								strRegister.append(columnas.get(i));
								strRegister.append(" ");
							}
							query.executeUpdate();
							eManager.getTransaction().commit();
							updateMessage(String.format("Procesado[%d]: %s",
									row++, strRegister.toString().trim()));
						} catch (Exception e) {
							updateMessage("Error en la fila " + row);
							errores.add(String.format(
									"Error [%s] en la fila %d", e.getCause(),
									row++));
							eManager.getTransaction().rollback();
						}
					}
				}
				return pair;
			}
		};

		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				Pair<String, Pair<Integer, List<String>>> pair = task
						.getValue();
				final List<String> errores = pair.getSecond().getSecond();
				Runnable r = new Runnable() {
					@Override
					public void run() {

						if (errores.isEmpty()) {
							FXDialogs.showInformation("Importación desde excel", "Ha finalizado proceso de importación de ["
									+ pair.getSecond()
									.getFirst()
							+ "] registros para tabla ["
							+ pair.getFirst() + "]");
						} else {
							final StringBuffer error = new StringBuffer();
							for (String str : errores) {
								error.append(str);
								error.append("\n");
							}
							try {
								FXDialogs.showError("Error de importación desde Excel", "Se grabará en el archivo de log.");

								FileChooser fileChooser = new FileChooser();
								fileChooser.setInitialFileName("import_"
										+ entity + "_"
										+ System.currentTimeMillis() + ".log");
								fileChooser.setInitialDirectory(Utils
										.getDefaultDirectory());
								FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
										"Archivo de log", "*.log");
								fileChooser.getExtensionFilters()
										.add(extFilter);
								File file = fileChooser.showSaveDialog(null);
								if (file != null) {
									FileWriter writer = new FileWriter(file);
									writer.write(error.toString());
									writer.close();
								}

							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}

				};
				Platform.runLater(r);
			}
		});
		FXDialogs.showProgressIndicator("Importando datos", "Esto tomará algunos minutos.", task);
		Executors.newSingleThreadExecutor().execute(task);

	}

	public List<Object> findAllSynchro(
			final Class<? extends IEntity> entityClazz) {
		List<Object> lresults = null;
		String findAll = entityClazz.getSimpleName() + ".findAll";

		Query query = eManager.createNamedQuery(findAll);

		if (query != null) {
			lresults = query.getResultList();
		}
		return lresults;
	}

}