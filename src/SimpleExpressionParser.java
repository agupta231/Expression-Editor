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
		if(str.length() == 1 && (str.equals("X") || Character.isLowerCase(str.charAt(0)) || Character.isDigit(str.charAt(0)))){
			LiteralExpression expression = new LiteralExpression();
			expression.setLiteral(str);
			return expression;
		}else if(str.length() == 1){
			return null;
		}

		int indexOfPlus = str.indexOf('+');

		System.out.println(str);
		System.out.println(indexOfPlus);

		if(indexOfPlus > 0){
			AdditiveExpression expression = new AdditiveExpression();

			Expression childExpression1 = parseExpression(str.substring(indexOfPlus));
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
		int indexOfStar = str.indexOf('*');
		if(indexOfStar > 0){
			MultiplicativeExpression expression = new MultiplicativeExpression();

			Expression childExpression1 = parseExpression(str.substring(indexOfStar));
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
		int indexOfOpenParen = str.indexOf('(');
		int indexOfCloseParen = str.indexOf(')');
		if(indexOfOpenParen > 0 && indexOfCloseParen > 0){
			ParentheticalExpression expression = new ParentheticalExpression();
			Expression childExpression = parseExpression(str.substring(indexOfOpenParen) + str.substring(indexOfCloseParen));
			if(childExpression != null){
				expression.addSubexpression(childExpression);
				return expression;
			}
			childExpression.setParent(expression);

		}
		return null;
	}
}
