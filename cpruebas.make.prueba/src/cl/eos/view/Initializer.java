package cl.eos.view;

import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import cl.eos.restful.tables.R_Alternativas;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_Habilidad;
import cl.eos.restful.tables.R_Imagenes;
import cl.eos.restful.tables.R_NivelEvaluacion;
import cl.eos.restful.tables.R_Objetivo;
import cl.eos.restful.tables.R_Preguntas;
import cl.eos.restful.tables.R_Profesor;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoCurso;
import cl.eos.restful.tables.R_TipoPrueba;
import cl.eos.util.MapBuilder;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

public class Initializer {
  static ItemList selected;
  static TextField[] alternatives = new TextField[5];
  static ImageView[] images = new ImageView[5];

  protected static void assignTestName(DefinirPrueba defPrueba) {
    String asign = "";
    if (defPrueba.cmbAsignatura.getValue() != null) {
      asign = defPrueba.cmbAsignatura.getValue().getName().replace(" ", "").replace(".", "_").toUpperCase();
    }
    String curso = "";
    if (defPrueba.cmbCurso.getValue() != null) {
      curso = defPrueba.cmbCurso.getValue().getName().replace(" ", "").replace(".", "_").toUpperCase();
    }
    String name = String.format("%s%s%04d.%02d%02d", asign, curso, defPrueba.fecFeha.getValue().getYear(),
        defPrueba.fecFeha.getValue().getMonthValue(), defPrueba.fecFeha.getValue().getDayOfMonth());
    final String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
    name = normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    defPrueba.txtNombre.setText(name);
  }

  private static void assignDefaultValues(DefinirPrueba defPrueba) {
    if (defPrueba.prueba == null) {
      defPrueba.spnNroPreguntas.setNumber(new BigDecimal(25));
      defPrueba.spnNroAlternativas.setNumber(new BigDecimal(5));
      defPrueba.spnPjeBase.setNumber(new BigDecimal(1));
      defPrueba.spnForma.setNumber(new BigDecimal(1));
      defPrueba.spnExigencia.setNumber(new BigDecimal(60));
      defPrueba.fecFeha.setValue(LocalDate.now());
      defPrueba.cmbAsignatura.getSelectionModel().select(0);
      defPrueba.cmbCurso.getSelectionModel().select(1);
      defPrueba.cmbTipoPrueba.getSelectionModel().select(0);
      defPrueba.cmbProfesor.getSelectionModel().select(0);
      defPrueba.cmbNivelEvaluacion.getSelectionModel().select(0);
      defPrueba.lstPreguntas.getSelectionModel().select(0);
      defPrueba.chkOpcionA.setSelected(true);
    }
  }

  public static void initialize(DefinirPrueba defPrueba) {
    Initializer.alternatives[0] = defPrueba.txtAlternativaA;
    Initializer.alternatives[1] = defPrueba.txtAlternativaB;
    Initializer.alternatives[2] = defPrueba.txtAlternativaC;
    Initializer.alternatives[3] = defPrueba.txtAlternativaD;
    Initializer.alternatives[4] = defPrueba.txtAlternativaE;
    Initializer.images[0] = defPrueba.img1;
    Initializer.images[1] = defPrueba.img2;
    Initializer.images[2] = defPrueba.img3;
    Initializer.images[3] = defPrueba.img4;
    Initializer.images[4] = defPrueba.img5;
    defPrueba.mnuGrabar.setOnAction(new MenuGrabarListener(defPrueba));
    defPrueba.dataContainer.setDisable(true);
    defPrueba.group = new ToggleGroup();
    defPrueba.chkOpcionA.setToggleGroup(defPrueba.group);
    defPrueba.chkOpcionC.setToggleGroup(defPrueba.group);
    defPrueba.chkOpcionB.setToggleGroup(defPrueba.group);
    defPrueba.chkOpcionE.setToggleGroup(defPrueba.group);
    defPrueba.chkOpcionD.setToggleGroup(defPrueba.group);
    defPrueba.chkOpcionF.setToggleGroup(defPrueba.group);
    defPrueba.chkOpcionV.setToggleGroup(defPrueba.group);
    defPrueba.chkOpcionMental.setToggleGroup(defPrueba.group);
    defPrueba.spnExigencia.setMinValue(new BigDecimal(0));
    defPrueba.spnExigencia.setMaxValue(new BigDecimal(80));
    defPrueba.spnForma.setMinValue(new BigDecimal(1));
    defPrueba.spnForma.setMaxValue(new BigDecimal(3));
    defPrueba.spnPjeBase.setMinValue(new BigDecimal(0));
    defPrueba.spnPjeBase.setMaxValue(new BigDecimal(7));
    defPrueba.spnNroAlternativas.setMinValue(new BigDecimal(3));
    defPrueba.spnNroAlternativas.setMaxValue(new BigDecimal(5));
    defPrueba.spnNroPreguntas.setMinValue(new BigDecimal(1));
    defPrueba.spnNroPreguntas.setMaxValue(new BigDecimal(75));
    defPrueba.spnNroPreguntas.numberProperty()
        .addListener((ChangeListener<BigDecimal>) (observable, oldValue, newValue) -> {
          if (oldValue == null) {
            for (int n1 = 0; n1 < newValue.intValue(); n1++)
              defPrueba.lstPreguntas.getItems().add(new ItemList(n1 + 1));
          } else if (oldValue.intValue() > newValue.intValue()) {
            int n2 = oldValue.intValue() - 1;
            while (n2 >= newValue.intValue()) {
              final int idx = defPrueba.lstPreguntas.getItems().size() - 1;
              defPrueba.lstPreguntas.getItems().remove(idx);
              n2--;
            }
          } else if (oldValue.intValue() < newValue.intValue()) {
            for (int n3 = oldValue.intValue(); n3 < newValue.intValue(); n3++)
              defPrueba.lstPreguntas.getItems().add(new ItemList(n3 + 1));
          }
        });
    defPrueba.spnNroAlternativas.numberProperty()
        .addListener((ChangeListener<BigDecimal>) (observable, oldValue, newValue) -> {
          if (oldValue != null && !oldValue.equals(newValue)) {
            defPrueba.chkOpcionA.setDisable(newValue.intValue() < 1);
            defPrueba.chkOpcionB.setDisable(newValue.intValue() < 2);
            defPrueba.chkOpcionC.setDisable(newValue.intValue() < 3);
            defPrueba.chkOpcionE.setDisable(newValue.intValue() < 4);
            defPrueba.chkOpcionD.setDisable(newValue.intValue() < 5);
            defPrueba.txtAlternativaA.setDisable(newValue.intValue() < 1);
            defPrueba.txtAlternativaB.setDisable(newValue.intValue() < 2);
            defPrueba.txtAlternativaC.setDisable(newValue.intValue() < 3);
            defPrueba.txtAlternativaD.setDisable(newValue.intValue() < 4);
            defPrueba.txtAlternativaE.setDisable(newValue.intValue() < 5);
            defPrueba.img1.setDisable(newValue.intValue() < 1);
            defPrueba.img2.setDisable(newValue.intValue() < 2);
            defPrueba.img3.setDisable(newValue.intValue() < 3);
            defPrueba.img4.setDisable(newValue.intValue() < 4);
            defPrueba.img5.setDisable(newValue.intValue() < 5);
          }
        });

    defPrueba.mnuEliminarPregunta.setOnAction(event -> {
      int idx = defPrueba.lstPreguntas.getSelectionModel().getSelectedIndex();
      if (idx == -1)
        return;
      defPrueba.lstPreguntas.getItems().remove(idx);
      while (idx < defPrueba.lstPreguntas.getItems().size()) {
        final ItemList item = defPrueba.lstPreguntas.getItems().get(idx);
        item.nro = idx + 1;
        idx++;
      }
      defPrueba.lstPreguntas.getSelectionModel().clearSelection();
      defPrueba.dataContainer.setDisable(true);
    });
    Initializer.initializeImages(defPrueba);
    Initializer.initializeList(defPrueba);
    Initializer.initializeInteraction(defPrueba);

    Initializer.assignDefaultValues(defPrueba);
    defPrueba.cmbAsignatura.setOnAction(event -> {
      processSelection(defPrueba);
    });
    defPrueba.cmbCurso.setOnAction(event -> {
      processSelection(defPrueba);
    });
  }

  /**
   * Procesa los eventos de selección de asignatura y curso.
   * 
   * @param defPrueba La vista que contiene los controles donde se presenta la prueba.
   */
  private static void processSelection(DefinirPrueba defPrueba) {
    R_Asignatura asignatura = defPrueba.cmbAsignatura.getSelectionModel().getSelectedItem();
    if (asignatura != null) {
      Map<String, Object> params =
          MapBuilder.<String, Object>unordered().put("asignatura_id", asignatura.getId()).build();
      defPrueba.getController().findByParam(R_Ejetematico.class, params, defPrueba);
      Initializer.assignTestName(defPrueba);

      if (defPrueba.cmbCurso.getSelectionModel().getSelectedItem() != null) {
        Initializer.assignTestName(defPrueba);
        defPrueba.cmbObjetivos.getItems().clear();
        params = MapBuilder.<String, Object>unordered().put("asignatura_id", asignatura.getId())
            .put("tipocurso_id", defPrueba.cmbCurso.getSelectionModel().getSelectedItem().getId()).build();
        defPrueba.getController().findByParam(R_Objetivo.class, params, defPrueba);
      }
    }
  }

  private static void initializeImages(DefinirPrueba defPrueba) {
    final EventHandler<MouseEvent> handler = event -> {
      if (event.getClickCount() == 2) {
        final FileChooser fc = new FileChooser();
        fc.setTitle("Seleccione imagen");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Todas las imagénes", "*.*"),
            new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));
        final File file = fc.showOpenDialog(null);
        if (file == null)
          return;
        try {
          final ImageView img = (ImageView) event.getSource();
          Image image;
          image = new Image(file.toURI().toURL().toString());
          img.setImage(image);
        } catch (final MalformedURLException e) {
          e.printStackTrace();
        }
      }
    };
    defPrueba.img1.setPreserveRatio(true);
    defPrueba.img2.setPreserveRatio(true);
    defPrueba.img3.setPreserveRatio(true);
    defPrueba.img4.setPreserveRatio(true);
    defPrueba.img5.setPreserveRatio(true);
    defPrueba.img1.setOnMouseClicked(handler);
    defPrueba.img2.setOnMouseClicked(handler);
    defPrueba.img3.setOnMouseClicked(handler);
    defPrueba.img4.setOnMouseClicked(handler);
    defPrueba.img5.setOnMouseClicked(handler);
  }

  private static void initializeInteraction(DefinirPrueba defPrueba) {
    defPrueba.txtPregunta.textProperty().addListener((observable, oldValue, newValue) -> {
      if (Initializer.selected == null)
        return;
      Initializer.selected.question = newValue;
    });
    defPrueba.txtAlternativaA.textProperty().addListener((observable, oldValue, newValue) -> {
      if (Initializer.selected == null)
        return;
      Initializer.selected.alternatives.set(0, newValue);
    });
    defPrueba.txtAlternativaB.textProperty().addListener((observable, oldValue, newValue) -> {
      if (Initializer.selected == null)
        return;
      Initializer.selected.alternatives.set(1, newValue);
    });
    defPrueba.txtAlternativaC.textProperty().addListener((observable, oldValue, newValue) -> {
      if (Initializer.selected == null)
        return;
      Initializer.selected.alternatives.set(2, newValue);
    });
    defPrueba.txtAlternativaD.textProperty().addListener((observable, oldValue, newValue) -> {
      if (Initializer.selected == null)
        return;
      Initializer.selected.alternatives.set(3, newValue);
    });
    defPrueba.txtAlternativaE.textProperty().addListener((observable, oldValue, newValue) -> {
      if (Initializer.selected == null)
        return;
      Initializer.selected.alternatives.set(4, newValue);
    });
    defPrueba.img1.imageProperty().addListener((observable, oldValue, newValue) -> {
      if (Initializer.selected == null)
        return;
      Initializer.selected.images.set(0, newValue);
    });
    defPrueba.img2.imageProperty().addListener((observable, oldValue, newValue) -> {
      if (Initializer.selected == null)
        return;
      Initializer.selected.images.set(1, newValue);
    });
    defPrueba.img3.imageProperty().addListener((observable, oldValue, newValue) -> {
      if (Initializer.selected == null)
        return;
      Initializer.selected.images.set(2, newValue);
    });
    defPrueba.img4.imageProperty().addListener((observable, oldValue, newValue) -> {
      if (Initializer.selected == null)
        return;
      Initializer.selected.images.set(3, newValue);
    });
    defPrueba.img5.imageProperty().addListener((observable, oldValue, newValue) -> {
      if (Initializer.selected == null)
        return;
      Initializer.selected.images.set(4, newValue);
    });
    defPrueba.cmbEjesTematicos.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {
          if (Initializer.selected == null)
            return;
          Initializer.selected.thematic = newValue;
        });
    defPrueba.cmbHabilidades.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {
          if (Initializer.selected == null)
            return;
          Initializer.selected.ability = newValue;
        });
    defPrueba.cmbObjetivos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (Initializer.selected == null)
        return;
      Initializer.selected.objetive = newValue;
    });
    defPrueba.chkOpcionA.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (Initializer.selected == null)
        return;
      if (newValue.booleanValue())
        Initializer.selected.rightAnswer = "A";
    });
    defPrueba.chkOpcionB.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (Initializer.selected == null)
        return;
      if (newValue.booleanValue())
        Initializer.selected.rightAnswer = "B";
    });
    defPrueba.chkOpcionC.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (Initializer.selected == null)
        return;
      if (newValue.booleanValue())
        Initializer.selected.rightAnswer = "C";
    });
    defPrueba.chkOpcionD.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (Initializer.selected == null)
        return;
      if (newValue.booleanValue())
        Initializer.selected.rightAnswer = "D";
    });
    defPrueba.chkOpcionE.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (Initializer.selected == null)
        return;
      if (newValue.booleanValue())
        Initializer.selected.rightAnswer = "E";
    });
    defPrueba.chkOpcionV.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (Initializer.selected == null)
        return;
      if (newValue.booleanValue())
        Initializer.selected.rightAnswer = "V";
    });
    defPrueba.chkOpcionF.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (Initializer.selected == null)
        return;
      if (newValue.booleanValue())
        Initializer.selected.rightAnswer = "F";
    });
    defPrueba.chkOpcionMental.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (Initializer.selected == null)
        return;
      if (newValue.booleanValue())
        Initializer.selected.rightAnswer = "M";
    });
  }

  private static void initializeList(DefinirPrueba defPrueba) {
    defPrueba.lstPreguntas.getSelectionModel().selectedItemProperty()
        .addListener((ChangeListener<ItemList>) (observable, oldValue, newValue) -> {
          defPrueba.dataContainer.setDisable(false);
          Initializer.selected = newValue;
          if (newValue == null)
            return;
          defPrueba.txtPregunta.setText(Initializer.selected.question);
          defPrueba.cmbEjesTematicos.getSelectionModel().select(Initializer.selected.thematic);
          defPrueba.cmbHabilidades.getSelectionModel().select(Initializer.selected.ability);
          defPrueba.cmbObjetivos.getSelectionModel().select(Initializer.selected.objetive);
          if (Initializer.selected.alternatives != null && !Initializer.selected.alternatives.isEmpty()) {
            for (int n1 = 0; n1 < Initializer.selected.alternatives.size(); n1++)
              Initializer.alternatives[n1].setText(Initializer.selected.alternatives.get(n1));
          }
          if (Initializer.selected.images != null && !Initializer.selected.images.isEmpty()) {
            for (int n2 = 0; n2 < Initializer.selected.alternatives.size(); n2++)
              Initializer.images[n2].setImage(Initializer.selected.images.get(n2));
          }
          final String ranswer = Initializer.selected.rightAnswer;
          if (ranswer == null || ranswer.isEmpty()) {
            defPrueba.chkOpcionA.setSelected(true);
            return;
          }
          defPrueba.chkOpcionA.setSelected(ranswer.equals("A"));
          defPrueba.chkOpcionB.setSelected(ranswer.equals("B"));
          defPrueba.chkOpcionC.setSelected(ranswer.equals("C"));
          defPrueba.chkOpcionD.setSelected(ranswer.equals("D"));
          defPrueba.chkOpcionE.setSelected(ranswer.equals("E"));
          defPrueba.chkOpcionV.setSelected(ranswer.equals("V"));
          defPrueba.chkOpcionF.setSelected(ranswer.equals("E"));
          defPrueba.chkOpcionMental.setSelected(ranswer.equals("M"));
        });
  }


  public static void setPrueba(R_Prueba prueba, DefinirPrueba defPrueba) {

    Integer nroAlternativas = prueba.getAlternativas();
    nroAlternativas = nroAlternativas == null || nroAlternativas < 3 ? 3 : nroAlternativas;
    final Integer nroPreguntas = prueba.getNropreguntas();


    // Asignatura
    final R_Asignatura asignatura = defPrueba.cmbAsignatura.getItems().stream()
        .filter(v -> v.getId().equals(prueba.getAsignatura_id())).findFirst().orElse(null);
    defPrueba.cmbAsignatura.getSelectionModel().select(asignatura);
    // Profesor
    final R_Profesor profesor = defPrueba.cmbProfesor.getItems().stream()
        .filter(v -> v.getId().equals(prueba.getProfesor_id())).findFirst().orElse(null);
    defPrueba.cmbProfesor.getSelectionModel().select(profesor);
    processSelection(defPrueba);
    // Curso
    final R_TipoCurso curso = defPrueba.cmbCurso.getItems().stream().filter(v -> v.getId().equals(prueba.getCurso_id()))
        .findFirst().orElse(null);
    defPrueba.cmbCurso.getSelectionModel().select(curso);
    // Tipo de prueba
    final R_TipoPrueba tipoPrueba = defPrueba.cmbTipoPrueba.getItems().stream()
        .filter(v -> v.getId().equals(prueba.getTipoprueba_id())).findFirst().orElse(null);
    defPrueba.cmbTipoPrueba.getSelectionModel().select(tipoPrueba);
    // Nivel de evaluación
    final R_NivelEvaluacion nivel = defPrueba.cmbNivelEvaluacion.getItems().stream()
        .filter(v -> v.getId().equals(prueba.getNivelevaluacion_id())).findFirst().orElse(null);
    defPrueba.cmbNivelEvaluacion.getSelectionModel().select(nivel);

    defPrueba.txtNombre.setText(prueba.getName());
    defPrueba.spnExigencia.setNumber(new BigDecimal(prueba.getExigencia()));
    defPrueba.spnPjeBase.setNumber(new BigDecimal(prueba.getPuntajebase()));
    defPrueba.spnNroAlternativas.setNumber(new BigDecimal(nroAlternativas));
    defPrueba.spnNroPreguntas.setNumber(new BigDecimal(prueba.getNropreguntas()));
    defPrueba.spnForma.setNumber(new BigDecimal(prueba.getNroformas()));
    defPrueba.fecFeha.setValue(prueba.getFechaLocal());



    // Se obtienen las Preguntas, respuestas esperadas, alternativas, imágenes, eje temático,
    // habilidad y objetivo por cada una



    Map<String, Object> params = MapBuilder.<String, Object>unordered().put("prueba_id", prueba.getId()).build();

    final List<R_RespuestasEsperadasPrueba> lstRespEsperadas =
        defPrueba.getController().findByParamsSynchro(R_RespuestasEsperadasPrueba.class, params);
    final List<R_Preguntas> lstPreguntas = defPrueba.getController().findByParamsSynchro(R_Preguntas.class, params);

    final ObservableList<ItemList> olstPreguntas = FXCollections.observableArrayList(Stream.generate(ItemList::new)
        .limit(defPrueba.spnNroPreguntas.getNumber().intValue()).collect(Collectors.toList()));

    if (lstRespEsperadas == null || lstRespEsperadas.isEmpty()) {
      IntStream.range(0, nroPreguntas).forEach(idx -> olstPreguntas.get(idx).nro = idx + 1);
      defPrueba.lstPreguntas.setItems(olstPreguntas);
    } else {
      lstRespEsperadas.forEach(resp -> {
        Map<String, Object> parameters =
            MapBuilder.<String, Object>unordered().put("respuesta_id", resp.getId()).build();
        List<R_Alternativas> lstAlternativas =
            defPrueba.getController().findByParamsSynchro(R_Alternativas.class, parameters);
        List<R_Imagenes> lstImagenes = defPrueba.getController().findByParamsSynchro(R_Imagenes.class, parameters);
        R_Ejetematico eje = defPrueba.getController().findByIdSynchro(R_Ejetematico.class, resp.getEjetematico_id());
        R_Habilidad hab = defPrueba.getController().findByIdSynchro(R_Habilidad.class, resp.getHabilidad_id());
        R_Objetivo obj = defPrueba.getController().findByIdSynchro(R_Objetivo.class, resp.getObjetivo_id());

        int n = resp.getNumero();
        List<String> sAlternativas = lstAlternativas == null ? null
            : lstAlternativas.stream().map(a -> a.getTexto()).collect(Collectors.toList());

        List<Image> iImages = lstImagenes.stream().map(i -> new Image(i.getName())).collect(Collectors.toList());
        String pregunta = "";
        if (lstPreguntas != null && lstPreguntas.size() > (n - 1)) {
          pregunta = lstPreguntas.get(n - 1).getName();
        }
        ItemList item = new ItemList.Builder().hability(hab).alternatives(sAlternativas).images(iImages).objetive(obj)
            .question(pregunta).thematic(eje).rightAnswer(resp.getRespuesta()).build(n);
        olstPreguntas.set(n - 1, item);
        defPrueba.lstPreguntas.setItems(olstPreguntas);
      });
    }


  }

}
