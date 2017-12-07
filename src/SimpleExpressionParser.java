 /**
 * Starter code to implement an ExpressionParser. Your parser methods should use the following grammar:
 * E := A | X
 * A := A+M | M
 * M := M*M| X
 * X := (E)  | L
 * L := [0-9]+ | [a-z]
 */
public class SimpleExpressionParser implements ExpressionParser {
	/**
	 * Attempts to create an expression tree -- flattened as much as possible -- from the specified String.
     * Throws a {@link ExpressionParseException} if the specified string cannot be parsed.
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
	
	/**
	 * Attemps to create an expression from a given String.
	 * @param str the string to parse
	 * @return the Expression representing the parsed expression.
	 */
	protected Expression parseExpression (String str) {
		if(str.length() == 0) {
			return null;
		}

		//TODO in part 2 make this a lambda function (not required for part 1)
		for(int indexOfPlus = str.indexOf('+'); indexOfPlus >= 0; indexOfPlus = str.indexOf('+', indexOfPlus + 1)) {
			if (indexOfPlus > 0 && areParenthesisBalanced(str, indexOfPlus)) {
				AdditiveExpression expression = new AdditiveExpression();
				return parseCollapsable(str,indexOfPlus,expression);
			}
		}

		for(int indexOfStar = str.indexOf('*'); indexOfStar >= 0; indexOfStar = str.indexOf('*', indexOfStar + 1)) {
			if (indexOfStar > 0 && areParenthesisBalanced(str, indexOfStar)) {
				MultiplicativeExpression expression = new MultiplicativeExpression();
				return parseCollapsable(str,indexOfStar,expression);
			}
		}

		if((str.length() == 1 && Character.isLowerCase(str.charAt(0))) || stringIsDigit(str)) {
			LiteralExpression expression = new LiteralExpression();
			expression.setLiteral(str);
			return expression;
		} else if(str.length() == 1) {
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
	  * Helper function to link an expression to its child expressions
	  * @param expression parent expression
	  * @param childExpression1 first child expression
	  * @param childExpression2 second child expression
	  * @return null if expression can't be parsed, returns the expression otherwise.
	  */
	private Expression linkExpression(CompoundExpression expression,
									  Expression childExpression1,
									  Expression childExpression2) {
		if (childExpression1 == null || childExpression2 == null) {
			return null;
		}

		childExpression1.setParent(expression);
		childExpression2.setParent(expression);

		expression.addSubexpression(childExpression1);
		expression.addSubexpression(childExpression2);

		return expression;
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

	 /**
	  * //TODO ankur write this
	  * @param str
	  * @param indexOfCollapsibleExpression
	  * @param expression
	  * @return
	  */
	private Expression parseCollapsable(String str, int indexOfCollapsibleExpression,CollapsibleExpression expression)
	{
		Expression childExpression1 = parseExpression(str.substring(0, indexOfCollapsibleExpression));
		Expression childExpression2 = parseExpression(str.substring(indexOfCollapsibleExpression + 1, str.length()));

		return linkExpression(expression, childExpression1, childExpression2);
	}
}
