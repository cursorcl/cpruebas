package cl.eos.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.NivelEvaluacion;
import cl.eos.persistence.models.RangoEvaluacion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.BigDecimalField;

public class NivelEvaluacionRangoEvaluacionEdicionController extends AFormView implements EventHandler<ActionEvent> {
	@FXML
	private TextField txtRango;
	@FXML
	private TextField txtAbreviacion;
	@FXML
	private MenuItem mnuCancelar;
	@FXML
	private MenuItem mnuGrabar;
	@FXML
	private BigDecimalField bdMinimo;
	@FXML
	private Button btnCancelar;
	@FXML
	private Button btnGrabar;
	@FXML
	private TextField txtNivelEvaluacion;
	@FXML
	private BigDecimalField bdMaximo;
	@FXML
	private TableView<RangoEvaluacion> tblRangos;
	@FXML
	private TableColumn<RangoEvaluacion, Float> colRangoMaximo;
	@FXML
	private TableColumn<RangoEvaluacion, Float> colRangoMinimo;
	@FXML
	private TableColumn<RangoEvaluacion, String> colRangosAbrev;
	@FXML
	private TableColumn<RangoEvaluacion, String> colRangosNombre;
	@FXML
	private Label lblButton;
	@FXML
	private Button btnCambiar;

	@FXML
	private MenuItem mnuItemAgregar;
	@FXML
	private MenuItem mnuItemEliminar;

	private NivelEvaluacion nivel;
	private boolean agregando;

	@FXML
	public void initialize() {
		inicializaTabla();
		mnuCancelar.setOnAction(this);
		mnuGrabar.setOnAction(this);
		btnGrabar.setOnAction(this);
		btnCancelar.setOnAction(this);
		btnCambiar.setOnAction(this);
		mnuItemAgregar.setOnAction(this);
		mnuItemEliminar.setOnAction(this);
	}

	private void inicializaTabla() {
		tblRangos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		colRangosNombre.setCellValueFactory(new PropertyValueFactory<RangoEvaluacion, String>("name"));
		colRangosAbrev.setCellValueFactory(new PropertyValueFactory<RangoEvaluacion, String>("abreviacion"));
		colRangoMinimo.setCellValueFactory(new PropertyValueFactory<RangoEvaluacion, Float>("minimo"));
		colRangoMaximo.setCellValueFactory(new PropertyValueFactory<RangoEvaluacion, Float>("maximo"));

		tblRangos.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (tblRangos.getItems() == null || tblRangos.getItems().isEmpty())
					return;
				RangoEvaluacion rango = tblRangos.getSelectionModel().getSelectedItem();
				if (rango == null)
					return;
				
				
				txtRango.setText(rango.getName());
				txtAbreviacion.setText(rango.getAbreviacion());
				bdMinimo.setNumber(new BigDecimal(rango.getMinimo()));
				bdMaximo.setNumber(new BigDecimal(rango.getMaximo()));
				agregando = false;
			}
		});
	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof NivelEvaluacion) {
			nivel = (NivelEvaluacion) entity;
			txtNivelEvaluacion.setText(nivel.getName());

			ObservableList<RangoEvaluacion> lsRangoEvaluacion = FXCollections.observableArrayList();
			for (RangoEvaluacion iEntity : nivel.getRangos()) {
				lsRangoEvaluacion.add((RangoEvaluacion) iEntity);
			}
			tblRangos.setItems(lsRangoEvaluacion);
		}
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == mnuGrabar || event.getSource() == btnGrabar) {
			Stage stage = (Stage) btnCancelar.getScene().getWindow();
			stage.close();

			if(nivel == null)
			{
				nivel = new NivelEvaluacion();
				nivel.setRangos(new ArrayList<RangoEvaluacion>());
			}
			
			nivel.setName(txtNivelEvaluacion.getText());
			ObservableList<RangoEvaluacion> items = tblRangos.getItems();
			nivel.setNroRangos(items.size());
			nivel.getRangos().clear();
			for (RangoEvaluacion rango : items) {
				nivel.getRangos().add(rango);
			}
			save(nivel);
		}
		if (event.getSource() == btnCancelar) {
			Stage stage = (Stage) btnCancelar.getScene().getWindow();
			stage.close();
		}
		if (event.getSource() == btnCambiar) {
			if (agregando) {
				RangoEvaluacion rango = new RangoEvaluacion();
				rango.setAbreviacion(txtAbreviacion.getText());
				rango.setName(txtRango.getText());
				rango.setMinimo(bdMinimo.getNumber().floatValue());
				rango.setMaximo(bdMaximo.getNumber().floatValue());
				tblRangos.getItems().add(rango);

			} else {
				int index = tblRangos.getSelectionModel().getSelectedIndex();
				RangoEvaluacion rango = tblRangos.getSelectionModel().getSelectedItem();
				rango.setAbreviacion(txtAbreviacion.getText());
				rango.setName(txtRango.getText());
				rango.setMinimo(bdMinimo.getNumber().floatValue());
				rango.setMaximo(bdMaximo.getNumber().floatValue());
				tblRangos.getItems().set(index, rango);
			}
		}
		if (event.getSource() == mnuItemEliminar) {
			RangoEvaluacion rango = tblRangos.getSelectionModel().getSelectedItem();
			if (rango != null) {

				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirma Eliminar");
				alert.setHeaderText(null);
				alert.setContentText("Confirma borrado de la prueba?");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					int index = tblRangos.getSelectionModel().getSelectedIndex();
					tblRangos.getItems().remove(index);
				}
			}
		}
		if (event.getSource() == mnuItemAgregar) {
			agregando = true;
			txtRango.setText("");
			txtAbreviacion.setText("");
			bdMinimo.setNumber(new BigDecimal(0));
			bdMaximo.setNumber(new BigDecimal(0));
			txtRango.requestFocus();
		}

	}

}
