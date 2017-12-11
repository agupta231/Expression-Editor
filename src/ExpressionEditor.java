import javafx.application.Application;
import java.util.*;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Bounds;

public class ExpressionEditor extends Application {
	public static void main (String[] args) {
		launch(args);
	}

	/**
	 * Mouse event handler for the entire pane that constitutes the ExpressionEditor
	 */
	private static class MouseEventHandler implements EventHandler<MouseEvent> {
		CompoundExpression rootExpression_;
		CompoundExpression currentFocus_;
		Pane currentPane;

		MouseEventHandler (Pane pane_, CompoundExpression rootExpression_) {
			this.rootExpression_ = rootExpression_;
			this.currentFocus_ = this.rootExpression_;
			this.currentPane = pane_;
		}

		public void handle (MouseEvent event) {
			if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {

				this.currentPane.setBorder(Expression.RED_BORDER);

				System.out.println(event.getX());
				System.out.println(event.getY());
				System.out.println(event.getSceneX());
				System.out.println(event.getSceneY());


				ObservableList<Node> HChildren = ((HBox) currentFocus_.getNode()).getChildren();

				Stack<Node> nodesToSearch = new Stack<>();
				HashSet<Node> vistedNodes = new HashSet<>();

				nodesToSearch.add(currentFocus_.getNode());
				vistedNodes.add(currentFocus_.getNode());

//				while (!nodesToSearch.empty()) {
//					System.out.println(nodesToSearch.size());
//
//					Node currentNode = nodesToSearch.pop();
//					vistedNodes.add(currentNode);
//
//
//					System.out.println("xxxxxxxxxxxxxxx");
//					System.out.println(currentNode.getBoundsInLocal());
//					System.out.println(currentNode.getBoundsInParent());
//					System.out.println(currentNode.getLayoutBounds());
//
//					if (currentNode.contains(event.getSceneX(), event.getSceneY())) {
//						System.out.println("found");
//					}
//
//					System.out.println("Not found");
//
//					for(Node n : ((HBox) currentFocus_.getNode()).getChildren()) {
//						if(!vistedNodes.contains(n)) {
//							nodesToSearch.add(n);
//						}
//					}
//				}

				for (int i = 0; i < HChildren.size(); i++) {
					Node currentNode = HChildren.get(i);

					//double xLocationMin = HChildren.get(i);
					//Bounds b = HChildren.get(i).getLayoutX();
					System.out.println("--------------");
					//System.out.println(xLocationMin);
//					System.out.println("X: " + event.getSceneX() + "Y: " + event.getSceneX());
//					System.out.println(HChildren.get(i));
//					System.out.println(HChildren.get(i).contains(event.getSceneX(), event.getSceneY()));
					//if(xLocationMin )


					System.out.println("xxxxxxxxxxxxxxx");
					System.out.println(currentNode.getBoundsInLocal());
					System.out.println(currentNode.getBoundsInParent());
					System.out.println(currentNode.getLayoutBounds());
				}
			}
			else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {

			}
			else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {

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
					final Expression expression = expressionParser.parse(textField.getText(), true);
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
}
