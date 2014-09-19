package cl.eos.view.dnd;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.Habilidad;
import cl.eos.view.RegistroDefinePrueba;

public class TableDND extends TableCell<RegistroDefinePrueba, Object> {

  public TableDND() {
    setOnDragDropped(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent dragEvent) {
        ObservableList<RegistroDefinePrueba> seleccionados =
            getTableView().getSelectionModel().getSelectedItems();
        if (seleccionados != null && !seleccionados.isEmpty()) {
          if (dragEvent.getDragboard().getContent(EjeTematicoDND.ejeTematicoTrackDataFormat) != null) {
            EjeTematico ejeTematico =
                (EjeTematico) dragEvent.getDragboard().getContent(
                    EjeTematicoDND.ejeTematicoTrackDataFormat);
            for (RegistroDefinePrueba registro : seleccionados) {
              registro.setEjeTematico(ejeTematico);
            }
          } else if (dragEvent.getDragboard().getContent(HabilidadDND.habilidadTrackDataFormat) != null) {
            Habilidad habilidad =
                (Habilidad) dragEvent.getDragboard().getContent(
                    HabilidadDND.habilidadTrackDataFormat);
            for (RegistroDefinePrueba registro : seleccionados) {
              registro.setHabilidad(habilidad);
            }
          }
        }
      }
    });

    setOnDragEntered(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent dragEvent) {
        TableDND.this.setBlendMode(BlendMode.DARKEN);
      }
    });

    setOnDragExited(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent dragEvent) {
        TableDND.this.setBlendMode(null);
      }
    });

    setOnDragOver(new EventHandler<DragEvent>() {
      @Override
      public void handle(DragEvent dragEvent) {
        if (dragEvent.getDragboard().getContent(EjeTematicoDND.ejeTematicoTrackDataFormat) != null
            || dragEvent.getDragboard().getContent(HabilidadDND.habilidadTrackDataFormat) != null) {
          dragEvent.acceptTransferModes(TransferMode.COPY);
        }
      }
    });
  }

  @Override
  public void updateItem(Object t, boolean empty) {
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
