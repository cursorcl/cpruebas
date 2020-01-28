package cl.eos.imp.view;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import jfxtras.labs.scene.control.BreadcrumbBar;
import jfxtras.labs.scene.control.BreadcrumbItem;
import jfxtras.labs.util.BreadcrumbBarEventHandler;
import cl.eos.interfaces.view.IView;
import cl.eos.interfaces.view.IWindowManager;

public class WindowManager2 implements IWindowManager {

	private Pane root;
	private Group group;

	private BreadcrumbBar breadCrum;
	private static WindowManager2 instance = null;

	private WindowManager2() {
		// secondStage.initStyle(StageStyle.UNDECORATED);
		// secondStage.initStyle(StageStyle.TRANSPARENT);
	}

	public static WindowManager2 getInstance() {
		if (instance == null) {
			instance = new WindowManager2();
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
			// breadCrum.removeItem(breadCrum.itemsProperty().size() - 1);
			BreadcrumbItem item = null;
			int n = 0;
			for (n = 0; n < breadCrum.itemsProperty().getSize(); n++) {
				BreadcrumbItem bItem = breadCrum.itemsProperty().get(n);
				if (bItem.getText().equals(window.getTitle())) {
					item = bItem;
					break;
				}
			}
			if (item != null) {
				while (breadCrum.itemsProperty().getSize() > (n+1)) {
					breadCrum
							.removeItem(breadCrum.itemsProperty().getSize() - 1);
				}
			} else {
				breadCrum.addItem(window.getTitle(), w);
			}
			
			group.getChildren().setAll(w);
		}
	}

	@Override
	public void hide(IView window) {
		for (int n = 0; n < breadCrum.itemsProperty().size(); n++) {
			BreadcrumbItem b = breadCrum.itemsProperty().get(n);
			if (b.getText() != null && b.getText().equals(window.getTitle())) {
				breadCrum.removeItem(n);
				group.getChildren().remove(b.getContent());
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
		if (root instanceof Group) {
			this.group = (Group) root;
		}
	}

	@Override
	public Object getBreadcrumbBar() {
		return breadCrum;
	}

	@Override
	public void setBreadcrumbBar(Object breadCrumb) {
		this.breadCrum = (BreadcrumbBar) breadCrumb;
		this.breadCrum
				.setOnItemAction(new BreadcrumbBarEventHandler<BreadcrumbItem>() {

					@Override
					public void handle(MouseEvent event) {
						BreadcrumbItem item = (BreadcrumbItem) event
								.getSource();
						Node node = item.getContent();
						group.getChildren().setAll(node);
					}
				});
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
			breadCrum.addHome(w);
		}
	}

}
