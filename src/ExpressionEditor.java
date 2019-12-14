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
    private Collection<ExpressionCandidate> possibleExpressions;

    private double initMouseX;
    private double initMouseY;
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
     * drag, when the mouse is clicked on the focused expression
     * @param event
     */
    public void drag(MouseEvent event) {
        if(focusedExpression != expression) {
            double xCoor = event.getX();
            double yCoor = event.getY();

            //deep copy of the focusedExpression
            Expression copiedFE = focusedExpression.deepCopy();

            ExpressionCandidate closestNode = closestNodeDistance((int) xCoor, (int) yCoor);

            // record a delta distance for the drag and drop operation.
            List<Integer> coordinates = getFocusedExpressionExpressionCoord();

            int focusedX = coordinates.get(0);
            int focusedY = coordinates.get(1);

            double x = focusedX - event.getX();
            double y = focusedY - event.getY();

            //setting the layout
            //desired location is initially the same as the node, than will change according to the closest node distance thing
            /*
            if (closestNode == null) {
                focusedExpression.getNode().setLayoutX(focusedX);
                focusedExpression.getNode().setLayoutY(focusedY);
            } else {
                focusedExpression.getNode().setLayoutX(closestNode._targetX - focusedX);
                focusedExpression.getNode().setLayoutY(focusedY);
            }
            */


            // shift node from its initial position by delta
            // calculated from mouse cursor movement
            focusedExpression.getNode().setTranslateX(event.getX() - initMouseX);
            focusedExpression.getNode().setTranslateY(event.getY() - initMouseY);

        }
        
    }

    /**
     * Calculates which option to drop the expression is the closest
     * @param xCoor
     * @param yCoor
     */
    private ExpressionCandidate closestNodeDistance(int xCoor, int yCoor) {
        ExpressionCandidate closest = null;
        
        double prevDistance = 1000000;
        double newDistance = 0;
        if (possibleExpressions != null) {
            for (ExpressionCandidate expressCandidate : possibleExpressions) {
                newDistance = expressCandidate._targetX;
                //change tp actually see which is close to x
                if (newDistance <= prevDistance) {
                    closest = expressCandidate;
                    prevDistance = expressCandidate._targetX;
                }
            }
        }
        return closest;
    }

    /**
     * get the values of the expression we want
     * @return
     */
    public List<Integer> getFocusedExpressionExpressionCoord() {

        List<Integer> coordinate=new ArrayList<Integer>();

        double minX = focusedExpression.getBounds().getMinX();
        double maxX = focusedExpression.getBounds().getMaxX();
        double minY = focusedExpression.getBounds().getMinY();
        double maxY = focusedExpression.getBounds().getMaxY();

        coordinate.add((int) ((minX + maxX) / 2));
        coordinate.add((int) ((minY + maxY) / 2));

        return coordinate;
    }

    /**
     * Drops the expression when the mouse is no longer pressed
     * @param event
     */
    private void drop(MouseEvent event) {
    }

}
