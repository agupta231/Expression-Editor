import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Child of expression that represents an parenthetical operator.
 */

public class ParentheticalExpression extends AbstractCompoundExpression implements Focusable{

    boolean focused;
    private Node node;

    /**
     * Will return a copy of the Additive expression to use
     * for the GUI.
     * @return an copy of the {@link ParentheticalExpression}
     */
    public Expression deepCopy(){
        ParentheticalExpression copy = new ParentheticalExpression();
        return super.deepCopy(copy);
    }

    public Node getNode() {
        if(node == null) {
            final HBox hbox = new HBox();
            hbox.getChildren().add(new Label("("));
            hbox.getChildren().add(this.getChildren().get(0).getNode());
            hbox.getChildren().add(new Label(")"));
            if (this.focused) {
                hbox.setBorder(RED_BORDER);
            }
            node = hbox;
            return hbox;
        }
        return node;
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

    @Override
    public boolean getFocused() {
        return this.focused;
    }

    @Override
    public void setFocused(boolean s) {
        this.focused = s;
    }
}
