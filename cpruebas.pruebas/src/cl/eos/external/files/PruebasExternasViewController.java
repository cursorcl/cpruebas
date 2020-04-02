package cl.eos.external.files;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import cl.eos.external.files.excel.ExcelExamsController;
import cl.eos.imp.view.AFormView;
import cl.eos.util.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private TableView<?> tblGoodValues;

    @FXML
    private TableColumn<?, ?> columnRut;

    @FXML
    private TableColumn<?, ?> columnCurso;

    @FXML
    private TableColumn<?, ?> columnAsignatura;

    @FXML
    private TableColumn<?, ?> columnRespuesta;

    @FXML
    private TableColumn<?, ?> columnNota;

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
    }

	private void processFile() {
		controller.process(file);
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
