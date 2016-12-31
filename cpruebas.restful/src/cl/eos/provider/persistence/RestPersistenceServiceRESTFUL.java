package cl.eos.provider.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import cl.eos.imp.view.ProgressForm;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.entity.IPersistenceListener;
import cl.eos.persistence.IPersistenceService;
import cl.eos.persistence.models.STipoPrueba;
import cl.eos.restful.RestFul2Entity;
import cl.eos.restful.RestfulClient;
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

    /**
     * Ejecutará todas las tareas en la medida que vayan llegando
     */
    ExecutorService executor = Executors.newFixedThreadPool(1);

    /**
     * Constructor de la clase.
     */
    public RestPersistenceServiceRESTFUL() {

    }

    @Override
    public IEntity delete(IEntity entity) {
        RestfulClient.delete(entity.getClass(), entity.getId());
        return entity;
    }

    @Override
    public void disconnect() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void find(final String namedQuery, final Map<String, Object> parameters,
            final IPersistenceListener listener) {

        Callable<List<Object>> task = () -> {
            List<Object> lresults = null;
            if (namedQuery.contains("findAll")) {
                lresults = (List<Object>) RestFul2Entity.getAll(getClazz(namedQuery));
            } else {
                lresults = (List<Object>) RestfulClient.getByParameters(getClazz(namedQuery), parameters);
            }

            return lresults;
        };
        Future<List<Object>> future = executor.submit(task);
        try {
            List<Object> result = future.get();
            listener.onFindAllFinished(result);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void findAll(final Class<? extends IEntity> entityClazz, final IPersistenceListener listener) {

        Callable<List<Object>> task = () -> {
            List<? extends IEntity> result = RestFul2Entity.getAll(entityClazz);
            return (List<Object>) result;
        };
        Future<List<Object>> future = executor.submit(task);
        try {
            List<Object> result = future.get();
            listener.onFindAllFinished(result);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void findByAllId(final Class<? extends IEntity> entityClazz, final Long[] id,
            final IPersistenceListener listener) {
        Callable<List<Object>> task = () -> {
            List<Object> lresult = new ArrayList<>();
            for (Long lId : id) {
                List<? extends IEntity> lList = RestfulClient.get(entityClazz, lId);
                for (Object o : lList) {
                    lresult.add(o);
                }
            }

            return lresult;
        };
        Future<List<Object>> future = executor.submit(task);
        try {
            List<Object> result = future.get();
            listener.onFindAllFinished(result);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void findById(final Class<? extends IEntity> entityClazz, final Long id,
            final IPersistenceListener listener) {
        Callable<Object> task = () -> {
            List<? extends IEntity> result = RestfulClient.get(entityClazz, id);
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        };
        Future<Object> future = executor.submit(task);
        try {
            Object result = future.get();
            listener.onFound((IEntity) result);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void findByName(final Class<? extends IEntity> entityClazz, final String name,
            final IPersistenceListener listener) {
        Callable<Object> task = () -> {
            Map<String, Object> params = new HashMap<>();
            params.put("name", name);
            List<? extends IEntity> result = RestfulClient.getByParameters(entityClazz, params);
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        };
        Future<Object> future = executor.submit(task);
        try {
            Object result = future.get();
            listener.onFound((IEntity) result);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object> findSynchro(final String namedQuery, final Map<String, Object> parameters) {
        Callable<List<Object>> task = () -> {
            List<Object> lresults = null;
            try {
                if (namedQuery.contains("findAll")) {

                    lresults = (List<Object>) RestFul2Entity.getAll(getClazz(namedQuery));
                } else {
                    lresults = (List<Object>) RestfulClient.getByParameters(getClazz(namedQuery), parameters);
                }
            } catch (ClassNotFoundException e) {
                lresults = null;
            }

            return lresults;
        };

        Future<List<Object>> future = executor.submit(task);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public IEntity findSynchroById(Class<? extends IEntity> entityClazz, Long id) {

        Callable<Object> task = () -> {
            List<? extends IEntity> result = RestfulClient.get(entityClazz, id);
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return null;
        };

        Future<Object> future = executor.submit(task);
        try {
            return (IEntity) future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends IEntity> List<T> findAllSynchro(Class<T> entityClazz) {
        Callable<List<T>> task = () -> {
            List<T> result = RestFul2Entity.getAll(entityClazz);
            return result;
        };

        Future<List<T>> future = executor.submit(task);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
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

    @Override
    public IEntity save(IEntity entity) {
        RestfulClient.post(entity);
        return entity;
    }

    @Override
    public IEntity update(IEntity entity) {
        RestfulClient.put(entity, entity.getId());
        return entity;
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends IEntity> getClazz(String namedQuery) throws ClassNotFoundException {
        String[] parts = namedQuery.split(".");
        Class<?> clazz = Class.forName("cl.eos.persistence.models." + parts[0]);
        return (Class<? extends IEntity>) clazz;
    }

    public static void main(String[] args) {
        List<STipoPrueba> result = RestFul2Entity.getAll(STipoPrueba.class);
    }

}