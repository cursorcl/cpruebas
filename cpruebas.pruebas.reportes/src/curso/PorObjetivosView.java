package curso;

import java.util.List;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.Objetivo;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.models.TipoAlumno;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ot.OTPorObjetivosCurso;

public class PorObjetivosView extends AFormView {

    @FXML // fx:id="tblObjetivos"
    private TableView<OTPorObjetivosCurso> tblObjetivos; // Value injected by
                                                         // FXMLLoader

    @FXML // fx:id="colObjetivo"
    private TableColumn<OTPorObjetivosCurso, Objetivo> colObjetivo; // Value
                                                                    // injected
                                                                    // by
                                                                    // FXMLLoader

    @FXML // fx:id="colPregAsociadas"
    private TableColumn<OTPorObjetivosCurso, String> colPregAsociadas; // Value
                                                                       // injected
                                                                       // by
                                                                       // FXMLLoader

    @FXML // fx:id="colEjes"
    private TableColumn<OTPorObjetivosCurso, String> colEjes; // Value injected
                                                              // by FXMLLoader

    @FXML // fx:id="colHabilidades"
    private TableColumn<OTPorObjetivosCurso, String> colHabilidades; // Value
                                                                     // injected
                                                                     // by
                                                                     // FXMLLoader

    @FXML // fx:id="colLPorLogro"
    private TableColumn<OTPorObjetivosCurso, Float> colLPorLogro; // Value
                                                                  // injected by
                                                                  // FXMLLoader

    @FXML // fx:id="lblTitle"
    private Label lblTitle; // Value injected by FXMLLoader

    @FXML // fx:id="btnGenerarReporte"
    private Button btnGenerarReporte;

    @FXML // fx:id="cmbTipoAlumno"
    private ComboBox<TipoAlumno> cmbTipoAlumno;

    private EvaluacionPrueba evaluacionPrueba;

    long tipoAlumno = Constants.PIE_ALL;

    @FXML
    public void initialize() {
        this.setTitle("Resumen de respuestas por objetivo");
        btnGenerarReporte.setDisable(true);
        cmbTipoAlumno.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (cmbTipoAlumno.getSelectionModel() == null)
                    return;
                tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();
                btnGenerarReporte.setDisable(false);
            }
        });
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            Object entity = list.get(0);
            if (entity instanceof TipoAlumno) {
                ObservableList<TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
                for (Object iEntity : list) {
                    tAlumnoList.add((TipoAlumno) iEntity);
                }
                cmbTipoAlumno.setItems(tAlumnoList);
            }
        }
    }

    @Override
    public void onFound(IEntity entity) {
        if (entity instanceof EvaluacionPrueba) {
            evaluacionPrueba = (EvaluacionPrueba) entity;
            generateReport();
        }
    }

    /**
     * Generar el reporte.
     */
    private void generateReport() {
        if (evaluacionPrueba != null && cmbTipoAlumno.getItems() != null && !cmbTipoAlumno.getItems().isEmpty()) {
            List<PruebaRendida> pRendidas = evaluacionPrueba.getPruebasRendidas();

            if (pRendidas != null && !pRendidas.isEmpty()) {
                List<RespuestasEsperadasPrueba> respEsperadas = evaluacionPrueba.getPrueba().getRespuestas();
                List<PruebaRendida> pruebasRendidas = evaluacionPrueba.getPruebasRendidas();
                
            }
        }
    }
}
