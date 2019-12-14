import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

class LiteralExpression extends ExpressionImpl {

    /**
     * String that represents the value of the literal
     */
    public String value;

    /**
     * Implements a terminal expression
     */
    LiteralExpression (String val) {
        super();
        this.value = val;
        this.container = new HBox(new Text(val));
    }

    @Override
    /**
     * Returns the JavaFX node associated with this expression.
     * @return the JavaFX node associated with this expression.
     */
    public Node getNode () {
        updateNode();
        return this.container;
    }

    /**
     * Updates the text representation of the literal
     * @return
     */
    public void updateNode() {
        final Text updatedText = new Text(this.value);
        updatedText.setFill(this.color);
        updatedText.setFont(this.font);
        this.container.getChildren().clear();
        this.container.getChildren().add(updatedText);
    }

    @Override
    /**
     * Creates and returns a deep copy of the expression.
     * The entire tree rooted at the target node is copied, i.e.,
     * the copied Expression is as deep as possible.
     * @return the deep copy
     */
    public Expression deepCopy()  {
        return new LiteralExpression(this.value);
    }

    @Override
    /**
     * Converts a literal to a string and adds it to the string builder at the correct level.
     * @param stringBuilder the string builder to add on to
     * @param indentLevel the indentation level at which to start
     */
    public void convertToString (StringBuilder stringBuilder, int indentLevel) {
        // Jump to starting indent level
        indent(stringBuilder, indentLevel);
        // Add current expression representation
        stringBuilder.append(this.value + "\n");
    } 
}    



