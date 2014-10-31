package cl.eos.view;


import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import org.controlsfx.dialog.Dialogs;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTAlumno;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.util.Utils;

public class AlumnosTreeView extends AFormView implements EventHandler<ActionEvent> {

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

	@FXML
	private MenuItem mnuImportar;

	@FXML
	private TextField txtRut;

	@FXML
	private TextField txtNombres;

	@FXML
	private TextField txtAPaterno;

	@FXML
	private TextField txtAMaterno;

	@FXML
	private TextField txtDireccion;

	@FXML
	private ComboBox<Colegio> cmbColegio;

	@FXML
	private ComboBox<Curso> cmbCurso;

	@FXML
	private Label lblError;

	@FXML
	private TreeView<OTAlumno> treeAlumnos;

	@FXML
	private TreeTableColumn<OTAlumno, Long> colId;

	@FXML
	private TreeTableColumn<OTAlumno, String> colRut;

	@FXML
	private TreeTableColumn<OTAlumno, String> colName;

	@FXML
	private TreeTableColumn<OTAlumno, String> colPaterno;

	@FXML
	private TreeTableColumn<OTAlumno, String> colMaterno;
	@FXML
	private TreeTableColumn<OTAlumno, String> colColegio;

	@FXML
	private TreeTableColumn<OTAlumno, String> colCurso;

	private ObservableList<Curso> oListCursos;

	private final Node rootIcon = new ImageView(new Image(getClass().getResourceAsStream("school-icon_32.png.png")));
	
	public AlumnosTreeView() {

	}

	@FXML
	public void initialize() {
		setTitle("Alumnos");
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
		mnuImportar.setOnAction(this);

		mnItemModificar.setDisable(true);
		mnuModificar.setDisable(true);
		mnuEliminar.setDisable(true);
		mnItemEliminar.setDisable(true);

		cmbColegio.setOnAction(this);
	}

	private void accionClicTabla() {
		treeAlumnos.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ObservableList<TreeItem<OTAlumno>> selected = treeAlumnos
						.getSelectionModel().getSelectedItems();

				if (selected.size() > 1) {
					mnItemModificar.setDisable(true);
					mnItemEliminar.setDisable(false);
					mnuModificar.setDisable(true);
					mnuEliminar.setDisable(false);
				} else if (selected.size() == 1) {

					mnItemModificar.setDisable(false);
					mnItemEliminar.setDisable(false);
					mnuModificar.setDisable(false);
					mnuEliminar.setDisable(false);
				}
			}
		});
	}

	private void accionModificar() {
		OTAlumno alumno = treeAlumnos.getSelectionModel().getSelectedItem().getValue();
		if (alumno != null) {
			txtRut.setText(alumno.getRut());
			txtNombres.setText(alumno.getName());
			txtAPaterno.setText(alumno.getPaterno());
			txtAMaterno.setText(alumno.getMaterno());
			txtDireccion.setText(alumno.getDireccion());
			cmbColegio.setValue(alumno.getColegio());
			cmbCurso.setValue(alumno.getCurso());
			select((IEntity) alumno.getAlumno());
		}
	}

	private void accionEliminar() {
		 ObservableList<TreeItem<OTAlumno>> otSeleccionados = treeAlumnos
				.getSelectionModel().getSelectedItems();

		if (otSeleccionados.size() == 0) {
			Dialogs.create().owner(null).title("Selección registro")
					.masthead(this.getName())
					.message("Debe seleccionar registro a procesar")
					.showInformation();
		} else {

			if (otSeleccionados != null && !otSeleccionados.isEmpty()) {
				List<Alumno> alumno = new ArrayList<Alumno>(
						otSeleccionados.size());
				for ( TreeItem<OTAlumno> ot : otSeleccionados) {
					alumno.add(ot.getValue().getAlumno());
				}
				delete(alumno);
				treeAlumnos.getSelectionModel().clearSelection();
				limpiarControles();
			}
		}
	}

	private void accionGrabar() {
		IEntity entitySelected = getSelectedEntity();
		removeAllStyles();
		if (validate()) {
			if (lblError != null) {
				lblError.setText(" ");
			}
			Alumno alumno = null;
			if (entitySelected != null && entitySelected instanceof Alumno) {
				alumno = (Alumno) entitySelected;
			} else {
				alumno = new Alumno();
			}
			alumno.setRut(txtRut.getText());
			alumno.setName(txtNombres.getText());
			alumno.setPaterno(txtAPaterno.getText());
			alumno.setMaterno(txtAMaterno.getText());
			alumno.setDireccion(txtDireccion.getText());
			alumno.setColegio(cmbColegio.getValue());
			alumno.setCurso(cmbCurso.getValue());
			save(alumno);
		} else {
			lblError.getStyleClass().add("bad");
			lblError.setText("Corregir campos destacados en color rojo");
		}
		limpiarControles();
	}

	private void limpiarControles() {
		txtRut.clear();
		txtNombres.clear();
		txtAPaterno.clear();
		txtAMaterno.clear();
		txtDireccion.clear();
		cmbColegio.getSelectionModel().clearSelection();
		cmbCurso.getSelectionModel().clearSelection();
		treeAlumnos.getSelectionModel().clearSelection();
		select(null);
	}

	private void inicializaTabla() {
		treeAlumnos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		colId.setCellValueFactory(new TreeItemPropertyValueFactory<OTAlumno, Long>("id"));
		colRut.setCellValueFactory(new TreeItemPropertyValueFactory<OTAlumno, String>(
				"rut"));
		colName.setCellValueFactory(new TreeItemPropertyValueFactory<OTAlumno, String>(
				"name"));
		colPaterno
				.setCellValueFactory(new TreeItemPropertyValueFactory<OTAlumno, String>(
						"paterno"));
		colMaterno
				.setCellValueFactory(new TreeItemPropertyValueFactory<OTAlumno, String>(
						"materno"));
		colColegio
				.setCellValueFactory(new TreeItemPropertyValueFactory<OTAlumno, String>(
						"colegio"));
		colCurso.setCellValueFactory(new TreeItemPropertyValueFactory<OTAlumno, String>(
				"curso"));
	}

	private void removeAllStyles() {
		removeAllStyle(lblError);
		removeAllStyle(txtRut);
		removeAllStyle(txtNombres);
		removeAllStyle(txtAPaterno);
		removeAllStyle(txtAMaterno);
		removeAllStyle(cmbColegio);
		removeAllStyle(cmbCurso);
	}

	@Override
	public void onSaved(IEntity otObject) {
		OTAlumno otAlumno = new OTAlumno((Alumno) otObject);
		//TODO EOS
//		int indice = tblAlumnos.getItems().lastIndexOf(otAlumno);
//		if (indice != -1) {
//			tblAlumnos.getItems().set(indice, otAlumno);
//		} else {
//			tblAlumnos.getItems().add(otAlumno);
//		}
	}

	@Override
	public void onDeleted(IEntity entity) {
//		tblAlumnos.getItems().remove(new OTAlumno((Alumno) entity));
		//TODO EOS
	}

	@Override
	public boolean validate() {
		boolean valida = true;
		if (cmbColegio.getValue() == null) {
			cmbColegio.getStyleClass().add("bad");
			valida = false;
		}
		if (cmbCurso.getValue() == null) {
			cmbCurso.getStyleClass().add("bad");
			valida = false;
		}
		valida = validaRut();
		if (txtNombres.getText() == null || txtNombres.getText().equals("")) {
			txtNombres.getStyleClass().add("bad");
			valida = false;
		}
		if (txtNombres.getText() != null
				&& txtNombres.getText().length() > LARGO_CAMPO_TEXT) {
			txtNombres.getStyleClass().add("bad");
			valida = false;
		}

		if (txtAPaterno.getText() == null || txtAPaterno.getText().equals("")) {
			txtAPaterno.getStyleClass().add("bad");
			valida = false;
		}
		if (txtAPaterno.getText() != null
				&& txtAPaterno.getText().length() > LARGO_CAMPO_TEXT) {
			txtAPaterno.getStyleClass().add("bad");
			valida = false;
		}
		if (txtAMaterno.getText() == null || txtAMaterno.getText().equals("")) {
			txtAMaterno.getStyleClass().add("bad");
			valida = false;
		}
		if (txtAMaterno.getText() != null
				&& txtAMaterno.getText().length() > LARGO_CAMPO_TEXT) {
			txtAMaterno.getStyleClass().add("bad");
			valida = false;
		}
		return valida;
	}

	private boolean validaRut() {
		boolean valida;
		String strRut = txtRut.getText();
		if (strRut.length() > 0) {
			if (Utils.validarRut(strRut)) {
				valida = true;
			} else {
				txtRut.getStyleClass().add("bad");
				valida = false;
			}
		} else {
			txtRut.getStyleClass().add("bad");
			valida = false;
		}
		return valida;
	}

	@Override
	public void onDataArrived(List<Object> list) {

		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof Alumno) {
				
				final TreeItem<OTAlumno> root = 
				        new TreeItem<>(new OTAlumno(), rootIcon);
				root.setExpanded(true);
				
				TreeItem<OTAlumno> itemColegio = null;
				TreeItem<OTAlumno> itemCurso = null;
				
				for (Object iEntity : list) {
					OTAlumno ot = new OTAlumno((Alumno) iEntity);
					if(itemColegio == null ||  !ot.getColegio().equals(itemColegio.getValue().getColegio()))
					{
						OTAlumno otColegio = new OTAlumno();
						otColegio.setColegio(ot.getColegio());
						itemColegio = new TreeItem<OTAlumno>(otColegio, new ImageView(new Image(getClass().getResourceAsStream("school-icon_16.png.png"))));
						root.getChildren().add(itemColegio);
					}
					if(itemCurso == null || !ot.getCurso().equals(itemCurso.getValue().getCurso()))
					{
						OTAlumno otCurso = new OTAlumno();
						otCurso.setCurso(ot.getCurso());
						otCurso.setColegio(ot.getColegio());
						itemCurso = new TreeItem<OTAlumno>(otCurso, new ImageView(new Image(getClass().getResourceAsStream("curso_16.png.png"))));
						itemColegio.getChildren().add(itemCurso);
					}
					itemCurso.getChildren().add(new TreeItem<OTAlumno>(ot));
					
				}
				treeAlumnos.setRoot(root);
			} else if (entity instanceof Curso) {
				oListCursos = FXCollections.observableArrayList();
				for (Object iEntity : list) {
					oListCursos.add((Curso) iEntity);
				}
				asignaCursos();
			} else if (entity instanceof Colegio) {
				ObservableList<Colegio> oList = FXCollections
						.observableArrayList();
				for (Object iEntity : list) {
					oList.add((Colegio) iEntity);
				}
				cmbColegio.setItems(oList);
			}
		}
	}

	@Override
	public void handle(ActionEvent event) {
		Object source = event.getSource();
		if (source == mnuModificar || source == mnItemModificar) {
			accionModificar();
		} else if (source == mnuGrabar) {
			accionGrabar();
		} else if (source == mnuEliminar || source == mnItemEliminar) {
			accionEliminar();
		} else if (source == mnuAgregar) {
			limpiarControles();
		} else if (source == mnuExportar || source == menuExportar) {
			treeAlumnos.setId("Alumnos");
			//ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblAlumnos); //TODO EOS
		} else if (source == cmbColegio) {
			asignaCursos();
		}

	}

	private void asignaCursos() {
		Colegio colegioSeleccionado = cmbColegio.getValue();
		if (colegioSeleccionado != null) {
			ObservableList<Curso> oList = FXCollections.observableArrayList();
			for (Curso object : oListCursos) {
				if (object.getColegio().equals(colegioSeleccionado)) {
					oList.add(object);
				}
			}
			cmbCurso.setItems(oList);
		}

	}
}
