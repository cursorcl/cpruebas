package cl.eos.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cl.eos.PruebasActivator;
import cl.eos.imp.view.AFormView;
import cl.eos.imp.view.WindowManager;
import cl.eos.interfaces.IActivator;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.SEvaluacionPrueba;
import cl.eos.persistence.models.SPrueba;
import cl.eos.persistence.models.SPrueba.Estado;
import cl.eos.persistence.models.SPruebaRendida;
import cl.eos.persistence.models.SRespuestasEsperadasPrueba;
import cl.eos.persistence.util.Comparadores;
import cl.eos.util.ExcelSheetWriterObj;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

public class AnularPreguntasViewController extends AFormView {

    @FXML
    private TableView<RegistroAnularPreguntas> tblRegistroDefinePrueba;
    @FXML
    private TableColumn<RegistroAnularPreguntas, Integer> preguntaCol;
    @FXML
    private TableColumn<RegistroAnularPreguntas, String> respuestaCol;
    @FXML
    private TableColumn<RegistroAnularPreguntas, Boolean> vfCol;
    @FXML
    private TableColumn<RegistroAnularPreguntas, Boolean> mentalCol;
    @FXML
    private TableColumn<RegistroAnularPreguntas, Boolean> anularCol;

    @FXML
    private MenuItem mnuGrabar;
    @FXML
    private MenuItem mnuVolver;
    @FXML
    private MenuItem mnuExportar;

    private SPrueba prueba;

    private ObservableList<RegistroAnularPreguntas> registros;

    public AnularPreguntasViewController() {
        setTitle("Anular Pregunta");
    }

    /**
     * Aqui voy
     */
    protected void ejecutaGrabar() {
        if (validate()) {
            updateRespuestasEsperadas();
            /*
             * Se van a actualizar todas las evaluaciones, recalculando las
             * notas.
             */

            for (final SEvaluacionPrueba evaluacion : prueba.getEvaluaciones()) {
                for (final SPruebaRendida pruebaRendida : evaluacion.getPruebasRendidas()) {
                    pruebaRendida.reEvaluate();
                }
            }
            save(prueba);
        }
    }

    @FXML
    public void initialize() {

        tblRegistroDefinePrueba.setEditable(true);
        tblRegistroDefinePrueba.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        preguntaCol.setCellValueFactory(new PropertyValueFactory<RegistroAnularPreguntas, Integer>("numero"));

        preguntaCol.setStyle("-fx-alignment: CENTER;");
        respuestaCol.setCellValueFactory(new PropertyValueFactory<RegistroAnularPreguntas, String>("respuesta"));
        respuestaCol.setStyle("-fx-alignment: CENTER;");

        vfCol.setCellValueFactory(new PropertyValueFactory<RegistroAnularPreguntas, Boolean>("verdaderoFalso"));
        vfCol.setCellFactory(CheckBoxTableCell.forTableColumn(vfCol));

        mentalCol.setCellValueFactory(new PropertyValueFactory<RegistroAnularPreguntas, Boolean>("mental"));
        mentalCol.setCellFactory(CheckBoxTableCell.forTableColumn(mentalCol));

        anularCol.setCellValueFactory(new PropertyValueFactory<RegistroAnularPreguntas, Boolean>("anulada"));
        anularCol.setCellFactory(CheckBoxTableCell.forTableColumn(anularCol));

        anularCol.setEditable(true);

        mnuGrabar.setOnAction(event -> {
            ejecutaGrabar();
            final IActivator activator = new PruebasActivator();
            WindowManager.getInstance().show(activator.getView());
        });

        mnuExportar.setOnAction(event -> {
            final List<TableView<? extends Object>> listaTablas = new LinkedList<>();
            listaTablas.add(tblRegistroDefinePrueba);
            ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
        });
        mnuVolver.setOnAction(event -> {
            final IActivator activator = new PruebasActivator();
            WindowManager.getInstance().show(activator.getView());
        });
    }

    @Override
    public void onDataArrived(List<Object> list) {

    }

    @Override
    public void onFound(IEntity entity) {
        if (entity instanceof SPrueba) {
            registros = FXCollections.observableArrayList();
            prueba = (SPrueba) entity;
            setTitle("Anular Pregunta: " + prueba.getName());
            if (prueba.getRespuestas() != null && !prueba.getRespuestas().isEmpty()) {

                final StringBuffer resps = new StringBuffer();
                final List<SRespuestasEsperadasPrueba> respuestas = new ArrayList<SRespuestasEsperadasPrueba>();

                final int nroPreguntas = prueba.getNroPreguntas();
                final int oldNroPreguntas = prueba.getRespuestas().size();
                for (final SRespuestasEsperadasPrueba respuesta : prueba.getRespuestas()) {
                    respuestas.add(respuesta);
                }
                if (nroPreguntas > oldNroPreguntas) {
                    for (int n = oldNroPreguntas + 1; n < nroPreguntas; n++) {
                        final SRespuestasEsperadasPrueba resp = new SRespuestasEsperadasPrueba();
                        resp.setNumero(n);
                        respuestas.add(resp);
                    }
                } else if (nroPreguntas < oldNroPreguntas) {
                    while (respuestas.size() > nroPreguntas) {
                        respuestas.remove(respuestas.size() - 1);
                    }
                }

                Collections.sort(respuestas, Comparadores.compararRespuestasEsperadas());
                for (final SRespuestasEsperadasPrueba respuesta : respuestas) {
                    final RegistroAnularPreguntas registro = new RegistroAnularPreguntas();
                    registro.setNumero(respuesta.getNumero());
                    registro.setRespuesta(respuesta.getRespuesta());
                    registro.setVerdaderoFalso(respuesta.getVerdaderoFalso());
                    registro.setMental(respuesta.getMental());
                    registro.setAnulada(respuesta.getAnulada());
                    resps.append(respuesta.getRespuesta());
                    registros.add(registro);
                }
            } else {
                final int nroPreguntas = prueba.getNroPreguntas();
                for (int n = 0; n < nroPreguntas; n++) {
                    final RegistroAnularPreguntas registro = new RegistroAnularPreguntas();
                    registro.setNumero(n + 1);
                    registros.add(registro);
                }
            }

            tblRegistroDefinePrueba.setItems(registros);

            final boolean editable = !prueba.getEstado().equals(Estado.EVALUADA);
            preguntaCol.setEditable(editable);
            respuestaCol.setEditable(editable);
            vfCol.setEditable(editable);
            mentalCol.setEditable(editable);
        }
    }

    private void updateRespuestasEsperadas() {
        List<SRespuestasEsperadasPrueba> fromPrueba = prueba.getRespuestas();
        if (fromPrueba == null) {
            fromPrueba = new ArrayList<SRespuestasEsperadasPrueba>();
        }

        int n = 0;
        while (n < fromPrueba.size() && n < registros.size()) {
            final RegistroAnularPreguntas registro = registros.get(n);
            final SRespuestasEsperadasPrueba respuesta = fromPrueba.get(n);
            respuesta.setMental(registro.getMental());
            respuesta.setName(registro.getNumero().toString());
            respuesta.setNumero(registro.getNumero());
            respuesta.setRespuesta(registro.getRespuesta());
            respuesta.setVerdaderoFalso(registro.getVerdaderoFalso());
            respuesta.setAnulada(registro.getAnulada());
            n++;
        }
        if (fromPrueba.size() < registros.size()) {

            while (n < registros.size()) {
                final RegistroAnularPreguntas registro = registros.get(n);
                final SRespuestasEsperadasPrueba respuesta = new SRespuestasEsperadasPrueba();
                respuesta.setMental(registro.getMental());
                respuesta.setName(registro.getNumero().toString());
                respuesta.setNumero(new Integer(n + 1));
                respuesta.setRespuesta(registro.getRespuesta());
                respuesta.setVerdaderoFalso(registro.getVerdaderoFalso());
                respuesta.setAnulada(registro.getAnulada());
                respuesta.setPrueba(prueba);
                fromPrueba.add(respuesta);
                n++;
            }
        } else if (fromPrueba.size() > registros.size()) {
            while (fromPrueba.size() > registros.size()) {
                fromPrueba.remove(registros.size());
            }
        }
    }

}
