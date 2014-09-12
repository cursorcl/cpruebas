package cl.eos;

import java.awt.MenuContainer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
	private MenuItem mnuHabilidades;
	@FXML
	private MenuItem mnuHacerPrueba;
	@FXML
	private MenuItem mnuEvaluarPrueba;
	@FXML
	private MenuItem mnuItemGeneraBD;
	@FXML
	private StackPane pnlContainer;

	public MainController() {
		super();
	}

	@FXML
	public void initialize() {
		pnlContainer.getChildren().clear();
		mnuAlumno.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
					IActivator activator = new AlumnosActivator();
					pnlContainer.getChildren().setAll((Pane)activator.getPane());
			}
		});
		
		mnuCursos.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
					IActivator activator = new CursosActivator();
					pnlContainer.getChildren().setAll((Pane)activator.getPane());
			}
		});
		
		mnuAsignaturas.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
					IActivator activator = new AsignaturasActivator();
					pnlContainer.getChildren().setAll((Pane)activator.getPane());
			}
		});
		
		mnuProfesores.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
					IActivator activator = new ProfesoresActivator();
					pnlContainer.getChildren().setAll((Pane)activator.getPane());
			}
		});
		
		mnuColegios.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
					IActivator activator = new ColegiosActivator();
					pnlContainer.getChildren().setAll((Pane)activator.getPane());
			}
		});
		
		mnuHabilidades.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
					IActivator activator = new HabilidadesActivator();
					pnlContainer.getChildren().setAll((Pane)activator.getPane());
			}
		});
		
		mnuEjesTematicos.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
					IActivator activator = new EjesTematicosActivator();
					pnlContainer.getChildren().setAll((Pane)activator.getPane());
			}
		});
		
		mnuHacerPrueba.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
					IActivator activator = new PruebasActivator();
					pnlContainer.getChildren().setAll((Pane)activator.getPane());
			}
		});
		mnuEvaluarPrueba.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
					IActivator activator = new EvaluacionPruebaActivator();
					pnlContainer.getChildren().setAll((Pane)activator.getPane());
			}
		});
		mnuItemGeneraBD.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				PersistenceServiceFactory.getPersistenceService();
			}
		});
	}
}
