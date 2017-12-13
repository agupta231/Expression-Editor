import org.junit.runner.Computer;

import java.util.LinkedList;

public abstract class AbstractCompoundExpression implements CompoundExpression, CopyAble{
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
        child.setParent(this);
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
            copy.addSubexpression(c);
        }
        for(Expression c: copy.getChildren())
        {
            c.setParent(copy);
        }
        return copy;
    }

    @Override
    public Expression trueCopy() {
        if(this.getParent() == null) {
            return this.deepCopy();
        }

        System.out.println("Start");
        System.out.println("Current Chill'n: ");
        System.out.println(this.convertToString(0));
        System.out.println("Parent: ");
        System.out.println(((AbstractCompoundExpression) this.getParent()).convertToString(0));

        AbstractCompoundExpression parent =  ((AbstractCompoundExpression) this.getParent());

//        try {
            AbstractCompoundExpression copy = (AbstractCompoundExpression) parent.trueCopy();

            for(Expression e:copy.getChildren()) {
                if(e.convertToString(0).equals(this.convertToString(0)))
                    return e;
                }
            }
//        }
//        catch (Exception ClassCastException){
//            System.out.println("This:\n" + this.convertToString(0));
//            System.out.println("Parent:\n" + parent.convertToString(0));
//            System.out.println("Parent's parent" + parent.getParent());
//            System.out.println("Parent's true:\n"+ parent.trueCopy());
//        }

        return null;

    }
    public static LinkedList<Expression> generateAllPossibleTrees(Expression parent, String selected) {

        Expression focused = null;

        for(Expression child : ((AbstractCompoundExpression) parent).getChildren()) {
            if (child.convertToString(0).equals(selected)) {
                focused = child;
                break;
            }
        }

        final LinkedList<Expression> children = ((AbstractCompoundExpression) parent).getChildren();
        final int childrenSize = children.size();

        int nodeIndex = -1;

        for(int i = 0; i < childrenSize; i++) {
            if (children.get(i) == focused) {
                nodeIndex = i;
                break;
            }
        }

        if (nodeIndex == -1) {
            return new LinkedList<>();
        }

        children.remove(nodeIndex);

        LinkedList<Expression> possibleTrees = new LinkedList<>();

        for (int i = 0; i < childrenSize; i++) {
            AbstractCompoundExpression tempParent = (AbstractCompoundExpression) ((AbstractCompoundExpression) parent).trueCopy();
            LinkedList<Expression> orderedChildren = new LinkedList<>();

            for (int j = 0; j < children.size(); j++) {
                if (j == i) {
                    orderedChildren.add(focused);
                }

                orderedChildren.add(children.get(j));
            }

            if(i == childrenSize - 1) {
                orderedChildren.add(focused);
            }

            tempParent.setChildren(orderedChildren);
            possibleTrees.add(tempParent);
        }

        return possibleTrees;
    }
}
