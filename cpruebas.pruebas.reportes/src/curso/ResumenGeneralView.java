package curso;

import java.util.LinkedList;
import java.util.List;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTResumenGeneral;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.util.ExcelSheetWriterObj;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

// TODO Auto-generated constructor stub
public class ResumenGeneralView extends AFormView implements EventHandler<ActionEvent> {
    @FXML
    private TextField txtPrueba;
    @FXML
    private TextField txtCurso;
    @FXML
    private TextField txtAsignatura;
    @FXML
    private TextField txtExigencia;
    @FXML
    private TextField txtNoPregunta;
    @FXML
    private TextField txtPjePrueba;

    @FXML
    private TableView<OTResumenGeneral> tblResumen;
    @FXML
    private TableColumn<OTResumenGeneral, String> colNombre;
    @FXML
    private TableColumn<OTResumenGeneral, Float> colNotas;
    @FXML
    private TableColumn<OTResumenGeneral, Float> colBuenas;
    @FXML
    private TableColumn<OTResumenGeneral, Integer> ColPuntos;
    @FXML
    private TableColumn<OTResumenGeneral, Float> colPuntaje;

    @FXML
    private TableView<R_PruebaRendida> tblAlumnos;
    @FXML
    private TableColumn<R_PruebaRendida, String> colRut;
    @FXML
    private TableColumn<R_PruebaRendida, String> colPaterno;
    @FXML
    private TableColumn<R_PruebaRendida, String> colMaterno;
    @FXML
    private TableColumn<R_PruebaRendida, String> colName;
    @FXML
    private TableColumn<R_PruebaRendida, Integer> colABuenas;
    @FXML
    private TableColumn<R_PruebaRendida, Integer> colAMalas;
    @FXML
    private TableColumn<R_PruebaRendida, Integer> colAOmitidas;
    @FXML
    private TableColumn<R_PruebaRendida, Float> colPBuenas;
    @FXML
    private TableColumn<R_PruebaRendida, Integer> colAPuntaje;
    @FXML
    private TableColumn<R_PruebaRendida, Float> colPPuntaje;
    @FXML
    private TableColumn<R_PruebaRendida, Float> colANota;
    @FXML
    private MenuItem mnuExportarAlumnos;
    @FXML
    private MenuItem mnuExportarResumen;

    @FXML
    private ComboBox<R_TipoAlumno> cmbTipoAlumno;

    @FXML
    private Button btnGenerar;

    long tipoAlumno = Constants.PIE_ALL;

    private Float notaMin = Float.MAX_VALUE;
    private Float notaMax = Float.MIN_VALUE;
    private Float pbuenasMin = Float.MAX_VALUE;
    private Float pbuenasMax = Float.MIN_VALUE;
    private Float ppuntajeMin = Float.MAX_VALUE;
    private Float ppuntajeMax = Float.MIN_VALUE;
    private Integer puntajeMin = Integer.MAX_VALUE;
    private Integer puntajeMax = Integer.MIN_VALUE;
    private R_EvaluacionPrueba evaluacionPrueba;

    public ResumenGeneralView() {
        // TODO Auto-generated constructor stub
    }

    private void asignarDatosEvalucion(R_EvaluacionPrueba entity, List<OTResumenGeneral> listaResumen) {
        if (entity.getCurso() != null) {
            txtCurso.setText(entity.getCurso().getName());
            txtExigencia.setText(String.valueOf(entity.getExigencia()));
        }

        if (entity.getPrueba() != null) {
            txtPrueba.setText(entity.getPrueba().getName());
            txtPjePrueba.setText(entity.getPrueba().getPuntajeBase().toString());
            txtNoPregunta.setText(entity.getPrueba().getNropreguntas().toString());
            if (entity.getPrueba().getAsignatura() != null) {
                txtAsignatura.setText(entity.getPrueba().getAsignatura().getName());
            }
        }

        final List<R_PruebaRendida> list = entity.getPruebasRendidas();
        if (list != null && !list.isEmpty()) {
            final ObservableList<R_PruebaRendida> oList = FXCollections.observableArrayList();
            for (final R_PruebaRendida pruebaRendida : list) {
                if (tipoAlumno != Constants.PIE_ALL
                        && !pruebaRendida.getAlumno().getTipoAlumno().getId().equals(tipoAlumno))
                    continue;
                oList.add(pruebaRendida);

            }
            tblAlumnos.setItems(oList);
        }

        final ObservableList<OTResumenGeneral> oList = FXCollections.observableArrayList(listaResumen);
        tblResumen.setItems(oList);
    }

    private void generateReport() {

        if (evaluacionPrueba == null || cmbTipoAlumno.getItems().isEmpty())
            return;

        final List<R_PruebaRendida> pRendidas = evaluacionPrueba.getPruebasRendidas();
        final List<OTResumenGeneral> listaResumen = procesarValoresMinMax(pRendidas);
        asignarDatosEvalucion(evaluacionPrueba, listaResumen);

    }

    @Override
    public void handle(ActionEvent event) {
        final Object source = event.getSource();
        if (source == mnuExportarResumen || source == mnuExportarAlumnos) {

            tblAlumnos.setId("Alumnos");
            tblResumen.setId("Resumen");

            final List<TableView<? extends Object>> listaTablas = new LinkedList<>();
            listaTablas.add(tblAlumnos);
            listaTablas.add(tblResumen);

            ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
        } else if (source == btnGenerar) {
            generateReport();
        }
    }

    private void inicializarTablaAlumnos() {
        tblAlumnos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        colRut.setCellValueFactory(new PropertyValueFactory<R_PruebaRendida, String>("rut"));
        colName.setCellValueFactory(new PropertyValueFactory<R_PruebaRendida, String>("nombre"));
        colPaterno.setCellValueFactory(new PropertyValueFactory<R_PruebaRendida, String>("paterno"));
        colMaterno.setCellValueFactory(new PropertyValueFactory<R_PruebaRendida, String>("materno"));
        colABuenas.setCellValueFactory(new PropertyValueFactory<R_PruebaRendida, Integer>("buenas"));
        colAMalas.setCellValueFactory(new PropertyValueFactory<R_PruebaRendida, Integer>("malas"));
        colAOmitidas.setCellValueFactory(new PropertyValueFactory<R_PruebaRendida, Integer>("omitidas"));
        colPBuenas.setCellValueFactory(new PropertyValueFactory<R_PruebaRendida, Float>("pbuenas"));
        colAPuntaje.setCellValueFactory(new PropertyValueFactory<R_PruebaRendida, Integer>("puntaje"));
        colPPuntaje.setCellValueFactory(new PropertyValueFactory<R_PruebaRendida, Float>("ppuntajes"));
        colANota.setCellValueFactory(new PropertyValueFactory<R_PruebaRendida, Float>("nota"));

    }

    private void inicializarTablaResumen() {
        tblResumen.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        colNombre.setCellValueFactory(new PropertyValueFactory<OTResumenGeneral, String>("name"));
        colNotas.setCellValueFactory(new PropertyValueFactory<OTResumenGeneral, Float>("nota"));
        colBuenas.setCellValueFactory(new PropertyValueFactory<OTResumenGeneral, Float>("pbuenas"));
        ColPuntos.setCellValueFactory(new PropertyValueFactory<OTResumenGeneral, Integer>("puntaje"));
        colPuntaje.setCellValueFactory(new PropertyValueFactory<OTResumenGeneral, Float>("ppuntaje"));

    }

    @FXML
    public void initialize() {
        setTitle("Resumen de respuestas generales");
        inicializarTablaResumen();
        inicializarTablaAlumnos();
        mnuExportarAlumnos.setOnAction(this);
        mnuExportarResumen.setOnAction(this);
        btnGenerar.setOnAction(this);

        cmbTipoAlumno.setOnAction(event -> {
            tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedIndex();
        });
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof R_TipoAlumno) {
                final ObservableList<R_TipoAlumno> tAlumnoList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    tAlumnoList.add((R_TipoAlumno) iEntity);
                }
                cmbTipoAlumno.setItems(tAlumnoList);
                cmbTipoAlumno.getSelectionModel().select(0);
                generateReport();
            }
        }
    }

    @Override
    public void onFound(IEntity entity) {
        if (entity instanceof R_EvaluacionPrueba) {
            evaluacionPrueba = (R_EvaluacionPrueba) entity;
            generateReport();
        }
    }

    private List<OTResumenGeneral> procesarValoresMinMax(List<R_PruebaRendida> pruebasRendidas) {
        notaMin = Float.MAX_VALUE;
        notaMax = Float.MIN_VALUE;
        pbuenasMin = Float.MAX_VALUE;
        pbuenasMax = Float.MIN_VALUE;
        ppuntajeMin = Float.MAX_VALUE;
        ppuntajeMax = Float.MIN_VALUE;
        puntajeMin = Integer.MAX_VALUE;
        puntajeMax = Integer.MIN_VALUE;
        int nCount = 0;
        for (final R_PruebaRendida pruebaRendida : pruebasRendidas) {

            if (tipoAlumno != Constants.PIE_ALL
                    && !pruebaRendida.getAlumno().getTipoAlumno().getId().equals(tipoAlumno))
                continue;
            nCount++;
            if (pruebaRendida.getNota() < notaMin) {
                notaMin = pruebaRendida.getNota();
            }
            if (pruebaRendida.getNota() > notaMax) {
                notaMax = pruebaRendida.getNota();
            }

            if (pruebaRendida.getBuenas().floatValue() < pbuenasMin) {
                pbuenasMin = pruebaRendida.getBuenas().floatValue();
            }
            if (pruebaRendida.getBuenas().floatValue() > pbuenasMax) {
                pbuenasMax = pruebaRendida.getBuenas().floatValue();
            }

            if (pruebaRendida.getPpuntaje() < ppuntajeMin) {
                ppuntajeMin = pruebaRendida.getPpuntaje();
            }
            if (pruebaRendida.getPpuntaje() > ppuntajeMax) {
                ppuntajeMax = pruebaRendida.getPpuntaje();
            }

            if (pruebaRendida.getPuntaje() < puntajeMin) {
                puntajeMin = pruebaRendida.getPuntaje();
            }
            if (pruebaRendida.getPuntaje() > puntajeMax) {
                puntajeMax = pruebaRendida.getPuntaje();
            }
        }

        final List<OTResumenGeneral> listaResumen = new LinkedList<OTResumenGeneral>();

        if (nCount > 0) {

            final Float promNota = (notaMax + notaMin) / 2;
            final Float prompBuenas = (pbuenasMax + pbuenasMin) / 2;
            final Float prompPuntaje = (ppuntajeMax + ppuntajeMin) / 2;
            final Integer promPuntaje = (puntajeMax + puntajeMin) / 2;
            listaResumen.add(new OTResumenGeneral("Máximo", notaMax, pbuenasMax, ppuntajeMax, puntajeMax));
            listaResumen.add(new OTResumenGeneral("Mínimo", notaMin, pbuenasMin, ppuntajeMin, puntajeMin));
            listaResumen.add(new OTResumenGeneral("Promedio", promNota, prompBuenas, prompPuntaje, promPuntaje));
        } else {
            listaResumen.add(new OTResumenGeneral("Máximo", 0f, 0f, 0f, 0));
            listaResumen.add(new OTResumenGeneral("Mínimo", 0f, 0f, 0f, 0));
            listaResumen.add(new OTResumenGeneral("Promedio", 0f, 0f, 0f, 0));

        }

        return listaResumen;
    }
}