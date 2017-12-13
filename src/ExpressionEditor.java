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
	//TODO: Akash look through this
	private static class MouseEventHandler implements EventHandler<MouseEvent> {
		CompoundExpression rootExpression_;
		Node currentFocus_;
		Node copyFocus_;
		Node previousFocus;
		Pane currentPane;
        ArrayList<Integer> distances = new ArrayList<Integer>();
        ArrayList<Expression> expressions = new ArrayList<Expression>();
        int closesExpression;

		double _lastX, _lastY;

		MouseEventHandler (Pane pane_, CompoundExpression rootExpression_) {
			this.rootExpression_ = rootExpression_;
			this.currentFocus_ = this.rootExpression_.getNode();
			this.currentPane = pane_;
		}

		public void handle (MouseEvent event) {
			final double sceneX = event.getSceneX();
			final double sceneY = event.getSceneY();

			if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
				ObservableList<Node> HChildren = ((HBox) currentFocus_).getChildren();
				System.out.println(HChildren);
				if(HChildren.size() == 1 && event.isDragDetect()){
					return;
				}

				boolean found = false;
				//System.out.println("FUCK CS");
				//System.out.println(HChildren);
				for (int i = 0; i < HChildren.size(); i++) {
					final Node currentNode = HChildren.get(i);
					if (currentNode instanceof HBox && !event.isDragDetect()) {
						System.out.println("Has HBox's");
						Point2D relativeClick = currentNode.sceneToLocal(sceneX, sceneY);
						//System.out.println(currentNode.contains(relativeClick.getX(), relativeClick.getY()));
						if (currentNode.contains(relativeClick.getX(), relativeClick.getY())) {
							System.out.println("Click found!");

							previousFocus = currentFocus_;
							currentFocus_ = currentNode;
							Point2D currentLocation = currentFocus_.localToScene(currentFocus_.getLayoutX(), currentFocus_.getLayoutY());

							copyFocus_ = newLabel(((CopyAble) nodeMap.get(currentFocus_)).convertToStringFlat());
							copyFocus_.setLayoutX(currentLocation.getX());
							copyFocus_.setLayoutY(currentLocation.getY());

							expressionPane.getChildren().add(copyFocus_);

							if(currentFocus_ instanceof Label){
								break;
							}
							((HBox) previousFocus).setBorder(Expression.NO_BORDER);
							((HBox) currentNode).setBorder(Expression.RED_BORDER);
							found = true;
						}
					}
				}
				if(!found) {
					System.out.println("No click found");
					((HBox) currentFocus_).setBorder(Expression.NO_BORDER);
					currentFocus_ = rootExpression_.getNode();
					distances = new ArrayList<>();
					expressions = new ArrayList<>();

				}

                Expression focusedExpression = nodeMap.get(currentFocus_);
                if(!currentFocus_.equals(rootExpression_.getNode())) {
					System.out.println(focusedExpression);
					for (Expression e : AbstractCompoundExpression.generateAllPossibleTrees(
                            ((AbstractCompoundExpression) focusedExpression.getParent()).deepCopy(),
                            focusedExpression.convertToString(0))) {
						getWidthOfNode(e, this.currentFocus_);
                        int minDistance = Integer.MAX_VALUE;
                        for(int i = 0; i < distances.size(); i++){
                            int localX = (int) expressions.get(0).getNode().sceneToLocal(sceneX, sceneY).getX();
                            if(Math.abs(localX-distances.get(i)) < minDistance){
                                minDistance = Math.abs(localX-distances.get(i));
                                closesExpression = i;
                            }
                        }
                    }
                }

				System.out.println("hbToString: " + hbToString((HBox) currentFocus_));
			}
			else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED && !currentFocus_.equals(rootExpression_.getNode())) {
			    int minDistance = Integer.MAX_VALUE;
			    for(int i = 0; i < distances.size(); i++){
					int localX = (int) expressions.get(0).getNode().sceneToLocal(sceneX, sceneY).getX();
					if(Math.abs(localX-distances.get(i)) < minDistance){
						minDistance = Math.abs(localX-distances.get(i));
                        closesExpression = i;
                    }
                }
				addToRoot(expressions.get(closesExpression), ((AbstractCompoundExpression)expressions.get(closesExpression)).getChildren().get(0));
				LinkedList<Expression> chillin = ((AbstractCompoundExpression)rootExpression_).getChildren();
				HBox hb = new HBox();

				//type values
				//1: Additive
				//2: Multiplative
				//3: Parenthetical
				int type = 0;
				if(rootExpression_ instanceof AdditiveExpression){
					type = 1;
				}else if(rootExpression_ instanceof MultiplicativeExpression){
					type = 2;
				}else if(rootExpression_ instanceof  ParentheticalExpression){
					type = 3;
				}else if(rootExpression_ instanceof LiteralExpression){
					type = 4;
					hb.getChildren().add(rootExpression_.getNode());
				}

				if(type != 4) {
					for (int i = 0; i < chillin.size(); i++) {
						if (type == 1 && hb.getChildren().size() != 0) {
							hb.getChildren().add(newLabel("+"));
						} else if (type == 2 && hb.getChildren().size() != 0) {
							hb.getChildren().add(newLabel("*"));
						} else if (type == 3) {
							hb.getChildren().add(newLabel("("));
						}
						boolean containsFocus = checkForFocus(chillin.get(i));
						if (containsFocus) {
							HBox t = fixFocus(chillin.get(i));
							hb.getChildren().add(t);
						} else {
							hb.getChildren().add(chillin.get(i).getNode());
						}
						if (type == 3) {
							hb.getChildren().add(newLabel(")"));
						}
					}
				}
				expressionPane.getChildren().clear();
				expressionPane.getChildren().add(hb);
				expressionPane.getChildren().add(copyFocus_);

				recolor(currentFocus_, Expression.GHOST_COLOR);

				hb.setLayoutX(WINDOW_WIDTH / 2);
				hb.setLayoutY(WINDOW_HEIGHT / 2);

				if(copyFocus_ != rootExpression_.getNode()) {
					copyFocus_.setTranslateX(copyFocus_.getTranslateX() + (sceneX - _lastX));
					copyFocus_.setTranslateY(copyFocus_.getTranslateY() + (sceneY - _lastY));
				}
			}
			else if (event.getEventType() == MouseEvent.MOUSE_RELEASED && !currentFocus_.equals(rootExpression_.getNode())) {
			    recolor(currentFocus_, Color.BLACK);

				copyFocus_ = new HBox();
				//On release update the root expression to be the closes expression to the mouse.
				System.out.println(expressions);
				addToRoot(expressions.get(closesExpression), ((AbstractCompoundExpression)expressions.get(closesExpression)).getChildren().get(0));
				LinkedList<Expression> chillin = ((AbstractCompoundExpression)rootExpression_).getChildren();
				HBox hb = new HBox();

				//type values
				//1: Additive
				//2: Multiplative
				//3: Parenthetical
				int type = 0;
				if(rootExpression_ instanceof AdditiveExpression){
					type = 1;
				}else if(rootExpression_ instanceof MultiplicativeExpression){
					type = 2;
				}else if(rootExpression_ instanceof  ParentheticalExpression){
					type = 3;
				}else if(rootExpression_ instanceof LiteralExpression){
					type = 4;
					hb.getChildren().add(rootExpression_.getNode());
				}
				if(type != 4) {
					for (int i = 0; i < chillin.size(); i++) {
						if (type == 1 && hb.getChildren().size() != 0) {
							hb.getChildren().add(newLabel("+"));
						} else if (type == 2 && hb.getChildren().size() != 0) {
							hb.getChildren().add(newLabel("*"));
						} else if (type == 3) {
							hb.getChildren().add(newLabel("("));
						}
						boolean containsFocus = checkForFocus(chillin.get(i));
						if (containsFocus) {
							HBox t = fixFocus(chillin.get(i));
							hb.getChildren().add(t);
						} else {
							hb.getChildren().add(chillin.get(i).getNode());
						}
						if (type == 3) {
							hb.getChildren().add(newLabel(")"));
						}
					}
				}
				expressionPane.getChildren().clear();
				expressionPane.getChildren().add(hb);
				String root = hbToString(hb);
				System.out.println(root);
				hb.setLayoutX(WINDOW_WIDTH / 2);
				hb.setLayoutY(WINDOW_HEIGHT / 2);

				System.out.println(rootExpression_.convertToString(0));

                closesExpression = 0;
			}
			_lastX = sceneX;
			_lastY = sceneY;

		}

//		private Expression hbToExpression(HBox h) {
//			String stringRep = hbToString(h);
//		}

		private String hbToString(HBox h){
			ObservableList<Node> babies = h.getChildren();
			String result = "";

			for (Node baby : h.getChildren()) {
				if(baby instanceof Label) {
					result += ((Label) baby).getText();
				}
				else {
					result += hbToString((HBox) baby);
				}
			}

			return result;
		}

		private HBox fixFocus(Expression e){
			HBox hb = new HBox();

			int type = 0;
			if(e instanceof AdditiveExpression){
				type = 1;
			}else if(e instanceof MultiplicativeExpression){
				type = 2;
			}else if(e instanceof  ParentheticalExpression){
				type = 3;
			}

			LinkedList<Expression> chillin = ((AbstractCompoundExpression)e).getChildren();

			for(int i = 0; i < chillin.size(); i++){
				if(type == 1 && hb.getChildren().size() != 0){
					hb.getChildren().add(newLabel("+"));
				}else if(type == 2 && hb.getChildren().size() != 0){
					hb.getChildren().add(newLabel("*"));
				}else if(type == 3){
					hb.getChildren().add(newLabel("("));
				}
				if(!(chillin.get(i) instanceof LiteralExpression)) {
					boolean containsFocus = checkForFocus(chillin.get(i));
					if (containsFocus) {
						HBox t = fixFocus(chillin.get(i));
						hb.getChildren().add(t);
					} else {
						hb.getChildren().add(chillin.get(i).getNode());
					}
				}else{
					hb.getChildren().add(chillin.get(i).getNode());
				}
				if(type == 3){
					hb.getChildren().add(newLabel(")"));
				}
			}
			return hb;
		}

		private boolean checkForFocus(Expression child){
			LinkedList<Expression> ll = ((AbstractCompoundExpression) child).getChildren();
			for(int i = 0; i < ll.size(); i++){
				if(ll.get(i).getNode().equals(currentFocus_)){
					return true;
				}else if(ll.get(i) instanceof AbstractCompoundExpression){
					return checkForFocus(ll.get(i));
				}
			}
			return false;
		}

		private void addToRoot(Expression parentE, Expression e){
			addToRootHelper(parentE, e, ((AbstractCompoundExpression) rootExpression_).getChildren(), rootExpression_);
		}

		private void addToRootHelper(Expression parentE, Expression e, LinkedList<Expression> ll, Expression parent){
			for(int i = 0; i < ll.size(); i++){
				if(ll.get(i).equals(e)){
					((AbstractCompoundExpression)parent).setChildren(((AbstractCompoundExpression)parentE).getChildren());
				}
			}
			for(int i = 0; i < ll.size(); i++){
				if(!(ll.get(i) instanceof LiteralExpression)) {
					addToRootHelper(parentE, e, ((AbstractCompoundExpression) ll.get(i)).getChildren(), ll.get(i));
				}
			}
		}

		private void getWidthOfNode(Expression e, Node n){
			getWidthOfNodeHelper(e, n, ((AbstractCompoundExpression) e).getChildren());
		}

		private void getWidthOfNodeHelper(Expression e, Node n, LinkedList<Expression> ll){
			for(int i = 0; i < ll.size(); i++){
				if (ll.get(i).getNode().equals(n)){
					getWidthOfTree(e, n, ll);
					return;
				}
			}
			for(int i = 0; i < ll.size(); i++){
				if(!(ll.get(i) instanceof LiteralExpression)){
					getWidthOfNodeHelper(e, n, ((AbstractCompoundExpression) ll.get(i)).getChildren());
				}
			}
		}
		private void getWidthOfTree(Expression e, Node n, LinkedList<Expression> ll){
			int currentX = -1;
			for(int i = 0; i < ll.size(); i++){
				if (ll.get(i).getNode().equals(n)){
					currentX = (int) ll.get(i).getNode().getLayoutX();
				}
			}
			int totalWidth = 0;
			for(int i = 0; i < ll.size(); i++){
				ll.get(i).getNode().getLayoutBounds().getWidth();
				if(ll.get(i).getNode().equals(this.currentFocus_)){
					totalWidth += ll.get(i).getNode().getLayoutBounds().getWidth()/2;
					break;
				}
				totalWidth += ll.get(i).getNode().getLayoutBounds().getWidth();
			}
			distances.add(totalWidth);
			expressions.add(e);
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
					expressionPane.getChildren().add(expression.getNode());

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

					expression.flatten();
					nodeMap = generateMap(expression);
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

	private HashMap<Node, Expression> generateMap (Expression e) {

		Stack<Expression> expressionsToVisit = new Stack<>();
		HashSet<Expression> vistedExpressions = new HashSet<>();
		HashMap<Node, Expression> map = new HashMap<>();

		map.put(e.getNode(), ((CopyAble)e).trueCopy());
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

			map.put(currentExpression.getNode(), ((CopyAble)currentExpression).trueCopy());
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

    public static void recolor(Node n, Color newColor) {
        if (n instanceof Label) {
            ((Label) n).setTextFill(newColor);
        }
        else {
            for (Node child : ((HBox) n).getChildren()) {
                recolor(child, newColor);
            }
        }
    }
}
