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
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ot.OTPorObjetivosCurso;

public class PorObjetivosView extends AFormView {

    @FXML // fx:id="tblObjetivos"
    private TableView<OTPorObjetivosCurso> tblObjetivos;
    @FXML // fx:id="colObjetivo"
    private TableColumn<OTPorObjetivosCurso, Objetivo> colObjetivo;
    @FXML // fx:id="colPregAsociadas"
    private TableColumn<OTPorObjetivosCurso, String> colPregAsociadas;
    @FXML // fx:id="colEjes"
    private TableColumn<OTPorObjetivosCurso, String> colEjes;
    @FXML // fx:id="colHabilidades"
    private TableColumn<OTPorObjetivosCurso, String> colHabilidades;
    @FXML // fx:id="colLPorLogro"
    private TableColumn<OTPorObjetivosCurso, Float> colLPorLogro;
    @FXML // fx:id="lblTitle"
    private Label lblTitle;
    @FXML // fx:id="btnGenerarReporte"
    private Button btnGenerarReporte;
    @FXML // fx:id="cmbTipoAlumno"
    private ComboBox<TipoAlumno> cmbTipoAlumno;

    private EvaluacionPrueba evaluacionPrueba;

    long tipoAlumno = Constants.PIE_ALL;

    @FXML
    public void initialize() {
        this.setTitle("Resumen de respuestas por objetivo");
        inicializeTable();
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
        btnGenerarReporte.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                generateReport();
            }
        });
    }

    private void inicializeTable() {

        colObjetivo.setCellValueFactory(new PropertyValueFactory<OTPorObjetivosCurso, Objetivo>("objetivo"));
        colPregAsociadas.setCellValueFactory(new PropertyValueFactory<OTPorObjetivosCurso, String>("preguntas"));
        colEjes.setCellValueFactory(new PropertyValueFactory<OTPorObjetivosCurso, String>("ejesAsociados"));
        colHabilidades
                .setCellValueFactory(new PropertyValueFactory<OTPorObjetivosCurso, String>("habilidadesAsociadas"));
        colLPorLogro.setCellValueFactory(new PropertyValueFactory<OTPorObjetivosCurso, Float>("porcentajes"));

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

                ObservableList<OTPorObjetivosCurso> objetivos = FXCollections.observableArrayList();
                for (RespuestasEsperadasPrueba re : respEsperadas) {
                    OTPorObjetivosCurso ot = new OTPorObjetivosCurso.Builder().objetivo(re.getObjetivo()).build();
                    int index = objetivos.indexOf(ot);
                    if (index != -1) {
                        ot = objetivos.get(index);
                    } else {
                        objetivos.add(ot);
                    }
                    String value;
                    if (!ot.getEjesAsociados().contains(re.getEjeTematico().getName())) {
                        value = ot.getEjesAsociados() + (ot.getEjesAsociados().isEmpty() ? "" : "\n")
                                + re.getEjeTematico().getName();
                        ot.setEjesAsociados(value);
                    }
                    if (!ot.getHabilidadesAsociadas().contains(re.getHabilidad().getName())) {
                        value = ot.getHabilidadesAsociadas() + (ot.getHabilidadesAsociadas().isEmpty() ? "" : "\n")
                                + re.getHabilidad().getName();
                        ot.setHabilidadesAsociadas(value);
                    }
                    value = ot.getPreguntas() + (ot.getPreguntas().isEmpty() ? "" : "-") + re.getNumero();
                    ot.setPreguntas(value);
                    ot.setNroPreguntas(ot.getNroPreguntas() + 1);
                }
                List<PruebaRendida> pruebasRendidas = evaluacionPrueba.getPruebasRendidas();
                int nroAlumnos = 0;
                for (PruebaRendida prR : pruebasRendidas) {
                    if (tipoAlumno != Constants.PIE_ALL && !prR.getAlumno().getTipoAlumno().getId().equals(tipoAlumno))
                        continue;
                    nroAlumnos++;
                    String respuestas = prR.getRespuestas().toUpperCase();
                    for (int n = 0; n < respuestas.length(); n++) {
                        RespuestasEsperadasPrueba respEsperada = respEsperadas.get(n);
                        String value = respuestas.substring(n, n + 1);
                        OTPorObjetivosCurso ot = new OTPorObjetivosCurso.Builder().objetivo(respEsperada.getObjetivo())
                                .build();
                        int index = objetivos.indexOf(ot);
                        if (index != -1) {
                            ot = objetivos.get(index);
                            if (respEsperada.validate(value)) {
                                ot.setPorcentajes(ot.getPorcentajes() + 1);
                            }
                        }
                    }
                }
                for (OTPorObjetivosCurso ot : objetivos) {
                    ot.setPorcentajes((ot.getPorcentajes() / (nroAlumnos * ot.getNroPreguntas())) * 100);
                }

                tblObjetivos.setItems(objetivos);
            }
        }
    }
}
