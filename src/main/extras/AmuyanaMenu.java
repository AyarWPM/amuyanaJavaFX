package main.extras;

import javafx.scene.control.Menu;

public class AmuyanaMenu extends Menu {
    Object object;

    public AmuyanaMenu(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
