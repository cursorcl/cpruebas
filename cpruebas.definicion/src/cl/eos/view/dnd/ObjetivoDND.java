package cl.eos.view.dnd;

import cl.eos.persistence.models.Objetivo;
import cl.eos.view.RegistroDefinePrueba;
import javafx.scene.control.TableCell;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;

public class ObjetivoDND extends TableCell<RegistroDefinePrueba, Objetivo> {
    public static final DataFormat objetivoTrackDataFormat = new DataFormat("cl.eos.persistence.Objetivo");

    public ObjetivoDND() {
        setOnDragDropped(dragEvent -> {
            final RegistroDefinePrueba myModel = getTableView().getItems().get(getTableRow().getIndex());
            if (myModel != null) {
                final Objetivo objetivo = (Objetivo) dragEvent.getDragboard()
                        .getContent(ObjetivoDND.objetivoTrackDataFormat);
                myModel.setObjetivo(objetivo);
            }
        });

        setOnDragEntered(dragEvent -> ObjetivoDND.this.setBlendMode(BlendMode.DARKEN));

        setOnDragExited(dragEvent -> ObjetivoDND.this.setBlendMode(null));

        setOnDragOver(dragEvent -> {
            if (dragEvent.getDragboard().getContent(ObjetivoDND.objetivoTrackDataFormat) != null) {
                dragEvent.acceptTransferModes(TransferMode.COPY);
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
