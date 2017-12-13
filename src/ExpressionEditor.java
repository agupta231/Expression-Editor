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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Bounds;
import org.junit.runner.Computer;

public class ExpressionEditor extends Application {
	public static Font FONT = javafx.scene.text.Font.font("Comic Sans MS", 36);

	private static HashMap<Node, Expression> nodeMap = new HashMap<>();

	public static void main (String[] args) {
		launch(args);
	}

	/**
	 * Mouse event handler for the entire pane that constitutes the ExpressionEditor
	 */
	private static class MouseEventHandler implements EventHandler<MouseEvent> {
		CompoundExpression rootExpression_;
		Expression currentExpression_;
		Node currentFocus_;
		Node copyFocus_;
		Node previousFocus;
		Pane currentPane;
        ArrayList<Integer> distances = new ArrayList<Integer>();
        ArrayList<Expression> expressions = new ArrayList<Expression>();
        int closestExpression;
        HashMap<Node,Expression> nodeMap;

		double _lastX, _lastY;
		boolean firstClick = true;

		MouseEventHandler (Pane pane_, CompoundExpression rootExpression_) {
			this.rootExpression_ = rootExpression_;
			this.currentExpression_ = rootExpression_;
			this.currentFocus_ = this.rootExpression_.getNode();
			this.currentPane = pane_;
			this.nodeMap = generateMap(rootExpression_);
		}

		public void handle (MouseEvent event) {
			final double sceneX = event.getSceneX();
			final double sceneY = event.getSceneY();

			if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {

				ObservableList<Node> HChildren = ((HBox) currentFocus_).getChildren();

				if(HChildren.size() == 1 && !firstClick){
					return;
				}

				if(currentFocus_ == rootExpression_.getNode()){
					firstClick = true;
				}

				boolean found = false;

				for (int i = 0; i < HChildren.size(); i++) {
					final Node currentNode = HChildren.get(i);
					if (currentNode instanceof HBox) {
						Point2D relativeClick = currentNode.sceneToLocal(sceneX, sceneY);

						if (currentNode.contains(relativeClick.getX(), relativeClick.getY())) {
							if(firstClick) {
								previousFocus = currentFocus_;
								currentFocus_ = currentNode;

								Point2D currentLocation = currentFocus_.localToScene(currentFocus_.getLayoutX(), currentFocus_.getLayoutY());

								//copyFocus_ = newLabel(((CopyAble) nodeMap.get(currentFocus_)).convertToStringFlat());
								System.out.println(currentLocation.getX());
								copyFocus_.setLayoutX(currentLocation.getX());
								copyFocus_.setLayoutY(currentLocation.getY());

								expressionPane.getChildren().add(copyFocus_);

								if(currentFocus_ instanceof Label){
									break;
								}
								((HBox) previousFocus).setBorder(Expression.NO_BORDER);
								((HBox) currentNode).setBorder(Expression.RED_BORDER);
							}
							found = true;
						}
					}
				}
				if(!found) {
					((HBox) currentFocus_).setBorder(Expression.NO_BORDER);
					currentFocus_ = rootExpression_.getNode();
				}

                Expression focusedExpression = nodeMap.get(currentFocus_);
                if(!nodeMap.get(currentFocus_).convertToString(0).equals(rootExpression_.convertToString(0))) {
                    //if(focusedExpression.getParent()!=null){
                    distances = new ArrayList<>();
                    expressions = new ArrayList<>();

					for (Expression e : AbstractCompoundExpression.generateAllPossibleTrees(
                            ((AbstractCompoundExpression) focusedExpression.getParent()).deepCopy(),
                            focusedExpression.convertToString(0))) {
                        LinkedList<Expression> chillin = ((AbstractCompoundExpression)e).getChildren();

						System.out.println(chillin);
                        int totalWidth = 0;
                        for(int i = 0; i < chillin.size(); i++){
                        	//System.out.println(chillin.get(i));
                            chillin.get(i).getNode().getLayoutBounds().getWidth();
                            if(chillin.get(i).getNode().equals(this.currentFocus_)){
                                break;
                            }
                            totalWidth += chillin.get(i).getNode().getLayoutBounds().getWidth();
                        }
                        distances.add(totalWidth);
                        expressions.add(e);
                    }
                }
			}
			else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				if(!currentExpression_.convertToString(0).equals(rootExpression_.convertToString(0))) {

					int minDistance = Integer.MAX_VALUE;
					for (int i = 0; i < distances.size(); i++) {
						int localX = (int) expressions.get(0).getNode().sceneToLocal(sceneX, sceneY).getX();
						if (Math.abs(localX - distances.get(i)) < minDistance) {
							minDistance = Math.abs(localX - distances.get(i));
							closestExpression = i;
						}
					}

					rootExpression_ = (CompoundExpression) expressions.get(closestExpression);
					nodeMap = generateMap(rootExpression_);
					expressionPane.getChildren().clear();
					expressionPane.getChildren().add(rootExpression_.getNode());
					expressionPane.getChildren().add(copyFocus_);

					rootExpression_.getNode().setLayoutX(WINDOW_WIDTH / 2);
					rootExpression_.getNode().setLayoutY(WINDOW_HEIGHT / 2);

					if (copyFocus_ != rootExpression_.getNode()) {
						copyFocus_.setTranslateX(copyFocus_.getTranslateX() + (sceneX - _lastX));
						copyFocus_.setTranslateY(copyFocus_.getTranslateY() + (sceneY - _lastY));
					}
				}
			}
			else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
				if(copyFocus_ !=null) {
					System.out.println(copyFocus_);
					copyFocus_ = null;
					//On release update the root expression to be the closes expression to the mouse.
					rootExpression_ = (CompoundExpression) expressions.get(closestExpression);
					LinkedList<Expression> chillin = ((AbstractCompoundExpression) rootExpression_).getChildren();
					HBox hb = new HBox();
					for (int i = 0; i < chillin.size(); i++) {
						System.out.println(chillin.get(i).convertToString(0));
						hb.getChildren().add(chillin.get(i).getNode());
					}
					expressionPane.getChildren().clear();
					expressionPane.getChildren().add(hb);

					hb.setLayoutX(WINDOW_WIDTH / 2);
					hb.setLayoutY(WINDOW_HEIGHT / 2);

					closestExpression = 0;
					firstClick = !firstClick;
				}
			}
			_lastX = sceneX;
			_lastY = sceneY;

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


	private static final Pane expressionPane = new Pane();

	@Override
	public void start (Stage primaryStage) {
		primaryStage.setTitle("Expression Editor");

		// Add the textbox and Parser button
		final Pane queryPane = new HBox();
		final TextField textField = new TextField(EXAMPLE_EXPRESSION);
		final Button button = new Button("Parse");

		queryPane.getChildren().add(textField);

		// Add the callback to handle when the Parse button is pressed
		button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle (MouseEvent e) {
				// Try to parse the expression
				try {
					// Success! Add the expression's Node to the expressionPane
					final Expression expression = expressionParser.parse(textField.getText(), true);

					expressionPane.getChildren().clear();
					expression.flatten();
					expressionPane.getChildren().add(expression.getNode());
					nodeMap = generateMap(expression);
					expression.getNode().setLayoutX(WINDOW_WIDTH / 2);
					expression.getNode().setLayoutY(WINDOW_HEIGHT / 2);
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

	private static HashMap<Node, Expression> generateMap (Expression e) {

		Stack<Expression> expressionsToVisit = new Stack<>();
		HashSet<Expression> vistedExpressions = new HashSet<>();
		HashMap<Node, Expression> map = new HashMap<>();

		map.put(e.getNode(),e);
		vistedExpressions.add(e);

		if (!(e instanceof CompoundExpression)) {
			return map;
		}

		for(Expression exp : ((AbstractCompoundExpression)e).getChildren()){
			if (!vistedExpressions.contains(exp)) {
				expressionsToVisit.add(exp);
			}
		}
		while (!expressionsToVisit.empty()) {
			Expression currentExpression = expressionsToVisit.pop();

			map.put(currentExpression.getNode(), currentExpression);
			vistedExpressions.add(currentExpression);

			if (currentExpression instanceof AbstractCompoundExpression) {
				for (Expression child : ((AbstractCompoundExpression) currentExpression).getChildren()) {
					if (!vistedExpressions.contains(child)) {
						expressionsToVisit.add(child);
					}
				}
			}
		}
		return map;
	}

	public static Label newLabel(String modifierText) {
		Label text = new Label(modifierText);
		text.setFont(ExpressionEditor.FONT);

		return text;
	}
}
