package com.amuyana.app.data.tod;

import com.amuyana.app.data.Syllogism;
import com.amuyana.app.node.NodeHandler;
import com.amuyana.app.node.tod.expression.SyllogismExp;

import java.util.List;

public class SyllogismTableEntry {
    // id
    // label
    // tod
    // Number of dynamisms
    // number of inclusions (#dyn-1)
    // The inclusions
    // Number of registers

    private String id;
    private String label;
    private String tod;
    private String numDynamisms;
    private String numInclusions;
    private SyllogismExp syllogismExp;
    private String numRegisters;

    public SyllogismTableEntry(Syllogism syllogism) {
        this.id = String.valueOf(syllogism.getIdSyllogism());
        this.label = syllogism.getLabel();

        this.tod = NodeHandler.getDataInterface().getTod(syllogism).getLabel();
        List<Inclusion> listInclusions = NodeHandler.getDataInterface().getInclusions(syllogism);
        String nD = String.valueOf(listInclusions.size()+1);
        this.numDynamisms = nD;
        this.numInclusions = String.valueOf(listInclusions.size());
        this.syllogismExp = new SyllogismExp(listInclusions);
        this.numRegisters = String.valueOf(NodeHandler.getDataInterface().getRegisters(syllogism).size());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTod() {
        return tod;
    }

    public void setTod(String tod) {
        this.tod = tod;
    }

    public String getNumDynamisms() {
        return numDynamisms;
    }

    public void setNumDynamisms(String numDynamisms) {
        this.numDynamisms = numDynamisms;
    }

    public String getNumInclusions() {
        return numInclusions;
    }

    public void setNumInclusions(String numInclusions) {
        this.numInclusions = numInclusions;
    }

    public SyllogismExp getSyllogismExp() {
        return syllogismExp;
    }

    public void setSyllogismExp(SyllogismExp syllogismExp) {
        this.syllogismExp = syllogismExp;
    }

    public String getNumRegisters() {
        return numRegisters;
    }

    public void setNumRegisters(String numRegisters) {
        this.numRegisters = numRegisters;
    }
}
