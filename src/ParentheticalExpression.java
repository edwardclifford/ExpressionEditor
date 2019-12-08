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
}
