package cl.eos.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cl.eos.activator.ListadoPruebasActivator;
import cl.eos.imp.view.AFormView;
import cl.eos.imp.view.WindowManager;
import cl.eos.interfaces.IActivator;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.util.Comparadores;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_Prueba.Estado;
import cl.eos.restful.tables.R_PruebaRendida;
import cl.eos.restful.tables.R_RangoEvaluacion;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.util.ExcelSheetWriterObj;
import cl.eos.util.MapBuilder;
import cl.eos.util.Utils;
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
    @FXML private TableView<RegistroAnularPreguntas> tblRegistroDefinePrueba;
    @FXML private TableColumn<RegistroAnularPreguntas, Integer> preguntaCol;
    @FXML private TableColumn<RegistroAnularPreguntas, String> respuestaCol;
    @FXML private TableColumn<RegistroAnularPreguntas, Boolean> vfCol;
    @FXML private TableColumn<RegistroAnularPreguntas, Boolean> mentalCol;
    @FXML private TableColumn<RegistroAnularPreguntas, Boolean> anularCol;
    @FXML private MenuItem mnuGrabar;
    @FXML private MenuItem mnuVolver;
    @FXML private MenuItem mnuExportar;
    private R_Prueba prueba;
    private ObservableList<RegistroAnularPreguntas> registros;
    private List<R_RespuestasEsperadasPrueba> respuestas;
    private List<R_EvaluacionPrueba> evaluaciones;
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
             * Se van a actualizar todas las evaluaciones, recalculando las notas.
             */
            Map<String, Object> params = MapBuilder.<String, Object>unordered().put("evaluacionprueba_id", prueba.getNivelevaluacion_id()).build();
            List<R_RangoEvaluacion> rangos = controller.findByParamsSynchro(R_RangoEvaluacion.class, params);
            for (final R_EvaluacionPrueba evaluacion : evaluaciones) {
                params = MapBuilder.<String, Object>unordered().put("evaluacionprueba_id", evaluacion.getId()).build();
                List<R_PruebaRendida> pruebasRendidas = controller.findByParamsSynchro(R_PruebaRendida.class, params);
                
                for (final R_PruebaRendida pruebaRendida : pruebasRendidas) {
                    pruebaRendida.reEvaluate(prueba, respuestas, rangos);
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
            final IActivator activator = new ListadoPruebasActivator();
            WindowManager.getInstance().show(activator.getView());
        });
        mnuExportar.setOnAction(event -> {
            final List<TableView<? extends Object>> listaTablas = new LinkedList<>();
            listaTablas.add(tblRegistroDefinePrueba);
            ExcelSheetWriterObj.convertirDatosALibroDeExcel(listaTablas);
        });
        mnuVolver.setOnAction(event -> {
            final IActivator activator = new ListadoPruebasActivator();
            WindowManager.getInstance().show(activator.getView());
        });
    }
    @Override
    public void onFound(IEntity entity) {
        if (entity instanceof R_Prueba) {
            registros = FXCollections.observableArrayList();
            prueba = (R_Prueba) entity;
            Map<String, Object> params = new HashMap<>();
            params.put("prueba_id", prueba.getId());
            respuestas = controller.findByParamsSynchro(R_RespuestasEsperadasPrueba.class, params);
            setTitle("Anular Pregunta: " + prueba.getName());
            if (respuestas != null && !respuestas.isEmpty()) {
                final StringBuffer resps = new StringBuffer();
                final int nroPreguntas = prueba.getNropreguntas();
                final int oldNroPreguntas = respuestas.size();
                for (final R_RespuestasEsperadasPrueba respuesta : respuestas) {
                    respuestas.add(respuesta);
                }
                if (nroPreguntas > oldNroPreguntas) {
                    for (int n = oldNroPreguntas + 1; n < nroPreguntas; n++) {
                        final R_RespuestasEsperadasPrueba resp = new R_RespuestasEsperadasPrueba.Builder().id(Utils.getLastIndex()).build();
                        resp.setNumero(n);
                        respuestas.add(resp);
                    }
                } else if (nroPreguntas < oldNroPreguntas) {
                    while (respuestas.size() > nroPreguntas) {
                        respuestas.remove(respuestas.size() - 1);
                    }
                }
                Collections.sort(respuestas, Comparadores.compararRespuestasEsperadas());
                for (final R_RespuestasEsperadasPrueba respuesta : respuestas) {
                    final RegistroAnularPreguntas registro = new RegistroAnularPreguntas();
                    registro.setNumero(respuesta.getNumero());
                    registro.setRespuesta(respuesta.getRespuesta());
                    registro.setVerdaderoFalso(respuesta.getVerdaderofalso());
                    registro.setMental(respuesta.getMental());
                    registro.setAnulada(respuesta.getAnulada());
                    resps.append(respuesta.getRespuesta());
                    registros.add(registro);
                }
            } else {
                final int nroPreguntas = prueba.getNropreguntas();
                for (int n = 0; n < nroPreguntas; n++) {
                    final RegistroAnularPreguntas registro = new RegistroAnularPreguntas();
                    registro.setNumero(n + 1);
                    registros.add(registro);
                }
            }
            tblRegistroDefinePrueba.setItems(registros);
            final boolean editable = !R_Prueba.Estado.getEstado(prueba.getEstado()).equals(Estado.EVALUADA);
            preguntaCol.setEditable(editable);
            respuestaCol.setEditable(editable);
            vfCol.setEditable(editable);
            mentalCol.setEditable(editable);
        }
    }
    private void updateRespuestasEsperadas() {
        List<R_RespuestasEsperadasPrueba> fromPrueba = respuestas;
        if (fromPrueba == null) {
            fromPrueba = new ArrayList<R_RespuestasEsperadasPrueba>();
        }
        int n = 0;
        while (n < fromPrueba.size() && n < registros.size()) {
            final RegistroAnularPreguntas registro = registros.get(n);
            final R_RespuestasEsperadasPrueba respuesta = fromPrueba.get(n);
            respuesta.setMental(registro.getMental());
            respuesta.setName(registro.getNumero().toString());
            respuesta.setNumero(registro.getNumero());
            respuesta.setRespuesta(registro.getRespuesta());
            respuesta.setVerdaderofalso(registro.getVerdaderoFalso());
            respuesta.setAnulada(registro.getAnulada());
            n++;
        }
        if (fromPrueba.size() < registros.size()) {
            while (n < registros.size()) {
                final RegistroAnularPreguntas registro = registros.get(n);
                final R_RespuestasEsperadasPrueba respuesta = new R_RespuestasEsperadasPrueba.Builder().id(Utils.getLastIndex())
                        .mental(registro.getMental()).name(registro.getNumero().toString()).numero(new Integer(n + 1))
                        .respuesta(registro.getRespuesta()).verdaderofalso(registro.getVerdaderoFalso()).anulada(registro.getAnulada())
                        .prueba_id(prueba.getId()).build();
                fromPrueba.add(respuesta);
                n++;
            }
        } else if (fromPrueba.size() > registros.size()) {
            while (fromPrueba.size() > registros.size()) {
                fromPrueba.remove(registros.size());
            }
        }
    }
    /* (non-Javadoc)
     * @see cl.eos.imp.view.AView#onDataArrived(java.util.List)
     */
    @Override
    public void onDataArrived(List<Object> list) {
        if (list != null && !list.isEmpty()) {
            final Object entity = list.get(0);
            if (entity instanceof R_EvaluacionPrueba) {
                evaluaciones = list.stream().map(e->(R_EvaluacionPrueba)e).collect(Collectors.toList()); 
            }
        }
        
    }
    
    
}
