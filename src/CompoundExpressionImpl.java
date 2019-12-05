import java.util.Collection;
import java.util.HashSet;

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
	 * @param subExpression the child expression to add
	 */
	public void addSubexpression (Expression subExpression) {
        this.children.add(subExpression);
    }

}
