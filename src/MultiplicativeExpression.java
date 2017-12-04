public class MultiplicativeExpression extends CollapsibleExpression{
    public Expression deepCopy(){
        MultiplicativeExpression copy = new MultiplicativeExpression();
        copy.setParent((CompoundExpression)this.getParent().deepCopy());
        for(Expression c: this.getChildren()){
            copy.addSubexpression(c.deepCopy());
        }
        return copy;
    }


    //TODO move more of converttoSTring to abstract level.
    public String convertToString(int indentLevel){
        return new String(new char[indentLevel]).replace("\0", "\t") + "*\n"
                + super.convertToString(indentLevel);
    }

}
