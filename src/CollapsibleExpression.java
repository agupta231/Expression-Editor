import java.util.LinkedList;

public abstract class CollapsibleExpression extends AbstractCompoundExpression {

    public void flatten() {
        super.flatten();
        LinkedList<Expression> children = new LinkedList<Expression>();
        for(Expression e: this.getChildren())
        {
            if(e.getClass() == this.getClass()){
                CollapsibleExpression tempExp = (CollapsibleExpression)e;
                for(Expression exp: tempExp.getChildren())
                {
                    children.add(exp);
                }
            }
            else{
                children.add(e);
            }
        }
        this.setChildren(children);
    }

}
