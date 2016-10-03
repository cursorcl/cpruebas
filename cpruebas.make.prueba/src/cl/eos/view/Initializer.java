package cl.eos.view;

import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
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

    public static void initialize(DefinirPrueba defPrueba) {
        alternatives[0] = defPrueba.txtAlternativaA;
        alternatives[1] = defPrueba.txtAlternativaB;
        alternatives[2] = defPrueba.txtAlternativaC;
        alternatives[3] = defPrueba.txtAlternativaD;
        alternatives[4] = defPrueba.txtAlternativaE;
        
        images[0] = defPrueba.img1;
        images[1] = defPrueba.img2;
        images[2] = defPrueba.img3;
        images[3] = defPrueba.img4;
        images[4] = defPrueba.img5;
        
        defPrueba.mnuGrabar.setOnAction(new MenuGrabarListener(defPrueba));
        
        defPrueba.group = new ToggleGroup();
        defPrueba.chkOpcionA.setToggleGroup(defPrueba.group);
        defPrueba.chkOpcionA.setSelected(true);
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

        defPrueba.spnNroAlternativas.setMinValue(new BigDecimal(2));
        defPrueba.spnNroAlternativas.setMaxValue(new BigDecimal(5));
        
        defPrueba.spnNroPreguntas.setMinValue(new BigDecimal(1));
        defPrueba.spnNroPreguntas.setMaxValue(new BigDecimal(75));

        defPrueba.spnNroPreguntas.numberProperty().addListener(new ChangeListener<BigDecimal>() {
            @Override
            public void changed(ObservableValue<? extends BigDecimal> observable, BigDecimal oldValue,
                    BigDecimal newValue) {
                if (oldValue == null) {
                    for (int n = 0; n < newValue.intValue(); n++)
                        defPrueba.lstPreguntas.getItems().add(new ItemList(n + 1));
                } else if (oldValue.intValue() > newValue.intValue()) {
                    int n = oldValue.intValue() - 1;
                    while (n >= newValue.intValue()) {
                        int idx = defPrueba.lstPreguntas.getItems().size() - 1;
                        defPrueba.lstPreguntas.getItems().remove(idx);
                        n--;
                    }
                } else if (oldValue.intValue() < newValue.intValue()) {
                    for (int n = oldValue.intValue(); n < newValue.intValue(); n++)
                        defPrueba.lstPreguntas.getItems().add(new ItemList(n + 1));
                }
            }
        });

        defPrueba.spnNroAlternativas.numberProperty().addListener(new ChangeListener<BigDecimal>() {
            @Override
            public void changed(ObservableValue<? extends BigDecimal> observable, BigDecimal oldValue,
                    BigDecimal newValue) {
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
            }
        });
        
        assignValues(defPrueba);
        
        defPrueba.cmbAsignatura.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("idAsignatura", defPrueba.cmbAsignatura.getSelectionModel().getSelectedItem().getId());
                defPrueba.getController().find("EjeTematico.findByAsigntura", parameters, defPrueba);
                
                parameters = new HashMap<>();
                if (defPrueba.cmbCurso.getSelectionModel().getSelectedItem() != null) {
                    
                    asinarNombrePrueba(defPrueba);
                    defPrueba.cmbObjetivos.getItems().clear();
                    parameters.put("asignaturaId",
                            defPrueba.cmbAsignatura.getSelectionModel().getSelectedItem().getId());
                    parameters.put("tipoCursoId", defPrueba.cmbCurso.getSelectionModel().getSelectedItem().getId());
                    defPrueba.getController().find("Objetivo.findByTipoCursoAsignatura", parameters, defPrueba);
                }
            }
        });
       
        defPrueba.cmbCurso.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Map<String, Object> parameters = new HashMap<>();
                if (defPrueba.cmbAsignatura.getSelectionModel().getSelectedItem() != null) {
                    asinarNombrePrueba(defPrueba);
                    defPrueba.cmbObjetivos.getItems().clear();
                    parameters.put("asignaturaId",
                            defPrueba.cmbAsignatura.getSelectionModel().getSelectedItem().getId());
                    parameters.put("tipoCursoId", defPrueba.cmbCurso.getSelectionModel().getSelectedItem().getId());
                    defPrueba.getController().find("Objetivo.findByTipoCursoAsignatura", parameters, defPrueba);
                }

            }
        });

        defPrueba.mnuEliminarPregunta.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int idx = defPrueba.lstPreguntas.getSelectionModel().getSelectedIndex();
                if(idx == -1)
                    return;
                defPrueba.lstPreguntas.getItems().remove(idx);
                while(idx < defPrueba.lstPreguntas.getItems().size())
                {
                    ItemList item = defPrueba.lstPreguntas.getItems().get(idx);
                    item.nro = idx + 1;
                    idx++;
                }
            }
        });
        
        initializeImages(defPrueba);
        initializeList(defPrueba);
        initializeInteraction(defPrueba);
    }
    protected static void asinarNombrePrueba(DefinirPrueba defPrueba) {
        
        String asign = defPrueba.cmbAsignatura.getValue().getName().replace(" ", "").replace(".", "_").toUpperCase();
        String curso = defPrueba.cmbCurso.getValue().getName().replace(" ", "").replace(".", "_").toUpperCase();
        String name =  String.format("%s%s%04d.%02d%02d", asign, curso, defPrueba.fecFeha.getValue().getYear(), defPrueba.fecFeha.getValue().getMonthValue(), defPrueba.fecFeha.getValue().getDayOfMonth());
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
        name = normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        
        defPrueba.txtNombre.setText(name);
        
    }
    private static void initializeInteraction(DefinirPrueba defPrueba) {
        defPrueba.txtPregunta.textProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            selected.question =newValue;
        });
        defPrueba.txtAlternativaA.textProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            selected.alternatives.set(0, newValue);
        });
        defPrueba.txtAlternativaB.textProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            selected.alternatives.set(1, newValue);
        });
        defPrueba.txtAlternativaC.textProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            selected.alternatives.set(2, newValue);
        });
        defPrueba.txtAlternativaD.textProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            selected.alternatives.set(3, newValue);
        });
        defPrueba.txtAlternativaE.textProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            selected.alternatives.set(4, newValue);
        });
        defPrueba.img1.imageProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            selected.images.set(0, newValue);
        });
        defPrueba.img2.imageProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            selected.images.set(1, newValue);
        });
        defPrueba.img3.imageProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            selected.images.set(2, newValue);
        });
        defPrueba.img4.imageProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            selected.images.set(3, newValue);
        });
        defPrueba.img5.imageProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            selected.images.set(4, newValue);
        });
        defPrueba.cmbEjesTematicos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            selected.thematic = newValue;
        });
        defPrueba.cmbHabilidades.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            selected.ability = newValue;
        });
        defPrueba.cmbObjetivos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            selected.objetive = newValue;
        });
        defPrueba.chkOpcionA.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            if(newValue.booleanValue())
                selected.rightAnswer = "A";
        });
        defPrueba.chkOpcionB.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            if(newValue.booleanValue())
                selected.rightAnswer = "B";
        });
        defPrueba.chkOpcionC.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            if(newValue.booleanValue())
                selected.rightAnswer = "C";
        });
        defPrueba.chkOpcionD.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            if(newValue.booleanValue())
                selected.rightAnswer = "D";
        });
        defPrueba.chkOpcionE.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            if(newValue.booleanValue())
                selected.rightAnswer = "E";
        });
        defPrueba.chkOpcionV.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            if(newValue.booleanValue())
                selected.rightAnswer = "V";
        });
        defPrueba.chkOpcionF.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            if(newValue.booleanValue())
                selected.rightAnswer = "F";
        });
        defPrueba.chkOpcionMental.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(selected == null) return;
            if(newValue.booleanValue())
                selected.rightAnswer = "M";
        });
        
    }

    private static void initializeList(DefinirPrueba defPrueba) {
        defPrueba.lstPreguntas.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ItemList>() {
            @Override
            public void changed(ObservableValue<? extends ItemList> observable, ItemList oldValue, ItemList newValue) {
                
                //clearQuestion(defPrueba);
                selected = newValue;
                defPrueba.txtPregunta.setText(selected.question);
                defPrueba.cmbEjesTematicos.getSelectionModel().select(selected.thematic);
                defPrueba.cmbHabilidades.getSelectionModel().select(selected.ability);
                defPrueba.cmbObjetivos.getSelectionModel().select(selected.objetive);
                if (selected.alternatives != null && !selected.alternatives.isEmpty()) {
                    for (int n = 0; n < selected.alternatives.size(); n++)
                        alternatives[n].setText(selected.alternatives.get(n));
                }
                if (selected.images != null && !selected.images.isEmpty()) {
                    for (int n = 0; n < selected.alternatives.size(); n++)
                        images[n].setImage(selected.images.get(n));
                }
                String ranswer = selected.rightAnswer;
                if(ranswer == null ||  ranswer.isEmpty())
                    return;
                defPrueba.chkOpcionA.setSelected(ranswer.equals("A"));
                defPrueba.chkOpcionB.setSelected(ranswer.equals("B"));
                defPrueba.chkOpcionC.setSelected(ranswer.equals("C"));
                defPrueba.chkOpcionD.setSelected(ranswer.equals("D"));
                defPrueba.chkOpcionE.setSelected(ranswer.equals("E"));
                defPrueba.chkOpcionV.setSelected(ranswer.equals("V"));
                defPrueba.chkOpcionF.setSelected(ranswer.equals("E"));
                defPrueba.chkOpcionMental.setSelected(ranswer.equals("M"));
            }
        });
    }

    
    private static void initializeImages(DefinirPrueba defPrueba) {
        EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    FileChooser fc = new FileChooser();
                    fc.setTitle("Seleccione imagen");
                    fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Todas las imagénes", "*.*"),
                            new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                            new FileChooser.ExtensionFilter("PNG", "*.png"));
                    File file = fc.showOpenDialog(null);
                    if (file == null)
                        return;

                    try {
                        ImageView img = (ImageView) event.getSource();
                        Image image;
                        image = new Image(file.toURI().toURL().toString());
                        img.setImage(image);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
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
 
    
    private static void assignValues(DefinirPrueba defPrueba)
    {
        if(defPrueba.prueba == null)
        {
            defPrueba.spnNroPreguntas.setNumber(new BigDecimal(25));
            defPrueba.spnNroAlternativas.setNumber(new BigDecimal(5));
            defPrueba.spnPjeBase.setNumber(new BigDecimal(1));
            defPrueba.spnForma.setNumber(new BigDecimal(1));
            defPrueba.spnExigencia.setNumber(new BigDecimal(60));
            defPrueba.fecFeha.setValue(LocalDate.now());
        }
        else
        {
//            Prueba prueba =  defPrueba.prueba;
//            defPrueba.cmbAsignatura.;
//            defPrueba.cmbProfesor;
//            defPrueba.spnExigencia;
//            defPrueba.spnPjeBase;
//            defPrueba.cmbNivelEvaluacion;
//            defPrueba.cmbTipoPrueba;
//            defPrueba.cmbCurso;
//            defPrueba.txtNombre;
//            defPrueba.spnNroAlternativas;
//            defPrueba.spnNroPreguntas;
//            defPrueba.spnForma;
//            defPrueba.fecFeha;
//            
//            ListView<ItemList> lstPreguntas;
            
        }
        
    }
}
