package ot;

import java.util.List;

public class TitledItemObjetivo {

    private Object title;
    private List<ItemObjetivo> items;

    public TitledItemObjetivo(List<ItemObjetivo> items, Object title) {
        this.title = title;
        this.items = items;
    }

    public List<ItemObjetivo> getItems() {
        return items;
    }

    public Object getTitle() {
        return title;
    }

    public void setItems(List<ItemObjetivo> items) {
        this.items = items;
    }

    public void setTitle(Object title) {
        this.title = title;
    }

}