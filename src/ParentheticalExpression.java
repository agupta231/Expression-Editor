/**
 * Child of expression that represents an parenthetical operator.
 */

public class ParentheticalExpression extends AbstractCompoundExpression {

    /**
     * Will return a copy of the Additive expression to use
     * for the GUI.
     * @return an copy of the {@link ParentheticalExpression}
     */
    public Expression deepCopy(){
        ParentheticalExpression copy = new ParentheticalExpression();
        return super.deepCopy(copy);
    }

    /**
     * Will generate a String representation for the the addition
     * operator, with the appropiate number of tabs to properly
     * represent it's position in the expression.
     * @param indentLevel how many "levels" down the operator is
     * @return a String representing the parenthetical operator.
     */
    public String convertToString(int indentLevel){
        return super.convertToString(indentLevel,"()");
    }

    /**
     * Flattens the expression and it's children
     */
    public void flatten(){
        flattenChildren();
    }
}
