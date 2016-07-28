package cl.eos.imp.view;


import cl.eos.interfaces.view.IView;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class WindowsView extends BorderPane {

	private Label windowTitle;
	private AnchorPane topPane;
	private IView view;

	public WindowsView() {
		setTop(getAnchorPane());
	}

	private AnchorPane getAnchorPane() {
		if (topPane == null) {
			topPane = new AnchorPane();
			topPane.setId("windows-title");

			AnchorPane.setRightAnchor(getLabel(), 20.0);
			AnchorPane.setTopAnchor(getLabel(), 4.0);
			AnchorPane.setLeftAnchor(getLabel(), 4.0);
			AnchorPane.setBottomAnchor(getLabel(), 8.0);
            topPane.getChildren().addAll(getLabel());
		}
		return topPane;
	}

	private Label getLabel() {
		if (windowTitle == null) {
			windowTitle = new Label();
			windowTitle.setStyle("-fx-alignment: CENTER;");
			windowTitle.setFont(new Font("System", 14));
			windowTitle.setTextFill(Color.WHITE);
		}
		return windowTitle;
	}

	public void setContent(Node node) {
		setCenter(node);
	}
	
	public void setText(String text)
	{
		getLabel().setText(text);
	}

	public IView getView() {
		return view;
	}

	public void setView(IView view) {
		this.view = view;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((windowTitle == null) ? 0 : windowTitle.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WindowsView other = (WindowsView) obj;
        if (windowTitle.getText() == null) {
            if (other.windowTitle.getText() != null)
                return false;
        } else if (!windowTitle.getText().equals(other.windowTitle.getText()))
            return false;
        return true;
    }
	
	
}
