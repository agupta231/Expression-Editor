import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

/**
 * Child of expression that represents an parenthetical operator.
 */

public class ParentheticalExpression extends AbstractCompoundExpression implements Focusable{

    private boolean focused;
    private Node node;

    /**
     * Will return a copy of the Additive expression to use
     * for the GUI.
     * @return an copy of the {@link ParentheticalExpression}
     */
    public Expression deepCopy(){
        ParentheticalExpression copy = new ParentheticalExpression();
        copy.node = node;

        return super.deepCopy(copy);
    }

    public Node getNode() {
        if(node == null) {
            final HBox hbox = new HBox();

            Label openParen = new Label("(");
            openParen.setFont(Font.font("Comic Sans MS", 36));

            Label closeParen = new Label(")");
            closeParen.setFont(Font.font("Comic Sans MS", 36));

            hbox.getChildren().add(openParen);
            hbox.getChildren().add(this.getChildren().get(0).getNode());
            hbox.getChildren().add(closeParen);
            if (this.getFocused()) {
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
