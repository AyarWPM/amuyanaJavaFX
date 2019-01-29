package com.amuyana.app.algebraWriter;

public class AmuyanaWriter {
    enum output{
        // produced in node.expression.Expression
        //FX,
        // Produced in algebraWriter.Latex
        LATEX,
        // Produced in algebraWriter.Image
        JPG,GIF,PNG,
        // Produced in algebraWriter.Html
        HTML,
        // Produced in algebraWriter.OpenDocument
        OPEN_DOCUMENT
    }
}
