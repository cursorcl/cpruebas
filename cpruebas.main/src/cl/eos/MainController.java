package cl.eos;

import java.awt.MenuContainer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
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
	private StackPane pnlContainer;
  public MainController() {
    super();
  }

  @FXML
  public void initialize() {
    try {
      WindowManager.getInstance().setRoot(pnlContainer);
    } catch (Exception e) {
      e.printStackTrace();
    }
    pnlContainer.getChildren().clear();
    mnuAlumno.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        IActivator activator = new AlumnosActivator();
        WindowManager.getInstance().show(activator.getPane(), true);
      }
    });

    mnuCursos.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        IActivator activator = new CursosActivator();
        WindowManager.getInstance().show(activator.getPane(), true);
      }
    });

    mnuAsignaturas.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        IActivator activator = new AsignaturasActivator();
        WindowManager.getInstance().show(activator.getPane(), true);
      }
    });

    mnuProfesores.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        IActivator activator = new ProfesoresActivator();
        WindowManager.getInstance().show(activator.getPane(), true);
      }
    });

    mnuColegios.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        IActivator activator = new ColegiosActivator();
        WindowManager.getInstance().show(activator.getPane(), true);
      }
    });

    mnuHabilidades.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        IActivator activator = new HabilidadesActivator();
        WindowManager.getInstance().show(activator.getPane(), true);
      }
    });

    mnuEjesTematicos.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        IActivator activator = new EjesTematicosActivator();
        WindowManager.getInstance().show(activator.getPane(), true);
      }
    });

    mnuHacerPrueba.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        IActivator activator = new PruebasActivator();
        WindowManager.getInstance().show(activator.getPane(), true);
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
				 WindowManager.getInstance().show(activator.getPane(), true);
		}
	});
	mnuNivelEvaluacion.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent arg0) {
				IActivator activator = new NivelEvaluacionActivator();
				 WindowManager.getInstance().show(activator.getPane(), true);
		}
	});
  }
}
