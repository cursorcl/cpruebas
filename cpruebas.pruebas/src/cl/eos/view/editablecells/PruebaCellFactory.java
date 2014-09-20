package cl.eos.view.editablecells;

import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.util.Callback;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.Prueba.Estado;

public class PruebaCellFactory implements
    Callback<TableColumn<Prueba, Estado>, TableCell<Prueba, Estado>> {

  @Override
  public TableCell<Prueba, Estado> call(TableColumn<Prueba, Estado> p) {

    TableCell<Prueba, Estado> cell = new TableCell<Prueba, Estado>() {
      @SuppressWarnings("unchecked")
      @Override
      public void updateItem(Estado item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? null : getString());
        setGraphic(null);
        TableRow<Prueba> currentRow = getTableRow();
        Prueba currentPrueba = currentRow == null ? null : (Prueba) currentRow.getItem();
        if (currentPrueba != null) {
          Estado estado = currentPrueba.getEstado();
          clearPriorityStyle();
          if (!isHover() && !isSelected() && !isFocused()) {
            setEstadoStyle(estado);
          }
        }
      }

      @Override
      public void updateSelected(boolean upd) {
        super.updateSelected(upd);
      }

      private void clearPriorityStyle() {
        ObservableList<String> styleClasses = getStyleClass();
        styleClasses.remove("priorityLow");
        styleClasses.remove("priorityMedium");
        styleClasses.remove("priorityHigh");
      }

      private void setEstadoStyle(Estado priority) {
        switch (priority) {
          case CREADA:
            getStyleClass().add("priorityLow");
            break;
          case DEFINIDA:
            getStyleClass().add("priorityMedium");
            break;
          case EVALUADA:
            getStyleClass().add("priorityHigh");
            break;
        }
      }

      private String getString() {
        return getItem() == null ? "" : getItem().toString();
      }
    };
    return cell;
  }
}
