package cl.eos;

import java.awt.MenuContainer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.BreadcrumbBar;
import cl.eos.imp.view.WindowButtons;
import cl.eos.imp.view.WindowManager;
import cl.eos.interfaces.IActivator;
import cl.eos.provider.persistence.PersistenceServiceFactory;

public class MainController {

  @FXML
  private MenuContainer mnuPrincipal;
  @FXML
  private Menu mnuAdministrar;
  @FXML
  private MenuItem mnuAlumno;
  @FXML
  private MenuItem mnuCursos;
  @FXML
  private MenuItem mnuProfesores;
  @FXML
  private MenuItem mnuAsignaturas;
  @FXML
  private MenuItem mnuColegios;
  @FXML
  private MenuItem mnuEjesTematicos;
  @FXML
  private MenuItem mnuTipoPrueba;
  @FXML
  private MenuItem mnuNivelEvaluacion;
  @FXML
  private MenuItem mnuHabilidades;
  @FXML
  private MenuItem mnuHacerPrueba;
  @FXML
  private MenuItem mnuEvaluarPrueba;
  @FXML
  private MenuItem mnuResumenGeneral;
  @FXML
  private MenuItem mnuItemGeneraBD;
  @FXML
  private MenuItem mnuCerrarAplicacion;
  @FXML
  private BreadcrumbBar breadCrumb;  
  @FXML
  private AnchorPane pnlWindow;
  @FXML
  private Group groupRoot;

  private Stage stage;
  
  public MainController() {
    super();
  }

  @FXML
  public void initialize() {
    try {
       WindowManager.getInstance().setRoot(groupRoot);
       WindowManager.getInstance().setBreadcrumbBar(breadCrumb);
    } catch (Exception e) {
      e.printStackTrace();
    }
    mnuAlumno.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        IActivator activator = new AlumnosActivator();
        WindowManager.getInstance().show(activator.getView());
      }
    });

    mnuCursos.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        IActivator activator = new CursosActivator();
        WindowManager.getInstance().show(activator.getView());
      }
    });

    mnuAsignaturas.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        IActivator activator = new AsignaturasActivator();
        WindowManager.getInstance().show(activator.getView());
      }
    });

    mnuProfesores.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        IActivator activator = new ProfesoresActivator();
        WindowManager.getInstance().show(activator.getView());
      }
    });

    mnuColegios.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        IActivator activator = new ColegiosActivator();
        WindowManager.getInstance().show(activator.getView());
      }
    });

    mnuHabilidades.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        IActivator activator = new HabilidadesActivator();
        WindowManager.getInstance().show(activator.getView());
      }
    });

    mnuEjesTematicos.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        IActivator activator = new EjesTematicosActivator();
        WindowManager.getInstance().show(activator.getView());
      }
    });

    mnuHacerPrueba.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        IActivator activator = new PruebasActivator();
        WindowManager.getInstance().show(activator.getView());
      }
    });
    mnuItemGeneraBD.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        PersistenceServiceFactory.getPersistenceService();
      }
    });

    mnuTipoPrueba.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        IActivator activator = new TipoPruebaActivator();
        WindowManager.getInstance().show(activator.getView());
      }
    });
    mnuNivelEvaluacion.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        IActivator activator = new NivelEvaluacionActivator();
        WindowManager.getInstance().show(activator.getView());
      }
    });
    mnuCerrarAplicacion.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        Platform.exit();

      }
    });
    
    WindowButtons wButtons = new WindowButtons(stage);
    pnlWindow.getChildren().add(wButtons);
  }

  public Group getGroup() {
    return groupRoot;
  }

public Stage getStage() {
	return stage;
}

public void setStage(Stage stage) {
	this.stage = stage;
}

}
