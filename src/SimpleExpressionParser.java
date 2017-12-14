/**
 * Starter code to implement an ExpressionParser. Your parser methods should use the following grammar:
 * E := A | X
 * A := A+M | M
 * M := M*M| X
 * X := (E)  | L
 * L := [0-9]+ | [a-z]
 */
public class SimpleExpressionParser implements ExpressionParser {

	private boolean FX;

	/**
	 * Attempts to create an expression tree -- flattened as much as possible -- from the specified String.
     * Throws a {@link ExpressionParseException} if the specified string cannot be parsed.
	 * @param str the string to parse into an expression tree
	 * @param withJavaFXControls you can just ignore this variable for R1
	 * @return the Expression object representing the parsed expression tree
	 */
	public Expression parse (String str, boolean withJavaFXControls) throws ExpressionParseException {
		FX = withJavaFXControls;
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
	
	/**
	 * Attempts to create an expression from a given String.
	 * @param str the string to parse
	 * @return the Expression representing the parsed expression.
	 */
	protected Expression parseExpression (String str) {
		if(str.length() == 0) {
			return null;
		}

		Expression e = parseCollapsable(str, '+', new AdditiveExpression());

		if(e != null) {
			return e;
		}

		e = parseCollapsable(str, '*', new MultiplicativeExpression());

		if(e != null) {
			return e;
		}

		if((str.length() == 1 && Character.isLowerCase(str.charAt(0))) || stringIsDigit(str)) {
			LiteralExpression expression = new LiteralExpression();
			expression.setLiteral(str);

			return expression;
		} else if (str.length() == 1) {
			return null;
		}

		if(str.charAt(0) == '(' && str.charAt(str.length() - 1) == ')') {
			ParentheticalExpression expression = new ParentheticalExpression();
			Expression childExpression = parseExpression(str.substring(1, str.length() - 1));

			if(childExpression != null){
				expression.addSubexpression(childExpression);
				childExpression.setParent(expression);

				return expression;
			}
			else {
				return null;
			}
		}

		return null;
	}

	/**
	 * Helper function to determine if a string is a digit
	 * @param str is the string which the user wants to determine is a digit or not
	 * @return true if the string is a digit, fase otherweise
	 */
	private boolean stringIsDigit(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Helper function which will determine if all of the parentheses to a point in
	 * a string are balanced. This function helps determine which "level" a certain
	 * opertor is within the expression.
	 * @param str is the expression
	 * @param index is the index of the char in the string that you want to check if
	 *              the paraenthesis are balanced.
	 * @return true if the parenthesis are balanced, false otherswise.
	 */
	private boolean areParenthesisBalanced (String str, int index) {
		int openParen = 0;
		int closedParen = 0;

		for(int i = 0; i < index; i++) {
			if(str.charAt(i) == '(') {
				openParen++;
			}
			if(str.charAt(i) == ')') {
				closedParen++;
			}
		}

		return openParen == closedParen;
	}

	private Expression parseCollapsable (String str,
										 char delimiter,
										 CollapsibleExpression expression) {

		for(int index = str.indexOf(delimiter); index >= 0; index = str.indexOf(delimiter, index + 1)) {
			if (index > 0 && areParenthesisBalanced(str, index)) {
				Expression childExpression1 = parseExpression(str.substring(0, index));
				Expression childExpression2 = parseExpression(str.substring(index + 1, str.length()));

				if (childExpression1 == null || childExpression2 == null) {
					return null;
				}

				childExpression1.setParent(expression);
				childExpression2.setParent(expression);

				expression.addSubexpression(childExpression1);
				expression.addSubexpression(childExpression2);

				return expression;
			}
		}

		return null;
	}
}
