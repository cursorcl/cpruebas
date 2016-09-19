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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.apache.log4j.Logger;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import cl.eos.Environment;
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
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

/**
 * Instancia de servicio para almacenamiento.
 * 
 * @author cursor
 */
public class PersistenceService implements IPersistenceService {

    static final Logger LOG = Logger.getLogger(PersistenceService.class);
    private EntityManagerFactory eFactory;
    private final static String NAME = "multi_cpruebas";

    /**
     * Constructor de la clase.
     */
    public PersistenceService() {

        Properties props = new Properties();

        props.put("javax.persistence.jdbc.user", "root");
        props.put("javax.persistence.jdbc.password", "admin");
        props.put("javax.persistence.jdbc.url", String.format("jdbc:mysql://localhost:3306/%s", Environment.database));
        props.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");
        props.put("eclipselink.allow-zero-id", "true");
        props.put("eclipselink.query-results-cache", "false");
        props.put("eclipselink.cache.shared.default", "false");
        
        //props.put("eclipselink.ddl-generation", "create-tables"); //create-or-extend-tables");
//        props.put("PersistenceUnitProperties.CREATE_JDBC_DDL_FILE", "create.sql");
//        props.put("eclipselink.ddl-generation.output-mode", "sql-script");
        
        eFactory = Persistence.createEntityManagerFactory(NAME, props);
        eFactory.getCache().evictAll();
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
        EntityManager eManager = eFactory.createEntityManager();
        eManager.getTransaction().begin();
        IEntity eMerged = eManager.merge(entity);
        eManager.persist(eMerged);
        eManager.getTransaction().commit();
        eManager.close();
        return eMerged;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cpruebas.osgi.persistence.IPersistenceService#delete(cl.eos.interfaces
     * .entity.IEntity)
     */
    @Override
    public IEntity delete(IEntity entity) {
        IEntity mEntity = null;
        try {
            EntityManager eManager = eFactory.createEntityManager();

            eManager.getTransaction().begin();
            mEntity = eManager.merge(entity);
            eManager.lock(mEntity, LockModeType.WRITE);
            eManager.remove(mEntity);
            eManager.getTransaction().commit();

            eManager.close();
        } catch (RollbackException exception) {
            mEntity = null;
            LOG.error(exception);
        }
        return mEntity;
    }

    @Override
    public void disconnect() {
        if (eFactory != null && eFactory.isOpen()) {
            eFactory.close();
        }
    }

    @Override
    public IEntity update(IEntity entity) {

        EntityManager eManager = eFactory.createEntityManager();
        eManager.getTransaction().begin();
        IEntity mEntity = eManager.merge(entity);
        eManager.lock(mEntity, LockModeType.OPTIMISTIC);
        eManager.persist(mEntity);
        eManager.getTransaction().commit();
        eManager.close();
        return mEntity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void findAll(final Class<? extends IEntity> entityClazz, final IPersistenceListener listener) {

        final Task<List<Object>> task = new Task<List<Object>>() {
            @Override
            protected List<Object> call() throws Exception {
                List<Object> lresults = null;
                String findAll = entityClazz.getSimpleName() + ".findAll";

                EntityManager eManager = eFactory.createEntityManager();
                eManager.getTransaction().begin();

                Query query = eManager.createNamedQuery(findAll);

                if (query != null) {
                    query.setHint(QueryHints.CACHE_STORE_MODE, HintValues.TRUE);
                    query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
                    try {
                        lresults = query.getResultList();
                        eManager.getTransaction().commit();
                    } catch (Exception e) {
                        eManager.getTransaction().rollback();
                        LOG.error(
                                "Error en el findAll de la entidad:" + entityClazz.getName() + " / " + e.getMessage());
                        LOG.error(e);
                    }
                }

                eManager.close();
                return lresults;
            }
        };
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                listener.onFindAllFinished(task.getValue());
            }
        });
        new Thread(task).start();

    }

    @Override
    public void find(final String namedQuery, final Map<String, Object> parameters,
            final IPersistenceListener listener) {

        final Task<List<Object>> task = new Task<List<Object>>() {
            @SuppressWarnings("unchecked")
            @Override
            protected List<Object> call() throws Exception {
                List<Object> lresults = null;
                EntityManager eManager = eFactory.createEntityManager();
                eManager.getTransaction().begin();

                Query query = eManager.createNamedQuery(namedQuery);
                if (query != null) {
                    query.setHint(QueryHints.CACHE_STORE_MODE, HintValues.TRUE);
                    if (parameters != null && !parameters.isEmpty()) {
                        for (Entry<String, Object> entry : parameters.entrySet()) {
                            query.setParameter(entry.getKey(), entry.getValue());
                        }
                    }
                    try {
                        lresults = query.setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();
                        eManager.getTransaction().commit();
                    } catch (Exception e) {
                        eManager.getTransaction().rollback();
                        LOG.error("Error en el find del namedQuery:" + namedQuery + " / " + e.getMessage());
                        LOG.error(e);
                    }

                }
                eManager.close();
                return lresults;
            }
        };
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, t -> listener.onFindFinished(task.getValue()));
        new Thread(task).start();

    }

    @Override
    public void findById(final Class<? extends IEntity> entityClazz, final Long id,
            final IPersistenceListener listener) {
        final Task<IEntity> task = new Task<IEntity>() {
            @Override
            protected IEntity call() throws Exception {
                IEntity lresult = null;
                String strEntity = entityClazz.getSimpleName();
                String strQuery = String.format("select c from %s c where c.id = :id", strEntity.toLowerCase());

                EntityManager eManager = eFactory.createEntityManager();
                eManager.getTransaction().begin();

                Query query = eManager.createQuery(strQuery);
                if (query != null) {
                    query.setHint(QueryHints.CACHE_STORE_MODE, HintValues.TRUE);
                    query.setParameter("id", id);
                    try {
                        lresult = (IEntity) query.setLockMode(LockModeType.PESSIMISTIC_WRITE).getSingleResult();
                        eManager.getTransaction().commit();
                    } catch (Exception e) {
                        eManager.getTransaction().rollback();
                        LOG.error(
                                "Error en el findById de la entidad:" + entityClazz.getName() + " / " + e.getMessage());
                        LOG.error(e);
                    }

                }
                eManager.close();
                return lresult;
            }
        };
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, t -> listener.onFound(task.getValue()));
        new Thread(task).start();

    }

    @SuppressWarnings("unchecked")
    @Override
    public void findByAllId(final Class<? extends IEntity> entityClazz, final Object[] id,
            final IPersistenceListener listener) {
        final Task<List<Object>> task = new Task<List<Object>>() {
            @Override
            protected List<Object> call() throws Exception {
                List<Object> lresult = null;
                StringBuffer ids = new StringBuffer();
                for (Object object : id) {
                    if (object instanceof Prueba) {
                        ids.append(((Prueba) object).getId());
                        ids.append(",");
                    }
                }
                int idLast = ids.lastIndexOf(",");
                String listaIds = ids.substring(0, idLast);
                String strEntity = entityClazz.getSimpleName();
                String strQuery = String.format("select c from %s c where c.id in (%s)", strEntity.toLowerCase(),
                        listaIds);

                EntityManager eManager = eFactory.createEntityManager();
                eManager.getTransaction().begin();
                Query query = eManager.createQuery(strQuery);
                if (query != null) {
                    query.setHint(QueryHints.CACHE_STORE_MODE, HintValues.TRUE);
                    try {
                        lresult = query.setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();
                    } catch (Exception e) {
                        eManager.getTransaction().rollback();
                        LOG.error("Error en el findByAllId de la entidad:" + entityClazz.getName() + " / "
                                + e.getMessage());
                        LOG.error(e);
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
    public void findByName(final Class<? extends IEntity> entityClazz, final String name,
            final IPersistenceListener listener) {
        final Task<IEntity> task = new Task<IEntity>() {
            @Override
            protected IEntity call() throws Exception {
                IEntity lresult = null;
                String strEntity = entityClazz.getSimpleName();

                EntityManager eManager = eFactory.createEntityManager();
                eManager.getTransaction().begin();
                Query query = eManager.createQuery(String.format("select c from %s c where c.name = :name", strEntity));
                if (query != null) {
                    query.setHint(QueryHints.CACHE_STORE_MODE, HintValues.TRUE);
                    query.setParameter("name", name);
                    try {
                        lresult = (IEntity) query.setLockMode(LockModeType.PESSIMISTIC_WRITE).getSingleResult();
                        eManager.getTransaction().commit();
                    } catch (Exception e) {
                        eManager.getTransaction().rollback();
                        LOG.error("Error en el findByName de la entidad:" + entityClazz.getName() + " / "
                                + e.getMessage());
                        LOG.error(e);
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
    public int executeUpdate(final String namedQuery, Map<String, Object> parameters) {

        EntityManager eManager = eFactory.createEntityManager();
        eManager.getTransaction().begin();
        Query query = eManager.createNamedQuery(namedQuery);
        for (Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        int res = 0;
        try {
            res = query.executeUpdate();
            eManager.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Error en el executeUpdate de:" + namedQuery + " / " + e.getMessage());
            eManager.getTransaction().rollback();

        }
        eManager.close();
        return res;
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

                String insert = "INSERT INTO " + entity + " values ( 0, " + parametros + ",?)";

                int row = 1;
                EntityManager eManager = eFactory.createEntityManager();
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
                            query.setParameter(indice++, 1);
                            query.executeUpdate();
                            eManager.getTransaction().commit();

                            updateMessage(String.format("Procesado[%d]: %s", row++, strRegister.toString().trim()));
                        } catch (Exception e) {
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

        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent arg0) {
                Pair<String, Pair<Integer, List<String>>> pair = task.getValue();
                final List<String> errores = pair.getSecond().getSecond();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {

                        if (errores.isEmpty()) {
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Importación desde excel");
                            alert.setHeaderText("Ha finalizado proceso de importación.");
                            alert.setContentText(" Se han importado [" + pair.getSecond().getFirst()
                                    + "] registros de [" + pair.getFirst() + "]");
                            alert.show();
                        } else {
                            final StringBuffer error = new StringBuffer();
                            for (String str : errores) {
                                error.append(str);
                                error.append("\n");
                            }
                            try {
                                Alert alert = new Alert(AlertType.ERROR);
                                alert.setTitle("Error de importación desde excel");
                                alert.setHeaderText("Se ha presentado algunos problemas");
                                alert.setContentText("Se grabará el archivo de log.");
                                alert.show();

                                FileChooser fileChooser = new FileChooser();
                                fileChooser.setInitialFileName(
                                        "import_" + entity + "_" + System.currentTimeMillis() + ".log");
                                fileChooser.setInitialDirectory(Utils.getDefaultDirectory());
                                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                                        "Archivo de log", "*.log");
                                fileChooser.getExtensionFilters().add(extFilter);
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
                        dlg.getDialogStage().hide();
                    }
                };
                Platform.runLater(r);
            }
        });
        task.setOnFailed((WorkerStateEvent t) -> {
            dlg.getDialogStage().hide();
            throw new RuntimeException(task.getException());
        });

        dlg.showWorkerProgress(task);
        Executors.newSingleThreadExecutor().execute(task);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IEntity> List<T> findAllSynchro(Class<T> entityClazz){
        List<T> lresults = null;
        String findAll = entityClazz.getSimpleName() + ".findAll";
        EntityManager eManager = eFactory.createEntityManager();
        eManager.getTransaction().begin();
        Query query = eManager.createNamedQuery(findAll);

        if (query != null) {
            query.setHint(QueryHints.CACHE_STORE_MODE, HintValues.TRUE);
            lresults = query.setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();
        }
        eManager.close();
        return lresults;
    }

    @SuppressWarnings("unchecked")
    public List<Object> findSynchro(final String namedQuery, final Map<String, Object> parameters) {

        List<Object> lresults = null;
        EntityManager eManager = eFactory.createEntityManager();
        eManager.getTransaction().begin();

        Query query = eManager.createNamedQuery(namedQuery);
        if (query != null) {
            query.setHint(QueryHints.CACHE_STORE_MODE, HintValues.TRUE);
            if (parameters != null && !parameters.isEmpty()) {
                for (Entry<String, Object> entry : parameters.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
            try {
                lresults = query.setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();
                eManager.getTransaction().commit();
            } catch (Exception e) {
                eManager.getTransaction().rollback();
                LOG.error("Error en el find del namedQuery:" + namedQuery + " / " + e.getMessage());
                LOG.error(e);
            }

        }
        eManager.close();
        return lresults;

    }


}