 /**
 * Starter code to implement an ExpressionParser. Your parser methods should use the following grammar:
 * E := A | X
 * A := A+M | M
 * M := M*M| X
  * X := (E)  | L
 * L := [0-9]+ | [a-z]
 */
public class SimpleExpressionParser implements ExpressionParser {
	/*
	 * Attempts to create an expression tree -- flattened as much as possible -- from the specified String.
         * Throws a ExpressionParseException if the specified string cannot be parsed.
	 * @param str the string to parse into an expression tree
	 * @param withJavaFXControls you can just ignore this variable for R1
	 * @return the Expression object representing the parsed expression tree
	 */
	public Expression parse (String str, boolean withJavaFXControls) throws ExpressionParseException {
		// Remove spaces -- this simplifies the parsing logic
		str = str.replaceAll(" ", "");
		Expression expression = parseExpression(str);
		if (expression == null) {
			// If we couldn't parse the string, then raise an error
			throw new ExpressionParseException("Cannot parse expression: " + str);
		}

		// Flatten the expression before returning
		expression.flatten();
		return expression;
	}
	
	protected Expression parseExpression (String str) {
		int indexOfPlus = str.indexOf('+');
		if(indexOfPlus > 0){
			AdditiveExpression expression = new AdditiveExpression();
			Expression childExpression1 = parseExpression(str.substring(indexOfPlus));
			childExpression1.setParent(expression);
			Expression childExpression2 = parseExpression(str.substring(indexOfPlus + 1, str.length()));
			childExpression2.setParent(expression);
			if(childExpression1 != null && childExpression2 != null){
				expression.addSubexpression(childExpression1);
				expression.addSubexpression(childExpression2);
				return expression;
			}
			return null;
		}
		int indexOfStar = str.indexOf('*');
		if(indexOfStar > 0){
			MultiplicativeExpression expression = new MultiplicativeExpression();
			Expression childExpression1 = parseExpression(str.substring(indexOfStar));
			childExpression1.setParent(expression);
			Expression childExpression2 = parseExpression(str.substring(indexOfStar + 1, str.length()));
			childExpression2.setParent(expression);
			if(childExpression1 != null && childExpression2 != null){
				expression.addSubexpression(childExpression1);
				expression.addSubexpression(childExpression2);
				return expression;
			}
			return  null;
		}
		int indexOfOpenParen = str.indexOf('(');
		int indexOfCloseParen = str.indexOf(')');
		if(indexOfOpenParen > 0 && indexOfCloseParen > 0){
			ParentheticalExpression expression = new ParentheticalExpression();
			Expression childExpression = parseExpression(str.substring(indexOfOpenParen) + str.substring(indexOfCloseParen));
			childExpression.setParent(expression);
			if(childExpression != null){
				expression.addSubexpression(childExpression);
				return expression;
			}
			return null;
		}

		if(str.length() == 1 && (Character.isLowerCase(str.charAt(0)) || Character.isDigit(str.charAt(0)))){
			LiteralExpression expression = new LiteralExpression();
			expression.setLiteral(str);
			return expression;
		}
		return null;
	}
}
