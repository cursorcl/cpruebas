package cl.eos.imp.view;

import cl.eos.interfaces.view.IView;
import cl.eos.interfaces.view.IWindowManager;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class WindowManager implements IWindowManager {

	private Pane root;
	private Group group;

	private static WindowManager instance = null;

	private WindowManager() {
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
			WindowsView w = new WindowsView();
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
		
		for(Node n: group.getChildren())
		{
			if(n instanceof WindowsView)
			{
				WindowsView w = (WindowsView)n;
				if(w.getView().equals(window))
				{
					root.getChildren().remove(n);
					break;
				}
			}
		}
//		for (int n = 0; n < breadCrum.itemsProperty().size(); n++) {
//			BreadcrumbItem b = breadCrum.itemsProperty().get(n);
//			if (b.getText() != null && b.getText().equals(window.getTitle())) {
//				breadCrum.removeItem(n);
//				group.getChildren().remove(b.getContent());
//				break;
//			}
//		}
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
		if (root instanceof Group) {
			this.group = (Group) root;
		}
	}

	@Override
	public void setHomeView(IView window) {
		if (group != null) {
			WindowsView w = new WindowsView();
			w.setView(window);
			w.setId(window.getName());
			w.setText(window.getTitle());
			w.setContent((Parent) window.getPanel());
			w.setVisible(true);
		}
	}

}
