/**
 * Child of expression that represents an addition operator.
 */
public class AdditiveExpression extends CollapsibleExpression{
	/**
	 * Will return a copy of the Additive expression to use
	 * for the GUI.
	 * @return an copy of the {@link AdditiveExpression}
	 */
    public Expression deepCopy() {
        AdditiveExpression copy = new AdditiveExpression();
        return super.deepCopy(copy);
    }

	/**
	 * Will generate a String representation for the the addition
	 * operator, with the appropiate number of tabs to properly
	 * represent it's position in the expression.
	 * @param indentLevel how many "levels" down the operator is
	 * @return a String representing the addition operator.
	 */
    public String convertToString(int indentLevel){
        return super.convertToString(indentLevel,"+");
    }

}
