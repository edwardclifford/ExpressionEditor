class AdditiveExpression extends CompoundExpressionImpl {

    /**
     * Implements a non-terminal additive expression
     */
    AdditiveExpression () {
        super();
        // TODO Implement
    }

    @Override
    public String getType () {
        return "Additive";
    }

    @Override
    /**
     * Recursively builds a string that represents the compound expression
     * @param stringBuilder the string builder to add on to
     * @param indentLevel the indentation level at which to start
     */
    public void convertToString (StringBuilder stringBuilder, int indentLevel) {
        convertToStringHelper(stringBuilder, indentLevel, "+");
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
        // TODO Implement, override in each class

        // Check first child 
        if (this.childSize() > 0) {
            Expression child = this.getSubexpressionAt(0);
            if (child.getClass().equals(this.getClass())) {
                // Matches parent type, replace child with its subchildren
                this.removeSubexpression(0);
                for (int i = 0; i < child.childSize(); i++) {
                    this._children.add(i, child.getExpression(i).flatten()); 
                }
            }
        }

        if (this.childSize() > 1) {
            Expression child = this.getSubexpressionAt(this.childSize() - 1);
            if (child.getClass().equals(this.getClass())) {
                this.removeSubexpression(this.childSize() - 1);
                for (int i = 0; i < child.childSize(); i++) {
                    this.addSubexpression(child.getExpression(i).flatten()); 
                }
            } 
        }
    }
}
 
