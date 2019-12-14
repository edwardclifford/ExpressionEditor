import javafx.application.Application;
import java.util.*;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ExpressionEditor extends Application {
    public static void main (String[] args) {
        launch(args);
    }

    /**
     * Mouse event handler for the entire pane that constitutes the ExpressionEditor
     */
    private class MouseEventHandler implements EventHandler<MouseEvent> {
        MouseEventHandler (Pane pane_, CompoundExpression rootExpression_) {
        }

        public void handle (MouseEvent event) {
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                initMouseX = event.getX();
                initMouseY = event.getY();

                possibleExpressions.clear();
                generateExpressionCandidates(expression, focusedExpression);
                for (ExpressionCandidate exp : possibleExpressions) {
                    System.out.println("Possible expression");
                    System.out.println(exp._expression.convertToString(0));
                }

            } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                    drag(event);

            } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                updateFocused(event);

                drop(event);
            }
        }
    }

    /**
     * Size of the GUI
     */
    private static final int WINDOW_WIDTH = 500, WINDOW_HEIGHT = 250;

    /**
     * Initial expression shown in the textbox
     */
    private static final String EXAMPLE_EXPRESSION = "2*x+3*y+4*z+(7+6*z)";

    /**
     * Parser used for parsing expressions.
     */
    private final ExpressionParser expressionParser = new SimpleExpressionParser();

    /**
     * the originally parsed expression
     */
    private Expression expression;

    /**
     * Focus on the expression that is clicked by the user
     */
    private Expression focusedExpression;

    /**
     * A collection of possible expression candidates where one node of the Expression is re-ordered
     */
    private Collection<ExpressionCandidate> possibleExpressions = new ArrayList<ExpressionCandidate>();

    /**
     * stores the initial X coordinate of a mouse click for dragging
     */
    private double initMouseX;

    /**
     * stores the initial Y coordinate of a mouse click for dragging
     */
    private double initMouseY;

    /**
     * Drop coordinates
     */
    public boolean wasDragged = false;

    /**
     * Deep copy of the focused expression
     */
    Expression copiedFE;

    /**
     * The ExpressionCandidate found to be the closest to the mouse event when dropped
     */
    ExpressionCandidate closest;


    /**
     * Implements a structure for storing a possible expression
     */
    private class ExpressionCandidate {
        public double _targetX;
        public Expression _expression;

        /**
         * Constructor for a possible Expression candidate
         */
        ExpressionCandidate (Expression rootExpression, double xCoordinate) {
            this._expression = rootExpression;
            this._targetX = xCoordinate;
        }
    }

    @Override
    public void start (Stage primaryStage) {
        primaryStage.setTitle("Expression Editor");

        // Add the textbox and Parser button
        final Pane queryPane = new HBox();
        final TextField textField = new TextField(EXAMPLE_EXPRESSION);
        final Button button = new Button("Parse");
        queryPane.getChildren().add(textField);

        final Pane expressionPane = new Pane();

        // Add the callback to handle when the Parse button is pressed
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle (MouseEvent e) {
                // Try to parse the expression
                try {
                    // Success! Add the expression's Node to the expressionPane
                    expression = expressionParser.parse(textField.getText(), true);
                    focusedExpression = expression;
                    System.out.println(expression.convertToString(0));
                    expressionPane.getChildren().clear();
                    expressionPane.getChildren().add(expression.getNode());
                    expression.getNode().setLayoutX(WINDOW_WIDTH/4);
                    expression.getNode().setLayoutY(WINDOW_HEIGHT/2);

                    // If the parsed expression is a CompoundExpression, then register some callbacks
                    if (expression instanceof CompoundExpression) {
                        ((Pane) expression.getNode()).setBorder(Expression.NO_BORDER);
                        final MouseEventHandler eventHandler = new MouseEventHandler(expressionPane, (CompoundExpression) expression);
                        expressionPane.setOnMousePressed(eventHandler);
                        expressionPane.setOnMouseDragged(eventHandler);
                        expressionPane.setOnMouseReleased(eventHandler);
                    }
                } catch (ExpressionParseException epe) {
                    // If we can't parse the expression, then mark it in red
                    textField.setStyle("-fx-text-fill: red");
                }
            }
        });
        queryPane.getChildren().add(button);

        // Reset the color to black whenever the user presses a key
        textField.setOnKeyPressed(e -> textField.setStyle("-fx-text-fill: black"));

        final BorderPane root = new BorderPane();
        root.setTop(queryPane);
        root.setCenter(expressionPane);

        primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.show();
    }

    /**
     * When mouse is pressed, set focus
     * @param event
     */
    public void updateFocused (MouseEvent event) {
        final int xCoord = (int) event.getX();
        final int yCoord = (int) event.getY();
        boolean childFound = false;

        if (focusedExpression instanceof CompoundExpressionImpl) {
            CompoundExpressionImpl focusExpression = (CompoundExpressionImpl) focusedExpression;

            if (focusExpression.containsPoint(xCoord, yCoord)) {
                List<Expression> children = focusExpression.getChildren();

                //iterate through immediate children to see which one has been clicked
                for (Expression child : children) {
                    if (child.containsPoint(xCoord, yCoord)) {
                        childFound = true;
                        //make a box around highlighted child
                        focusedExpression.setBorder(Expression.NO_BORDER);
                        child.setBorder(Expression.RED_BORDER);
                        //than return the child as the new expression
                        focusedExpression = child;
                        return;
                    }
                }
            }
        }
        // Reset focus if no conditions are met
        focusedExpression.setBorder(Expression.NO_BORDER);
        focusedExpression = expression;
    }

    /**
     * Updates the collection of Expression candidates based on an Expression and one of its subexpressions
     * @param parentExpression the Expression being modified
     * @param subExpression the expression that will be shifted
     */
    public void generateExpressionCandidates (Expression parentExpression, Expression subExpression) {
        final int possiblePermutations = subExpression.getParent().getChildren().size();

        for (int i = 0; i < possiblePermutations; i++) {
            subExpression.getParent().getChildren().remove(subExpression);
            subExpression.getParent().getChildren().add(i, subExpression);

            subExpression.setColor(Expression.GHOST_COLOR);
            parentExpression.updateNode();

            final double minX = subExpression.getBounds().getMinX();
            final double maxX = subExpression.getBounds().getMaxX();
            final double centerX = (minX + maxX) / 2;

            final Expression copyExpression = parentExpression.deepCopy();
            possibleExpressions.add(new ExpressionCandidate(copyExpression, centerX));
        }
    }

    /**
     * When the mouse is clicked on the focused expression and used to drag the expression with the mouse
     * @param event - MouseEvent
     */
    public void drag(MouseEvent event) {
        if(focusedExpression != expression) {
            double xCoor = event.getX();
            double yCoor = event.getY();

            //deep copy of the focusedExpression
            copiedFE = focusedExpression.deepCopy();

            //running closestNodeDistance to constantly update the closest expression
            closestNodeDistance((int) xCoor, (int) yCoor, event);

            // shift node from its initial position by delta
            // calculated from mouse cursor movement
            focusedExpression.getNode().setTranslateX(event.getX() - initMouseX);
            focusedExpression.getNode().setTranslateY(event.getY() - initMouseY);

            //setting boolean, letting drop know that drag was initialized and was successful
            wasDragged = true;
        }
    }

    /**
     * Calculates which option to drop the expression is the closest
     * @param xCoor - x coordinate of the mouse
     * @param yCoor - y coordinate of the mouse
     * @param event - mouse event
     */
    private ExpressionCandidate closestNodeDistance(int xCoor, int yCoor, MouseEvent event) {
        closest = null;
        
        double prevDistance = 1000000;
        double newDistance = 0;
        if (possibleExpressions != null) {
            for (ExpressionCandidate expressCandidate : possibleExpressions) {
                newDistance = expressCandidate._targetX;
                newDistance = newDistance - event.getX();
                if (newDistance <= prevDistance) {
                    closest = expressCandidate;
                    prevDistance = expressCandidate._targetX;
                }
            }
        }
        return closest;
    }

    /**
     * Drops the expression when the mouse is no longer pressed
     * @param event
     */
    private void drop(MouseEvent event) {
        if(wasDragged) {
            //re-setting the original expression to the new expression from dragging
            expression = (Expression) closest;

            //re-set wasDragged
            wasDragged = false;
        }
    }
}
