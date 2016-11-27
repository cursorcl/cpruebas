package cl.eos.provider.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import cl.eos.imp.view.ProgressForm;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;
import cl.eos.persistence.IPersistenceService;
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
public class RestPersistenceServiceRESTFUL implements IPersistenceService {

    static final Logger LOG = Logger.getLogger(RestPersistenceServiceRESTFUL.class.getName());
    private final static String NAME = "multi_cpruebas";

    /**
     * Constructor de la clase.
     */
    public RestPersistenceServiceRESTFUL() {

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
        
        return mEntity;
    }

    @Override
    public void disconnect() {
    }

    @Override
    public int executeUpdate(final String namedQuery, Map<String, Object> parameters) {

        int res = 0;
        return res;
    }

    @Override
    public void find(final String namedQuery, final Map<String, Object> parameters,
            final IPersistenceListener listener) {

        final Task<List<Object>> task = new Task<List<Object>>() {
            @Override
            protected List<Object> call() throws Exception {
                List<Object> lresults = null;

                return lresults;
            }
        };
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, t -> listener.onFindFinished(task.getValue()));
        new Thread(task).start();

    }

    @Override
    public void findAll(final Class<? extends IEntity> entityClazz, final IPersistenceListener listener) {

        final Task<List<Object>> task = new Task<List<Object>>() {
            @Override
            protected List<Object> call() throws Exception {
                List<Object> lresults = null;
                return lresults;
            }
        };
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, t -> listener.onFindAllFinished(task.getValue()));
        new Thread(task).start();

    }

    @Override
    public <T extends IEntity> List<T> findAllSynchro(Class<T> entityClazz) {
        List<T> lresults = null;
        return lresults;
    }

    @Override
    public void findByAllId(final Class<? extends IEntity> entityClazz, final Object[] id,
            final IPersistenceListener listener) {
        final Task<List<Object>> task = new Task<List<Object>>() {
            @Override
            protected List<Object> call() throws Exception {
                List<Object> lresult = null;
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
    public List<Object> findSynchro(final String namedQuery, final Map<String, Object> parameters) {

        List<Object> lresults = null;
        return lresults;

    }

    @Override
    public IEntity findSynchroById(Class<? extends IEntity> entityClazz, Long id) {
        IEntity lresult = null;
        return lresult;
    }

    @Override
    public void insert(final String entity, final List<Object> list, final IPersistenceListener listener) {

        final ProgressForm dlg = new ProgressForm();
        dlg.title("Importando datos");
        dlg.message("Esto tomará algunos minutos.");

        final Task<Pair<String, Pair<Integer, List<String>>>> task = new Task<Pair<String, Pair<Integer, List<String>>>>() {
            @Override
            protected Pair<String, Pair<Integer, List<String>>> call() {
                final Pair<String, Pair<Integer, List<String>>> pair = new Pair<String, Pair<Integer, List<String>>>();
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
        final IEntity eMerged = null;
        return eMerged;
    }

    @Override
    public IEntity update(IEntity entity) {

        final IEntity mEntity = null;
        return mEntity;
    }

}