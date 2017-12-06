/**
 * Child of expression that represents a multiplicative operator.
 */
public class MultiplicativeExpression extends CollapsibleExpression{

    /**
     * Will return a copy of the Additive expression to use
     * for the GUI.
     * @return an copy of the {@link MultiplicativeExpression}
     */
    public Expression deepCopy(){
        MultiplicativeExpression copy = new MultiplicativeExpression();
        return super.deepCopy(copy);
    }

    /**
     * Will generate a String representation for the the addition
     * operator, with the appropiate number of tabs to properly
     * represent it's position in the expression.
     * @param indentLevel how many "levels" down the operator is
     * @return a String representing the multiplicative operator.
     */
    public String convertToString(int indentLevel){
        return super.convertToString(indentLevel,"*");
    }

}
