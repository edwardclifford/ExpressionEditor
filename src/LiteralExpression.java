class LiteralExpression extends ExpressionImpl {

    /**
     * String that represents the value of the literal
     */
    public String value;

    /**
     * Implements a terminal expression
     */
    LiteralExpression (String val) {
        super();
        this.value = val;
    } 

    @Override
    /**
     * Creates and returns a deep copy of the expression.
     * The entire tree rooted at the target node is copied, i.e.,
     * the copied Expression is as deep as possible.
     * @return the deep copy
     */
    public Expression deepCopy() {
        return new LiteralExpression(value);
    }

    @Override
    /**
     * Converts a literal to a string and adds it to the string builder at the correct level.
     * @param stringBuilder the string builder to add on to
     * @param indentLevel the indentation level at which to start
     */
    public void convertToString (StringBuilder stringBuilder, int indentLevel) {
        // Jump to starting indent level
        indent(stringBuilder, indentLevel);
        // Add current expression representation
        stringBuilder.append(this.value + "\n");
    } 
}    



