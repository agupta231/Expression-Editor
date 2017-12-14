import javafx.scene.Node;
import javafx.scene.layout.HBox;
import java.util.LinkedList;

/**
 * Child function of CompoundExpression. Meant to abstract a lot of the code in
 * Multiplicative and Additive Expression classes
 */
public abstract class AbstractCompoundExpression implements CompoundExpression, CopyAble {
    private CompoundExpression parent = null;
    private LinkedList<Expression> children = new LinkedList<Expression>();
    protected boolean focused;
    protected Node node;

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
     * Will generate a String representation for the the
     * operator, with the appropiate number of tabs to properly
     * represent it's position in the expression.
     * @param indentLevel the indentation level
     * @param name what the current operator is
     * @return a String containing this expression and all its children in hierarchic format.
     */

    public String convertToString(int indentLevel, String name) {
        StringBuffer sb = new StringBuffer();
        Expression.indent(sb,indentLevel);
        sb.append(name + "\n");

        for(Expression e: this.getChildren()){
            sb.append(e.convertToString(indentLevel+1));
        }

        return sb.toString();
    }

    /**
     * Will generate a String representation for the operator, with no
     * space though.
     * @param delimiter the operator
     * @return a String containing this expresison.
     */
    public String convertToStringFlat(String delimiter) {
        String outputString = "";

        for(int i = 0; i < this.getChildren().size() - 1; i++) {
            outputString += ((CopyAble) this.getChildren().get(i)).convertToStringFlat();
            outputString += delimiter;
        }

        outputString += ((CopyAble) this.getChildren().get(this.getChildren().size() - 1)).convertToStringFlat();

        return outputString;
    }

    /**
     * Creates a deep copy of the expression
     * @param copy, an empty Compound expression that will be returned.
     * @return a deep copy of the expression
     */
    public AbstractCompoundExpression deepCopy(AbstractCompoundExpression copy){
        for(Expression c: this.getChildren()) {
            copy.addSubexpression(c);
        }
        for(Expression c: copy.getChildren()) {
            c.setParent(copy);
        }

        copy.node = node;
        return copy;
    }

    /**
     * Abstracted function for getNode() for Multiplicative and Additive Expressions
     * @param delimiter the operator
     * @return a Node represeneting the current Expression
     */
    public Node getNode(String delimiter) {
        if(node == null) {
            final HBox hbox = new HBox();
            hbox.getChildren().add(this.getChildren().get(0).getNode());
            for (int i = 1; i < this.getChildren().size(); i++) {
                hbox.getChildren().add(ExpressionEditor.newLabel(delimiter));
                hbox.getChildren().add(this.getChildren().get(i).getNode());
            }
            if (this.getFocused()) {
                hbox.setBorder(RED_BORDER);
            }
            node = hbox;
            return hbox;
        }

        return node;
    }

    /**
     * Getter for if the current expression is focused.
     * @return true if the expression is focused, false
     *         otherwise
     */
    public boolean getFocused() {
        return focused;
    }

    /**
     * Setter for if the current expression is focused.
     * @param s A boolean representing if the current
     *          expression is focused or not
     */
    public void setFocused(boolean s) {
        focused = s;
    }

    /**
     * Will create a copy of the current expression, and transfer
     * the Node pointers as well
     * @return a copy of Expression with old Node pointers
     */
    @Override
    public Expression trueCopy() {
        if(this.getParent() == null) {
            return this.deepCopy();
        }

        AbstractCompoundExpression parent =  ((AbstractCompoundExpression) this.getParent());
        AbstractCompoundExpression copy = (AbstractCompoundExpression) parent.trueCopy();

        for(Expression e:copy.getChildren()) {
            if(e.convertToString(0).equals(this.convertToString(0))) {
                return e;
            }
        }

        return null;
    }

    /**
     * Will generate all possible trees that an expression can take
     * @param parent Parent of the focused expression
     * @param selected the focused expression, represented by the .convertToString(0) method
     * @return a LinkedList of Expressions of all of the permutations of the Expression
     */
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
