package cl.eos.view.dnd;

import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import cl.eos.persistence.models.Habilidad;
import cl.eos.view.RegistroDefinePrueba;

public class HabilidadDND extends TableCell<RegistroDefinePrueba, Habilidad> {
  public static final DataFormat habilidadTrackDataFormat = new DataFormat(
      "cl.eos.persistence.Habilidad");

  public HabilidadDND() {
    setOnDragDropped(new EventHandler<DragEvent>() {

      @Override
      public void handle(DragEvent dragEvent) {
        RegistroDefinePrueba myModel = getTableView().getItems().get(getTableRow().getIndex());
        if (myModel != null) {
          Habilidad habilidad =
              (Habilidad) dragEvent.getDragboard().getContent(habilidadTrackDataFormat);
          myModel.setHabilidad(habilidad);
        }
      }
    });

    setOnDragEntered(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent dragEvent) {
        HabilidadDND.this.setBlendMode(BlendMode.DARKEN);
      }
    });

    setOnDragExited(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent dragEvent) {
        HabilidadDND.this.setBlendMode(null);
      }
    });

    setOnDragOver(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent dragEvent) {
         if (dragEvent.getDragboard().getContent(habilidadTrackDataFormat) != null)
         {
          dragEvent.acceptTransferModes(TransferMode.COPY);
        }

      }
    });
  }

  @Override
  public void updateItem(Habilidad t, boolean empty) {
    super.updateItem(t, empty);
    if (t == null) {
      setTooltip(null);
      setText(null);
    } else {
      // RegistroDefinePrueba myModel = getTableView().getItems().get(getTableRow().getIndex());
      setText(t.toString());
    }
  }


}
