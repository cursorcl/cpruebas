package cl.eos.imp.view;

import cl.eos.interfaces.view.IView;
import cl.eos.interfaces.view.IWindowManager;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import jfxtras.labs.scene.control.BreadcrumbBar;
import jfxtras.labs.scene.control.BreadcrumbItem;

public class WindowManager implements IWindowManager {

    private static WindowManager instance = null;

    public static WindowManager getInstance() {
        if (WindowManager.instance == null) {
            WindowManager.instance = new WindowManager();
        }
        return WindowManager.instance;
    }

    private Pane root;
    private Group group;

    private BreadcrumbBar breadCrum;

    private WindowManager() {
        // secondStage.initStyle(StageStyle.UNDECORATED);
        // secondStage.initStyle(StageStyle.TRANSPARENT);
    }

    @Override
    public Object getBreadcrumbBar() {
        return breadCrum;
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public void hide(IView window) {
        for (int n = 0; n < breadCrum.itemsProperty().size(); n++) {
            final BreadcrumbItem b = breadCrum.itemsProperty().get(n);
            if (b.getText() != null && b.getText().equals(window.getTitle())) {
                breadCrum.removeItem(n);
                group.getChildren().remove(b.getContent());
                break;
            }
        }
    }

    @Override
    public void hideAll() {
        for (final Node node : root.getChildren()) {
            root.getChildren().remove(node);
        }
    }

    @Override
    public void setBreadcrumbBar(Object breadCrumb) {
        breadCrum = (BreadcrumbBar) breadCrumb;
        breadCrum.setOnItemAction(event -> {
            final BreadcrumbItem item = (BreadcrumbItem) event.getSource();
            final Node node = item.getContent();
            group.getChildren().setAll(node);
        });
    }

    @Override
    public void setHomeView(IView window) {
        if (group != null) {
            final WindowsView w = new WindowsView();
            w.setView(window);
            w.setId(window.getName());
            w.setText(window.getTitle());
            w.setContent((Parent) window.getPanel());
            w.setVisible(true);
            breadCrum.addHome(w);
        }
    }

    @Override
    public void setRoot(Object root) throws Exception {
        if (root instanceof Group) {
            group = (Group) root;
        }
    }

    @Override
    public void show(IView window) {
        if (group != null) {
            final WindowsView w = new WindowsView();
            w.setView(window);
            w.setId(window.getName());
            w.setText(window.getTitle());
            w.setContent((Parent) window.getPanel());
            w.setVisible(true);
            BreadcrumbItem item = null;
            int n = 0;
            for (n = 0; n < breadCrum.itemsProperty().getSize(); n++) {
                final BreadcrumbItem bItem = breadCrum.itemsProperty().get(n);
                if (bItem.getText().equals(window.getTitle())) {
                    item = bItem;
                    break;
                }
            }
            if (item != null) {
                while (breadCrum.itemsProperty().getSize() > n + 1) {
                    breadCrum.removeItem(breadCrum.itemsProperty().getSize() - 1);
                }
            } else {
                breadCrum.addItem(window.getTitle(), w);
            }

            group.getChildren().setAll(w);
        }
    }

    @Override
    public void showOver(IView window) {
        if (group != null) {
            final WindowsView w = new WindowsView();
            w.setView(window);
            w.setId(window.getName());
            w.setText(window.getTitle());
            w.setContent((Parent) window.getPanel());
            w.setVisible(true);
            BreadcrumbItem item = null;
            int n = 0;
            for (n = 0; n < breadCrum.itemsProperty().getSize(); n++) {
                final BreadcrumbItem bItem = breadCrum.itemsProperty().get(n);
                if (bItem.getText().equals(window.getTitle())) {
                    item = bItem;
                    break;
                }
            }
            if (item != null) {
                while (breadCrum.itemsProperty().getSize() > n + 1) {
                    breadCrum.removeItem(breadCrum.itemsProperty().getSize() - 1);
                }
            } else {
                breadCrum.addItem(window.getTitle(), w);
            }

            group.getChildren().add(w);
        }
    }

    @Override
    public void repaint() {
        if(group ==  null)
            return;
        group.requestLayout();
        
    }
    
    

}
