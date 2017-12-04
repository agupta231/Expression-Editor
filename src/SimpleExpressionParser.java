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
		int indexOfOpenParen = str.indexOf('(');
		int indexOfCloseParen = str.indexOf(')');
		int indexOfPlus = str.indexOf('+');
		int indexOfStar = str.indexOf('*');

		if(indexOfOpenParen == 0 && indexOfCloseParen == str.length() - 1){
			ParentheticalExpression expression = new ParentheticalExpression();
			Expression childExpression = parseExpression(str.substring(indexOfOpenParen + 1, indexOfCloseParen));
			if(childExpression != null){
				expression.addSubexpression(childExpression);
				return expression;
			}
			childExpression.setParent(expression);
		}

		if((str.length() == 1 && Character.isLowerCase(str.charAt(0))) || stringIsDigit(str)){
			LiteralExpression expression = new LiteralExpression();
			expression.setLiteral(str);
			return expression;
		}else if(str.length() == 1){
			return null;
		}


		if(indexOfPlus > 0 && (indexOfOpenParen == -1 || indexOfOpenParen > indexOfPlus)){
			AdditiveExpression expression = new AdditiveExpression();

			Expression childExpression1 = parseExpression(str.substring(0, indexOfPlus));
			Expression childExpression2 = parseExpression(str.substring(indexOfPlus + 1, str.length()));
			if(childExpression1 == null || childExpression2 == null){
				return null;
			}

			childExpression1.setParent(expression);
			childExpression2.setParent(expression);

			expression.addSubexpression(childExpression1);
			expression.addSubexpression(childExpression2);

			return expression;
		}

		if(indexOfStar > 0 && (indexOfOpenParen == -1 || indexOfOpenParen > indexOfStar)){
			MultiplicativeExpression expression = new MultiplicativeExpression();
			Expression childExpression1 = parseExpression(str.substring(0, indexOfStar));
			Expression childExpression2 = parseExpression(str.substring(indexOfStar + 1, str.length()));

			if(childExpression1 == null || childExpression2 == null){
				return null;
			}

			childExpression1.setParent(expression);
			childExpression2.setParent(expression);

			expression.addSubexpression(childExpression1);
			expression.addSubexpression(childExpression2);

			return expression;
		}
		return null;
	}

	private boolean stringIsDigit(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}