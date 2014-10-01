package cl.eos.imp.view;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import cl.eos.interfaces.view.IView;
import cl.eos.interfaces.view.IWindowManager;

public class WindowManager implements IWindowManager {

	private Pane root;
	private Stage mainStage;
	private Group group;

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
			w = new WindowsView();
			w.setView(window);
			w.setId(window.getName());
			w.setText(window.getTitle());
			w.setContent((Parent) window.getPanel());
			w.setVisible(true);
			group.getChildren().setAll(w);
			
		}
	}

	@Override
	public void hide(IView window) {
		group.getChildren().remove(window);
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
			//w.setVisible(false);
			this.root.getChildren().setAll(w);
		} else if (root instanceof Stage) {
			mainStage = (Stage) root;
			stage.initOwner(mainStage);
			stage.initModality(Modality.NONE);
			// stage.centerOnScreen();
			stage.initStyle(StageStyle.UTILITY);
		} else if (root instanceof Group) {
			this.group = (Group) root;
			//w.setVisible(false);
//			group.getChildren().add(w);
		}
	}
}
