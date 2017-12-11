import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Child of expression that represents a multiplicative operator.
 */
public class MultiplicativeExpression extends CollapsibleExpression implements Focusable{

    private boolean focused;
    private Node node;

    /**
     * Will return a copy of the Additive expression to use
     * for the GUI.
     * @return an copy of the {@link MultiplicativeExpression}
     */
    public Expression deepCopy(){
        MultiplicativeExpression copy = new MultiplicativeExpression();
        return super.deepCopy(copy);
    }

    @Override
    public Node getNode() {
        if(node == null) {
            final HBox hbox = new HBox();
            hbox.getChildren().add(this.getChildren().get(0).getNode());
            for (int i = 1; i < this.getChildren().size(); i++) {
                hbox.getChildren().add(new Label("*"));
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
     * Will generate a String representation for the the addition
     * operator, with the appropiate number of tabs to properly
     * represent it's position in the expression.
     * @param indentLevel how many "levels" down the operator is
     * @return a String representing the multiplicative operator.
     */
    public String convertToString(int indentLevel){
        return super.convertToString(indentLevel,"*");
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
