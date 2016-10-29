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
    private int nroAlternativas;

    public MenuGrabarListener(DefinirPrueba defPrueba) {
        this.defPrueba = defPrueba;
    }

    @Override
    public void handle(ActionEvent event) {

        name = defPrueba.txtNombre.getText();
        idAsignatura = defPrueba.cmbAsignatura.getValue().getId();
        idCurso = defPrueba.cmbAsignatura.getValue().getId();
        nroAlternativas = defPrueba.spnNroAlternativas.getNumber().intValue();

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
        prueba.setAlternativas(nroAlternativas);
        prueba.setTipoPrueba(defPrueba.cmbTipoPrueba.getValue());
        prueba.setExigencia(defPrueba.spnExigencia.getNumber().intValue());

        final ObservableList<ItemList> items = defPrueba.lstPreguntas.getItems();
        // List<RespuestasEsperadasPrueba> lstRespuestas = new ArrayList<>();
        String respuestas = "";

        for (final ItemList item : items) {
            respuestas = respuestas + item.rightAnswer;
        }
        prueba.setResponses(respuestas);

        prueba = (Prueba) defPrueba.save(prueba);

        for (final ItemList item : items) {
            final String itemName = String.format("%d", item.nro);

            final boolean isMental = item.rightAnswer.equals("M");
            final boolean isTrueFalse = "VF".contains(item.rightAnswer.toUpperCase());

            RespuestasEsperadasPrueba respuesta = new RespuestasEsperadasPrueba.Builder().anulada(false)
                    .ejeTematico(item.thematic).habilidad(item.ability).mental(isMental).name(itemName).numero(item.nro)
                    .objetivo(item.objetive).respuesta(item.rightAnswer).verdaderoFalso(isTrueFalse).prueba(prueba)
                    .build();
            final List<Imagenes> lstImages = processImages(item, respuesta);
            final List<Alternativas> lstAlternativas = processAlteratives(item, respuesta);

            respuesta = (RespuestasEsperadasPrueba) defPrueba.save(respuesta);

            // respuesta.setAlternativas(lstAlternativas);

            for (int n = 0; n < lstAlternativas.size(); n++) {
                Alternativas alt = lstAlternativas.get(n);
                alt.setRespuesta(respuesta);
                alt = (Alternativas) defPrueba.save(alt);
                lstAlternativas.set(n, alt);
            }

            if (lstImages != null && !lstImages.isEmpty()) {
                for (int n = 0; n < lstImages.size(); n++) {
                    Imagenes img = lstImages.get(n);
                    img.setRespuesta(respuesta);
                    img = (Imagenes) defPrueba.save(img);
                    lstImages.set(n, img);
                }
            }

            // respuesta.setImagenes(lstImages);
            // lstRespuestas.add(respuesta);
        }
        // prueba.setRespuestas(lstRespuestas);

    }

    private List<Alternativas> processAlteratives(ItemList item, RespuestasEsperadasPrueba respuesta) {
        List<Alternativas> lstAlternatives = null;
        for (int n = 0; n < nroAlternativas; n++) {
            if (lstAlternatives == null)
                lstAlternatives = new ArrayList<>();
            final String altName = String.format("%d_%d", item.nro, n + 1);
            final Alternativas alternative = new Alternativas.Builder().name(altName).numero(n).texto(item.question)
                    .respuesta(respuesta).build();
            lstAlternatives.add(alternative);
        }
        return lstAlternatives;
    }

    private List<Imagenes> processImages(ItemList item, RespuestasEsperadasPrueba respuesta) {
        List<Imagenes> lstImages = null;
        for (int n = 0; n < item.images.size(); n++) {
            if (item.images.get(n) == null)
                break;
            final Image img = item.images.get(n);
            final BufferedImage bimg = SwingFXUtils.fromFXImage(img, null);
            final BufferedImage scaledImg = Scalr.resize(bimg, Method.ULTRA_QUALITY, Mode.AUTOMATIC, 512, 512,
                    Scalr.OP_ANTIALIAS);

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
                final Imagenes image = new Imagenes.Builder().numero(item.nro).name(fName).respuesta(respuesta).build();
                lstImages.add(image);
            }
        }
        return lstImages;
    }

}
