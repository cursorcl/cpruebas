package cl.eos;

import java.awt.MenuContainer;
import java.io.File;
import java.util.List;

import cl.eos.exception.ExceptionBD;
import cl.eos.imp.view.WindowButtons;
import cl.eos.imp.view.WindowManager;
import cl.eos.interfaces.IActivator;
import cl.eos.interfaces.view.FXDialogs;
import cl.eos.provider.persistence.PersistenceServiceFactory;
import cl.eos.util.ExcelSheetReader;
import cl.eos.util.ExcelSheetReaderx;
import cl.eos.util.Utils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
//import jfxtras.labs.scene.control.BreadcrumbBar;

public class MainController {

	private static final String EXTENSION_XLSX = "xlsx";
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
//	@FXML
//	private BreadcrumbBar breadCrumb;
	@FXML
	private AnchorPane pnlWindow;
	@FXML
	private Group groupRoot;

	private Stage stage;
	@FXML
	private MenuItem mnuImportar;

	public MainController() {
		super();
	}

	@FXML
	public void initialize() {
		try {
			WindowManager.getInstance().setRoot(groupRoot);
//			WindowManager.getInstance().setBreadcrumbBar(breadCrumb);
			IActivator activator = new PruebasActivator();
			WindowManager.getInstance().setHomeView(activator.getView());

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
		// mnuCerrarAplicacion.setOnAction(new EventHandler<ActionEvent>() {
		//
		// @Override
		// public void handle(ActionEvent event) {
		// Platform.exit();
		// }
		// });

		mnuImportar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				importarExcel();
			}
		});

	}

	protected void importarExcel() {
		String response = FXDialogs.showChoiceDialog("Información a Importar:", "Ingrese cantidad de hojas a imprimir",
				"Alumno", "Colegio", "Curso", "Profesor");

		if (response != null && !response.isEmpty()) {
			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XLS files (*.xls), (*.xlsx)",
					"*.xls", "*.xlsx");
			fileChooser.getExtensionFilters().add(extFilter);
			fileChooser.setInitialDirectory(Utils.getDefaultDirectory());

			File file = fileChooser.showOpenDialog(null);
			if (file != null) {
				List lista = null;
				String extension = Utils.getFileExtension(file);
				try {
					if (extension.equals(EXTENSION_XLSX)) {
						ExcelSheetReaderx excel = new ExcelSheetReaderx();
						lista = excel.readExcelFile(file);
					} else {
						ExcelSheetReader excel = new ExcelSheetReader();
						lista = excel.readExcelFile(file);
					}
					PersistenceServiceFactory.getPersistenceService().insert(response, lista, null);

				} catch (ExceptionBD e) {
					FXDialogs.showException("Error", "Error en la importación desde excel", e);

				} catch (OutOfMemoryError e) {
					FXDialogs.showError("Error en la importación desde excel",
							"Archivo demasiado grande. Reinicie la aplicación");

				}
			}
		}
	}

	public Group getGroup() {
		return groupRoot;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
		WindowButtons wButtons = new WindowButtons(stage);
		AnchorPane.setRightAnchor(wButtons, 1.0);
		AnchorPane.setTopAnchor(wButtons, 1.0);
		pnlWindow.getChildren().add(wButtons);
	}

}
