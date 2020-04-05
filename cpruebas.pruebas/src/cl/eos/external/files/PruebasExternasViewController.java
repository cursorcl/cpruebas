package cl.eos.external.files;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import cl.eos.external.files.excel.ExcelExams;
import cl.eos.external.files.excel.ExcelExamsController;
import cl.eos.external.files.utils.RegisterForView;
import cl.eos.external.files.utils.RegisterStatus;
import cl.eos.external.files.utils.Results;
import cl.eos.imp.view.AFormView;
import cl.eos.imp.view.WindowManager;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.util.Utils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

public class PruebasExternasViewController extends AFormView {

	Logger log = Logger.getLogger(PruebasExternasViewController.class.getName()); 
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField txtFile;

	@FXML
	private Button btnSearchFile;

	@FXML
	private Button btnProcess;

	@FXML
	private ProgressBar progressBar;
	@FXML
	private TextField txtProgress;

	@FXML
	private TableView<RegisterForView> tblGoodValues;

	@FXML
	private TableColumn<RegisterForView, String> columnRut;

	@FXML
	private TableColumn<RegisterForView, Colegio> columnColegio;

	@FXML
	private TableColumn<RegisterForView, Curso> columnCurso;

	@FXML
	private TableColumn<RegisterForView, Asignatura> columnAsignatura;

	@FXML
	private TableColumn<RegisterForView, String> columnRespuesta;

	@FXML
	private TableColumn<RegisterForView, Float> columnNota;

	@FXML
	private TableView<RegisterForView> tblBadValues;

	@FXML
	private TableColumn<RegisterForView, String> columnRut_bad;

	@FXML
	private TableColumn<RegisterForView, Integer> columnColegio_bad;

	@FXML
	private TableColumn<RegisterForView, Integer> columnCurso_bad;

	@FXML
	private TableColumn<RegisterForView, Integer> columnAsignatura_bad;

	@FXML
	private TableColumn<RegisterForView, String> columnRespuesta_bad;

	@FXML
	private TableColumn<RegisterForView, RegisterStatus> columnError_bad;

	@FXML
	private Button btnCancel;

	@FXML
	private Button btnClose;

	@FXML
	private Button btnExport;

	private ExcelExamsController controller = new ExcelExamsController();

	public PruebasExternasViewController() {
		setTitle("Importar Pruebas");
	}

	// Contenedor del archivo a procesar.
	private File file = null;

	private Task<Results> task;

	@FXML
	void initialize() {
		initWindowComponents();
		btnProcess.setOnAction(event -> {
			processFile();
		});
		btnSearchFile.setOnAction(event -> {
			searchExcelFileToRead();
		});
		btnClose.setOnAction(event -> {
			closeWindows();
		});
		btnCancel.setOnAction(event -> {
			cancelTask();
		});
		btnExport.setOnAction(event -> {
			export();
		});

		tblGoodValues.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		columnRut.setCellValueFactory(new PropertyValueFactory<RegisterForView, String>("rut"));
		columnCurso.setCellValueFactory(new PropertyValueFactory<RegisterForView, Curso>("curso"));
		columnColegio.setCellValueFactory(new PropertyValueFactory<RegisterForView, Colegio>("colegio"));
		columnAsignatura.setCellValueFactory(new PropertyValueFactory<RegisterForView, Asignatura>("asignatura"));
		columnRespuesta.setCellValueFactory(new PropertyValueFactory<RegisterForView, String>("respuestas"));
		columnNota.setCellValueFactory(new PropertyValueFactory<RegisterForView, Float>("nota"));

		columnRut_bad.setCellValueFactory(new PropertyValueFactory<RegisterForView, String>("rut"));
		columnColegio_bad.setCellValueFactory(new PropertyValueFactory<RegisterForView, Integer>("idColegio"));
		columnCurso_bad.setCellValueFactory(new PropertyValueFactory<RegisterForView, Integer>("idCurso"));
		columnAsignatura_bad.setCellValueFactory(new PropertyValueFactory<RegisterForView, Integer>("idAsignatura"));
		columnRespuesta_bad.setCellValueFactory(new PropertyValueFactory<RegisterForView, String>("respuestas"));
		columnError_bad.setCellValueFactory(new PropertyValueFactory<RegisterForView, RegisterStatus>("status"));

	}

	private void export() {
		ExcelExams excelExams =  new ExcelExams();
		if(file == null)
		{
			initWindowComponents();
			return;
		}
		List<RegisterForView> goods = tblGoodValues.getItems();
		List<RegisterForView> bads = tblBadValues.getItems();
		excelExams.saveExamsResults(file,goods,bads, null);  
		final Runnable r = () -> {
			final Alert error = new Alert(AlertType.INFORMATION);
			error.setTitle("Se Finalizado la exportación.");
			error.setHeaderText("Exportación finalizada");
			error.setContentText("Se ha agregado una nueva hoja al archivo con los resultados");
			error.showAndWait();
			initWindowComponents();
		};
		Platform.runLater(r);
	}

	private void initWindowComponents() {
		btnProcess.setDisable(true);
		btnCancel.setDisable(true);
		btnClose.setDisable(false);
		btnSearchFile.setDisable(false);

		txtFile.setText("");
		progressBar.progressProperty().unbind();
		progressBar.setProgress(0f);
		txtProgress.textProperty().unbind();
		txtProgress.setText("");

		boolean hasBadValues = tblBadValues.getItems() != null && !tblBadValues.getItems().isEmpty();
		boolean hasGoodValues = tblGoodValues.getItems() != null && !tblGoodValues.getItems().isEmpty();
		btnExport.setDisable(!hasBadValues && !hasGoodValues);

	}

	private void cancelTask() {
		if (task == null)
			return;
		if (task.isRunning()) {
			boolean isCancelled = task.cancel();
			if (isCancelled) {
				initWindowComponents();
			}
		}

	}

	private void closeWindows() {
		WindowManager.getInstance().hide(this);
	}

	private void processFile() {

		tblBadValues.getItems().clear();
		tblGoodValues.getItems().clear();

		task = controller.process(file);
		task.setOnSucceeded(event -> success((Results) event.getSource().getValue()));
		task.setOnCancelled(event -> cancelled(event));
		task.setOnFailed(event -> failed(event));

		txtProgress.textProperty().bind(task.messageProperty());
		progressBar.progressProperty().bind(task.progressProperty());
		btnCancel.setDisable(false);
		Executors.newSingleThreadExecutor().execute(task);

	}

	private void failed(WorkerStateEvent event) {
		final Runnable r = () -> {
			log.log(Level.INFO, "Se ha producido un error.", event.getSource().getException());
			final Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Se ha producido un error.");
			error.setHeaderText(event.getSource().getException().getMessage());
			error.setContentText(event.getSource().getException().toString());
			error.showAndWait();
			initWindowComponents();
		};
		Platform.runLater(r);
	}

	private void cancelled(WorkerStateEvent event) {

		final Runnable r = () -> {
			log.info("Se ha Cancelado la ejecución.");
			final Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Se ha Cancelado la ejecución.");
			error.setHeaderText(event.getSource().getException().getMessage());
			error.setContentText(event.getSource().getException().toString());
			error.showAndWait();
			initWindowComponents();
		};
		Platform.runLater(r);

	}

	private void success(Results result) {

		ObservableList<RegisterForView> registros = FXCollections.observableArrayList(result.results);
		tblGoodValues.setItems(registros);

		ObservableList<RegisterForView> badRegisters = FXCollections.observableArrayList(result.badResults);
		tblBadValues.setItems(badRegisters);

		final Runnable r = () -> {
			final Alert information = new Alert(AlertType.INFORMATION);
			information.setTitle("Finalizada la importación.");
			information.setHeaderText("La importación ha finalizado.");
			int nReg = result.badResults.size() + result.results.size();
			System.out.println(result.results.size());
			information.setContentText(String.format("Se han procesado %d registros", nReg));
			information.showAndWait();
			initWindowComponents();
		};
		Platform.runLater(r);

	}

	private void searchExcelFileToRead() {
		final FileChooser fileChooser = new FileChooser();
		final FileChooser.ExtensionFilter excelExtFilter = new FileChooser.ExtensionFilter("Archivos Excel",
				new String[] { "*.xls", "*.xlsx", "*.xlsm", "*.csv" });
		fileChooser.getExtensionFilters().add(excelExtFilter);
		fileChooser.setInitialDirectory(Utils.getDefaultDirectory());
		fileChooser.setTitle("Seleccione Archivo Excel de Respuestas");
		file = fileChooser.showOpenDialog(null);
		if (file != null && file.exists()) {
			txtFile.setText(file.getAbsolutePath());
			btnProcess.setDisable(false);
			btnSearchFile.setDisable(true);

		}

	}
}