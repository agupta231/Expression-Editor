import javafx.scene.Node;
import javafx.scene.layout.HBox;

import java.util.LinkedList;

public abstract class CollapsibleExpression extends AbstractCompoundExpression implements Focusable {
    /**
     * Flattens the expression in order to make it easier for the GUI to parse.
     */
    public void flatten() {
        flattenChildren();
        LinkedList<Expression> children = new LinkedList<>();

        for(Expression e: this.getChildren()) {
            if(e.getClass() == this.getClass()){
                CollapsibleExpression tempExp = (CollapsibleExpression)e;

                for(Expression exp: tempExp.getChildren()) {
                    children.add(exp);
                }
            }
            else {
                children.add(e);
            }
        }
        this.setChildren(children);
    }
}
