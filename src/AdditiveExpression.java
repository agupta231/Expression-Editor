import javafx.scene.Node;


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

	/**
	 * Gives the Node representation of the current Additive Expression
	 * @return a Node representing the current AdditiveExpression
	 */
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

	/**
	 * Will generate a String representation for the the addition,
	 * with no whitespace.
	 * @return a flattened String representing the addition operator.
	 */
	@Override
	public String convertToStringFlat() {
		return super.convertToStringFlat("+");
	}
}