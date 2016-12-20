package cl.eos.view;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.ResolutionSyntax;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.PrinterResolution;

import org.apache.pdfbox.pdmodel.PDDocument;

import cl.eos.detection.ImpresionPrueba;
import cl.eos.detection.ImpresionPruebaVacia;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.SColegio;
import cl.eos.persistence.models.SCurso;
import cl.eos.persistence.models.SEjeTematico;
import cl.eos.persistence.models.SHabilidad;
import cl.eos.persistence.models.SProfesor;
import cl.eos.persistence.models.SPrueba;
import cl.eos.persistence.models.SRespuestasEsperadasPrueba;
import cl.eos.util.Utils;
import cl.eos.view.ots.OTImprimirPrueba;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

public class ImprimirPruebaView extends AFormView {

    /**
     * Clase dedicada a responder los eventos de menu.
     */
    private class RespondeEventos implements EventHandler<ActionEvent> {

        public PrintService choosePrinter() {
            final PrintRequestAttributeSet printAttributes = new HashPrintRequestAttributeSet();
            printAttributes.add(PrintQuality.HIGH);
            printAttributes.add(new PrinterResolution(600, 600, ResolutionSyntax.DPI));
            final PrinterJob printJob = PrinterJob.getPrinterJob();

            if (printJob.printDialog(printAttributes)) {
                return printJob.getPrintService();
            } else {
                return null;
            }
        }

        @Override
        public void handle(ActionEvent event) {
            if (event.getSource() == mnuAgregar) {
                if (validate()) {
                    final OTImprimirPrueba ot = new OTImprimirPrueba(prueba);
                    ot.setColegio(cmbColegios.getValue());
                    ot.setCurso(cmbCursos.getValue());
                    ot.setFecha(dtpFecha.getValue().toEpochDay());
                    ot.setProfesor(cmbProfesor.getValue());
                    tblListadoPruebas.getItems().add(ot);
                }
            }
            if (event.getSource() == mnuQuitarPopup || event.getSource() == mnuQuitar) {
                if (tblListadoPruebas.getSelectionModel().getSelectedItems() != null
                        && !tblListadoPruebas.getSelectionModel().isEmpty()) {
                    final ObservableList<OTImprimirPrueba> selecteds = tblListadoPruebas.getSelectionModel()
                            .getSelectedItems();
                    while (!selecteds.isEmpty()) {
                        tblListadoPruebas.getItems().remove(selecteds.get(0));
                    }
                }
            }

            if (event.getSource() == mnuEliminar || event.getSource() == mnuEliminarPopup) {
                if (tblListadoPruebas.getSelectionModel().getSelectedItems() != null
                        && !tblListadoPruebas.getSelectionModel().isEmpty()) {
                    tblListadoPruebas.getItems().clear();
                }
            }
            if (event.getSource() == mnuImprimeVacia) {

                final TextInputDialog response = new TextInputDialog("10");
                response.setTitle("Cuanats hojas quiere imprimir?");
                response.setContentText("Ingrese cantidad de hojas a imprimir");
                final Optional<String> result = response.showAndWait();
                int n = 10;
                try {
                    if (result.isPresent()) {
                        final String value = result.get();
                        n = Integer.parseInt(value);
                        if (Utils.isInteger(value)) {
                            final ImpresionPruebaVacia impresion = new ImpresionPruebaVacia();
                            final SCurso curso = cmbCursos.getValue();
                            final SProfesor profesor = cmbProfesor.getValue();
                            final SColegio colegio = cmbColegios.getValue();

                            final PDDocument doc = impresion.imprimir(prueba, curso, profesor, colegio,
                                    dtpFecha.getValue(), n);
                            final List<PDDocument> documentos = new ArrayList<PDDocument>();
                            documentos.add(doc);
                            printPDF(documentos);
                        }
                    }
                } catch (final NoSuchElementException exep) {

                }

            }

            if (event.getSource() == mnuImprimir) {
                procesarImprimir();
            }
        }

        public void printPDF(List<PDDocument> documentos) {

            final Alert response = new Alert(AlertType.CONFIRMATION);
            response.setTitle("Pruebas para impresión generadas");
            response.setHeaderText(String.format("Se ha(n) generado %d prueba(s).", documentos.size()));
            response.setContentText("Confirma la impresión?");

            final ButtonType buttonAceptar = new ButtonType("Aceptar");
            final ButtonType buttonCacelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
            response.getButtonTypes().setAll(buttonAceptar, buttonCacelar);
            final Optional<ButtonType> result = response.showAndWait();

            if (result.get() == buttonAceptar) {
                final PrintService pService = choosePrinter();
                if (pService == null) {
                    final Alert alert = new Alert(AlertType.ERROR);
                    alert.setHeaderText("No se imrpimirán los documentos.");
                    alert.setTitle("No se ha seleccionado impresora.");
                    alert.setContentText("Debe seleccionar impresora antes de imprimir.");
                    alert.show();
                } else {
                    for (final PDDocument doc : documentos) {
                        try {
                            printPDF(doc, pService);
                        } catch (final IOException e) {
                            final Alert alert = new Alert(AlertType.ERROR);
                            alert.setHeaderText("Se omitirá la impresión del documento");
                            alert.setTitle("Problemas al leer documento.");
                            alert.setContentText(doc.getDocumentInformation().getTitle());
                            alert.show();

                        } catch (final PrinterException e) {
                            final Alert alert = new Alert(AlertType.ERROR);
                            alert.setHeaderText("Se omitirá la impresión del documento");
                            alert.setTitle("Problemas con la impresora.");
                            alert.setContentText(doc.getDocumentInformation().getTitle());
                            alert.show();
                        }
                    }
                    final Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Finalizada la impresión de documentos.");
                    alert.setContentText("Se han enviado todos los documentos a la impresora.");
                    alert.show();
                }
            }
        }

        public void printPDF(PDDocument document, PrintService printer) throws IOException, PrinterException {
            final PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintService(printer);
            document.silentPrint(job);
            document.close();
        }

        private void procesarImprimir() {
            final ImpresionPrueba impresion = new ImpresionPrueba();
            final ObservableList<OTImprimirPrueba> selecteds = tblListadoPruebas.getItems();

            final List<PDDocument> documentos = new ArrayList<PDDocument>();
            for (final OTImprimirPrueba ot : selecteds) {
                final PDDocument doc = impresion.imprimir(prueba, ot.getCurso(), ot.getProfesor(), ot.getColegio(),
                        dtpFecha.getValue());
                documentos.add(doc);
            }
            printPDF(documentos);
            tblListadoPruebas.getItems().clear();
        }
    }

    private SPrueba prueba;
    @FXML
    private TableView<OTImprimirPrueba> tblListadoPruebas;
    @FXML
    private TableColumn<OTImprimirPrueba, String> colegioCol;
    @FXML
    private TableColumn<OTImprimirPrueba, String> cursoCol;
    @FXML
    private TableColumn<OTImprimirPrueba, String> profesorCol;
    @FXML
    private TableColumn<OTImprimirPrueba, String> fechaCol;
    @FXML
    private TableColumn<OTImprimirPrueba, Integer> nroAlumnosCol;
    @FXML
    private ComboBox<SColegio> cmbColegios;
    @FXML
    private ComboBox<SCurso> cmbCursos;
    @FXML
    private ComboBox<SProfesor> cmbProfesor;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtAsignatura;
    @FXML
    private DatePicker dtpFecha;
    @FXML
    private ListView<SEjeTematico> lstEjes;
    @FXML
    private ListView<SHabilidad> lstHabilidad;
    @FXML
    private MenuItem mnuAgregar;
    @FXML
    private MenuItem mnuQuitar;

    @FXML
    private MenuItem mnuEliminar;
    @FXML
    private MenuItem mnuQuitarPopup;

    @FXML
    private MenuItem mnuEliminarPopup;

    @FXML
    private MenuItem mnuImprimir;

    @FXML
    private MenuItem mnuImprimeVacia;

    public ImprimirPruebaView() {
        setTitle("Imprimir");
    }

    @FXML
    public void initialize() {
        cmbCursos.setDisable(true);
        dtpFecha.setValue(LocalDate.now());
        cmbColegios.setOnAction(event -> {
            final SColegio colegio = cmbColegios.getSelectionModel().getSelectedItem();

            cmbCursos.getItems().clear();
            final Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("tcursoId", prueba.getCurso().getId());
            parameters.put("colegioId", colegio.getId());
            controller.find("Curso.findByTipoColegio", parameters);
            final boolean disable = cmbColegios.getSelectionModel().getSelectedItem() == null
                    || cmbCursos.getSelectionModel().getSelectedItem() == null
                    || cmbProfesor.getSelectionModel().getSelectedItem() == null;
            mnuImprimir.setDisable(disable);
            mnuImprimeVacia.setDisable(disable);
        });
        cmbCursos.setOnAction(event -> {
            final boolean disable = cmbColegios.getSelectionModel().getSelectedItem() == null
                    || cmbCursos.getSelectionModel().getSelectedItem() == null
                    || cmbProfesor.getSelectionModel().getSelectedItem() == null;
            mnuImprimir.setDisable(disable);
            mnuImprimeVacia.setDisable(disable);
        });

        cmbProfesor.setOnAction(event -> {
            final boolean disable = cmbColegios.getSelectionModel().getSelectedItem() == null
                    || cmbCursos.getSelectionModel().getSelectedItem() == null
                    || cmbProfesor.getSelectionModel().getSelectedItem() == null;
            mnuImprimir.setDisable(disable);
            mnuImprimeVacia.setDisable(disable);

        });

        mnuImprimir.setDisable(true);
        mnuImprimeVacia.setDisable(true);

        final RespondeEventos eventHandler = new RespondeEventos();
        mnuAgregar.setOnAction(eventHandler);
        mnuEliminar.setOnAction(eventHandler);
        mnuImprimir.setOnAction(eventHandler);
        mnuImprimeVacia.setOnAction(eventHandler);
        mnuQuitar.setOnAction(eventHandler);

        mnuEliminarPopup.setOnAction(eventHandler);
        mnuQuitarPopup.setOnAction(eventHandler);

        colegioCol.setCellValueFactory(new PropertyValueFactory<OTImprimirPrueba, String>("colegio"));
        cursoCol.setCellValueFactory(new PropertyValueFactory<OTImprimirPrueba, String>("colegio"));
        profesorCol.setCellValueFactory(new PropertyValueFactory<OTImprimirPrueba, String>("profesor"));
        fechaCol.setCellValueFactory(new PropertyValueFactory<OTImprimirPrueba, String>("fechaLocal"));
        nroAlumnosCol.setCellValueFactory(new PropertyValueFactory<OTImprimirPrueba, Integer>("nroAlumnos"));
    }

    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof SColegio) {
                final ObservableList<SColegio> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add((SColegio) iEntity);
                }
                cmbColegios.setItems(oList);
            } else if (entity instanceof SCurso) {
                final ObservableList<SCurso> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add((SCurso) iEntity);
                }
                cmbCursos.setItems(oList);
                cmbCursos.setDisable(false);
            } else if (entity instanceof SProfesor) {
                final ObservableList<SProfesor> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add((SProfesor) iEntity);
                }
                cmbProfesor.setItems(oList);
                cmbCursos.setDisable(false);
            }

        }
    }

    @Override
    public void onFound(IEntity entity) {
        if (entity instanceof SPrueba) {
            prueba = (SPrueba) entity;
            txtName.setText(prueba.getName());
            txtAsignatura.setText(prueba.getAsignatura().getName());
            final List<SRespuestasEsperadasPrueba> respuestas = prueba.getRespuestas();
            final ObservableList<SEjeTematico> lEjes = FXCollections.observableArrayList();
            final ObservableList<SHabilidad> lHabilidad = FXCollections.observableArrayList();

            for (final SRespuestasEsperadasPrueba respuesta : respuestas) {
                if (!lEjes.contains(respuesta.getEjeTematico())) {
                    lEjes.add(respuesta.getEjeTematico());
                }
                if (!lHabilidad.contains(respuesta.getHabilidad())) {
                    lHabilidad.add(respuesta.getHabilidad());
                }
            }
            lstEjes.setItems(lEjes);
            lstHabilidad.setItems(lHabilidad);
        }

    }
}
