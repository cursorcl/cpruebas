package cl.eos.view;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javax.imageio.ImageIO;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Colegio;
import cl.eos.util.ExcelSheetWriter;

public class ColegiosView extends AFormView implements
		EventHandler<ActionEvent> {

	private static final int LARGO_CAMPO_TEXT = 100;

	@FXML
	private MenuItem mnuAgregar;

	@FXML
	private MenuItem mnuGrabar;

	@FXML
	private MenuItem mnItemEliminar;

	@FXML
	private MenuItem mnItemModificar;

	@FXML
	private MenuItem mnuEliminar;

	@FXML
	private MenuItem mnuModificar;

	@FXML
	private MenuItem menuExportar;

	@FXML
	private MenuItem mnuExportar;

//	@FXML
//	private Button btnImagen;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtDireccion;

	@FXML
	private ImageView imgColegio;

	@FXML
	private TableView<Colegio> tblColegio;

	@FXML
	private TableColumn<Colegio, Long> colId;
	@FXML
	private TableColumn<Colegio, String> colNombre;

	@FXML
	private TableColumn<Colegio, String> colDireccion;

	@FXML
	private Label lblError;

	public ColegiosView() {
		setTitle("Colegios");
	}

	@FXML
	public void initialize() {
		inicializaTabla();
		accionClicTabla();
		mnuAgregar.setOnAction(this);
		mnuGrabar.setOnAction(this);
		mnuModificar.setOnAction(this);
		mnuEliminar.setOnAction(this);
		mnItemEliminar.setOnAction(this);
		mnItemModificar.setOnAction(this);
		mnuExportar.setOnAction(this);
		menuExportar.setOnAction(this);
//		btnImagen.setOnAction(this);
	}

//	private void accionButtonImagen() {
//		FileChooser fileChooser = new FileChooser();
//		File file = fileChooser.showOpenDialog(null);
//		if (file != null) {
//			Dimension dim = Utils.getImageDim(file.getPath());
//			if (dim.getHeight() <= 256 && dim.getWidth() <= 256) {
//				try {
//					URL url = file.toURI().toURL();
//					imgColegio.setImage(new Image(url.toString()));
//				} catch (MalformedURLException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}

	private void accionClicTabla() {
		tblColegio.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ObservableList<Colegio> itemsSelec = tblColegio
						.getSelectionModel().getSelectedItems();

				if (itemsSelec.size() > 1) {
					mnItemModificar.setDisable(true);
					mnItemEliminar.setDisable(false);
				} else if (itemsSelec.size() == 1) {
					select((IEntity) itemsSelec.get(0));
					mnItemModificar.setDisable(false);
					mnItemEliminar.setDisable(false);
				}
			}
		});
	}

	private void inicializaTabla() {
		tblColegio.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		colId.setCellValueFactory(new PropertyValueFactory<Colegio, Long>("id"));
		colNombre
				.setCellValueFactory(new PropertyValueFactory<Colegio, String>(
						"name"));
		colDireccion
				.setCellValueFactory(new PropertyValueFactory<Colegio, String>(
						"direccion"));
	}

	private void accionModificar() {
		Colegio colegio = tblColegio.getSelectionModel().getSelectedItem();
		if (colegio != null) {
			txtNombre.setText(colegio.getName());
			txtDireccion.setText(colegio.getDireccion());

			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					colegio.getImage());
			javafx.scene.image.Image image = new javafx.scene.image.Image(
					byteArrayInputStream);
			imgColegio.setImage(image);
		}
	}

	private void accionEliminar() {
		ObservableList<Colegio> colegiosSelec = tblColegio.getSelectionModel()
				.getSelectedItems();
		delete(colegiosSelec);
		tblColegio.getSelectionModel().clearSelection();
	}

	private void accionGrabar() {
		IEntity entitySelected = getSelectedEntity();
		removeAllStyles();
		if (validate()) {
			if (lblError != null) {
				lblError.setText(" ");
			}
			Colegio colegio = null;
			if (entitySelected != null && entitySelected instanceof Colegio) {
				colegio = (Colegio) entitySelected;
			} else {
				colegio = new Colegio();
			}
			colegio.setName(txtNombre.getText());
			colegio.setDireccion(txtDireccion.getText());
			BufferedImage bufferImg = SwingFXUtils.fromFXImage(
					imgColegio.getImage(), null);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try {
				ImageIO.write(bufferImg, "png", outputStream);
				colegio.setImage(outputStream.toByteArray());
			} catch (IOException e) {
				e.printStackTrace();
			}
			save(colegio);
		} else {
			lblError.getStyleClass().add("bad");
			lblError.setText("Corregir campos destacados en color rojo");
		}
		limpiarControles();
	}

	private void limpiarControles() {
		txtNombre.clear();
		txtDireccion.clear();
		imgColegio.setImage(null);
		select(null);
		tblColegio.getSelectionModel().clearSelection();
	}

	@Override
	public void onSaved(IEntity otObject) {
		System.out.println("Elemento grabando:" + otObject.toString());
		int indice = tblColegio.getItems().lastIndexOf(otObject);
		if (indice != -1) {
			tblColegio.getItems().remove(otObject);
			tblColegio.getItems().add(indice, (Colegio) otObject);
		} else {
			tblColegio.getItems().add((Colegio) otObject);
		}
	}

	@Override
	public void onDeleted(IEntity entity) {
		System.out.println("Elementoeliminando:" + entity.toString());
		tblColegio.getItems().remove(entity);
	}

	public boolean validate() {
		boolean valida = true;
		if (txtNombre.getText() == null || txtNombre.getText().equals("")) {
			txtNombre.getStyleClass().add("bad");
			valida = false;
		}
		if (txtNombre.getText() != null
				&& txtNombre.getText().length() > LARGO_CAMPO_TEXT) {
			txtNombre.getStyleClass().add("bad");
			valida = false;
		}
		return valida;
	}

	@Override
	public void onDataArrived(List<Object> list) {
		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof Colegio) {
				ObservableList<Colegio> oList = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					oList.add((Colegio) iEntity);
				}
				tblColegio.setItems(oList);
			}
		}
	}

	private void removeAllStyles() {
		removeAllStyle(lblError);
		removeAllStyle(txtNombre);
		removeAllStyle(txtDireccion);
	}

	@Override
	public void handle(ActionEvent event) {
		Object source = event.getSource();
		if (source == mnuAgregar) {
			limpiarControles();
		} else if (source == mnuModificar || source == mnItemModificar) {
			accionModificar();
		} else if (source == mnuGrabar) {
			accionGrabar();
		} else if (source == mnuEliminar || source == mnItemEliminar) {
			accionEliminar();
			// } else if (source == btnImagen) {
			// accionButtonImagen();
		} else if (source == mnuExportar || source == menuExportar) {
			ExcelSheetWriter.convertirDatosALibroDeExcel(tblColegio);
		}
	}

}
