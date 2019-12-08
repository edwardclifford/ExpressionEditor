class MultiplicativeExpression extends CompoundExpressionImpl {

    /**
     * Implements a non-terminal multiplicative expression
     */
    MultiplicativeExpression () {
        super();
    }

    @Override
    /**
     * Creates and returns a deep copy of the expression.
     * The entire tree rooted at the target node is copied, i.e.,
     * the copied Expression is as deep as possible.
     * @return the deep copy
     */
    public Expression deepCopy () {
        CompoundExpression copyExpression = new MultiplicativeExpression();

        for (Expression child : this.getChildren()) {
            Expression copyChild = child.deepCopy();
            copyChild.setParent(copyExpression);
            copyExpression.addSubexpression(child);
        }

        return copyExpression; 
    }

    @Override
    /**
     * Recursively builds a string that represents the compound expression
     * @param stringBuilder the string builder to add on to
     * @param indentLevel the indentation level at which to start
     */
    public void convertToString (StringBuilder stringBuilder, int indentLevel) {
        convertToStringHelper(stringBuilder, indentLevel, "*");
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

        // Flatten all children
        for (Expression child : this.getChildren()) {
            child.flatten();
        }

        // Check first child 
        if (this.childrenSize() > 0) {
            if (this.getSubexpressionAt(0).getClass().equals(this.getClass())) {
                MultiplicativeExpression child = (MultiplicativeExpression) this.getSubexpressionAt(0);
                // Matches parent type, replace child with its subchildren
                this.removeSubexpressionAt(0);
                for (int i = 0; i < child.childrenSize(); i++) {
                    this._children.add(i, child.getSubexpressionAt(i)); 
                }
            }
        }

        // Check last child
        if (this.childrenSize() > 1) {
            if (this.getSubexpressionAt(this.childrenSize() - 1).getClass().equals(this.getClass())) {
                MultiplicativeExpression child = (MultiplicativeExpression) this.getSubexpressionAt(this.childrenSize() - 1);
                // Matches the parent type, replace the child with it's children at the end of the list
                this.removeSubexpressionAt(this.childrenSize() - 1);
                for (int i = 0; i < child.childrenSize(); i++) {
                    this.addSubexpression(child.getSubexpressionAt(i)); 
                }
            } 
        }
    }
}
