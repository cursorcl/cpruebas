package colegio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jfree.util.Log;

import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Objetivo;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.util.MapBuilder;
import cl.eos.util.Pair;
import javafx.beans.property.ReadOnlyFloatWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import ot.XItemObjetivo;
import ot.XItemTablaObjetivo;
import ot.XUtilReportBuilder;

/**
 * Obtiene los objetivos de un curso
 *
 * @author curso
 *
 */
public class PorObjetivosColegioView extends AFormView {

  private static final String TITLE_FORMAT =
      "Porcentaje de logro por objetivo colegio %s / asignatura %s";
  private static final String TITLE = "Porcentaje de logro por objetivo";
  private static final String ASIGNATURA_ID = "asignatura_id";
  private static final String COLEGIO_ID = "colegio_id";
  @FXML
  private TableView<XItemTablaObjetivo> tblObjetivos;
  @FXML
  private TableColumn<XItemTablaObjetivo, R_Objetivo> colObjetivos;

  @FXML
  private Button btnGenerarReporte;
  @FXML
  private ComboBox<R_TipoAlumno> cmbTipoAlumno;
  @FXML
  private ComboBox<R_Colegio> cmbColegio;
  @FXML
  private ComboBox<R_Asignatura> cmbAsignaturas;


  private Map<String, Object> parameters = new HashMap<String, Object>();

  long tipoAlumno = Constants.PIE_ALL;
  private ObservableList<R_EvaluacionPrueba> evaluacionesPrueba;

  protected void clearContent() {
    if (tblObjetivos.getItems() != null)
      tblObjetivos.getItems().clear();
    if (tblObjetivos.getColumns() != null) {
      for (int n = 3; n < tblObjetivos.getColumns().size(); n++)
        tblObjetivos.getColumns().remove(n);
    }
  }

  /**
   * Generar el reporte.
   */
  private void generateReport() {

    if (cmbAsignaturas.getItems() != null && cmbTipoAlumno.getItems() != null
        && cmbTipoAlumno.getSelectionModel().getSelectedItem() != null) {
      tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();

      final List<R_PruebaRendida> pRendidas = new ArrayList<>();
      for (R_EvaluacionPrueba evaluacionPrueba : evaluacionesPrueba) {
        Map<String, Object> params = MapBuilder.<String, Object>unordered()
            .put("evaluacionprueba_id", evaluacionPrueba.getId()).build();
        List<R_PruebaRendida> lstR_PruebaRendida =
            controller.findByParamsSynchro(R_PruebaRendida.class, params);
        pRendidas.addAll(lstR_PruebaRendida);
      }

      if (pRendidas != null && !pRendidas.isEmpty()) {
        final Pair<List<R_Curso>, List<XItemTablaObjetivo>> reporte =
            XUtilReportBuilder.reporteColegio(pRendidas, tipoAlumno);

        List<R_Curso> cursos = reporte.getFirst();

        final ObservableList<XItemTablaObjetivo> itemsTable =
            FXCollections.observableList(reporte.getSecond());
        final Optional<XItemTablaObjetivo> opFirst = itemsTable.stream().findFirst();
        if (!opFirst.isPresent())
          return;

        while (tblObjetivos.getColumns().size() > 3)
          tblObjetivos.getColumns().remove(tblObjetivos.getColumns().size() - 1);


        for (int n = 0; n < cursos.size(); n++) {
          final int idx = n;
          TableColumn<XItemTablaObjetivo, String> headerColumn =
              new TableColumn<>(cursos.get(n).getName());
          headerColumn.setStyle("-fx-font-size:10;-fx-alignment: CENTER;");

          final TableColumn<XItemTablaObjetivo, String> columnEjes =
              new TableColumn<>("Ejes Asociados");
          columnEjes.setStyle("-fx-font-size:10;-fx-alignment: CENTER-LEFT;");
          columnEjes.setCellValueFactory(c -> {
            if (c == null || c.getValue() == null || c.getValue().getItems() == null
                || c.getValue().getItems().get(idx) == null)
              return new ReadOnlyStringWrapper("");
            final List<XItemObjetivo> lItems = c.getValue().getItems();
            Log.info("idx=" + idx + " lItems =" + lItems + " lItems.get(idx)=" + lItems.get(idx));
            return new ReadOnlyStringWrapper(lItems.get(idx).getEjesAsociados());
          });
          columnEjes.setCellFactory(
              new Callback<TableColumn<XItemTablaObjetivo, String>, TableCell<XItemTablaObjetivo, String>>() {

                @Override
                public TableCell<XItemTablaObjetivo, String> call(
                    TableColumn<XItemTablaObjetivo, String> param) {
                  TableCell<XItemTablaObjetivo, String> cell = new TableCell<>();
                  Text text = new Text();
                  cell.setGraphic(text);
                  cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                  text.wrappingWidthProperty().bind(cell.widthProperty());
                  text.textProperty().bind(cell.itemProperty());
                  return cell;
                }
              });

          final TableColumn<XItemTablaObjetivo, String> columnHabilidades =
              new TableColumn<>("Habilidades Asociados");
          columnHabilidades.setStyle("-fx-font-size:10;-fx-alignment: CENTER-LEFT;");
          columnHabilidades.setCellValueFactory(c -> {
            if (c == null || c.getValue() == null || c.getValue().getItems() == null
                || c.getValue().getItems().get(idx) == null)
              return new ReadOnlyStringWrapper("");

            final List<XItemObjetivo> lItems = c.getValue().getItems();
            return new ReadOnlyStringWrapper(lItems.get(idx).getHabilidades());
          });
          columnHabilidades.setCellFactory(
              new Callback<TableColumn<XItemTablaObjetivo, String>, TableCell<XItemTablaObjetivo, String>>() {

                @Override
                public TableCell<XItemTablaObjetivo, String> call(
                    TableColumn<XItemTablaObjetivo, String> param) {
                  TableCell<XItemTablaObjetivo, String> cell = new TableCell<>();
                  Text text = new Text();
                  cell.setGraphic(text);
                  cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                  text.wrappingWidthProperty().bind(cell.widthProperty());
                  text.textProperty().bind(cell.itemProperty());
                  return cell;
                }
              });

          final TableColumn<XItemTablaObjetivo, String> columnPreguntas =
              new TableColumn<>("Preguntas");
          columnPreguntas.setCellFactory(
              new Callback<TableColumn<XItemTablaObjetivo, String>, TableCell<XItemTablaObjetivo, String>>() {

                @Override
                public TableCell<XItemTablaObjetivo, String> call(
                    TableColumn<XItemTablaObjetivo, String> param) {
                  TableCell<XItemTablaObjetivo, String> cell = new TableCell<>();
                  Text text = new Text();
                  cell.setGraphic(text);
                  cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                  text.wrappingWidthProperty().bind(cell.widthProperty());
                  text.textProperty().bind(cell.itemProperty());
                  return cell;
                }
              });
          columnPreguntas.setStyle("-fx-font-size:10;-fx-alignment: CENTER-LEFT;");
          columnPreguntas.setPrefWidth(100);
          columnPreguntas.setMaxWidth(100);
          columnPreguntas.setCellValueFactory(c -> {
            if (c == null || c.getValue() == null || c.getValue().getItems() == null
                || c.getValue().getItems().get(idx) == null)
              return new ReadOnlyStringWrapper("");

            final List<XItemObjetivo> lItems = c.getValue().getItems();
            return new ReadOnlyStringWrapper(lItems.get(idx).getPreguntas());
          });

          final TableColumn<XItemTablaObjetivo, Number> columnPercent =
              new TableColumn<>("% aprobación");
          columnPercent.setStyle("-fx-font-size:10;-fx-alignment: CENTER;");
          columnPercent.setCellValueFactory(c -> {
            if (c == null || c.getValue() == null || c.getValue().getItems() == null
                || c.getValue().getItems().get(idx) == null)
              return new ReadOnlyFloatWrapper(0);

            final List<XItemObjetivo> lItems = c.getValue().getItems();
            return new ReadOnlyFloatWrapper(lItems.get(idx).getPorcentajeAprobacion());
          });

          columnPercent.setCellFactory(c -> {
            return new TableCell<XItemTablaObjetivo, Number>() {

              @Override
              protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (value != null) {
                  setText(String.format("%5.2f%%", value.doubleValue()));
                  if (value.doubleValue() < 60) {
                    setTextFill(Color.RED);
                  } else {
                    setTextFill(Color.BLUE);
                  }
                }
              }

            };
          });
          headerColumn.getColumns().add(columnEjes);
          headerColumn.getColumns().add(columnHabilidades);
          headerColumn.getColumns().add(columnPreguntas);
          headerColumn.getColumns().add(columnPercent);
          tblObjetivos.getColumns().add(headerColumn);
        }
        tblObjetivos.setItems(itemsTable);
      }
    }
  }

  private void inicializeTable() {
    colObjetivos
        .setCellValueFactory(new PropertyValueFactory<XItemTablaObjetivo, R_Objetivo>("objetivo"));
  }

  @FXML
  public void initialize() {
    setTitle(TITLE);
    inicializeTable();

    cmbColegio.setOnAction(event -> {
      R_Colegio colegio = cmbColegio.getSelectionModel().getSelectedItem();
      if (colegio != null) {
        parameters.put(COLEGIO_ID, colegio.getId());
        R_Asignatura asig = cmbAsignaturas.getSelectionModel().getSelectedItem();
        String asignatura = asig != null ? asig.getName() : "-";
        setTitle(String.format(TITLE_FORMAT, colegio.getName(), asignatura));
      }
    });

    cmbAsignaturas.setOnAction(event -> {
      R_Asignatura asignatura = cmbAsignaturas.getSelectionModel().getSelectedItem();
      if (asignatura != null) {
        parameters.put(ASIGNATURA_ID, asignatura.getId());

        R_Colegio colegio = cmbColegio.getSelectionModel().getSelectedItem();
        String strColegio = colegio != null ? colegio.getName() : "-";

        setTitle(String.format(TITLE_FORMAT, strColegio,
            cmbAsignaturas.getSelectionModel().getSelectedItem()));

      }
    });

    cmbTipoAlumno.setOnAction(event -> {
      if (cmbTipoAlumno.getSelectionModel() == null)
        return;
      tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedItem().getId();
    });
    btnGenerarReporte.setOnAction(event -> handlerReporte());
  }

  private void handlerReporte() {
    if (!parameters.isEmpty() && parameters.containsKey(COLEGIO_ID)
        && parameters.containsKey(ASIGNATURA_ID)) {
      controller.findByParam(R_EvaluacionPrueba.class, parameters, this);
    }
  }

  @Override
  public void onDataArrived(List<Object> list) {
    if (list != null && !list.isEmpty()) {
      final Object entity = list.get(0);
      if (entity instanceof R_TipoAlumno) {
        final List<R_TipoAlumno> values =
            list.stream().map(t -> (R_TipoAlumno) t).collect(Collectors.toList());
        final ObservableList<R_TipoAlumno> tAlumnoList = FXCollections.observableArrayList(values);
        cmbTipoAlumno.setItems(tAlumnoList);
      }

      if (entity instanceof R_Colegio) {
        final List<R_Colegio> values =
            list.stream().map(t -> (R_Colegio) t).collect(Collectors.toList());
        final ObservableList<R_Colegio> colegios = FXCollections.observableArrayList(values);
        cmbColegio.setItems(colegios);
      }

      if (entity instanceof R_Asignatura) {
        final List<R_Asignatura> values =
            list.stream().map(t -> (R_Asignatura) t).collect(Collectors.toList());
        final ObservableList<R_Asignatura> asginaturas = FXCollections.observableArrayList(values);
        cmbAsignaturas.setItems(asginaturas);
      }

      if (entity instanceof R_EvaluacionPrueba) {
        evaluacionesPrueba = FXCollections.observableArrayList();
        for (Object object : list) {
          R_EvaluacionPrueba evaluacion = (R_EvaluacionPrueba) object;
          evaluacionesPrueba.add(evaluacion);
        }
        generateReport();
      }
    }

  }

}
