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
  ExecutorService executor = Executors.newFixedThreadPool(5);

  /**
   * Constructor de la clase.
   */
  public RestPersistenceServiceRESTFUL() {

  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends IEntity> void findAll(final Class<T> entityClazz,
      final IPersistenceListener listener) {

    List<? extends IEntity> result = RestfulClient.get(entityClazz);
    listener.onFindAllFinished((List<Object>) result);


//    Runnable r = () -> {
//      List<? extends IEntity> result = RestfulClient.get(entityClazz);
//
//      Platform.runLater(new Runnable() {
//        @Override
//        public void run() {
//          listener.onFindAllFinished((List<Object>) result);
//        }
//      });
//
//    };
//    executor.execute(r);
  }

  @Override
  public <T extends IEntity> void findAll(Class<T> entityClazz, IPersistenceListener listener,
      int offset, int items) {
    Runnable r = () -> {
      List<T> result = RestfulClient.get(entityClazz);

      Platform.runLater(new Runnable() {
        @SuppressWarnings("unchecked")
        @Override
        public void run() {
          listener.onFindAllFinished((List<Object>) result);
        }
      });

    };
    executor.execute(r);

  }

  @Override
  public <T extends IEntity> void findByAllId(final Class<T> entityClazz, final Long[] id,
      final IPersistenceListener listener) {
    Runnable r = () -> {
      List<T> lresult = new ArrayList<>();
      for (Long lId : id) {
        List<T> lList = RestfulClient.get(entityClazz, lId);
        for (T o : lList) {
          lresult.add(o);
        }
      }
      Platform.runLater(new Runnable() {
        @SuppressWarnings("unchecked")
        @Override
        public void run() {
          listener.onFindAllFinished((List<Object>) lresult);
        }
      });

    };
    executor.execute(r);
  }

  @Override
  public <T extends IEntity> void findById(final Class<T> entityClazz, final Long id,
      final IPersistenceListener listener) {
    Runnable r = () -> {
      List<? extends IEntity> result = RestfulClient.get(entityClazz, id);
      IEntity res = null;
      if (result != null && !result.isEmpty()) {
        res = result.get(0);
      }
      final IEntity fResult = res;
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          listener.onFound(fResult);
        }
      });

    };
    executor.execute(r);
  }

  @Override
  public <T extends IEntity> void findByName(final Class<T> entityClazz, final String name,
      final IPersistenceListener listener) {
    Runnable r = () -> {
      Map<String, Object> params = new HashMap<>();
      params.put("name", name);
      List<? extends IEntity> result = RestfulClient.getByParameters(entityClazz, params);
      IEntity res = null;
      if (result != null && !result.isEmpty()) {
        res = result.get(0);
      }
      final IEntity fResult = res;
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          listener.onFound(fResult);
        }
      });
    };
    executor.execute(r);
  }

  @Override
  public <T extends IEntity> void findByParams(Class<T> entityClazz, Map<String, Object> params,
      IPersistenceListener listener) {

    Runnable r = () -> {
      final List<T> result = RestfulClient.getByParameters(entityClazz, params);
      Platform.runLater(new Runnable() {
        @SuppressWarnings("unchecked")
        @Override
        public void run() {
          listener.onFindFinished((List<Object>) result);
        }
      });
    };
    executor.execute(r);
  }


  /************ Synchro methods **************/

  @Override
  public <T extends IEntity> List<T> findAllSynchro(Class<T> entityClazz) {
    Callable<List<T>> task = () -> {
      List<T> result = RestfulClient.get(entityClazz);
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
  public <T extends IEntity> List<T> findAllSynchro(Class<T> entityClazz, int offset, int items) {
    Callable<List<T>> task = () -> {
      List<T> result = RestfulClient.get(entityClazz);
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
  public <T extends IEntity> List<T> findByAllIdSynchro(Class<T> entityClazz, Long[] id) {
    List<T> lresult = new ArrayList<>();
    for (Long lId : id) {
      List<T> lList = RestfulClient.get(entityClazz, lId);
      for (T o : lList) {
        lresult.add(o);
      }
    }
    return lresult;
  }

  @Override
  public <T extends IEntity> T findByIdSynchro(Class<T> entityClazz, Long id) {

    List<T> result = RestfulClient.get(entityClazz, id);
    if (result != null && !result.isEmpty()) {
      return result.get(0);
    }
    return null;
  }

  @Override
  public <T extends IEntity> T findByNameSynchro(Class<T> entityClazz, String name) {
    Map<String, Object> params = new HashMap<>();
    params.put("name", name);
    List<T> result = RestfulClient.getByParameters(entityClazz, params);
    T res = null;
    if (result != null && !result.isEmpty()) {
      res = result.get(0);
    }
    return res;
  }


  @Override
  public <T extends IEntity> List<T> findByParamsSynchro(Class<T> entityClazz,
      Map<String, Object> params) {
    return RestfulClient.getByParameters(entityClazz, params);
  }

  @Override
  public void insert(final String entity, final List<Object> list,
      final IPersistenceListener listener) {

    final ProgressForm dlg = new ProgressForm();
    dlg.title("Importando datos");
    dlg.message("Esto tomará algunos minutos.");

    final Task<Pair<String, Pair<Integer, List<String>>>> task =
        new Task<Pair<String, Pair<Integer, List<String>>>>() {
          @Override
          protected Pair<String, Pair<Integer, List<String>>> call() {
            final Pair<String, Pair<Integer, List<String>>> pair =
                new Pair<String, Pair<Integer, List<String>>>();
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
          alert1.setContentText(" Se han importado [" + pair.getSecond().getFirst()
              + "] registros de [" + pair.getFirst() + "]");
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
            fileChooser
                .setInitialFileName("import_" + entity + "_" + System.currentTimeMillis() + ".log");
            fileChooser.setInitialDirectory(Utils.getDefaultDirectory());
            final FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Archivo de log", "*.log");
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

  @Override
  public IEntity delete(IEntity entity) {
    RestfulClient.delete(entity.getClass(), entity.getId());
    return entity;
  }

  @Override
  public void disconnect() {}

  @Override
  public <T extends IEntity> void deleteByParams(T entity, Map<String, Object> params) {
    RestfulClient.deleteByParams(entity.getClass(), params);
  }

}
