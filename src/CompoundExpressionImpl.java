import java.util.Collection;
import java.util.HashSet;

class CompoundExpressionImpl extends ExpressionImpl implements CompoundExpression {

    public Collection<Expression> children = new HashSet<Expression>();

    /**
     * Implements a non-terminal expression
     */
    CompoundExpressionImpl () {
        super();
    }

    /**
	 * Adds the specified expression as a child.
	 * @param subExpression the child expression to add
	 */
	public void addSubexpression (Expression subExpression) {
        this.children.add(subExpression);
    }

}
