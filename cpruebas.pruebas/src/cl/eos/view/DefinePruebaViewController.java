package cl.eos.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.Formas;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
import cl.eos.persistence.util.Comparadores;
import cl.eos.view.dnd.EjeTematicoDND;
import cl.eos.view.dnd.HabilidadDND;
import cl.eos.view.editablecells.EditingCellRespuesta;
import cl.eos.view.editablecells.StyleChangingRowFactory;

public class DefinePruebaViewController extends AFormView {

	private static final String VALID_LETTERS = "ABCDE";
	private String respsValidas = "";
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
	private TableColumn<RegistroDefinePrueba, Habilidad> habCol;
	@FXML
	private TableColumn<RegistroDefinePrueba, EjeTematico> ejeCol;
	@FXML
	private TableColumn<RegistroDefinePrueba, String> badCol;
	@FXML
	private TableView<EjeTematico> tblEjesTematicos;
	@FXML
	private TableColumn<EjeTematico, String> ejeTematicoCol;
	@FXML
	private TableView<Habilidad> tblHabilidades;
	@FXML
	private TableColumn<Habilidad, String> habilidadCol;
	@FXML
	private MenuItem mnuGrabar;
	@FXML
	private MenuItem mnuVolver;
	@FXML
	private Button btnListo;
	@FXML
	private TextField txtRespuestas;
	@FXML
	private Label lblCount;

	private Prueba prueba;

	private ObservableList<RegistroDefinePrueba> registros;
	private StyleChangingRowFactory<RegistroDefinePrueba> rowFactory;

	public DefinePruebaViewController() {
		setTitle("Definir Prueba");
	}

	@FXML
	public void initialize() {

		tblRegistroDefinePrueba.setEditable(true);
		tblRegistroDefinePrueba.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);
		registerDND();

		rowFactory = new StyleChangingRowFactory<>("bad");
		tblRegistroDefinePrueba.setRowFactory(rowFactory);

		preguntaCol
				.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, Integer>(
						"numero"));

		preguntaCol.setStyle("-fx-alignment: CENTER;");
		respuestaCol
				.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, String>(
						"respuesta"));
		respuestaCol.setEditable(true);
		respuestaCol.setStyle("-fx-alignment: CENTER;");

		vfCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, Boolean>(
				"verdaderoFalso"));
		vfCol.setCellFactory(CheckBoxTableCell.forTableColumn(vfCol));
		vfCol.setEditable(true);

		mentalCol
				.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, Boolean>(
						"mental"));
		mentalCol.setCellFactory(CheckBoxTableCell.forTableColumn(mentalCol));
		mentalCol.setEditable(true);

		habCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, Habilidad>(
				"habilidad"));
		ejeCol.setCellValueFactory(new PropertyValueFactory<RegistroDefinePrueba, EjeTematico>(
				"ejeTematico"));
		habilidadCol
				.setCellValueFactory(new PropertyValueFactory<Habilidad, String>(
						"name"));
		ejeTematicoCol
				.setCellValueFactory(new PropertyValueFactory<EjeTematico, String>(
						"name"));


		tblHabilidades.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Dragboard db = tblHabilidades
						.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				content.putIfAbsent(HabilidadDND.habilidadTrackDataFormat,
						tblHabilidades.getSelectionModel().getSelectedItem());
				db.setContent(content);
				event.consume();
			}
		});
		tblEjesTematicos.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Dragboard db = tblHabilidades
						.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				content.putIfAbsent(EjeTematicoDND.ejeTematicoTrackDataFormat,
						tblEjesTematicos.getSelectionModel().getSelectedItem());
				db.setContent(content);
				event.consume();
			}
		});

		mnuGrabar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ejecutaGrabar();
			}
		});
		btnListo.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ejecutarAccionListo();

			}
		});
		mnuVolver.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
			}
		});
		txtRespuestas.setOnKeyTyped(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				final String strValid = respsValidas + " VF";
				if (strValid.indexOf(event.getCharacter().toUpperCase()) == -1) {
					event.consume();
				}
				if (txtRespuestas.getText() != null
						&& txtRespuestas.getText().length() >= prueba
								.getNroPreguntas()) {
					event.consume();
				}
				if (txtRespuestas.getText() != null) {
					actualizaLabelCuenta(txtRespuestas.getText().length() + 1);
				}
			}
		});
	}

	private void registerDND() {
		tblRegistroDefinePrueba.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent dragEvent) {
				ObservableList<RegistroDefinePrueba> seleccionados = tblRegistroDefinePrueba
						.getSelectionModel().getSelectedItems();
				if (seleccionados != null && !seleccionados.isEmpty()) {
					if (dragEvent.getDragboard().getContent(
							EjeTematicoDND.ejeTematicoTrackDataFormat) != null) {
						EjeTematico ejeTematico = (EjeTematico) dragEvent
								.getDragboard()
								.getContent(
										EjeTematicoDND.ejeTematicoTrackDataFormat);
						for (RegistroDefinePrueba registro : seleccionados) {
							registro.setEjeTematico(ejeTematico);
						}
					} else if (dragEvent.getDragboard().getContent(
							HabilidadDND.habilidadTrackDataFormat) != null) {
						Habilidad habilidad = (Habilidad) dragEvent
								.getDragboard().getContent(
										HabilidadDND.habilidadTrackDataFormat);
						for (RegistroDefinePrueba registro : seleccionados) {
							registro.setHabilidad(habilidad);
						}
					}
				}
			}
		});

		tblRegistroDefinePrueba.setOnDragEntered(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent dragEvent) {
				tblRegistroDefinePrueba.setBlendMode(BlendMode.DARKEN);
			}
		});

		tblRegistroDefinePrueba.setOnDragExited(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent dragEvent) {
				tblRegistroDefinePrueba.setBlendMode(null);
			}
		});

		tblRegistroDefinePrueba.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent dragEvent) {
				if (dragEvent.getDragboard().getContent(
						EjeTematicoDND.ejeTematicoTrackDataFormat) != null
						|| dragEvent.getDragboard().getContent(
								HabilidadDND.habilidadTrackDataFormat) != null) {
					dragEvent.acceptTransferModes(TransferMode.COPY);
				}
			}
		});
	}

	protected void ejecutarAccionListo() {
		String resps = txtRespuestas.getText();
		int n = 0;
		for (RegistroDefinePrueba registro : registros) {
			String r = resps.substring(n, ++n).toUpperCase();
			if (r.equals("V") || r.equals("F")) {
				registro.setVerdaderoFalso(true);
				registro.setRespuesta(r);
			} else if (r.equals(" ")) {
				registro.setMental(true);
				registro.setRespuesta(r);
			} else if (respsValidas.indexOf(r) != -1) {
				registro.setMental(false);
				registro.setVerdaderoFalso(false);
				registro.setRespuesta(r);
			}
		}
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
			registros = FXCollections.observableArrayList();
			prueba = (Prueba) entity;

			respsValidas = VALID_LETTERS.substring(0, prueba.getAlternativas());
			respuestaCol
					.setCellFactory(new Callback<TableColumn<RegistroDefinePrueba, String>, TableCell<RegistroDefinePrueba, String>>() {
						@Override
						public TableCell<RegistroDefinePrueba, String> call(
								final TableColumn<RegistroDefinePrueba, String> column) {
							EditingCellRespuesta editing = new EditingCellRespuesta();
							editing.setValidValues(respsValidas);
							return editing;
						}
					});

			txtRespuestas.setText("");
			if (prueba.getRespuestas() != null
					&& !prueba.getRespuestas().isEmpty()) {
				StringBuffer resps = new StringBuffer();
				List<RespuestasEsperadasPrueba> respuestas = new ArrayList<RespuestasEsperadasPrueba>();
				for (RespuestasEsperadasPrueba respuesta : prueba
						.getRespuestas()) {
					respuestas.add(respuesta);
				}
				Collections.sort(respuestas,
						Comparadores.compararRespuestasEsperadas());
				for (RespuestasEsperadasPrueba respuesta : respuestas) {
					RegistroDefinePrueba registro = new RegistroDefinePrueba();
					registro.setNumero(respuesta.getNumero());
					registro.setRespuesta(respuesta.getRespuesta());
					registro.setEjeTematico(respuesta.getEjeTematico());
					registro.setHabilidad(respuesta.getHabilidad());
					registro.setVerdaderoFalso(respuesta.getVerdaderoFalso());
					registro.setMental(respuesta.getMental());
					resps.append(respuesta.getRespuesta());
					registros.add(registro);
				}
				txtRespuestas.setText(resps.toString());
				actualizaLabelCuenta(txtRespuestas.getText().length());
			} else {
				int nroPreguntas = prueba.getNroPreguntas();
				for (int n = 0; n < nroPreguntas; n++) {
					RegistroDefinePrueba registro = new RegistroDefinePrueba();
					registro.setNumero(n + 1);
					registros.add(registro);
					actualizaLabelCuenta(0);
				}
			}

			tblRegistroDefinePrueba.setItems(registros);
		}
	}

	private void actualizaLabelCuenta(int nro) {
		lblCount.setText(String.format("%d/%d", nro, prueba.getNroPreguntas()));
	}

	protected void ejecutaGrabar() {
		if (validate()) {
			List<RespuestasEsperadasPrueba> respuestas = new ArrayList<RespuestasEsperadasPrueba>();
			List<Integer> numeros = new ArrayList<Integer>();
			String responses = "";
			for (RegistroDefinePrueba registro : registros) {
				RespuestasEsperadasPrueba respuesta = new RespuestasEsperadasPrueba();
				respuesta.setEjeTematico(registro.getEjeTematico());
				respuesta.setHabilidad(registro.getHabilidad());
				respuesta.setMental(registro.getMental());
				respuesta.setName(registro.getNumero().toString());
				respuesta.setNumero(registro.getNumero());
				respuesta.setRespuesta(registro.getRespuesta());
				respuesta.setVerdaderoFalso(registro.getVerdaderoFalso());
				respuesta.setPrueba(prueba);
				respuestas.add(respuesta);
				numeros.add(registro.getNumero());
				responses += registro.getRespuesta() == " " ? "V" : registro
						.getRespuesta();
			}
			prueba.setRespuestas(respuestas);
			prueba.setResponses(responses);
			List<Formas> formas = new ArrayList<Formas>();
			for (int n = 0; n < prueba.getNroFormas().intValue(); n++) {
				Formas forma = new Formas();
				forma.setForma(n + 1);
				forma.setName("Forma " + (n + 1));
				String orden = new String();
				for (int idx = 0; idx < numeros.size(); idx++) {
					if (idx > 0) {
						orden += ",";
					}
					orden += numeros.get(idx).toString();
				}
				forma.setOrden(orden);
				forma.setPrueba(prueba);
				formas.add(forma);
				Collections.shuffle(numeros);
			}
			prueba.setFormas(formas);
			save(prueba);
		}
	}

	@Override
	public boolean validate() {
		boolean valido = true;
		ObservableList<Integer> badIdx = FXCollections.observableArrayList();
		int n = 0;
		boolean todosValidos = true;
		for (RegistroDefinePrueba registro : registros) {
			valido = (registro.getEjeTematico() != null);
			valido = valido && (registro.getHabilidad() != null);
			valido = valido && !registro.getRespuesta().isEmpty();
			valido = valido
					&& ((registro.getMental() && registro.getRespuesta() == " ")
							|| (registro.getVerdaderoFalso() && "VF"
									.contains(registro.getRespuesta()
											.toUpperCase())) || (respsValidas
								.contains(registro.getRespuesta().toUpperCase())));
			if (!valido) {
				badIdx.add(n);
			}
			n++;
			todosValidos = todosValidos && valido;
		}
		if (!todosValidos) {
			rowFactory.getStyledRowIndices().setAll(badIdx);
		} else {
			badIdx.add(-1);
			rowFactory.getStyledRowIndices().setAll(badIdx);
		}
		return todosValidos;
	}
}
