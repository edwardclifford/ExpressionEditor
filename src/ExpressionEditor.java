import javafx.application.Application;
import java.util.*;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jdk.jfr.Experimental;

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
                focus(event);
            } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                drag(event);
            } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
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
	private Expression highlighted;

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
					highlighted = expression;
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
    public void focus(MouseEvent event) {
		final int xCoord = (int) event.getX();
		final int yCoord = (int) event.getY();
		boolean childFound = false;

		if (highlighted instanceof CompoundExpressionImpl) {
			CompoundExpressionImpl focusExpression = (CompoundExpressionImpl) highlighted;

			if (focusExpression.containsPoint(xCoord, yCoord)) {
				List<Expression> children = focusExpression.getChildren();

				//iterate through immediate children to see which one has been clicked
				for (Expression child : children) {
					if (child.containsPoint(xCoord, yCoord)) {
						childFound = true;
						//TODO make a box around highlighted child
						highlighted.setBorder(Expression.NO_BORDER);
						child.setBorder(Expression.RED_BORDER);
						//than return the child as the new expression
						highlighted = child;
						return;
					}
				}
			}
		}
		// Reset focus if no conditions are met
		highlighted.setBorder(Expression.NO_BORDER);
		highlighted = expression;
    }

    /**
     * drag, when the mouse is clicked on the focused expression
     * @param event
     */
    public void drag(MouseEvent event) {

    }

    /**
     * Drops the expression when the mouse is no longer pressed
     * @param event
     */
    private void drop(MouseEvent event) {
    }

}
