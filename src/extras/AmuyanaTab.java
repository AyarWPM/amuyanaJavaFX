package extras;

import data.LogicSystem;
import javafx.scene.Node;
import javafx.scene.control.Tab;

public class AmuyanaTab extends Tab {
    TabType type;
    private LogicSystem logicSystem;

    public enum TabType{
            LOGICSYSTEM;
    }
    public AmuyanaTab(String text, Node content, TabType TYPE) {
        super(text, content);
        this.type = TYPE;
    }

    public AmuyanaTab(TabType TYPE) {
        this.type = TYPE;
    }

    public void setLogicSystem(LogicSystem logicSystem) {
        this.logicSystem = logicSystem;
    }

    public LogicSystem getLogicSystem() {
        return this.logicSystem;
    }

    public TabType getType() {
        return this.type;
    }
}
