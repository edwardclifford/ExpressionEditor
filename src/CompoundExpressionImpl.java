class CompoundExpressionImpl extends ExpressionImpl implements CompoundExpression {

    /**
     * Implements a non-terminal expression
     */
    CompoundExpressionImpl () {
        super();
        Collection<Expression> children = new HashSet<Expression>();
    }

    /**
	 * Adds the specified expression as a child.
	 * @param subexpression the child expression to add
	 */
	public void addSubexpression (Expression subExpression) {
        this.children.add(subExpression);
    }

}
