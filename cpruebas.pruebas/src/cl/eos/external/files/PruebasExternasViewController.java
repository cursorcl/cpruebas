package cl.eos.external.files;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;

import cl.eos.external.files.excel.ExcelExamsController;
import cl.eos.external.files.utils.Register;
import cl.eos.external.files.utils.Results;
import cl.eos.imp.view.AFormView;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Objetivo;
import cl.eos.persistence.models.TipoCurso;
import cl.eos.util.Utils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

public class PruebasExternasViewController extends AFormView{

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
    private TableView<Register> tblGoodValues;

    @FXML
    private TableColumn<Register, String> columnRut;
    
    @FXML
    private TableColumn<Register, Integer> columnCurso;

    @FXML
    private TableColumn<Register, Integer> columnAsignatura;

    @FXML
    private TableColumn<Register, String> columnRespuesta;

    @FXML
    private TableColumn<Register, Float> columnNota;

    @FXML
    private ListView<?> lstBadValues;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnClose;
    
    private ExcelExamsController controller = new ExcelExamsController();
    public PruebasExternasViewController() {
    	setTitle("Importar Pruebas");
	}

    
    // Contenedor del archivo a procesar.
    private File file = null;
    @FXML
    void initialize() {
    	btnProcess.setDisable(true);
    	btnCancel.setDisable(true);
    	btnClose.setDisable(false);
    	btnProcess.setOnAction(event -> {
    		processFile();
    	});
    	btnSearchFile.setOnAction(event -> {
    		searchExcelFile();
    	});
    	progressBar.setProgress(0f);
    	
    	
    	tblGoodValues.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	
    	columnRut.setCellValueFactory(new PropertyValueFactory<Register, String>("rut"));
        columnCurso.setCellValueFactory(new PropertyValueFactory<Register, Integer>("idCurso"));
        columnAsignatura.setCellValueFactory(new PropertyValueFactory<Register, Integer>("idAsignatura"));
        columnRespuesta.setCellValueFactory(new PropertyValueFactory<Register, String>("respuestas"));
        columnNota.setCellValueFactory(new PropertyValueFactory<Register, Float>("nota"));
    }

	private void processFile() {
		 Task<Results> task = controller.process(file);
		 task.setOnSucceeded(event -> success((Results)event.getSource().getValue()));
		 task.setOnCancelled(event -> cancelled());
		 task.setOnFailed(event -> failed(event));
		 
		 txtProgress.textProperty().bind(task.messageProperty());
		 progressBar.progressProperty().bind(task.progressProperty());
		 
		 Executors.newSingleThreadExecutor().execute(task);
			 
	}

	private void failed(WorkerStateEvent event) {
		final Runnable r = () -> {
            final Alert error = new Alert(AlertType.ERROR);
            error.setTitle("Se ha producido un error.");
            error.setHeaderText(event.getSource().getException().getMessage());
            error.setContentText(event.getSource().getException().toString());
            error.showAndWait();
        };
        Platform.runLater(r);
	}

	private void cancelled() {
	}

	private void success(Results result) {

		
		ObservableList<Register> registros =  FXCollections.observableArrayList(result.results);
		tblGoodValues.setItems(registros);
		final Runnable r = () -> {
            final Alert information = new Alert(AlertType.INFORMATION);
            information.setTitle("Finalizada la importación.");
            information.setHeaderText("La importación fue exitosa.");
            information.setContentText(String.format("Se han procesado %d registros", result.badResults.size() + result.results.size()));
            information.showAndWait();
            
        	btnProcess.setDisable(true);
        	btnCancel.setDisable(true);
        	btnClose.setDisable(false);
        	btnSearchFile.setDisable(true);
        	txtFile.setText("");
        	progressBar.progressProperty().unbind();
        	progressBar.setProgress(0f);
        	txtProgress.textProperty().unbind();
        	txtProgress.setText("");
        };
        Platform.runLater(r);
        
        
	}

	private void searchExcelFile() {
		final FileChooser fileChooser = new FileChooser();
        final FileChooser.ExtensionFilter excelExtFilter = new FileChooser.ExtensionFilter(
                "Archivos Excel", new String[] {"*.xls", "*.xlsx", "*.xlsm", "*.csv"});
        fileChooser.getExtensionFilters().add(excelExtFilter);
        fileChooser.setInitialDirectory(Utils.getDefaultDirectory());
        fileChooser.setTitle("Seleccione Archivo Excel de Respuestas");
        file = fileChooser.showOpenDialog(null);
        if (file != null && file.exists()) {
        	txtFile.setText(file.getAbsolutePath());
        	btnProcess.setDisable(false);
    		btnSearchFile.setDisable(true);
    		btnCancel.setDisable(false);
    		btnClose.setDisable(true);
    		
        }
		
	}
}
