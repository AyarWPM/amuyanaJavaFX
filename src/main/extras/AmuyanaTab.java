package main.extras;

import main.data.LogicSystem;
import main.data.tod.containers.Tod;
import javafx.scene.Node;
import javafx.scene.control.Tab;

public class AmuyanaTab extends Tab {
    TabType type;
    private LogicSystem logicSystem;
    private Tod tod;

    public enum TabType{
            LOGICSYSTEM,
            TOD;
    }
    public AmuyanaTab(String text, Node content, TabType TYPE) {
        super(text, content);
        this.type = TYPE;
    }

    public AmuyanaTab(TabType TYPE) {
        this.type = TYPE;
    }

    public TabType getType() {
        return this.type;
    }

    public void setLogicSystem(LogicSystem logicSystem) {
        this.logicSystem = logicSystem;
    }

    public LogicSystem getLogicSystem() {
        return this.logicSystem;
    }

    public Tod getTod() {
        return tod;
    }

    public void setTod(Tod tod) {
        this.tod = tod;
    }
}
