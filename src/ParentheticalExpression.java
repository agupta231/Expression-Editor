import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Child of expression that represents an parenthetical operator.
 */

public class ParentheticalExpression extends AbstractCompoundExpression implements Focusable {
    /**
     * Will return a copy of the Additive expression to use
     * for the GUI.
     *
     * @return an copy of the {@link ParentheticalExpression}
     */
    public Expression deepCopy() {
        return super.deepCopy(new ParentheticalExpression());
    }

    /**
     * Create/return the Node representing this Expression
     * @return a Node represeneting the current Expression
     */
    public Node getNode() {
        if (node == null) {
            final HBox hbox = new HBox();

            hbox.getChildren().add(ExpressionEditor.newLabel("("));
            hbox.getChildren().add(this.getChildren().get(0).getNode());
            hbox.getChildren().add(ExpressionEditor.newLabel(")"));

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
     *
     * @param indentLevel how many "levels" down the operator is
     * @return a String representing the parenthetical operator.
     */
    public String convertToString(int indentLevel) {
        return super.convertToString(indentLevel, "()");
    }

    /**
     * Flattens the expression and it's children
     */
    public void flatten() {
        flattenChildren();
    }

    /**
     * Will generate a String representation for the current Expression,
     * with no whitespace.
     * @return a flattened String representing the parenthetical operator
     */
    @Override
    public String convertToStringFlat() {
        return "(" + ((CopyAble) this.getChildren().get(0)).convertToStringFlat() + ")";
    }
}