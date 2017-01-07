package cl.eos.view.dnd;

import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_Habilidad;
import cl.eos.view.RegistroDefinePrueba;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.TransferMode;

public class TableDND extends TableCell<RegistroDefinePrueba, Object> {

    public TableDND() {
        setOnDragDropped(dragEvent -> {
            final ObservableList<RegistroDefinePrueba> seleccionados = getTableView().getSelectionModel()
                    .getSelectedItems();
            if (seleccionados != null && !seleccionados.isEmpty()) {
                if (dragEvent.getDragboard().getContent(EjeTematicoDND.ejeTematicoTrackDataFormat) != null) {
                    final R_Ejetematico ejeTematico = (R_Ejetematico) dragEvent.getDragboard()
                            .getContent(EjeTematicoDND.ejeTematicoTrackDataFormat);
                    for (final RegistroDefinePrueba registro1 : seleccionados) {
                        registro1.setEjeTematico(ejeTematico);
                    }
                } else if (dragEvent.getDragboard().getContent(HabilidadDND.habilidadTrackDataFormat) != null) {
                    final R_Habilidad habilidad = (R_Habilidad) dragEvent.getDragboard()
                            .getContent(HabilidadDND.habilidadTrackDataFormat);
                    for (final RegistroDefinePrueba registro2 : seleccionados) {
                        registro2.setHabilidad(habilidad);
                    }
                }
            }
        });

        setOnDragEntered(dragEvent -> TableDND.this.setBlendMode(BlendMode.DARKEN));

        setOnDragExited(dragEvent -> TableDND.this.setBlendMode(null));

        setOnDragOver(dragEvent -> {
            if (dragEvent.getDragboard().getContent(EjeTematicoDND.ejeTematicoTrackDataFormat) != null
                    || dragEvent.getDragboard().getContent(HabilidadDND.habilidadTrackDataFormat) != null) {
                dragEvent.acceptTransferModes(TransferMode.COPY);
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
            // RegistroDefinePrueba myModel =
            // getTableView().getItems().get(getTableRow().getIndex());
            setText(t.toString());
        }
    }

}
