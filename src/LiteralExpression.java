public class LiteralExpression implements Expression {

    private CompoundExpression parent;
    private String literal;


    public String convertToString(int indentLevel){
        StringBuffer sb = new StringBuffer();
        Expression.indent(sb,indentLevel);
        return sb.append(literal + "\n").toString();
    }

    public void setParent(CompoundExpression parent){
        this.parent = parent;
    }

    public CompoundExpression getParent() {
        return this.parent;
    }

    public LiteralExpression deepCopy(){
        LiteralExpression copy = new LiteralExpression();
        copy.parent = (CompoundExpression) parent.deepCopy();
        copy.literal = this.literal;
        return copy;
    }

    public void setLiteral(String str){
        this.literal = str;
    }

    //does nothing, but is needed because higher functions may call it.
    public void flatten(){
    }
}
