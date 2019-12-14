import java.util.List;

interface CompoundExpression extends Expression {
    /**
     * Adds the specified expression as a child.
     * @param subexpression the child expression to add
     */
    void addSubexpression (Expression subexpression);

    /**
     * Returns a list of the node's children.
     * @return a list of the node's children
     */
    List<Expression> getChildren ();
}
