import javafx.application.Application;
import java.util.*;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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
        int numberOfClicks;
        boolean restart = false;

		double _lastX, _lastY;

		MouseEventHandler (Pane pane_, CompoundExpression rootExpression_) {
			this.rootExpression_ = rootExpression_;
			this.currentFocus_ = this.rootExpression_.getNode();
			this.currentPane = pane_;
			numberOfClicks = 0;
		}

		public void handle (MouseEvent event) {
			final double sceneX = event.getSceneX();
			final double sceneY = event.getSceneY();


			//If the mouse is clicked, look at the clicked location and check each node to see if it was clicked.
			if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
				numberOfClicks ++;
				if(numberOfClicks%2 != 0) {
					handleClick(event, sceneX, sceneY);
				}

			}
			else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED && !restart && numberOfClicks%2 == 0) {
				handleDrag(event, sceneX, sceneY);
			}
			else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
				handleRelease(event, sceneX, sceneY);
			}
			_lastX = sceneX;
			_lastY = sceneY;

		}


		private void handleClick(MouseEvent event, double sceneX, double sceneY){
			//Get the children of the current focus to be checked
			ObservableList<Node> HChildren = ((HBox) currentFocus_).getChildren();

			//If the focus is a single variable then don't bother checking for
			if(HChildren.size() == 1 && event.isDragDetect()){
				return;
			}

			boolean found = false;
			//Go through each child of the current focus
			for (int i = 0; i < HChildren.size(); i++) {
				final Node currentNode = HChildren.get(i);
				//If the current node is a HBox (so not a Label) check it
				if (currentNode instanceof HBox) {
					//Turn the click into terms of the current node
					Point2D relativeClick = currentNode.sceneToLocal(sceneX, sceneY);

					if (currentNode.contains(relativeClick.getX(), relativeClick.getY())) {
						//Change the focus
						previousFocus = currentFocus_;
						currentFocus_ = currentNode;

						Point2D currentLocation = currentFocus_.localToScene(currentFocus_.getLayoutX(), currentFocus_.getLayoutY());
						//Make Label to follow around mouse
						copyFocus_ = newLabel(((CopyAble) nodeMap.get(currentFocus_)).convertToStringFlat());
						copyFocus_.setLayoutX(currentLocation.getX()-currentNode.getLayoutX());
						copyFocus_.setLayoutY(currentLocation.getY()-((HBox) currentNode).getHeight()/2);

						//expressionPane.getChildren().add(copyFocus_);

						if(currentFocus_ instanceof Label){
							break;
						}
						//Deselect previous focus
						if(numberOfClicks%2 != 0) {
							((HBox) previousFocus).setBorder(Expression.NO_BORDER);
							((HBox) currentNode).setBorder(Expression.RED_BORDER);
						}
						found = true;
					}
				}
			}
			//If it wasn't found then the user didn't click on anything so restart focus
			if(!found) {
				numberOfClicks = 0;
				((HBox) currentFocus_).setBorder(Expression.NO_BORDER);
				restart = true;
			}
			//As long as something was selected begin the process generating all the possible trees
			Expression focusedExpression = nodeMap.get(currentFocus_);
			if(!currentFocus_.equals(rootExpression_.getNode())) {

				distances.clear();
				expressions.clear();

				for (Expression e : AbstractCompoundExpression.generateAllPossibleTrees((focusedExpression.getParent()).deepCopy(), focusedExpression.convertToString(0))) {
					//Calls function that calculates the x position of each possibility
					getWidthOfNode(e, this.currentFocus_);
					//After gets the current possibility by checking which is the closest
				}
                calculateClosestPosition(sceneX, sceneY);
            }
		}

		private void handleDrag(MouseEvent event, double sceneX, double sceneY){
			//Calculate the closest position
			calculateClosestPosition(sceneX, sceneY);
			//Get the closest orientation then append it to the root node in order to create the full tree

            addToRoot(expressions.get(closesExpression), ((AbstractCompoundExpression)expressions.get(closesExpression)).getChildren().get(0));
			//Generate the next tree
			HBox hb = generateHBox();
			//Display the new tree
			expressionPane.getChildren().clear();
			expressionPane.getChildren().add(hb);
			expressionPane.getChildren().add(copyFocus_);


			recolor(currentFocus_, Expression.GHOST_COLOR);

			hb.setLayoutX(WINDOW_WIDTH / 2);
			hb.setLayoutY(WINDOW_HEIGHT / 2);
			//If the root is not the current focus move the current focus in the direction of the mouse
			if(copyFocus_ != rootExpression_.getNode()) {
				copyFocus_.setTranslateX(copyFocus_.getTranslateX() + (sceneX - _lastX));
				copyFocus_.setTranslateY(copyFocus_.getTranslateY() + (sceneY - _lastY));
			}
		}

		private void handleRelease(MouseEvent event, double sceneX, double sceneY) {
			recolor(currentFocus_, Color.BLACK);
			if(numberOfClicks%2 == 0) {
				copyFocus_ = new HBox();
			}
			//On release update the root expression to be the closes expression to the mouse, but only do this if
			//not de-selecting the focus
			if(!restart && numberOfClicks%2 == 0 && (Math.abs(_lastX-sceneX) != 0)) {
                calculateClosestPosition(sceneX, sceneY);

                addToRoot(expressions.get(closesExpression), ((AbstractCompoundExpression) expressions.get(closesExpression)).getChildren().get(0));
			}

			HBox hb = generateHBox();
			//If de-selecting the focus (restart == true) rebuild the root expression out of the current
			//expression on the screen
			if(restart){
				try {
					final Expression expression = expressionParser.parse(hbToString(hb), true);

					expressionPane.getChildren().clear();
					expressionPane.getChildren().add(expression.getNode());

					expression.getNode().setLayoutX(WINDOW_WIDTH / 2);
					expression.getNode().setLayoutY(WINDOW_HEIGHT / 2);

					rootExpression_ = ((CompoundExpression) expression);
					currentFocus_ = rootExpression_.getNode();
					expression.flatten();
					nodeMap = generateMap(expression);
					restart = false;
				} catch (ExpressionParseException epe) {
				}
			}else {
				//If not just update the pane
				expressionPane.getChildren().clear();
				expressionPane.getChildren().add(hb);

				hb.setLayoutX(WINDOW_WIDTH / 2);
				hb.setLayoutY(WINDOW_HEIGHT / 2);
			}
			System.out.println(rootExpression_.convertToString(0));
			closesExpression = 0;
		}

		private HBox generateHBox(){
			HBox hb = new HBox();
			//Since rootExpression_.getNode() returns a list of nodes not in order, get the children
			//which is a linked list
			LinkedList<Expression> children = ((AbstractCompoundExpression)rootExpression_).getChildren();

			//type values
			//1: Additive
			//2: Multiplative
			//3: Parenthetical
			int type = 0;
			//Since only the children of the rootExpression_ are preserved in the children linked list
			//the type of expression must be figured out.
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
				//Go through the children
				for (int i = 0; i < children.size(); i++) {
					//Check to see if any children are added, if so add a sign between them
					if (type == 1 && hb.getChildren().size() != 0) {
						hb.getChildren().add(newLabel("+"));
					} else if (type == 2 && hb.getChildren().size() != 0) {
						hb.getChildren().add(newLabel("*"));
					} else if (type == 3) {
						hb.getChildren().add(newLabel("("));
					}

					//Check to see if branch contains focus because focus is the only thing that can change
					//so a next HBox must be made for it
					boolean containsFocus = checkForFocus(children.get(i));
					if (containsFocus) {
						//If the node contains the focus then "fix" the focus by manually creating an HBox
						HBox t = fixFocus(children.get(i));
						hb.getChildren().add(t);
					} else {
						//If not just add the node since the order is preserved and not altered by user
						hb.getChildren().add(children.get(i).getNode());
					}
					if (type == 3) {
						hb.getChildren().add(newLabel(")"));
					}
				}
			}
			return hb;
		}

		private void calculateClosestPosition(double sceneX, double sceneY){
			int minDistance = Integer.MAX_VALUE;
			//Goes through each distance and sets closestExperssion to which ever one is the closest
			for(int i = 0; i < distances.size(); i++){
				int localX = (int) expressions.get(0).getNode().sceneToLocal(sceneX, sceneY).getX();
				if(Math.abs(localX-distances.get(i)) < minDistance){
					minDistance = Math.abs(localX-distances.get(i));
					closesExpression = i;
				}
			}
		}

		//Recusively goes down HBox until reaches Label then appends all the values together
        private String hbToString(HBox h){
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

		//Creates a new HBox in the direction of the focus
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

			LinkedList<Expression> children = ((AbstractCompoundExpression)e).getChildren();

			for(int i = 0; i < children.size(); i++){
				if(type == 1 && hb.getChildren().size() != 0){
					hb.getChildren().add(newLabel("+"));
				}else if(type == 2 && hb.getChildren().size() != 0){
					hb.getChildren().add(newLabel("*"));
				}else if(type == 3){
					hb.getChildren().add(newLabel("("));
				}
				if(!(children.get(i) instanceof LiteralExpression)) {
					boolean containsFocus = checkForFocus(children.get(i));
					if (containsFocus) {
						HBox t = fixFocus(children.get(i));
						hb.getChildren().add(t);
					} else {
						hb.getChildren().add(children.get(i).getNode());
					}
				}else{
					hb.getChildren().add(children.get(i).getNode());
				}
				if(type == 3){
					hb.getChildren().add(newLabel(")"));
				}
			}
			return hb;
		}

		private boolean checkForFocus(Expression child){
			if(!(child instanceof LiteralExpression)) {
				LinkedList<Expression> children = ((AbstractCompoundExpression) child).getChildren();
				for (int i = 0; i < children.size(); i++) {
					if (children.get(i).getNode().equals(currentFocus_)) {
						return true;
					} else if (children.get(i) instanceof AbstractCompoundExpression) {
						boolean t = checkForFocus(children.get(i));
						if(t){
						    return t;
                        }
					}
				}
			}
			return false;
		}

		//Function to find node and replace it with the new order
		private void addToRoot(Expression parentE, Expression e){
			addToRootHelper(parentE, e, ((AbstractCompoundExpression) rootExpression_).getChildren(), rootExpression_);
		}

		private void addToRootHelper(Expression parentE, Expression e, LinkedList<Expression> children, Expression parent){
			for(int i = 0; i < children.size(); i++){
				if(children.get(i).equals(e)){
					((AbstractCompoundExpression)parent).setChildren(((AbstractCompoundExpression)parentE).getChildren());
				}
			}
			for(int i = 0; i < children.size(); i++){
				if(!(children.get(i) instanceof LiteralExpression)) {
					addToRootHelper(parentE, e, ((AbstractCompoundExpression) children.get(i)).getChildren(), children.get(i));
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
	private static final ExpressionParser expressionParser = new SimpleExpressionParser();


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

	private static HashMap<Node, Expression> generateMap(Expression e) {

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
