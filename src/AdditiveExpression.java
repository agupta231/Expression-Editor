import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

/**
 * Child of expression that represents an addition operator.
 */
public class AdditiveExpression extends CollapsibleExpression {

	/**
	 * Will return a copy of the Additive expression to use
	 * for the GUI.
	 * @return a copy of the {@link AdditiveExpression}
	 */
	public Expression deepCopy() {
		return super.deepCopy(new AdditiveExpression());
	}

	public Node getNode() {
		return super.getNode("+");
	}

	/**
	 * Will generate a String representation for the the addition
	 * operator, with the appropiate number of tabs to properly
	 * represent it's position in the expression.
	 * @param indentLevel how many "levels" down the operator is
	 * @return a String representing the addition operator.
	 */
	public String convertToString(int indentLevel) {
		return super.convertToString(indentLevel,"+");
	}

}