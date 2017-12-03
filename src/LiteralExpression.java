public class LiteralExpression implements Expression {

    private CompoundExpression parent;
    private String literal;


    public String convertToString(int indentLevel){
        return new String(new char[indentLevel]).replace("\0", "/t")+ literal;
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

    //does nothing, but is needed because higher functions may call it.
    public void flatten(){
    }
}
