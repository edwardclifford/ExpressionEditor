import java.util.*;

public class CompoundExpressionImpl extends ExpressionImpl implements CompoundExpression {

    /**
     * ArrayList of all children and subtrees contained in the Compound Expression.
     */
    public List<Expression> children = new ArrayList<Expression>();

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
