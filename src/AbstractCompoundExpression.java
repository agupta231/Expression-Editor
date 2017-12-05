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


    //TODO make this a StringBuffer
    public String convertToString(int indentLevel, String name){

        StringBuffer sb = new StringBuffer();
        Expression.indent(sb,indentLevel);
        sb.append(name + "\n");
        for(Expression e: this.getChildren()){
            sb.append(e.convertToString(indentLevel+1));
        }
        return sb.toString();
    }

    public AbstractCompoundExpression deepCopy(AbstractCompoundExpression copy){
        copy.setParent((CompoundExpression)this.getParent().deepCopy());
        for(Expression c: this.getChildren()){
            copy.addSubexpression(c.deepCopy());
        }
        return copy;
    }

}
