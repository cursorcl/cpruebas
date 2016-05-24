package curso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import cl.eos.common.Constants;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.EvaluacionPrueba;
import cl.eos.persistence.models.PruebaRendida;
import cl.eos.persistence.models.TipoAlumno;
import cl.eos.util.ExcelSheetWriterObj;

public class ResumenAlumnoView extends AFormView implements EventHandler<ActionEvent> {
	private static final String M2 = "M";
	private static final String MINUS = "-";
	private static final String B = "B";
	private static final String PLUS = "+";
	private static final String O = "O";
	@FXML
	private TextField txtPrueba;
	@FXML
	private TextField txtCurso;
	@FXML
	private TextField txtAsignatura;
	@FXML
	private MenuItem mnuExportarRespuestas;
	@FXML
	private MenuItem mnuExportarAlumnos;

	@FXML
	private TableView<PruebaRendida> tblAlumnos;
	@FXML
	private TableColumn<PruebaRendida, String> colARut;
	@FXML
	private TableColumn<PruebaRendida, String> colAPaterno;
	@FXML
	private TableColumn<PruebaRendida, String> colAMaterno;
	@FXML
	private TableColumn<PruebaRendida, String> colAName;
	@FXML
	private TableColumn<PruebaRendida, Integer> colABuenas;
	@FXML
	private TableColumn<PruebaRendida, Integer> colAMalas;
	@FXML
	private TableColumn<PruebaRendida, Integer> colAOmitidas;

	@FXML
	private TableView<ObservableList<String>> tblRespuestas;

	@FXML
	private ComboBox<TipoAlumno> cmbTipoAlumno;

	long tipoAlumno = Constants.PIE_ALL;

	@FXML
	private Button btnGenerar;
	private EvaluacionPrueba evaluacionPrueba;

	public ResumenAlumnoView() {
	}

	@FXML
	public void initialize() {
		this.setTitle("Resumen de respuestas por alumno");
		inicializarTablaAlumnos();
		clicTablaRespuesta();
		clicTablaAlumnos();
		mnuExportarRespuestas.setOnAction(this);
		mnuExportarAlumnos.setOnAction(this);
		btnGenerar.setOnAction(this);

		cmbTipoAlumno.setOnAction(event -> {
			tipoAlumno = cmbTipoAlumno.getSelectionModel().getSelectedIndex();
		});
	}

	private void clicTablaRespuesta() {
		tblAlumnos.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				tblRespuestas.getSelectionModel().clearSelection();
				tblRespuestas.getSelectionModel().select(tblAlumnos.getSelectionModel().getSelectedIndex());
			}
		});

	}

	private void clicTablaAlumnos() {
		tblRespuestas.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				tblAlumnos.getSelectionModel().clearSelection();
				tblAlumnos.getSelectionModel().select(tblRespuestas.getSelectionModel().getSelectedIndex());
			}
		});

	}

	private void inicializarTablaAlumnos() {
		tblAlumnos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		colARut.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>("rut"));
		colAName.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>("nombre"));
		colAPaterno.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>("paterno"));
		colAMaterno.setCellValueFactory(new PropertyValueFactory<PruebaRendida, String>("materno"));
		colABuenas.setCellValueFactory(new PropertyValueFactory<PruebaRendida, Integer>("buenas"));
		colAMalas.setCellValueFactory(new PropertyValueFactory<PruebaRendida, Integer>("malas"));
		colAOmitidas.setCellValueFactory(new PropertyValueFactory<PruebaRendida, Integer>("omitidas"));
	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof EvaluacionPrueba) {
			evaluacionPrueba = (EvaluacionPrueba) entity;
			generateReport();
		}
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
				cmbTipoAlumno.getSelectionModel().select(0);
				generateReport();
			}
		}
	}

	@Override
	public void handle(ActionEvent event) {
		Object source = event.getSource();
		if (source == mnuExportarRespuestas || source == mnuExportarAlumnos) {

			tblAlumnos.setId("Alumnos");
			tblRespuestas.setId("Respuestas");

			List<TableView<? extends Object>> listaTablas = new LinkedList<>();
			listaTablas.add((TableView<? extends Object>) tblAlumnos);
			listaTablas.add((TableView<? extends Object>) tblRespuestas);

			ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
		} else if (source == btnGenerar) {
			generateReport();
		}
	}

	private void generateReport() {
		if (evaluacionPrueba != null && cmbTipoAlumno.getItems() != null && !cmbTipoAlumno.getItems().isEmpty()) {
			tblRespuestas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

			List<PruebaRendida> list = evaluacionPrueba.getPruebasRendidas();

			ObservableList<PruebaRendida> oList = FXCollections.observableArrayList();
			if (list != null && !list.isEmpty()) {
				for (PruebaRendida pr : list) {
					if (tipoAlumno == Constants.PIE_ALL || pr.getAlumno().getTipoAlumno().getId().equals(tipoAlumno)) {
						oList.add(pr);
					}
				}
				tblAlumnos.setItems(oList);
			}

			if (evaluacionPrueba.getCurso() != null) {
				txtCurso.setText(evaluacionPrueba.getCurso().getName());
			}

			if (evaluacionPrueba.getPrueba() != null) {
				txtPrueba.setText(evaluacionPrueba.getPrueba().getName());
				if (evaluacionPrueba.getPrueba().getAsignatura() != null) {
					txtAsignatura.setText(evaluacionPrueba.getPrueba().getAsignatura().getName());
				}
			}
			crearTabla(evaluacionPrueba);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void crearTabla(EvaluacionPrueba eval) {

		List<String> columns = new ArrayList<String>();

		columns.add("Rut");
		columns.add("Nombre");
		columns.add("Paterno");
		columns.add("Materno");
		for (int n = 0; n < eval.getNroPreguntas(); n++) {
			columns.add(String.valueOf(n + 1));
		}

		TableColumn[] tableColumns = new TableColumn[columns.size()];
		int columnIndex = 0;
		for (String columName : columns) {
			tableColumns[columnIndex] = new TableColumn(columName);
			final int idx = columnIndex;
			tableColumns[columnIndex].setCellValueFactory(
					new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
						public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
							return new SimpleStringProperty(param.getValue().get(idx).toString());
						}
					});
			tableColumns[columnIndex].setCellFactory(new Callback<TableColumn, TableCell>() {

				public TableCell call(TableColumn param) {
					TableCell cell = new TableCell() {
						@Override
						public void updateItem(Object item, boolean empty) {
							if (item != null) {
								setText(item.toString());
							}
						}
					};
					// cell.setAlignment(Pos.CENTER);
					return cell;
				}
			});

			if (columnIndex < 4) {
				tableColumns[columnIndex].setMaxWidth(100);
				tableColumns[columnIndex].setMinWidth(100);
				tableColumns[columnIndex].setSortable(true);
			} else {
				tableColumns[columnIndex].setMaxWidth(20);
				tableColumns[columnIndex].setMinWidth(20);
				tableColumns[columnIndex].setSortable(false);
			}
			tableColumns[columnIndex].setResizable(false);
			columnIndex++;
		}
		tblRespuestas.getColumns().setAll(tableColumns);

		ObservableList<ObservableList<String>> csvData = FXCollections.observableArrayList();

		for (PruebaRendida pr : eval.getPruebasRendidas()) {
			if (tipoAlumno != Constants.PIE_ALL && !pr.getAlumno().getTipoAlumno().getId().equals(tipoAlumno)) {
				continue;
			}
			ObservableList<String> row = FXCollections.observableArrayList();
			row.add(pr.getRut());
			row.add(pr.getPaterno());
			row.add(pr.getMaterno());
			row.add(pr.getNombre());

			String resps = pr.getRespuestas().toUpperCase();
			for (int m = 0; m < eval.getNroPreguntas(); m++) {

				String rP = resps.substring(m, m + 1);

				if (ResumenAlumnoView.O.equals(rP)) {
					rP = ResumenAlumnoView.O;
				} else if (ResumenAlumnoView.PLUS.equals(rP)) {
					rP = ResumenAlumnoView.B;
				} else if (ResumenAlumnoView.MINUS.equals(rP)) {
					rP = ResumenAlumnoView.M2;
				}
				row.add(rP);
			}
			csvData.add(row);
		}

		tblRespuestas.getItems().setAll(csvData);

	}
}
