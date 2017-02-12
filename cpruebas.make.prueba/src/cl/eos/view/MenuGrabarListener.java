package cl.eos.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;

import cl.eos.restful.tables.R_Alternativas;
import cl.eos.restful.tables.R_Imagenes;
import cl.eos.restful.tables.R_Preguntas;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.util.MapBuilder;
import cl.eos.util.Utils;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;

public class MenuGrabarListener implements EventHandler<ActionEvent> {

  DefinirPrueba defPrueba;
  private String name;
  private Object idAsignatura;
  private Object idCurso;
  private int nroAlternativas;

  public MenuGrabarListener(DefinirPrueba defPrueba) {
    this.defPrueba = defPrueba;

  }

  @Override
  public void handle(ActionEvent event) {

    if (!validate())
      return;
    boolean isNew = false;
    name = defPrueba.txtNombre.getText();
    idAsignatura = defPrueba.cmbAsignatura.getValue().getId();
    idCurso = defPrueba.cmbAsignatura.getValue().getId();
    nroAlternativas = defPrueba.spnNroAlternativas.getNumber().intValue();

    R_Prueba prueba = defPrueba.prueba;
    if (defPrueba.prueba == null) {
      isNew = true;
      prueba = new R_Prueba.Builder().id(Utils.getLastIndex()).build();
    }

    prueba.setAsignatura_id(defPrueba.cmbAsignatura.getValue().getId());
    prueba.setCurso_id(defPrueba.cmbCurso.getValue().getId());
    prueba.setFecha(defPrueba.fecFeha.getValue().toEpochDay());
    prueba.setNroformas(defPrueba.spnForma.getNumber().intValue());
    prueba.setName(name);
    prueba.setNivelevaluacion_id(defPrueba.cmbNivelEvaluacion.getValue().getId());
    prueba.setProfesor_id(defPrueba.cmbProfesor.getValue().getId());
    prueba.setPuntajebase(defPrueba.spnPjeBase.getNumber().intValue());
    prueba.setNropreguntas(defPrueba.spnNroPreguntas.getNumber().intValue());
    prueba.setAlternativas(nroAlternativas);
    prueba.setTipoprueba_id(defPrueba.cmbTipoPrueba.getValue().getId());
    prueba.setExigencia(defPrueba.spnExigencia.getNumber().intValue());

    final ObservableList<ItemList> items = defPrueba.lstPreguntas.getItems();
    // List<R_RespuestasEsperadasPrueba> lstRespuestas = new ArrayList<>();
    String respuestas = "";

    for (final ItemList item : items) {
      respuestas = respuestas + item.rightAnswer;
    }
    prueba.setResponses(respuestas);

    if (!isNew) {
      Map<String, Object> params = MapBuilder.<String, Object>unordered().put("prueba_id", prueba.getId()).build();
      defPrueba.getController().deleteByParams(R_RespuestasEsperadasPrueba.class, params);
      defPrueba.getController().deleteByParams(R_Preguntas.class, params);
      defPrueba.getController().deleteByParams(R_Alternativas.class, params);
      defPrueba.getController().deleteByParams(R_Imagenes.class, params);
    }
    for (final ItemList item : items) {
      final String itemName = String.format("%d", item.nro);

      final boolean isMental = item.rightAnswer.equals("M");
      final boolean isTrueFalse = "VF".contains(item.rightAnswer.toUpperCase());

      R_RespuestasEsperadasPrueba respuesta = new R_RespuestasEsperadasPrueba.Builder().id(Utils.getLastIndex())
          .anulada(false).ejetematico_id(item.thematic.getId()).habilidad_id(item.ability.getId()).mental(isMental)
          .name(itemName).numero(item.nro).objetivo_id(item.objetive.getId()).respuesta(item.rightAnswer)
          .verdaderofalso(isTrueFalse).prueba_id(prueba.getId()).build();

      R_Preguntas pregunta = new R_Preguntas.Builder().id(Utils.getLastIndex()).name(item.question).numero(item.nro)
          .prueba_id(prueba.getId()).build();

      final List<R_Imagenes> lstImages = processImages(item, respuesta);
      final List<R_Alternativas> lstAlternativas = processAlteratives(item, respuesta);


      prueba = (R_Prueba) defPrueba.save(prueba);

      defPrueba.save(respuesta);
      defPrueba.save(pregunta);


      for (int n = 0; n < lstAlternativas.size(); n++) {
        R_Alternativas alt = lstAlternativas.get(n);
        alt.setId(Utils.getLastIndex());
        alt.setRespuesta_id(respuesta.getId());
        alt = (R_Alternativas) defPrueba.save(alt);
        lstAlternativas.set(n, alt);
      }

      
      if (lstImages != null && !lstImages.isEmpty()) {
        for (int n = 0; n < lstImages.size(); n++) {
          R_Imagenes img = lstImages.get(n);
          img.setId(Utils.getLastIndex());
          img.setRespuesta_id(respuesta.getId());
          img = (R_Imagenes) defPrueba.save(img);
          lstImages.set(n, img);
        }
      }

    }
  }

  /**
   * Corrresponde validar todas las entradas de datos.
   * 
   * @return
   */
  private boolean validate() {
    return defPrueba.validate();
  }

  private List<R_Alternativas> processAlteratives(ItemList item, R_RespuestasEsperadasPrueba respuesta) {

    List<R_Alternativas> lstAlternatives = null;
    for (int n = 0; n < nroAlternativas; n++) {
      if (lstAlternatives == null)
        lstAlternatives = new ArrayList<>();
      final String altName = String.format("%d_%d", item.nro, n + 1);
      final String text =
          item.alternatives.get(n) == null || item.alternatives.get(n).isEmpty() ? altName : item.alternatives.get(n);
      final R_Alternativas alternative =
          new R_Alternativas.Builder().name(altName).numero(n).texto(text).respuesta_id(respuesta.getId()).build();
      lstAlternatives.add(alternative);
    }
    return lstAlternatives;
  }

  private List<R_Imagenes> processImages(ItemList item, R_RespuestasEsperadasPrueba respuesta) {
    List<R_Imagenes> lstImages = null;
    for (int n = 0; n < item.images.size(); n++) {
      if (item.images.get(n) == null)
        break;
      final Image img = item.images.get(n);
      final BufferedImage bimg = SwingFXUtils.fromFXImage(img, null);
      final BufferedImage scaledImg =
          Scalr.resize(bimg, Method.ULTRA_QUALITY, Mode.AUTOMATIC, 512, 512, Scalr.OP_ANTIALIAS);

      final String dirName = Utils.getDefaultDirectory() + "/images/." + name;
      final File theDir = new File(dirName);
      if (!theDir.exists())
        theDir.mkdirs();

      if (theDir.exists()) {
        final String fName = String.format("%02d_%02d_%02d_%02d.png", idAsignatura, idCurso, item.nro, n);
        final String fileName = String.format("%s/%s", dirName, fName);

        try {
          ImageIO.write(scaledImg, "png", new File(fileName));
        } catch (final IOException e) {
          e.printStackTrace();
        }
        if (lstImages == null)
          lstImages = new ArrayList<>();
        final R_Imagenes image =
            new R_Imagenes.Builder().numero(item.nro).name(fName).respuesta_id(respuesta.getId()).build();
        lstImages.add(image);
      }
    }
    return lstImages;
  }

}
