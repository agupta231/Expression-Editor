public class MultiplicativeExpression extends CollapsibleExpression{
    public Expression deepCopy(){
        MultiplicativeExpression copy = new MultiplicativeExpression();
        return super.deepCopy(copy);
    }
    public String convertToString(int indentLevel){
        return super.convertToString(indentLevel,"*");
    }

}
