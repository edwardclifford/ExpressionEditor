
class ExpressionImpl implements Expression {

	public CompoundExpression parent;

	/**
     * Implements an expression
     */
    ExpressionImpl () {
	}

    /**
     * Returns the expression's parent.
     * @return the expression's parent
     */
    public CompoundExpression getParent () {
        return this.parent;
    }

    /**
     * Sets the parent to be the specified expression.
     * @param parent the CompoindExpression that should be the parent of the target object
     */
    public void setParent (CompoundExpression newParent) {
        this.parent = newParent; 
    }

    /**
	 * Creates and returns a deep copy of the expression.
	 * The entire tree rooted at the target node is copied, i.e.,
	 * the copied Expression is as deep as possible.
	 * @return the deep copy
	 */
    public Expression deepCopy () {
        // TODO override in each class
        Expression copyExpression = new ExpressionImpl();
        copyExpression.setParent(this.parent);
        return copyExpression;
    }

    /**
	 * Recursively flattens the expression as much as possible
	 * throughout the entire tree. Specifically, in every multiplicative
	 * or additive expression x whose first or last
	 * child c is of the same type as x, the children of c will be added to x, and
	 * c itself will be removed. This method modifies the expression itself.
	 */
    public void flatten () {
        // TODO Implement, override in each class   
    }

    /**
	 * Creates a String representation by recursively printing out (using indentation) the
	 * tree represented by this expression, starting at the specified indentation level.
	 * @param stringBuilder the StringBuilder to use for building the String representation
	 * @param indentLevel the indentation level (number of tabs from the left margin) at which to start
	 */
    @Override
	public void convertToString (StringBuilder stringBuilder, int indentLevel) {
		final StringBuilder stringBuilder = new StringBuilder();
		convertToString(stringBuilder, indentLevel);
		return stringBuilder.toString();
	}

    /**
	 * Static helper method to indent a specified number of times from the left margin, by
	 * appending tab characters to the specified StringBuilder.
	 * @param sb the StringBuilder to which to append tab characters.
	 * @param indentLevel the number of tabs to append.
	 */
	public static void indent (StringBuilder stringBuilder, int indentLevel) {
		for (int i = 0; i < indentLevel; i++) {
			stringBuilder.append('\t');
		}
	}
}
