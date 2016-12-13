package cl.eos.view.dnd;

import cl.eos.persistence.models.SHabilidad;
import cl.eos.view.RegistroDefinePrueba;
import javafx.scene.control.TableCell;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;

public class HabilidadDND extends TableCell<RegistroDefinePrueba, SHabilidad> {
    public static final DataFormat habilidadTrackDataFormat = new DataFormat("cl.eos.persistence.Habilidad");

    public HabilidadDND() {
        setOnDragDropped(dragEvent -> {
            final RegistroDefinePrueba myModel = getTableView().getItems().get(getTableRow().getIndex());
            if (myModel != null) {
                final SHabilidad habilidad = (SHabilidad) dragEvent.getDragboard()
                        .getContent(HabilidadDND.habilidadTrackDataFormat);
                myModel.setHabilidad(habilidad);
            }
        });

        setOnDragEntered(dragEvent -> HabilidadDND.this.setBlendMode(BlendMode.DARKEN));

        setOnDragExited(dragEvent -> HabilidadDND.this.setBlendMode(null));

        setOnDragOver(dragEvent -> {
            if (dragEvent.getDragboard().getContent(HabilidadDND.habilidadTrackDataFormat) != null) {
                dragEvent.acceptTransferModes(TransferMode.COPY);
            }

        });
    }

    @Override
    public void updateItem(SHabilidad t, boolean empty) {
        super.updateItem(t, empty);
        if (t == null) {
            setTooltip(null);
            setText(null);
        } else {
            // RegistroDefinePrueba myModel =
            // getTableView().getItems().get(getTableRow().getIndex());
            setText(t.toString());
        }
    }

}
