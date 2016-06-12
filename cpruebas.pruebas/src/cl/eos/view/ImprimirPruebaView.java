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
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.PrinterResolution;

import org.apache.pdfbox.pdmodel.PDDocument;

import cl.eos.detection.ImpresionPrueba;
import cl.eos.detection.ImpresionPruebaVacia;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.Profesor;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
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

	private Prueba prueba;
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
	private ComboBox<Colegio> cmbColegios;
	@FXML
	private ComboBox<Curso> cmbCursos;
	@FXML
	private ComboBox<Profesor> cmbProfesor;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtAsignatura;
	@FXML
	private DatePicker dtpFecha;
	@FXML
	private ListView<EjeTematico> lstEjes;
	@FXML
	private ListView<Habilidad> lstHabilidad;
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
		cmbColegios.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Colegio colegio = cmbColegios.getSelectionModel().getSelectedItem();

				cmbCursos.getItems().clear();
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("tcursoId", prueba.getCurso().getId());
				parameters.put("colegioId", colegio.getId());
				controller.find("Curso.findByTipoColegio", parameters);
				boolean disable = cmbColegios.getSelectionModel().getSelectedItem() == null
						|| cmbCursos.getSelectionModel().getSelectedItem() == null
						|| cmbProfesor.getSelectionModel().getSelectedItem() == null;
				mnuImprimir.setDisable(disable);
				mnuImprimeVacia.setDisable(disable);
			}
		});
		cmbCursos.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				boolean disable = cmbColegios.getSelectionModel().getSelectedItem() == null
						|| cmbCursos.getSelectionModel().getSelectedItem() == null
						|| cmbProfesor.getSelectionModel().getSelectedItem() == null;
				mnuImprimir.setDisable(disable);
				mnuImprimeVacia.setDisable(disable);
			}
		});

		cmbProfesor.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				boolean disable = cmbColegios.getSelectionModel().getSelectedItem() == null
						|| cmbCursos.getSelectionModel().getSelectedItem() == null
						|| cmbProfesor.getSelectionModel().getSelectedItem() == null;
				mnuImprimir.setDisable(disable);
				mnuImprimeVacia.setDisable(disable);

			}
		});

		mnuImprimir.setDisable(true);
		mnuImprimeVacia.setDisable(true);

		RespondeEventos eventHandler = new RespondeEventos();
		mnuAgregar.setOnAction(eventHandler);
		mnuEliminar.setOnAction(eventHandler);
		mnuImprimir.setOnAction(eventHandler);
		mnuImprimeVacia.setOnAction(eventHandler);
		mnuQuitar.setOnAction(eventHandler);

		mnuEliminarPopup.setOnAction(eventHandler);
		mnuQuitarPopup.setOnAction(eventHandler);

		colegioCol.setCellValueFactory(new PropertyValueFactory<OTImprimirPrueba, String>("colegio"));
		cursoCol.setCellValueFactory(new PropertyValueFactory<OTImprimirPrueba, String>("curso"));
		profesorCol.setCellValueFactory(new PropertyValueFactory<OTImprimirPrueba, String>("profesor"));
		fechaCol.setCellValueFactory(new PropertyValueFactory<OTImprimirPrueba, String>("fechaLocal"));
		nroAlumnosCol.setCellValueFactory(new PropertyValueFactory<OTImprimirPrueba, Integer>("nroAlumnos"));
	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof Prueba) {
			prueba = (Prueba) entity;
			txtName.setText(prueba.getName());
			txtAsignatura.setText(prueba.getAsignatura().getName());
			List<RespuestasEsperadasPrueba> respuestas = prueba.getRespuestas();
			ObservableList<EjeTematico> lEjes = FXCollections.observableArrayList();
			ObservableList<Habilidad> lHabilidad = FXCollections.observableArrayList();

			for (RespuestasEsperadasPrueba respuesta : respuestas) {
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

	@Override
	public void onDataArrived(List<Object> list) {
		if (list != null && !list.isEmpty()) {
			Object entity = list.get(0);
			if (entity instanceof Colegio) {
				ObservableList<Colegio> oList = FXCollections.observableArrayList();
				for (Object iEntity : list) {
					oList.add((Colegio) iEntity);
				}
				cmbColegios.setItems(oList);
			} else if (entity instanceof Curso) {
				ObservableList<Curso> oList = FXCollections.observableArrayList();
				for (Object iEntity : list) {
					oList.add((Curso) iEntity);
				}
				cmbCursos.setItems(oList);
				cmbCursos.setDisable(false);
			} else if (entity instanceof Profesor) {
				ObservableList<Profesor> oList = FXCollections.observableArrayList();
				for (Object iEntity : list) {
					oList.add((Profesor) iEntity);
				}
				cmbProfesor.setItems(oList);
				cmbCursos.setDisable(false);
			}

		}
	}

	/**
	 * Clase dedicada a responder los eventos de menu.
	 */
	private class RespondeEventos implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			if (event.getSource() == mnuAgregar) {
				if (validate()) {
					OTImprimirPrueba ot = new OTImprimirPrueba(prueba);
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
					ObservableList<OTImprimirPrueba> selecteds = tblListadoPruebas.getSelectionModel()
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

				TextInputDialog response = new TextInputDialog("10");
				response.setTitle("Cuanats hojas quiere imprimir?");
				response.setContentText("Ingrese cantidad de hojas a imprimir");
				Optional<String> result = response.showAndWait();
				int n = 10;
				try {
					if (result.isPresent()) {
						String value = result.get();
						n = Integer.parseInt(value);
						if (Utils.isInteger(value)) {
							ImpresionPruebaVacia impresion = new ImpresionPruebaVacia();
							Curso curso = cmbCursos.getValue();
							Profesor profesor = cmbProfesor.getValue();
							Colegio colegio = cmbColegios.getValue();

							PDDocument doc = impresion.imprimir(prueba, curso, profesor, colegio, dtpFecha.getValue(),
									n);
							List<PDDocument> documentos = new ArrayList<PDDocument>();
							documentos.add(doc);
							printPDF(documentos);
						}
					}
				} catch (NoSuchElementException exep) {

				}

			}

			if (event.getSource() == mnuImprimir) {
				procesarImprimir();
			}
		}

		private void procesarImprimir() {
			ImpresionPrueba impresion = new ImpresionPrueba();
			ObservableList<OTImprimirPrueba> selecteds = tblListadoPruebas.getItems();

			List<PDDocument> documentos = new ArrayList<PDDocument>();
			for (OTImprimirPrueba ot : selecteds) {
				PDDocument doc = impresion.imprimir(prueba, ot.getCurso(), ot.getProfesor(), ot.getColegio(),
						dtpFecha.getValue());
				documentos.add(doc);
			}
			printPDF(documentos);
			tblListadoPruebas.getItems().clear();
		}

		public PrintService choosePrinter() {
			PrintRequestAttributeSet printAttributes = new HashPrintRequestAttributeSet();
			printAttributes.add(PrintQuality.HIGH);
			printAttributes.add(new PrinterResolution(600, 600, PrinterResolution.DPI));
			PrinterJob printJob = PrinterJob.getPrinterJob();

			if (printJob.printDialog(printAttributes)) {
				return printJob.getPrintService();
			} else {
				return null;
			}
		}

		public void printPDF(PDDocument document, PrintService printer) throws IOException, PrinterException {
			PrinterJob job = PrinterJob.getPrinterJob();
			job.setPrintService(printer);
			document.silentPrint(job);
			document.close();
		}

		public void printPDF(List<PDDocument> documentos) {

			Alert response = new Alert(AlertType.CONFIRMATION);
			response.setTitle("Pruebas para impresión generadas");
			response.setHeaderText(String.format("Se ha(n) generado %d prueba(s).", documentos.size()));
			response.setContentText("Confirma la impresión?");

			ButtonType buttonAceptar = new ButtonType("Aceptar");
			ButtonType buttonCacelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
			response.getButtonTypes().setAll(buttonAceptar, buttonCacelar);
			Optional<ButtonType> result = response.showAndWait();

			if (result.get() == buttonAceptar) {
				PrintService pService = choosePrinter();
				if (pService == null) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("No se imrpimirán los documentos.");
					alert.setTitle("No se ha seleccionado impresora.");
					alert.setContentText("Debe seleccionar impresora antes de imprimir.");
					alert.show();
				} else {
					for (PDDocument doc : documentos) {
						try {
							printPDF(doc, pService);
						} catch (IOException e) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setHeaderText("Se omitirá la impresión del documento");
							alert.setTitle("Problemas al leer documento.");
							alert.setContentText(doc.getDocumentInformation().getTitle());
							alert.show();

						} catch (PrinterException e) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setHeaderText("Se omitirá la impresión del documento");
							alert.setTitle("Problemas con la impresora.");
							alert.setContentText(doc.getDocumentInformation().getTitle());
							alert.show();
						}
					}
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText(null);
					alert.setTitle("Finalizada la impresión de documentos.");
					alert.setContentText("Se han enviado todos los documentos a la impresora.");
					alert.show();
				}
			}
		}
	}
}
