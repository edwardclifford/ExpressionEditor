import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

class ParentheticalExpression extends CompoundExpressionImpl {

    /**
     * Implements a non-terminal parenthetical expression
     */
    ParentheticalExpression () {
        super();
    }

    @Override
    /**
     * Returns the JavaFX node associated with this expression.
     * @return the JavaFX node associated with this expression.
     */
    public Node getNode () {
        Text text1 = new Text("(");
        Text text2 = new Text(")");
        Expression exprs = this.getSubexpressionAt(0);
        HBox hbox = new HBox(text1, (Node) exprs, text2);
        return hbox;
    }

    @Override
    /**
     * Creates and returns a deep copy of the expression.
     * The entire tree rooted at the target node is copied, i.e.,
     * the copied Expression is as deep as possible.
     * @return the deep copy
     */
    public Expression deepCopy () {
        CompoundExpression copyExpression = new ParentheticalExpression();

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
        convertToStringHelper(stringBuilder, indentLevel, "()");
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
        // Flatten the inside of this expression    
        for (Expression child : this.getChildren()) {
            child.flatten();
        }
    }

}
