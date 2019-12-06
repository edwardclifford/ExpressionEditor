class AdditiveExpression extends CompoundExpressionImpl {

    /**
     * Implements a non-terminal additive expression
     */
    AdditiveExpression () {
        super();
        // TODO Implement
    }

    
    /**
     * Recursively builds a string that represents the compound expression
     * @param stringBuilder the string builder to add on to
     * @param indentLevel the indentation level at which to start
     */
    public void convertToString (StringBuilder stringBuilder, int indentLevel) {
        
        // Jump to starting indent level
        // Add current expression representation
        // Newline
        // For each child, with indent + 1, convert to string recursively
        // Newline when finished 

    } 
}
