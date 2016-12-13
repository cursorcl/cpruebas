package cl.eos.view.dnd;

import cl.eos.persistence.models.SEjeTematico;
import cl.eos.view.RegistroDefinePrueba;
import javafx.scene.control.TableCell;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;

public class EjeTematicoDND extends TableCell<RegistroDefinePrueba, SEjeTematico> {
    public static final DataFormat ejeTematicoTrackDataFormat = new DataFormat("cl.eos.persistence.EjeTematico");

    public EjeTematicoDND() {
        setOnDragDropped(dragEvent -> {
            final RegistroDefinePrueba myModel = getTableView().getItems().get(getTableRow().getIndex());
            if (myModel != null) {
                final SEjeTematico ejeTematico = (SEjeTematico) dragEvent.getDragboard()
                        .getContent(EjeTematicoDND.ejeTematicoTrackDataFormat);
                myModel.setEjeTematico(ejeTematico);
            }
        });

        setOnDragEntered(dragEvent -> EjeTematicoDND.this.setBlendMode(BlendMode.DARKEN));

        setOnDragExited(dragEvent -> EjeTematicoDND.this.setBlendMode(null));

        setOnDragOver(dragEvent -> {
            if (dragEvent.getDragboard().getContent(EjeTematicoDND.ejeTematicoTrackDataFormat) != null) {
                dragEvent.acceptTransferModes(TransferMode.COPY);
            }
        });
    }

    @Override
    public void updateItem(SEjeTematico t, boolean empty) {
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
