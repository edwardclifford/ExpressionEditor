class LiteralExpression extends ExpressionImpl {

    public String value;
    /**
     * Implements a terminal expression
     */
    LiteralExpression (String val) {
        super();
        this.value = val;
    } 

    /**
     * Converts a literal to a string and adds it to the string builder at the correct level.
     * @param stringBuilder the string builder to add on to
     * @param indentLevel the indentation level at which to start
     */
    public void convertToString (StringBuilder stringBuilder, int indentLevel) {
        
        // Jump to starting indent level
        // Add literal to sting
        // Newline when finished 

    } 
}


