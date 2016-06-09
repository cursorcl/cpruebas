package cl.eos.view.dnd;

import cl.eos.persistence.models.Objetivo;
import cl.eos.view.RegistroDefinePrueba;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

public class ObjetivoDND extends TableCell<RegistroDefinePrueba, Objetivo> {
  public static final DataFormat objetivoTrackDataFormat = new DataFormat(
      "cl.eos.persistence.Objetivo");

  public ObjetivoDND() {
    setOnDragDropped(new EventHandler<DragEvent>() {

      @Override
      public void handle(DragEvent dragEvent) {
        RegistroDefinePrueba myModel = getTableView().getItems().get(getTableRow().getIndex());
        if (myModel != null) {
        	Objetivo objetivo =
              (Objetivo) dragEvent.getDragboard().getContent(objetivoTrackDataFormat);
          myModel.setObjetivo(objetivo);
        }
      }
    });

    setOnDragEntered(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent dragEvent) {
        ObjetivoDND.this.setBlendMode(BlendMode.DARKEN);
      }
    });

    setOnDragExited(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent dragEvent) {
        ObjetivoDND.this.setBlendMode(null);
      }
    });

    setOnDragOver(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent dragEvent) {
        if (dragEvent.getDragboard().getContent(objetivoTrackDataFormat) != null) {
          dragEvent.acceptTransferModes(TransferMode.COPY);
        }
      }
    });
  }

  @Override
  public void updateItem(Objetivo t, boolean empty) {
    super.updateItem(t, empty);
    if (t == null) {
      setTooltip(null);
      setText(null);
    } else {
      setText(t.toString());
    }
  }


}
