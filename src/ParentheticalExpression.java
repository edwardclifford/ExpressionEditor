class ParentheticalExpression extends CompoundExpressionImpl {

    /**
     * Implements a non-terminal parenthetical expression
     */
    ParentheticalExpression () {
        super();
        // TODO Implement me?
    }
 
    @Override
    public String getType () {
        return "Parenthetical";
    }

   
    @Override
    /**
     * Recursively builds a string that represents the compound expression
     * @param stringBuilder the string builder to add on to
     * @param indentLevel the indentation level at which to start
     */
    public void convertToString (StringBuilder stringBuilder, int indentLevel) {
        convertToStringHelper(stringBuilder, indentLevel, "()");
    }

    @Override
    /**
     * Recursively flattens the expression as much as possible
     * throughout the entire tree. Specifically, in every multiplicative
     * or additive expression x whose first or last
     * child c is of the same type as x, the children of c will be added to x, and
     * c itself will be removed. This method modifies the expression itself.
     */
    public void flatten () {
        // Flatten the inside of this expression    
        for (Expression child : this.getChildren()) {
            child.flatten();
        }
    }

}
