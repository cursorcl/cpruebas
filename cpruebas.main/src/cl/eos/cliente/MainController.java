package cl.eos.cliente;

import java.awt.MenuContainer;
import java.io.File;
import java.util.List;
import java.util.Optional;

import cl.eos.AlumnosActivator;
import cl.eos.AsignaturasActivator;
import cl.eos.ColegiosActivator;
import cl.eos.CursosActivator;
import cl.eos.EjesTematicosActivator;
import cl.eos.EvaluacionEjeTematicoActivator;
import cl.eos.HabilidadesActivator;
import cl.eos.NivelEvaluacionActivator;
import cl.eos.ObjetivosActivator;
import cl.eos.ProfesoresActivator;
import cl.eos.TipoPruebaActivator;
import cl.eos.activator.ListadoPruebasActivator;
import cl.eos.calidadlectora.CalidadLectoraActivator;
import cl.eos.comprensionlectora.ComprensionLectoraActivator;
import cl.eos.exception.ExceptionBD;
import cl.eos.imp.view.WindowButtons;
import cl.eos.imp.view.WindowManager;
import cl.eos.interfaces.IActivator;
import cl.eos.provider.persistence.PersistenceServiceFactory;
import cl.eos.rangoslectura.RangosLecturaActivator;
import cl.eos.util.ExcelSheetReader;
import cl.eos.util.ExcelSheetReaderx;
import cl.eos.util.Utils;
import cl.eos.velocidadlectora.VelocidadLectoraActivator;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.BreadcrumbBar;

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
    private MenuItem mnuEvaluarPrueba;
    @FXML
    private MenuItem mnuResumenGeneral;
    @FXML
    private MenuItem mnuItemGeneraBD;
    @FXML
    private MenuItem mnuCerrarAplicacion;
    @FXML
    private MenuItem mnuEvaluaciones;
    @FXML
    private MenuItem mnuNivelEvaluaciones;
    @FXML
    private MenuItem mnuObjetivos;

    @FXML
    private MenuItem mnuVelocidadLectora;
    @FXML
    private MenuItem mnuCalidadLectora;
    @FXML
    private MenuItem mnuComprensionLectora;
    @FXML
    private MenuItem mnuRangoLectores;

    @FXML
    private BreadcrumbBar breadCrumb;
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

    public Group getGroup() {
        return groupRoot;
    }

    public Stage getStage() {
        return stage;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void importarExcel() {
        final ButtonType bTypeAlumno = new ButtonType("R_Alumno");
        final ButtonType bTypeColegio = new ButtonType("SColegio");
        final ButtonType bTypeCurso = new ButtonType("SCurso");
        final ButtonType bTypeProfesor = new ButtonType("SProfesor");
        final ButtonType bTypeCancel = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información a Importar:");
        alert.setHeaderText("");
        alert.getButtonTypes().setAll(bTypeAlumno, bTypeColegio, bTypeCurso, bTypeProfesor, bTypeCancel);
        final Optional<ButtonType> result = alert.showAndWait();

        if (result.get() != bTypeCancel) {
            final FileChooser fileChooser = new FileChooser();
            final FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XLS files (*.xls), (*.xlsx)",
                    "*.xls", "*.xlsx");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialDirectory(Utils.getDefaultDirectory());

            final File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                List lista = null;
                final String extension = Utils.getFileExtension(file);
                try {
                    if (extension.equals(MainController.EXTENSION_XLSX)) {
                        final ExcelSheetReaderx excel = new ExcelSheetReaderx();
                        lista = excel.readExcelFile(file);
                    } else {
                        final ExcelSheetReader excel = new ExcelSheetReader();
                        lista = excel.readExcelFile(file);
                    }
                    if (lista == null || lista.isEmpty()) {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error en la importación desde excel");
                        alert.setHeaderText(null);
                        alert.setContentText("No hay registros que importar");
                        alert.showAndWait();
                    } else
                        PersistenceServiceFactory.getPersistenceService().insert(result.get().getText(), lista, null);

                } catch (final ExceptionBD e) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error en la importación desde excel");
                    alert.setHeaderText(null);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();

                } catch (final OutOfMemoryError e) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error en la importación desde excel");
                    alert.setHeaderText(null);
                    alert.setContentText("Archivo demasiado grande. Reinicie la aplicación");
                    alert.showAndWait();

                }
            }
        }
    }

    @FXML
    public void initialize() {
        try {
            WindowManager.getInstance().setRoot(groupRoot);
            WindowManager.getInstance().setBreadcrumbBar(breadCrumb);
            final IActivator activator = new ListadoPruebasActivator();
            WindowManager.getInstance().setHomeView(activator.getView());

        } catch (final Exception e) {
            e.printStackTrace();
        }
        mnuAlumno.setOnAction(arg0 -> {
            final IActivator activator = new AlumnosActivator();
            WindowManager.getInstance().show(activator.getView());
        });

        mnuCursos.setOnAction(arg0 -> {
            final IActivator activator = new CursosActivator();
            WindowManager.getInstance().show(activator.getView());
        });

        mnuAsignaturas.setOnAction(arg0 -> {
            final IActivator activator = new AsignaturasActivator();
            WindowManager.getInstance().show(activator.getView());
        });

        mnuProfesores.setOnAction(arg0 -> {
            final IActivator activator = new ProfesoresActivator();
            WindowManager.getInstance().show(activator.getView());
        });

        mnuColegios.setOnAction(arg0 -> {
            final IActivator activator = new ColegiosActivator();
            WindowManager.getInstance().show(activator.getView());
        });

        mnuHabilidades.setOnAction(arg0 -> {
            final IActivator activator = new HabilidadesActivator();
            WindowManager.getInstance().show(activator.getView());
        });

        mnuEjesTematicos.setOnAction(arg0 -> {
            final IActivator activator = new EjesTematicosActivator();
            WindowManager.getInstance().show(activator.getView());
        });

        mnuTipoPrueba.setOnAction(arg0 -> {
            final IActivator activator = new TipoPruebaActivator();
            WindowManager.getInstance().show(activator.getView());
        });
        mnuNivelEvaluacion.setOnAction(arg0 -> {
            final IActivator activator = new NivelEvaluacionActivator();
            WindowManager.getInstance().show(activator.getView());
        });
        mnuImportar.setOnAction(event -> importarExcel());
        mnuEvaluaciones.setOnAction(event -> {
            final IActivator activator = new EvaluacionEjeTematicoActivator();
            WindowManager.getInstance().show(activator.getView());
        });
        mnuNivelEvaluaciones.setOnAction(event -> {
            final IActivator activator = new NivelEvaluacionActivator();
            WindowManager.getInstance().show(activator.getView());
        });
        mnuObjetivos.setOnAction(event -> {
            final IActivator activator = new ObjetivosActivator();
            WindowManager.getInstance().show(activator.getView());
        });
        mnuVelocidadLectora.setOnAction(event -> {
            final IActivator activator = new VelocidadLectoraActivator();
            WindowManager.getInstance().show(activator.getView());
        });

        mnuCalidadLectora.setOnAction(event -> {
            final IActivator activator = new CalidadLectoraActivator();
            WindowManager.getInstance().show(activator.getView());
        });
        mnuComprensionLectora.setOnAction(event -> {
            final IActivator activator = new ComprensionLectoraActivator();
            WindowManager.getInstance().show(activator.getView());
        });
        mnuRangoLectores.setOnAction(event -> {
            final IActivator activator = new RangosLecturaActivator();
            WindowManager.getInstance().show(activator.getView());
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        final WindowButtons wButtons = new WindowButtons(stage);
        AnchorPane.setRightAnchor(wButtons, 1.0);
        AnchorPane.setTopAnchor(wButtons, 1.0);
        pnlWindow.getChildren().add(wButtons);

    }

}
