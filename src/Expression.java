import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.scene.text.Font;

interface Expression {
    /**
     * Border for showing a focused expression
     */
    public static final Border RED_BORDER = new Border(
            new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)
    );

    /**
     * Border for showing a non-focused expression
     */
    public static final Border NO_BORDER = null;

    /**
     * Color used for a "ghosted" expression
     */
    public static final Color GHOST_COLOR = Color.LIGHTGREY;

    /**
     * Color used for a "ghosted" expression
     */
    public static final Color NORMAL_COLOR = Color.BLACK;

    /**
     * Returns the expression's parent.
     * @return the expression's parent
     */
    CompoundExpression getParent ();

    /**
     * Sets the parent be the specified expression.
     * @param parent the CompoundExpression that should be the parent of the target object
     */
    void setParent (CompoundExpression parent);

    /**
     * Creates and returns a deep copy of the expression.
     * The entire tree rooted at the target node is copied, i.e.,
     * the copied Expression is as deep as possible.
     * @return the deep copy
     */
    Expression deepCopy ();

    /**
     * Returns the JavaFX node associated with this expression.
     * @return the JavaFX node associated with this expression.
     */
    Node getNode ();

    /**
     * Updates and re-creates current and sub-nodes of the expression
     */
    void updateNode();

    /**
     * Returns the children of the JavaFX node representing the Expression
     * @return the children of the JavaFX node
     */
    ObservableList<Node> getNodeChildren ();

    /**
     * Sets the border of the HBox container representing the Expression
     * @param border the border to set to
     */
    void setBorder (Border border);

    /**
     * Sets the color of the JavaFX node to a specific color
     * @param color the color being set
     */
    void setColor (Color color);

    /**
     * Sets the font of the JavaFX node
     * @param font the font being set
     */
    void setFont (Font font);

    /**
     * Checks if a point is contained inside the node relative to the pane
     * @param x the x value of the check point
     * @param y the y value of the check point
     * @return true if the tested point is contained in the node
     */
    boolean containsPoint (double x, double y);

    /**
     * Gets the current bounds of the JavaFX node representing the Expression
     * @return a Bounds object containing information about the bounds of the node
     */
    Bounds getBounds ();

    /**
     * Recursively flattens the expression as much as possible
     * throughout the entire tree. Specifically, in every multiplicative
     * or additive expression x whose first or last
     * child c is of the same type as x, the children of c will be added to x, and
     * c itself will be removed. This method modifies the expression itself.
     */
    void flatten ();

    /**
     * Creates a String representation by recursively printing out (using indentation) the
     * tree represented by this expression, starting at the specified indentation level.
     * @param stringBuilder the StringBuilder to use for building the String representation
     * @param indentLevel the indentation level (number of tabs from the left margin) at which to start
     */
    void convertToString (StringBuilder stringBuilder, int indentLevel);

    public default String convertToString (int indentLevel) {
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
