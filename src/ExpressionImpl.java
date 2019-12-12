import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public  class ExpressionImpl implements Expression {

    /**
     * Pointer to the Expression's parent
     */
    public CompoundExpression parent;

    /**
     * JavaFX node that contains all sub nodes
     */
    public HBox container = new HBox();

    /**
     * Current color of the text of the Expression
     */
    public Color color = new Color(0, 0, 0, 1.0);

    /**
     * Current size of the text of the Expression
     */
    public Font font = new Font(24);

    /**
     * Implements an expression
     */
    ExpressionImpl () {}

    /**
     * Returns the expression's parent.
     * @return the expression's parent
     */
    public CompoundExpression getParent () {
        return this.parent;
    }

    /**
     * Sets the parent to be the specified expression.
     * @param newParent the CompoindExpression that should be the parent of the target object
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
        return this;
    }

    /**
     * Recursively flattens the expression as much as possible
     * throughout the entire tree. Specifically, in every multiplicative
     * or additive expression x whose first or last
     * child c is of the same type as x, the children of c will be added to x, and
     * c itself will be removed. This method modifies the expression itself.
     */
    public void flatten () {}

    /**
     * Returns the JavaFX node associated with this expression.
     * @return the JavaFX node associated with this expression.
     */
    public Node getNode () {
        return new Text("Expression");
    }

    /**
     * Updates and re-creates current and sub-nodes of the expression
     */
    public void updateNode () {}

    /**
     * Returns the children of the JavaFX node representing the Expression
     * @return the children of the JavaFX node
     */
    public ObservableList<Node> getNodeChildren() {
        return this.container.getChildren();
    }

    /**
     * Sets the border of the HBox container representing the Expression
     * @param border the border to set to
     */
    public void setBorder (Border border) {
        this.container.setBorder(border);
    }

    /**
     * Checks if a point is contained inside the node relative to the pane
     * @param x the x value of the check point
     * @param y the y value of the check point
     * @return true if the tested point is contained in the node
     */
    public boolean containsPoint (double x, double y) {
        final Bounds boundsInScene = this.getBounds();

        return (x >= boundsInScene.getMinX() &&
                x <= boundsInScene.getMaxX() &&
                y >= boundsInScene.getMinY() &&
                y <= boundsInScene.getMaxY());
    }

    /**
     * Gets the current bounds of the JavaFX node representing the Expression
     * @return a Bounds object containing information about the bounds of the node
     */
    public Bounds getBounds() {
        return this.getNode().localToScene(this.getNode().getBoundsInLocal());
    }

    /**
     * Sets the color of the JavaFX node to a specific color
     * @param color the color being set
     */
    public void setColor (Color color) {
        this.color = color;
        this.updateNode();
    }

    /**
     * Sets the font of the JavaFX node
     * @param font the font being set
     */
    public void setFont (Font font) {
        this.font = font;
    }

    /**
     * Creates a String representation by recursively printing out (using indentation) the
     * tree represented by this expression, starting at the specified indentation level.
     * @param stringBuilder the StringBuilder to use for building the String representation
     * @param indentLevel the indentation level (number of tabs from the left margin) at which to start
     */
    public void convertToString (StringBuilder stringBuilder, int indentLevel) {}

    /**
     * Static helper method to indent a specified number of times from the left margin, by
     * appending tab characters to the specified StringBuilder.
     * @param stringBuilder the StringBuilder to which to append tab characters.
     * @param indentLevel the number of tabs to append.
     */
    public static void indent (StringBuilder stringBuilder, int indentLevel) {

        for (int i = 0; i < indentLevel; i++) {
            stringBuilder.append('\t');
        }
    }
}
