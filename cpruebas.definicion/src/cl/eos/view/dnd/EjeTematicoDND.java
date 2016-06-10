package cl.eos.view.dnd;

import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.view.RegistroDefinePrueba;

public class EjeTematicoDND extends TableCell<RegistroDefinePrueba, EjeTematico> {
  public static final DataFormat ejeTematicoTrackDataFormat = new DataFormat(
      "cl.eos.persistence.EjeTematico");

  public EjeTematicoDND() {
    setOnDragDropped(new EventHandler<DragEvent>() {

      @Override
      public void handle(DragEvent dragEvent) {
        RegistroDefinePrueba myModel = getTableView().getItems().get(getTableRow().getIndex());
        if (myModel != null) {
          EjeTematico ejeTematico =
              (EjeTematico) dragEvent.getDragboard().getContent(ejeTematicoTrackDataFormat);
          myModel.setEjeTematico(ejeTematico);
        }
      }
    });

    setOnDragEntered(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent dragEvent) {
        EjeTematicoDND.this.setBlendMode(BlendMode.DARKEN);
      }
    });

    setOnDragExited(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent dragEvent) {
        EjeTematicoDND.this.setBlendMode(null);
      }
    });

    setOnDragOver(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent dragEvent) {
        if (dragEvent.getDragboard().getContent(ejeTematicoTrackDataFormat) != null) {
          dragEvent.acceptTransferModes(TransferMode.COPY);
        }
      }
    });
  }

  @Override
  public void updateItem(EjeTematico t, boolean empty) {
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
