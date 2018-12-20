package extras;

import data.LogicSystem;
import javafx.scene.control.Menu;

public class LogicSystemMenu extends Menu {
    LogicSystem logicSystem;

    public LogicSystemMenu(LogicSystem logicSystem) {
        this.logicSystem = logicSystem;
    }

    public void setLogicSystem(LogicSystem logicSystem) {
        this.logicSystem = logicSystem;
    }

    public LogicSystem getLogicSystem() {
        return this.logicSystem;
    }
}
