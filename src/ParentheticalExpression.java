public class ParentheticalExpression extends AbstractCompoundExpression {
    public Expression deepCopy(){
        ParentheticalExpression copy = new ParentheticalExpression();
        return super.deepCopy(copy);
    }
    public String convertToString(int indentLevel){
        return super.convertToString(indentLevel,"()");
    }
}
