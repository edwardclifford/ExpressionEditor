import java.util.*;

public class CompoundExpressionImpl extends ExpressionImpl implements CompoundExpression {

    /**
     * ArrayList of all children and subtrees contained in the Compound Expression.
     */
    public List<Expression> _children = new ArrayList<Expression>();

    /**
     * Hashset of all children, used to avoid adding the same child twice
     */
    public Collection<Expression> _seenChildren = new HashSet<Expression>();

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
        if (!(this._seenChildren.contains(subExpression))) {
            this._children.add(subExpression);
            this._seenChildren.add(subExpression);
        }
    }

    /**
     * Returns a list of the node's children.
     * @return a list of the node's children
     */
    public List<Expression> getChildren () {
        return this._children;
    }

    /**
     * Returns the child node at a given index.
     * @param index the requested index
     * @return the Expression at the index
     */
    public Expression getSubexpressionAt (int index) {
        return this._children.get(index);
    }

    /**
     * Removes the child node at a given index.
     * @param index the index at which to remove the child
     */
    public void removeSubexpressionAt (int index) {
        this._children.remove(index);
    }

    /**
     * Returns the ammount of children nodes.
     * @return the ammount of children nodes
     */
    public int childrenSize () {
        return this._children.size();
    }

    /**
     * Recursively builds a string that represents the compound expression of a given symbol
     * @param stringBuilder the string builder to add on to
     * @param indentLevel the indentation level at which to start
     * @param symbol the mathematical symbol to insert
     */
    public void convertToStringHelper (StringBuilder stringBuilder, int indentLevel, String symbol) {

        // Jump to starting indent level
        indent(stringBuilder, indentLevel);
        // Add current expression representation
        stringBuilder.append(symbol + "\n");
        // For each child, with indent + 1, convert to string recursively
        for (Expression child : _children) {
            child.convertToString(stringBuilder, indentLevel + 1);
        }
    }

    /**
     * Recursively builds a string that represents the compound expression
     * @param stringBuilder the string builder to add on to
     * @param indentLevel the indentation level at which to start
     */
    public void convertToString (StringBuilder stringBuilder, int indentLevel) {}

}
