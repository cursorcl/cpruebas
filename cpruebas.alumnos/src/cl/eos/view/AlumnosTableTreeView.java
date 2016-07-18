package cl.eos.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.ot.OTAlumno;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.TipoAlumno;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class AlumnosTableTreeView extends AFormView implements EventHandler<ActionEvent> {

    private static final int LARGO_CAMPO_TEXT = 100;
    static final Logger LOG = Logger.getLogger(AlumnosTableTreeView.class);

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
    private TreeTableView<OTAlumno> tblAlumnos;

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

    @FXML
    private TreeTableColumn<OTAlumno, String> colTipoAlumno;

    @FXML
    private ComboBox<TipoAlumno> cmbTipoAlumno;

    private ObservableList<Curso> oListCursos;

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
        tblAlumnos.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ObservableList<TreeItem<OTAlumno>> selected = tblAlumnos.getSelectionModel().getSelectedItems();

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
        OTAlumno alumno = tblAlumnos.getSelectionModel().getSelectedItem().getValue();
        if (alumno != null) {
            txtRut.setText(alumno.getRut());
            txtNombres.setText(alumno.getName());
            txtAPaterno.setText(alumno.getPaterno());
            txtAMaterno.setText(alumno.getMaterno());
            txtDireccion.setText(alumno.getDireccion());
            cmbColegio.setValue(alumno.getColegio());
            cmbCurso.setValue(alumno.getCurso());
            cmbTipoAlumno.setValue(alumno.getTipoAlumno());
            select((IEntity) alumno.getAlumno());
        }
    }

    private void accionEliminar() {
        ObservableList<TreeItem<OTAlumno>> otSeleccionados = tblAlumnos.getSelectionModel().getSelectedItems();

        if (otSeleccionados == null || otSeleccionados.isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Selección registro");
            alert.setHeaderText(this.getName());
            alert.setContentText("Debe seleccionar registro a procesar");
            alert.showAndWait();
            return;
        }

        List<Alumno> alumno = new ArrayList<>(otSeleccionados.size());
        for (TreeItem<OTAlumno> ot : otSeleccionados) {
            if (ot.getValue().getAlumno() != null) {
                Alumno item = ot.getValue().getAlumno();
                alumno.add(item);
            }
        }
        delete(alumno);
        tblAlumnos.getSelectionModel().clearSelection();
        limpiarControles();
    }

    private void accionGrabar() {
        IEntity entitySelected = getSelectedEntity();
        removeAllStyles();
        if (validate()) {
            if (lblError != null) {
                lblError.setText(" ");
            }
            Alumno alumno;
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
            alumno.setTipoAlumno(cmbTipoAlumno.getValue());
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
        tblAlumnos.getSelectionModel().clearSelection();
        cmbTipoAlumno.getSelectionModel().clearSelection();
        select(null);
    }

    private void inicializaTabla() {
        tblAlumnos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        colId.setCellValueFactory(new TreeItemPropertyValueFactory<OTAlumno, Long>("id"));
        colRut.setCellValueFactory(new TreeItemPropertyValueFactory<OTAlumno, String>("rut"));
        colName.setCellValueFactory(new TreeItemPropertyValueFactory<OTAlumno, String>("name"));
        colPaterno.setCellValueFactory(new TreeItemPropertyValueFactory<OTAlumno, String>("paterno"));
        colMaterno.setCellValueFactory(new TreeItemPropertyValueFactory<OTAlumno, String>("materno"));
        colColegio.setCellValueFactory(new TreeItemPropertyValueFactory<OTAlumno, String>("colegio"));
        colCurso.setCellValueFactory(new TreeItemPropertyValueFactory<OTAlumno, String>("curso"));
        colTipoAlumno.setCellValueFactory(new TreeItemPropertyValueFactory<OTAlumno, String>("tipoAlumno"));
        tblAlumnos.setShowRoot(false);
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

        TreeItem<OTAlumno> root = tblAlumnos.getRoot();
        addItem(root, otAlumno);
    }

    private void addItem(TreeItem<OTAlumno> root, OTAlumno otAlumno) {
        boolean founded = false;

        for (TreeItem<OTAlumno> item : root.getChildren()) {
            OTAlumno ot = item.getValue();

            if (!ot.getColegio().equals(otAlumno.getColegio()))
                continue;

            for (TreeItem<OTAlumno> itemCurso : item.getChildren()) {
                if (!itemCurso.getValue().getCurso().equals(otAlumno.getCurso()))
                    continue;
                for (TreeItem<OTAlumno> itemAlumno : itemCurso.getChildren()) {
                    if (itemAlumno.getValue().getAlumno().equals(otAlumno.getAlumno())) {
                        itemAlumno.setValue(otAlumno);
                        founded = true;
                        break;
                    }
                }
                if (!founded) {
                    TreeItem<OTAlumno> nuevo = new TreeItem<>(otAlumno);
                    itemCurso.getChildren().add(nuevo);
                    founded = true;
                }
            }
            if (!founded) {
                OTAlumno otCNuevo = new OTAlumno();
                otCNuevo.setColegio(otAlumno.getColegio());
                otCNuevo.setCurso(otAlumno.getCurso());
                TreeItem<OTAlumno> cursoNuevo = new TreeItem<>(otCNuevo, getImagenCurso());
                item.getChildren().add(cursoNuevo);
                TreeItem<OTAlumno> nuevo = new TreeItem<>(otAlumno);
                cursoNuevo.getChildren().add(nuevo);
                founded = true;
            }
        }
        if (!founded) {
            OTAlumno otLNuevo = new OTAlumno();
            otLNuevo.setColegio(otAlumno.getColegio());

            OTAlumno otCNuevo = new OTAlumno();
            otCNuevo.setColegio(otAlumno.getColegio());
            otCNuevo.setCurso(otAlumno.getCurso());

            TreeItem<OTAlumno> colegioNuevo = new TreeItem<OTAlumno>(otLNuevo, getImagenColegio());
            root.getChildren().add(colegioNuevo);

            TreeItem<OTAlumno> cursoNuevo = new TreeItem<OTAlumno>(otCNuevo, getImagenCurso());
            colegioNuevo.getChildren().add(cursoNuevo);

            TreeItem<OTAlumno> nuevo = new TreeItem<OTAlumno>(otAlumno);
            cursoNuevo.getChildren().add(nuevo);
        }
    }

    @Override
    public void onDeleted(IEntity entity) {
        OTAlumno otAlumno = new OTAlumno((Alumno) entity);
        TreeItem<OTAlumno> root = tblAlumnos.getRoot();
        for (TreeItem<OTAlumno> item : root.getChildren()) {
            OTAlumno ot = item.getValue();
            if (ot.getColegio().equals(otAlumno.getColegio())) {
                for (TreeItem<OTAlumno> itemCurso : item.getChildren()) {
                    if (itemCurso.getValue().getCurso().equals(otAlumno.getCurso())) {
                        int index = 0;
                        for (TreeItem<OTAlumno> itemAlumno : itemCurso.getChildren()) {
                            if (itemAlumno.getValue().getAlumno().equals(otAlumno.getAlumno())) {
                                itemCurso.getChildren().remove(index);
                                break;
                            }
                            index++;
                        }
                    }
                }
            }
        }
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
        valida = valida && validaRut();
        if (txtNombres.getText() == null || "".equals(txtNombres.getText())) {
            txtNombres.getStyleClass().add("bad");
            valida = false;
        }
        if (txtNombres.getText() != null && txtNombres.getText().length() > LARGO_CAMPO_TEXT) {
            txtNombres.getStyleClass().add("bad");
            valida = false;
        }

        if (txtAPaterno.getText() == null || "".equals(txtAPaterno.getText())) {
            txtAPaterno.getStyleClass().add("bad");
            valida = false;
        }
        if (txtAPaterno.getText() != null && txtAPaterno.getText().length() > LARGO_CAMPO_TEXT) {
            txtAPaterno.getStyleClass().add("bad");
            valida = false;
        }
        if (txtAMaterno.getText() == null || "".equals(txtAMaterno.getText())) {
            txtAMaterno.getStyleClass().add("bad");
            valida = false;
        }
        if (txtAMaterno.getText() != null && txtAMaterno.getText().length() > LARGO_CAMPO_TEXT) {
            txtAMaterno.getStyleClass().add("bad");
            valida = false;
        }
        return valida;
    }

    private boolean validaRut() {
        boolean valida = true;
        
        if (txtRut.getText() == null || txtRut.getText().trim().length()== 0) {
            txtRut.getStyleClass().add("bad");
            valida = false;
        }
        else if (!Utils.validarRut(txtRut.getText().trim()))
        {
            txtRut.getStyleClass().add("bad");
            valida = false;
        }
        if (cmbColegio.getSelectionModel().getSelectedItem() == null) {
            cmbColegio.getStyleClass().add("bad");
            valida = false;
        }
        if (cmbCurso.getSelectionModel().getSelectedItem() == null) {
            cmbColegio.getStyleClass().add("bad");
            valida = false;
        }
        if (txtAMaterno.getText() == null || txtAMaterno.getText().trim().length()== 0) {
            txtAMaterno.getStyleClass().add("bad");
            valida = false;
        }
        if (txtAPaterno.getText() == null || txtAPaterno.getText().trim().length()== 0) {
            txtAPaterno.getStyleClass().add("bad");
            valida = false;
        }     
        if (txtNombres.getText() == null || txtNombres.getText().trim().length()== 0) {
            txtNombres.getStyleClass().add("bad");
            valida = false;
        }     
        if (txtDireccion.getText() == null || txtDireccion.getText().trim().length()== 0) {
            txtDireccion.getStyleClass().add("bad");
            valida = false;
        }                    
        return valida;
    }

    @Override
    public void onDataArrived(List<Object> list) {

        if (list != null && !list.isEmpty()) {
            Object entity = list.get(0);
            if (entity instanceof Alumno) {
                final TreeItem<OTAlumno> root = new TreeItem<>(new OTAlumno());
                root.setExpanded(true);

                TreeItem<OTAlumno> itemColegio = null;
                TreeItem<OTAlumno> itemCurso = null;

                for (Object iEntity : list) {
                    OTAlumno ot = new OTAlumno((Alumno) iEntity);
                    if (itemColegio == null || !ot.getColegio().equals(itemColegio.getValue().getColegio())) {
                        OTAlumno otColegio = new OTAlumno();
                        otColegio.setColegio(ot.getColegio());
                        itemColegio = new TreeItem<OTAlumno>(otColegio, getImagenColegio());
                        root.getChildren().add(itemColegio);
                    }
                    if (itemCurso == null || !ot.getCurso().equals(itemCurso.getValue().getCurso())) {
                        OTAlumno otCurso = new OTAlumno();
                        otCurso.setCurso(ot.getCurso());
                        otCurso.setColegio(ot.getColegio());
                        itemCurso = new TreeItem<OTAlumno>(otCurso, getImagenCurso());
                        itemColegio.getChildren().add(itemCurso);
                    }
                    itemCurso.getChildren().add(new TreeItem<OTAlumno>(ot));

                }
                tblAlumnos.setRoot(root);
            } else if (entity instanceof Curso) {
                oListCursos = FXCollections.observableArrayList();
                for (Object iEntity : list) {
                    oListCursos.add((Curso) iEntity);
                }
                asignaCursos();
            } else if (entity instanceof Colegio) {
                ObservableList<Colegio> oList = FXCollections.observableArrayList();
                for (Object iEntity : list) {
                    oList.add((Colegio) iEntity);
                }
                cmbColegio.setItems(oList);
            } else if (entity instanceof TipoAlumno) {
                ObservableList<TipoAlumno> oList = FXCollections.observableArrayList();
                for (Object iEntity : list) {
                    oList.add((TipoAlumno) iEntity);
                }
                cmbTipoAlumno.setItems(oList);
            }

        }
    }

    private Node getImagenCurso() {
        try {
            File img = new File("res/curso_16.png");
            return new ImageView(img.toURI().toURL().toString());
        } catch (IOException e) {
            return null;
        }
    }

    private Node getImagenColegio() {
        try {
            File img = new File("res/school-icon1_16.png");
            return new ImageView(img.toURI().toURL().toString());
        } catch (IOException e) {
            LOG.error(e);
            return null;
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
            tblAlumnos.setId("Alumnos");
            ExcelSheetWriterObj.convertirDatosALibroDeExcel(tblAlumnos);
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
