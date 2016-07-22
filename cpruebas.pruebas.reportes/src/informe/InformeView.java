package informe;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import cl.eos.imp.view.AFormView;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.TipoAlumno;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

public class InformeView extends AFormView {

    private static final String INFORME_COMPLETO = "Informe Completo";

    @FXML
    private Button btnGenerar;
    @FXML
    private AnchorPane pnlEdition;
    @FXML
    private Button btnCancelar;
    @FXML
    private ComboBox<Colegio> cmbColegio;
    @FXML
    private ComboBox<TipoAlumno> cmbTipoAlumno;
    @FXML
    ProgressBar progressBar;

    ObservableList<Asignatura> lstAsignaturas;
    protected Colegio colegio;
    protected TipoAlumno tipoAlumno;
    protected XWPFDocument doc;
    Map<String, Object> parameters = new HashMap<>();

    public InformeView() {
        setTitle(INFORME_COMPLETO);
    }

    @FXML
    public void initialize() {
        cmbColegio.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                colegio = cmbColegio.getSelectionModel().getSelectedItem();
                if (colegio == null)
                    return;
                setTitle(INFORME_COMPLETO + " " + colegio.getName());
            }
        });
        cmbTipoAlumno.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem();
            }
        });
        btnGenerar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    InformeManager manager = new InformeManager(progressBar);
                    for (Asignatura asignatura : lstAsignaturas) {
                        manager.processAsignatura(tipoAlumno, colegio, asignatura);
                    }
                    manager.finish();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            Object entity = list.get(0);
            if (entity instanceof Colegio) {
                ObservableList<Colegio> pruebas = FXCollections.observableArrayList();
                for (Object lEntity : list) {
                    pruebas.add((Colegio) lEntity);
                }
                cmbColegio.setItems(pruebas);
            } else if (entity instanceof Asignatura) {
                lstAsignaturas = FXCollections.observableArrayList();
                for (Object lEntity : list) {
                    lstAsignaturas.add((Asignatura) lEntity);
                }
            } else if (entity instanceof TipoAlumno) {
                ObservableList<TipoAlumno> lstTipoAlumno = FXCollections.observableArrayList();
                for (Object lEntity : list) {
                    lstTipoAlumno.add((TipoAlumno) lEntity);
                }
                cmbTipoAlumno.setItems(lstTipoAlumno);
            }
        }
    }

}
