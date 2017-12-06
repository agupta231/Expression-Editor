public class LiteralExpression implements Expression {

    private CompoundExpression parent;
    private String literal;

    /**
     * Converts the expression to string
     * @param indentLevel the indentation level
     * @return a String containing the expression indented
     */
    public String convertToString(int indentLevel){
        StringBuffer sb = new StringBuffer();
        Expression.indent(sb,indentLevel);
        return sb.append(literal + "\n").toString();
    }

    /**
     * Sets the parent of the expression
     * @param parent the CompoundExpression that should be the parent of the target object
     */

    public void setParent(CompoundExpression parent){
        this.parent = parent;
    }

    /**
     * Returns the parent of the expression
     * @return the parent of the expression
     */
    public CompoundExpression getParent() {
        return this.parent;
    }

    /**
     * Returns a deep copy of the expression
     * @return a deep copy of the expression
     */
    public LiteralExpression deepCopy(){
        LiteralExpression copy = new LiteralExpression();
        copy.parent = (CompoundExpression) parent.deepCopy();
        copy.literal = this.literal;
        return copy;
    }

    /**
     * Sets the literal of the expression
     * @param Literal , the literal of the expression.
     */
    public void setLiteral(String Literal){
        this.literal = Literal;
    }

    /**
     * Flattens the expression
     * Does nothing, but higher functions may call it sometimes
     */
    public void flatten(){
    }
}
