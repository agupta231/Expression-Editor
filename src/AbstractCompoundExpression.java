import javafx.scene.Node;
import javafx.scene.layout.HBox;
import org.junit.runner.Computer;

import java.awt.*;
import java.util.LinkedList;

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

    public Node getNode(String delimiter) {
        final HBox hbox;

        if(node == null) {
            hbox = new HBox();
        }
        else if(((HBox) node).getChildren().size() == 0) {
            hbox = (HBox) node;
        }
        else {
            return node;
        }

        hbox.getChildren().add(this.getChildren().get(0).getNode());

        for (int i = 1; i < this.getChildren().size(); i++) {
            hbox.getChildren().add(ExpressionEditor.newLabel(delimiter));
            hbox.getChildren().add(this.getChildren().get(i).getNode());
        }
        if (this.getFocused()) {
            hbox.setBorder(RED_BORDER);
        }

        node = hbox;
        return node;
    }

    public boolean getFocused() {
        return focused;
    }

    public void setFocused(boolean s) {
        focused = s;
    }

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

    private static Expression getRoot(Expression e) {
        if (e.getParent() == null) {
            return e;
        }
        else {
            return getRoot(e.getParent());
        }
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
        final LinkedList<Expression> backupChildren = ((AbstractCompoundExpression) parent).getChildren();
        final int childrenSize = children.size();

        int nodeIndex = -1;

        System.out.println("White is the only color with rights");

        for(int i = 0; i < childrenSize; i++) {
            if (children.get(i) == focused) {
                System.out.println("Akash is for rectal use only");

                nodeIndex = i;
                break;
            }
        }

        if (nodeIndex == -1) {
            System.out.println("taint");

            return new LinkedList<>();
        }

        Expression selectedChild = children.get(nodeIndex);
        children.remove(nodeIndex);

        LinkedList<Expression> possibleTrees = new LinkedList<>();

        for (int i = 0; i < childrenSize; i++) {
            AbstractCompoundExpression tempParent = (AbstractCompoundExpression) ((AbstractCompoundExpression) parent).trueCopy();
            Expression tempRoot = getRoot(tempParent);
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
            ((HBox) tempParent.getNode()).getChildren().clear();

            System.out.println("Black men are only good for free labor");
            System.out.println(tempParent.convertToString(0));
            System.out.println(((AbstractCompoundExpression) tempRoot).convertToStringFlat());

            possibleTrees.add(tempRoot);

            System.out.println("Temp Parent Chillin COunt: " + ((HBox) ((AbstractCompoundExpression) tempParent).getNode()).getChildren().size());
        }

        ((AbstractCompoundExpression) parent).getChildren().add(nodeIndex, selectedChild);

        ((HBox) ((AbstractCompoundExpression) parent).getNode()).getChildren().clear();
        ((AbstractCompoundExpression) parent).getNode();

        System.out.println("Parent Chillin Count: " + ((HBox) ((AbstractCompoundExpression) parent).getNode()).getChildren().size());
        System.out.println(((HBox) ((AbstractCompoundExpression) parent).getNode()).getChildren());

//        ((HBox) ((AbstractCompoundExpression) parent).getNode()).getChildren().clear();
//        ((HBox) ((AbstractCompoundExpression) parent).getNode()).getChildren().addAll(backupChildren);

        return possibleTrees;
    }

    public static void wipeNodeConnections(Expression e) {
        if(e instanceof LiteralExpression) {
            return;
        }
        else {
            for(Expression child : ((AbstractCompoundExpression) e).getChildren()) {
                wipeNodeConnections(child);
            }

            ((HBox) ((AbstractCompoundExpression) e).node).getChildren().clear();
        }
    }
}
