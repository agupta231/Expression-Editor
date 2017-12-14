import javafx.scene.Node;


/**
 * Child of expression that represents a multiplicative operator.
 */
public class MultiplicativeExpression extends CollapsibleExpression {
    /**
     * Will return a copy of the Additive expression to use
     * for the GUI.
     * @return an copy of the {@link MultiplicativeExpression}
     */
    public Expression deepCopy(){
        return super.deepCopy(new MultiplicativeExpression());
    }

    /**
     * Gives the Node representation of the current Multiplicative Expression
     * @return a Node representing the current AdditiveExpression
     */
    @Override
    public Node getNode() {
        return super.getNode("*");
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

    /**
     * Will generate a String representation for the the multiplication,
     * with no whitespace.
     * @return a flattened String representing the addition operator.
     */
    @Override
    public String convertToStringFlat() {
        return super.convertToStringFlat("*");
    }
}
