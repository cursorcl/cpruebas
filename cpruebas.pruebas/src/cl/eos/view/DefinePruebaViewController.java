package cl.eos.view;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.Prueba;

public class DefinePruebaViewController extends AFormView {

	private static final String VALID_LETTERS = "ABCDEVF";

	@FXML
	private TableView<RegistroDefinePrueba> tblRegistroDefinePrueba;
	@FXML
	private TableColumn<RegistroDefinePrueba, Integer> preguntaCol;
	@FXML
	private TableColumn<RegistroDefinePrueba, String> respuestaCol;
	@FXML
	private TableColumn<RegistroDefinePrueba, Boolean> vfCol;
	@FXML
	private TableColumn<RegistroDefinePrueba, Boolean> mentalCol;
	@FXML
	private TableView<EjeTematico> tblEjesTematicos;
	@FXML
	private TableColumn<EjeTematico, String> ejeTematicoCol;
	@FXML
	private TableView<Habilidad> tblHabilidades;
	@FXML
	private TableColumn<Habilidad, String> habilidadCol;

	/**
	 * A que prueba pertenece.
	 */
	private Prueba prueba;

	@FXML
	public void initialize() {

		preguntaCol
				.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, Integer>(
						"numero"));
		respuestaCol
				.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, String>(
						"respuesta"));
		respuestaCol.setCellFactory(TextFieldTableCell
				.<RegistroDefinePrueba> forTableColumn());

		respuestaCol
				.setCellFactory(new Callback<TableColumn<RegistroDefinePrueba, String>, TableCell<RegistroDefinePrueba, String>>() {
					@Override
					public TableCell<RegistroDefinePrueba, String> call(
							final TableColumn<RegistroDefinePrueba, String> column) {
						return new EditingCellRespuesta();
					}
				});

		respuestaCol.setEditable(true);
		respuestaCol
				.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<RegistroDefinePrueba, String>>() {
					@Override
					public void handle(
							TableColumn.CellEditEvent<RegistroDefinePrueba, String> event) {
						String values = VALID_LETTERS.substring(0,
								prueba.getAlternativas());
						String answer = event.getNewValue().substring(0, 1)
								.toUpperCase();
						if (values.indexOf(answer) != -1) {
							event.getRowValue().setRespuesta(answer);
						} else {
							event.getRowValue().setRespuesta(
									event.getOldValue());
						}
					}
				});

		vfCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, Boolean>(
				"verdaderoFalso"));
		vfCol.setCellFactory(CheckBoxTableCell.forTableColumn(vfCol));
		vfCol.setEditable(true);
		vfCol.setOnEditCommit(new EventHandler<CellEditEvent<RegistroDefinePrueba, Boolean>>() {
			@Override
			public void handle(
					CellEditEvent<RegistroDefinePrueba, Boolean> event) {

				if (event.getRowValue().getVerdaderoFalso().booleanValue()) {
					event.getRowValue().setMental(false);
				}
			}

		});

		mentalCol
				.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, Boolean>(
						"mental"));
		mentalCol.setCellFactory(CheckBoxTableCell.forTableColumn(mentalCol));
		mentalCol.setEditable(true);
		tblRegistroDefinePrueba.setEditable(true);

		habilidadCol
				.setCellValueFactory(new PropertyValueFactory<Habilidad, String>(
						"name"));
		ejeTematicoCol
				.setCellValueFactory(new PropertyValueFactory<EjeTematico, String>(
						"name"));
	}

	@Override
	public void onDataArrived(List<Object> list) {

		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof EjeTematico) {
				ObservableList<EjeTematico> ejesTematicos = FXCollections
						.observableArrayList();
				for (Object lEntity : list) {
					ejesTematicos.add((EjeTematico) lEntity);
				}
				tblEjesTematicos.setItems(ejesTematicos);
			} else if (entity instanceof Habilidad) {
				ObservableList<Habilidad> habilidades = FXCollections
						.observableArrayList();
				for (Object lEntity : list) {
					habilidades.add((Habilidad) lEntity);
				}
				tblHabilidades.setItems(habilidades);
			}
		}
	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof Prueba) {
			ObservableList<RegistroDefinePrueba> registros = FXCollections
					.observableArrayList();

			respuestaCol
			.setCellFactory(new Callback<TableColumn<RegistroDefinePrueba, String>, TableCell<RegistroDefinePrueba, String>>() {
				@Override
				public TableCell<RegistroDefinePrueba, String> call(
						final TableColumn<RegistroDefinePrueba, String> column) {
					EditingCellRespuesta editing = new EditingCellRespuesta();
					editing.setValidValues(VALID_LETTERS.substring(0, prueba.getAlternativas()));
					return editing;
				}
			});

			prueba = (Prueba) entity;
			if (prueba.getResponses() != null) {
				String respuestas = prueba.getResponses();
				int nroPreguntas = prueba.getNroPreguntas();
				
				for (int n = 0; n < nroPreguntas; n++) {
					RegistroDefinePrueba registro = new RegistroDefinePrueba();
					registro.setNumero(n + 1);
					registro.setRespuesta(respuestas.substring(n, n + 1));
					registros.add(registro);
				}
			} else {
				int nroPreguntas = prueba.getNroPreguntas();
				for (int n = 0; n < nroPreguntas; n++) {
					RegistroDefinePrueba registro = new RegistroDefinePrueba();
					registro.setNumero(n + 1);
					registros.add(registro);
				}
			}
			
		}
	}

	public Prueba getPrueba() {
		return prueba;
	}

	public void setPrueba(Prueba prueba) {
		this.prueba = prueba;
		if (this.prueba != null) {
			this.prueba.getAlternativas();
		}
	}

	public static void main(String[] args) {
		String koala = "cualquier palabra";
		System.out.println(koala.substring(2, 3));
	}

}
