package cl.eos.imp.view;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.labs.scene.control.BreadcrumbBar;
import jfxtras.labs.scene.control.BreadcrumbItem;
import jfxtras.labs.util.BreadcrumbBarEventHandler;
import cl.eos.interfaces.view.IView;
import cl.eos.interfaces.view.IWindowManager;

public class WindowManager implements IWindowManager {

	private Pane root;
	private Stage mainStage;
	private Group group;

	private BreadcrumbBar breadCrum;
	private static WindowManager instance = null;
	private WindowsView w = new WindowsView();

	private Stage stage = new Stage();

	private WindowManager() {
		// secondStage.initStyle(StageStyle.UNDECORATED);
		// secondStage.initStyle(StageStyle.TRANSPARENT);
	}

	public static WindowManager getInstance() {
		if (instance == null) {
			instance = new WindowManager();
		}
		return instance;
	}

	@Override
	public void show(IView window) {
		if (group != null) {
			w.setId(window.getName());
			w.setText(window.getTitle());
			w.setContent((Parent) window.getPanel());
			w.setVisible(true);
			breadCrum.addItem(window.getTitle(), w);
			
		}
	}

	@Override
	public void hide(IView window) {
		group.getChildren().remove(window);
		for (int n = 0; n < breadCrum.itemsProperty().size(); n++) {
			BreadcrumbItem b = breadCrum.itemsProperty().get(n);
			if (b.getText() != null && b.getText().equals(window.getTitle())) {
				breadCrum.removeItem(n);
				break;
			}
		}
	}

	@Override
	public void hideAll() {
		for (Node node : root.getChildren()) {
			root.getChildren().remove(node);
		}
	}

	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public void setRoot(Object root) throws Exception {
		if (root instanceof Pane) {
			this.root = (Pane) root;
			w.setVisible(false);
			this.root.getChildren().setAll(w);
		} else if (root instanceof Stage) {
			mainStage = (Stage) root;
			stage.initOwner(mainStage);
			stage.initModality(Modality.NONE);
			// stage.centerOnScreen();
			stage.initStyle(StageStyle.UTILITY);
		} else if (root instanceof Group) {
			this.group = (Group) root;
			w.setVisible(false);
			group.getChildren().add(w);
		}
	}

	@Override
	public Object getBreadcrumbBar() {
		return breadCrum;
	}

	@Override
	public void setBreadcrumbBar(Object breadCrumb) {
		this.breadCrum = (BreadcrumbBar) breadCrumb;
		this.breadCrum.setOnItemAction(new BreadcrumbBarEventHandler<BreadcrumbItem>() {

			@Override
			public void handle(MouseEvent event) {
				
			}
		});
	}
}
