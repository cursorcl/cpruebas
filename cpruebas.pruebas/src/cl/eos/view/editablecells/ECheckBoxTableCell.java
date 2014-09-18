package cl.eos.view.editablecells;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;

public class ECheckBoxTableCell<S, T> extends TableCell<S, T> {
  private final CheckBox checkBox;
  private ObservableValue<T> ov;

  public ECheckBoxTableCell() {
    this.checkBox = new CheckBox();
    this.checkBox.setAlignment(Pos.CENTER);
    setAlignment(Pos.CENTER);
    setGraphic(checkBox);
  }

  public void setOnAction(EventHandler<ActionEvent> actionListener)
  {
      this.checkBox.setOnAction(actionListener);
  }
  
  @Override
  public void updateItem(T item, boolean empty) {
    super.updateItem(item, empty);
    if (empty) {
      setText(null);
      setGraphic(null);
    } else {
      setGraphic(checkBox);
      if (ov instanceof BooleanProperty) {
        checkBox.selectedProperty().unbindBidirectional((BooleanProperty) ov);
      }
      ov = getTableColumn().getCellObservableValue(getIndex());
      if (ov instanceof BooleanProperty) {
        checkBox.selectedProperty().bindBidirectional((BooleanProperty) ov);
      }
    }
  }
}