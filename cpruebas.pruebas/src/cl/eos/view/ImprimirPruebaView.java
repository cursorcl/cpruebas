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
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_Formas;
import cl.eos.restful.tables.R_Profesor;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoColegio;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

public class ImprimirPruebaView extends AFormView {
    private R_Prueba prueba;
    @FXML private TableView<OTImprimirPrueba> tblListadoPruebas;
    @FXML private TableColumn<OTImprimirPrueba, String> colegioCol;
    @FXML private TableColumn<OTImprimirPrueba, String> cursoCol;
    @FXML private TableColumn<OTImprimirPrueba, String> profesorCol;
    @FXML private TableColumn<OTImprimirPrueba, String> fechaCol;
    @FXML private TableColumn<OTImprimirPrueba, Integer> nroAlumnosCol;
    @FXML private ComboBox<R_Colegio> cmbColegios;
    @FXML private ComboBox<R_Curso> cmbCursos;
    @FXML private ComboBox<R_Profesor> cmbProfesor;
    @FXML private TextField txtName;
    @FXML private TextField txtAsignatura;
    @FXML private DatePicker dtpFecha;
    @FXML private MenuItem mnuAgregar;
    @FXML private MenuItem mnuQuitar;
    @FXML private MenuItem mnuEliminar;
    @FXML private MenuItem mnuQuitarPopup;
    @FXML private MenuItem mnuEliminarPopup;
    @FXML private MenuItem mnuImprimir;
    @FXML private MenuItem mnuImprimeVacia;
    private R_Asignatura asignatura;
    public ImprimirPruebaView() {
        setTitle("Imprimir");
    }
    @FXML
    public void initialize() {
        cmbCursos.setDisable(true);
        dtpFecha.setValue(LocalDate.now());
        cmbColegios.setOnAction(event -> {
            final R_Colegio colegio = cmbColegios.getSelectionModel().getSelectedItem();
            cmbCursos.getItems().clear();
            final Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("colegio_id", colegio.getId());
            controller.findByParam(R_Curso.class, parameters, this);
            final boolean disable = cmbColegios.getSelectionModel().getSelectedItem() == null
                    || cmbCursos.getSelectionModel().getSelectedItem() == null || cmbProfesor.getSelectionModel().getSelectedItem() == null;
            mnuImprimir.setDisable(disable);
            mnuImprimeVacia.setDisable(disable);
        });
        cmbCursos.setOnAction(event -> {
            final boolean disable = cmbColegios.getSelectionModel().getSelectedItem() == null
                    || cmbCursos.getSelectionModel().getSelectedItem() == null || cmbProfesor.getSelectionModel().getSelectedItem() == null;
            mnuImprimir.setDisable(disable);
            mnuImprimeVacia.setDisable(disable);
        });
        cmbProfesor.setOnAction(event -> {
            final boolean disable = cmbColegios.getSelectionModel().getSelectedItem() == null
                    || cmbCursos.getSelectionModel().getSelectedItem() == null || cmbProfesor.getSelectionModel().getSelectedItem() == null;
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
            if (entity instanceof R_Colegio) {
                final ObservableList<R_Colegio> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add((R_Colegio) iEntity);
                }
                cmbColegios.setItems(oList);
            } else if (entity instanceof R_Curso) {
                final ObservableList<R_Curso> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add((R_Curso) iEntity);
                }
                cmbCursos.setItems(oList);
                cmbCursos.setDisable(false);
            } else if (entity instanceof R_Profesor) {
                final ObservableList<R_Profesor> oList = FXCollections.observableArrayList();
                for (final Object iEntity : list) {
                    oList.add((R_Profesor) iEntity);
                    
                }
                cmbProfesor.setItems(oList);
                if(oList != null && oList.size() > 0)
                  cmbProfesor.getSelectionModel().select(0);
                cmbCursos.setDisable(false);
            }
        }
    }
    @Override
    public void onFound(IEntity entity) {
        if (entity instanceof R_Prueba) {
            prueba = (R_Prueba) entity;
            txtName.setText(prueba.getName());
            controller.findById(R_Asignatura.class, prueba.getAsignatura_id(), this);
        } else if (entity instanceof R_Asignatura) {
            asignatura = (R_Asignatura) entity;
            txtAsignatura.setText(((R_Asignatura) entity).getName());
        }
    }
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
                if (tblListadoPruebas.getSelectionModel().getSelectedItems() != null && !tblListadoPruebas.getSelectionModel().isEmpty()) {
                    final ObservableList<OTImprimirPrueba> selecteds = tblListadoPruebas.getSelectionModel().getSelectedItems();
                    while (!selecteds.isEmpty()) {
                        tblListadoPruebas.getItems().remove(selecteds.get(0));
                    }
                }
            }
            if (event.getSource() == mnuEliminar || event.getSource() == mnuEliminarPopup) {
                if (tblListadoPruebas.getSelectionModel().getSelectedItems() != null && !tblListadoPruebas.getSelectionModel().isEmpty()) {
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
                            Map<String, Object> params = new HashMap<>();
                            params.put("prueba_id", prueba.getId());
                            List<R_Formas> formas = controller.findByParamsSynchro(R_Formas.class, params);
                            List<R_RespuestasEsperadasPrueba> respEsperadas = controller.findByParamsSynchro(R_RespuestasEsperadasPrueba.class, params);
                            
                            final R_Curso curso = cmbCursos.getValue();
                            final R_Colegio colegio = cmbColegios.getValue();
                            final LocalDate fecha = dtpFecha.getValue();
                            final ImpresionPruebaVacia impresion = new ImpresionPruebaVacia.Builder().prueba(prueba).curso(curso).colegio(colegio).fecha(fecha).forma(formas.get(0)).nPruebas(n).respEsperadas(respEsperadas).build();
                            // Debo obteber las alternativas, la forma.
                            final PDDocument doc = impresion.imprimir();
                            final List<PDDocument> documentos = new ArrayList<PDDocument>();
                            documentos.add(doc);
                            printPDF(documentos);
                        }
                    }
                } catch (final NoSuchElementException exep) {}
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
            Map<String, Object> params = new HashMap<>();
            params.put("prueba_id", prueba.getId());
             
            List<R_Formas> formas = controller.findByParamsSynchro(R_Formas.class, params);
            List<R_RespuestasEsperadasPrueba> respEsperadas = controller.findByParamsSynchro(R_RespuestasEsperadasPrueba.class, params);
            
            final ImpresionPrueba impresion = new ImpresionPrueba.Builder().fecha(dtpFecha.getValue())
                    .formas(formas).prueba(prueba).respEsperadas(respEsperadas).asignatura(asignatura.getName()).build();
            
            
            final ObservableList<OTImprimirPrueba> selecteds = tblListadoPruebas.getItems();
            final List<PDDocument> documentos = new ArrayList<PDDocument>();
            for (final OTImprimirPrueba ot : selecteds) {
                params.clear();
                params.put("curso_id", ot.getCurso().getId());
                List<R_Alumno> alumnos = controller.findByParamsSynchro(R_Alumno.class, params);
                impresion.setAlumnos(alumnos);
                impresion.setColegio(ot.getColegio().getName());
                impresion.setCurso(ot.getCurso().getName());
                impresion.setProfesor(ot.getProfesor().toString());
                
                final PDDocument doc = impresion.imprimir();
                documentos.add(doc);
            }
            printPDF(documentos);
            tblListadoPruebas.getItems().clear();
        }
    }
}
