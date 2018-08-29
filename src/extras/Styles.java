
package extras;

public enum Styles {
    SIMPLE("Simple","/styles/formulaSimple.css"),
    NO_LABELS("No labels","/formulaNoLabels.css"),
    DEFAULT("Default","/styles/formulaDefault.css"),
    DEFAULT_REDUCED("Default reduced","/styles/formulaDefaultReduced.css"),
    PHRASES("Phrases","/styles/formulaPhrases.css");

    private final String label;
    private final String url;
    
    Styles(String label,String url) {
        this.label=label;
        this.url=url;
    }
    
    public String getCssUrl(){
        return this.url;
    }
    
    public String getLabel(){
        return this.label;
    }
    
    @Override
    public String toString(){
        return this.label;
    }
    
}
