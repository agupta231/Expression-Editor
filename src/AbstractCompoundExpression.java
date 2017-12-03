import java.util.LinkedList;

public abstract class AbstractCompoundExpression implements CompoundExpression{
    private CompoundExpression parent;
    private LinkedList<Expression> children = new LinkedList<Expression>();

    @Override
    public void flatten() {
        for (Expression c: children){
            c.flatten();
        }
    }
    public void setParent(CompoundExpression parent){
        this.parent = parent;
    }
    public void setChildren(LinkedList<Expression> children){
        this.children = children;
    }
    public void addSubexpression(Expression child){
        this.children.add(child);
    }

    public LinkedList<Expression> getChildren(){
        return this.children;
    }
    @Override
    public CompoundExpression getParent() {
        return parent;
    }

    public String convertToString(int indentLevel){
        String conversion = "";
        for(Expression e: this.getChildren()){
            conversion += "/n" + e.convertToString(indentLevel+1);
        }
        return conversion;
    }

}
