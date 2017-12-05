public class AdditiveExpression extends CollapsibleExpression{
    public Expression deepCopy(){
        AdditiveExpression copy = new AdditiveExpression();
        return super.deepCopy(copy);
    }
    public String convertToString(int indentLevel){
        return super.convertToString(indentLevel,"+");
    }

}
