public class ParentheticalExpression extends AbstractCompoundExpression {
    public Expression deepCopy(){
        ParentheticalExpression copy = new ParentheticalExpression();
        copy.setParent((CompoundExpression)this.getParent().deepCopy());
        for(Expression c: this.getChildren()){
            copy.addSubexpression(c.deepCopy());
        }
        return copy;
    }

    public String convertToString(int indentLevel){
        return new String(new char[indentLevel]).replace("\0", "/t") + "()\n"
                + super.convertToString(indentLevel);
    }
}
