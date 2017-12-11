import java.util.LinkedList;

public abstract class AbstractCompoundExpression implements CompoundExpression{
    private CompoundExpression parent = null;
    private LinkedList<Expression> children = new LinkedList<Expression>();

    /**
     * Flatten's the expression's children.
     */
    public void flattenChildren() {
        for (Expression c: children){
            c.flatten();
        }
    }

    /**
     * Returns expression's parent
     * @return returns the parent.
     */
    @Override
    public CompoundExpression getParent() {
        return parent;
    }

    /**
     * Returns expression's children
     * @return expression's children
     */
    public LinkedList<Expression> getChildren(){
        return this.children;
    }

    /**
     * Sets the parent of the expression
     * @param parent the CompoundExpression that should be the parent of the target object
     */
    public void setParent(CompoundExpression parent){
        this.parent = parent;
    }

    /**
     * Sets the children of the expression
     * @param children A linkedlist of expressions.
     */
    public void setChildren(LinkedList<Expression> children){
        this.children = children;
    }

    /**
     * Adds a child to the expression
     * @param child An expression
     */
    public void addSubexpression(Expression child){
        this.children.add(child);
    }

    /**
     * Will generate a String representation for the the addition
     * operator, with the appropiate number of tabs to properly
     * represent it's position in the expression.
     * @param indentLevel the indentation level
     * @param name the name of the expression
     * @return a String containing this expression and all its children in hierarchic format.
     */

    public String convertToString(int indentLevel, String name){
        StringBuffer sb = new StringBuffer();
        Expression.indent(sb,indentLevel);
        sb.append(name + "\n");
        for(Expression e: this.getChildren()){
            sb.append(e.convertToString(indentLevel+1));
        }
        return sb.toString();
    }

    /**
     * Creates a deep copy of the expression
     * @param copy, an empty Compound expression that will be returned.
     * @return a deep copy of the expression
     */
    public AbstractCompoundExpression deepCopy(AbstractCompoundExpression copy){
        for(Expression c: this.getChildren()){
            copy.addSubexpression(c.deepCopy());
        }
        for(Expression c: copy.getChildren())
        {
            c.setParent(copy);
        }
        return copy;
    }

}
