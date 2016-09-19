package cl.eos.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;

import cl.eos.persistence.models.Alternativas;
import cl.eos.persistence.models.Imagenes;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.RespuestasEsperadasPrueba;
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

    public MenuGrabarListener(DefinirPrueba defPrueba) {
        this.defPrueba = defPrueba;
    }

    @Override
    public void handle(ActionEvent event) {

        name = defPrueba.txtNombre.getText();
        idAsignatura = defPrueba.cmbAsignatura.getValue().getId();
        idCurso = defPrueba.cmbAsignatura.getValue().getId();

        Prueba prueba = new Prueba();

        prueba.setAsignatura(defPrueba.cmbAsignatura.getValue());
        prueba.setCurso(defPrueba.cmbCurso.getValue());
        prueba.setFecha(defPrueba.fecFeha.getValue().toEpochDay());
        prueba.setNroFormas(defPrueba.spnForma.getNumber().intValue());
        prueba.setName(name);
        prueba.setNivelEvaluacion(defPrueba.cmbNivelEvaluacion.getValue());
        prueba.setProfesor(defPrueba.cmbProfesor.getValue());
        prueba.setPuntajeBase(defPrueba.spnPjeBase.getNumber().intValue());
        prueba.setNroPreguntas(defPrueba.spnNroPreguntas.getNumber().intValue());
        prueba.setAlternativas(defPrueba.spnNroAlternativas.getNumber().intValue());
        prueba.setTipoPrueba(defPrueba.cmbTipoPrueba.getValue());
        prueba.setExigencia(defPrueba.spnExigencia.getNumber().intValue());


        ObservableList<ItemList> items = defPrueba.lstPreguntas.getItems();
        List<RespuestasEsperadasPrueba> lstRespuestas = new ArrayList<>();
        String respuestas = "";

        for (ItemList item : items) {
            respuestas = respuestas + item.rightAnswer;
        }
        prueba.setResponses(respuestas);

        for (ItemList item : items) {
            String itemName = String.format("%d", item.nro);

            boolean isMental = item.rightAnswer.equals("M");
            boolean isTrueFalse = "VF".contains(item.rightAnswer.toUpperCase());

            RespuestasEsperadasPrueba respuesta = new RespuestasEsperadasPrueba.Builder().anulada(false)
                    .ejeTematico(item.thematic).habilidad(item.ability).mental(isMental).name(itemName).numero(item.nro)
                    .objetivo(item.objetive).respuesta(item.rightAnswer).verdaderoFalso(isTrueFalse).prueba(prueba)
                    .build();
            List<Imagenes> lstImages = processImages(item, respuesta);
            List<Alternativas> lstAlternativas = processAlteratives(item, respuesta);



            respuesta.setAlternativas(lstAlternativas);
            respuesta.setImagenes(lstImages);
            lstRespuestas.add(respuesta);
        }
        prueba.setRespuestas(lstRespuestas);
        
        defPrueba.save(prueba);
    }

    private List<Alternativas> processAlteratives(ItemList item, RespuestasEsperadasPrueba respuesta) {
        List<Alternativas> lstAlternatives = null;
        for (int n = 0; n < item.alternatives.size(); n++) {
            if(lstAlternatives == null)
                lstAlternatives = new ArrayList<>();
            String altName = String.format("%d_%d", item.nro, (n + 1));
            Alternativas alternative = new Alternativas.Builder().name(altName).numero(n).texto(item.question).respuesta(respuesta).build();
            lstAlternatives.add(alternative);
        }
        return lstAlternatives;
    }

    private List<Imagenes> processImages(ItemList item, RespuestasEsperadasPrueba respuesta) {
        List<Imagenes> lstImages = null;
        for (int n = 0; n < item.images.size(); n++) {
            if (item.images.get(n) == null)
                break;
            Image img = item.images.get(n);
            BufferedImage bimg = SwingFXUtils.fromFXImage(img, null);
            BufferedImage scaledImg = Scalr.resize(bimg, Method.ULTRA_QUALITY, Mode.AUTOMATIC, 512, 512,
                    Scalr.OP_ANTIALIAS);

            String dirName = Utils.getDefaultDirectory() + "/images/." + name;
            File theDir = new File(dirName);
            if (!theDir.exists())
                theDir.mkdirs();

            if (theDir.exists()) {
                String fName = String.format("%02d_%02d_%02d_%02d.png", idAsignatura, idCurso, item.nro, n);
                String fileName = String.format("%s/%s", dirName, fName);

                try {
                    ImageIO.write(scaledImg, "png", new File(fileName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(lstImages == null)
                    lstImages = new ArrayList<>();
                Imagenes image = new Imagenes.Builder().numero(item.nro).name(fName).respuesta(respuesta).build();
                lstImages.add(image);
            }
        }
        return lstImages;
    }

}
