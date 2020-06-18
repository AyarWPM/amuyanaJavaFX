package com.amuyana.app.data.tod;

import com.amuyana.app.data.Fcc;
import com.amuyana.app.data.tod.containers.Tod;
import com.amuyana.app.node.NodeHandler;

import java.util.Objects;

public class FccTableEntry {
    String fccId;
    String logicSystem;
    String tods="";
    String element;
    String antiElement;
    String fccName;
    String fccDescription;
    String positiveName;
    String positiveDescription;
    String negativeName;
    String negativeDescription;
    String symmetricName;
    String symmetricDescription;

    public FccTableEntry(Fcc fcc) {
        this.fccId = String.valueOf(fcc.getIdFcc());
        if (!Objects.isNull(NodeHandler.getDataInterface().getLogicSystem(fcc))) {
            this.logicSystem = NodeHandler.getDataInterface().getLogicSystem(fcc).getLabel();
        }
        for (Tod tod : NodeHandler.getDataInterface().getTods(fcc)) {
            this.tods += tod.getLabel() + ", ";
        }
        this.element = NodeHandler.getDataInterface().getElement(fcc,0).getSymbol();
        this.antiElement = NodeHandler.getDataInterface().getElement(fcc,1).getSymbol();
        this.fccName = fcc.getName();
        this.fccDescription = fcc.getDescription();
        this.positiveName = NodeHandler.getDataInterface().getDynamism(fcc,0).getProposition();
        this.positiveDescription = NodeHandler.getDataInterface().getDynamism(fcc,0).getDescription();
        this.negativeName = NodeHandler.getDataInterface().getDynamism(fcc,1).getProposition();
        this.negativeDescription = NodeHandler.getDataInterface().getDynamism(fcc,1).getDescription();
        this.symmetricName = NodeHandler.getDataInterface().getDynamism(fcc,2).getProposition();
        this.symmetricDescription = NodeHandler.getDataInterface().getDynamism(fcc,2).getDescription();
    }

    public String getFccId() {
        return fccId;
    }

    public void setFccId(String fccId) {
        this.fccId = fccId;
    }

    public String getLogicSystem() {
        return logicSystem;
    }

    public void setLogicSystem(String logicSystem) {
        this.logicSystem = logicSystem;
    }

    public String getTods() {
        return tods;
    }

    public void setTods(String tods) {
        this.tods = tods;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getAntiElement() {
        return antiElement;
    }

    public void setAntiElement(String antiElement) {
        this.antiElement = antiElement;
    }

    public String getFccName() {
        return fccName;
    }

    public void setFccName(String fccName) {
        this.fccName = fccName;
    }

    public String getFccDescription() {
        return fccDescription;
    }

    public void setFccDescription(String fccDescription) {
        this.fccDescription = fccDescription;
    }

    public String getPositiveName() {
        return positiveName;
    }

    public void setPositiveName(String positiveName) {
        this.positiveName = positiveName;
    }

    public String getPositiveDescription() {
        return positiveDescription;
    }

    public void setPositiveDescription(String positiveDescription) {
        this.positiveDescription = positiveDescription;
    }

    public String getNegativeName() {
        return negativeName;
    }

    public void setNegativeName(String negativeName) {
        this.negativeName = negativeName;
    }

    public String getNegativeDescription() {
        return negativeDescription;
    }

    public void setNegativeDescription(String negativeDescription) {
        this.negativeDescription = negativeDescription;
    }

    public String getSymmetricName() {
        return symmetricName;
    }

    public void setSymmetricName(String symmetricName) {
        this.symmetricName = symmetricName;
    }

    public String getSymmetricDescription() {
        return symmetricDescription;
    }

    public void setSymmetricDescription(String symmetricDescription) {
        this.symmetricDescription = symmetricDescription;
    }

}
