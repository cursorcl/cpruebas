package cl.eos.view;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;

import cl.eos.restful.tables.R_Alternativas;
import cl.eos.restful.tables.R_EstadoPruebaCliente;
import cl.eos.restful.tables.R_Formas;
import cl.eos.restful.tables.R_Imagenes;
import cl.eos.restful.tables.R_Preguntas;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_Prueba.Estado;
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
  private int nroAlternativas;

  public MenuGrabarListener(DefinirPrueba defPrueba) {
    this.defPrueba = defPrueba;

  }

  @SuppressWarnings("unchecked")
  @Override
  public void handle(ActionEvent event) {

    if (!validate())
      return;
    boolean isNew = false;
    name = defPrueba.txtNombre.getText();
    nroAlternativas = defPrueba.spnNroAlternativas.getNumber().intValue();

    R_Prueba prueba = defPrueba.prueba;
    R_EstadoPruebaCliente estadoprueba =
        new R_EstadoPruebaCliente.Builder().estado_id((long) Estado.CREADA.getId()).prueba_id(-1L).build();
    if (defPrueba.prueba == null) {
      isNew = true;
      prueba = new R_Prueba.Builder().id(Utils.getLastIndex()).build();
    } else {
      Map<String, Object> params =
          MapBuilder.<String, Object>unordered().put("prueba_id", defPrueba.getPrueba().getId()).build();
      List<R_EstadoPruebaCliente> lst =
          (List<R_EstadoPruebaCliente>) defPrueba.findSynchroByParams(R_EstadoPruebaCliente.class, params);
      if (lst != null && lst.size() > 0)
        estadoprueba = lst.get(0);

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
    defPrueba.prueba = (R_Prueba) defPrueba.save(prueba);

    estadoprueba.setPrueba_id(defPrueba.prueba.getId());

    boolean hasRespuestaEsperada = false;

    for (final ItemList item : items) {
      final String itemName = String.format("%d", item.nro);

      final boolean isMental = item.rightAnswer.equals("M");
      final boolean isTrueFalse = "VF".contains(item.rightAnswer.toUpperCase());

      // Almacenando la respuesta esperadade la preguta
      R_RespuestasEsperadasPrueba respuesta = new R_RespuestasEsperadasPrueba.Builder().id(Utils.getLastIndex())
          .anulada(false).ejetematico_id(item.thematic == null ? -1 : item.thematic.getId())
          .habilidad_id(item.ability == null ? -1 : item.ability.getId()).mental(isMental).name(itemName)
          .numero(item.nro).objetivo_id(item.objetive == null ? -1 : item.objetive.getId()).respuesta(item.rightAnswer)
          .verdaderofalso(isTrueFalse).prueba_id(prueba.getId()).build();
      respuesta = (R_RespuestasEsperadasPrueba) defPrueba.save(respuesta);
      hasRespuestaEsperada = true;


      // Si la pregunta es vacía, no tiene sentido almacenar el resto.
      if (item.question == null || item.question.isEmpty())
        continue;

      // Almancenando la pregunta
      R_Preguntas pregunta = new R_Preguntas.Builder().id(Utils.getLastIndex()).name(item.question).numero(item.nro)
          .prueba_id(prueba.getId()).build();
      pregunta = (R_Preguntas) defPrueba.save(pregunta);

      final List<R_Imagenes> lstImages = processImages(item, respuesta);
      final List<R_Alternativas> lstAlternativas = processAlteratives(item, respuesta);

      // Almacenando las alterntivas de la pregunta
      if (lstAlternativas != null && !lstAlternativas.isEmpty()) {
        for (int n = 0; n < lstAlternativas.size(); n++) {
          R_Alternativas alt = lstAlternativas.get(n);
          alt.setId(Utils.getLastIndex());
          alt.setRespuesta_id(respuesta.getId());
          alt = (R_Alternativas) defPrueba.save(alt);
          lstAlternativas.set(n, alt);
        }
      }
      // Almacenando las imágenes de la pregunta
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
    final List<R_Formas> lstFormas = getFormasPrueba();
    if (lstFormas != null && !lstFormas.isEmpty()) {
      for (R_Formas forma : lstFormas) {
        defPrueba.save(forma);
      }
    }
    long estado = estadoprueba.getEstado_id();
    if (Estado.EVALUADA.getId() != estado) {

      if (hasRespuestaEsperada)
        estadoprueba.setEstado_id(Estado.DEFINIDA.getId());
      else
        estadoprueba.setEstado_id(Estado.CREADA.getId());

      defPrueba.save(estadoprueba);
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
      final String altName = "";
      final String text =
          item.alternatives == null || item.alternatives.get(n) == null || item.alternatives.get(n).isEmpty() ? altName
              : item.alternatives.get(n);
      final R_Alternativas alternative =
          new R_Alternativas.Builder().name(altName).numero(n).texto(text).respuesta_id(respuesta.getId()).build();
      lstAlternatives.add(alternative);
    }
    return lstAlternatives;
  }

  private List<R_Imagenes> processImages(ItemList item, R_RespuestasEsperadasPrueba respuesta) {
    if (item == null || item.images == null)
      return null;
    List<R_Imagenes> lstImages = null;
    for (int n = 0; n < item.images.size(); n++) {
      if (item.images.get(n) == null)
        break;
      final Image img = item.images.get(n);
      BufferedImage scaledImg = SwingFXUtils.fromFXImage(img, null);

      if (scaledImg.getWidth() > 512 || scaledImg.getHeight() > 512) {
        // TODO notificar que la imagen será recortada a 512x512
        scaledImg = Scalr.resize(scaledImg, Method.ULTRA_QUALITY, Mode.AUTOMATIC, 512, 512, Scalr.OP_ANTIALIAS);
      }

      ByteArrayOutputStream output = new ByteArrayOutputStream();
      try {
        ImageIO.write(scaledImg, "jpg", output);
        String s = DatatypeConverter.printBase64Binary(output.toByteArray());
        if (lstImages == null)
          lstImages = new ArrayList<>();
        final R_Imagenes image = new R_Imagenes.Builder().numero(n + 1).name("img-" + (n + 1)).image(s)
            .respuesta_id(respuesta.getId()).build();
        lstImages.add(image);

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return lstImages;

  }


  private List<R_Formas> getFormasPrueba() {

    Map<String, Object> params =
        MapBuilder.<String, Object>unordered().put("prueba_id", defPrueba.prueba.getId()).build();

    defPrueba.deleteByParams(R_Formas.class, params);
    List<R_Formas> formas = new ArrayList<>();
    int nroFormas = defPrueba.prueba.getNroformas().intValue();


    int nroPreguntas = defPrueba.prueba.getNropreguntas();
    List<Integer> numeros = new ArrayList<Integer>();

    for (int n = 1; n <= nroPreguntas; n++) {
      numeros.add(new Integer(n));
    }
    for (int n = 0; n < nroFormas; n++) {
      String orden = new String();
      for (int idx = 0; idx < numeros.size(); idx++) {
        if (idx > 0) {
          orden += ",";
        }
        orden += numeros.get(idx).toString();
      }
      R_Formas forma = new R_Formas.Builder().forma(n + 1).name("Forma " + (n + 1)).orden(orden)
          .prueba_id(defPrueba.prueba.getId()).id(Utils.getLastIndex()).build();
      formas.add(forma);
      Collections.shuffle(numeros);
    }
    return formas;
  }

}
