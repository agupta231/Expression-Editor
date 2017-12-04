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

		System.out.println(str);

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
		if(str.length() == 0) {
			return null;
		}

		System.out.print("Current String: ");
		System.out.println(str);


//		int indexOfOpenParen = str.indexOf('(');
//		int indexOfCloseParen = str.indexOf(')');

//		if(indexOfOpenParen == 0 && indexOfCloseParen == str.length() - 1){
//		if(indexOfOpenParen == 0 && str.charAt(str.length() - 1) == ')'){
		if(str.charAt(0) == '(' && str.charAt(str.length() - 1) == ')') {
			ParentheticalExpression expression = new ParentheticalExpression();

//			Expression childExpression = parseExpression(str.substring(indexOfOpenParen + 1, indexOfCloseParen));
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

		if((str.length() == 1 && Character.isLowerCase(str.charAt(0))) || stringIsDigit(str)){
			LiteralExpression expression = new LiteralExpression();
			expression.setLiteral(str);
			return expression;
		}else if(str.length() == 1){
			return null;
		}

		int indexOfOpenParen = str.indexOf('(');


		for(int indexOfPlus = str.indexOf('+'); indexOfPlus >= 0; indexOfPlus = str.indexOf('+', indexOfPlus + 1)) {
//		if(indexOfPlus > 0 && (indexOfOpenParen == -1 || indexOfOpenParen > indexOfPlus)) {
			if (indexOfPlus > 0 && areParenthesisBalanced(str, indexOfPlus)) {
//		if(indexOfPlus > 0){
				System.out.println("Add me, daddy");

				AdditiveExpression expression = new AdditiveExpression();

				Expression childExpression1 = parseExpression(str.substring(0, indexOfPlus));
				Expression childExpression2 = parseExpression(str.substring(indexOfPlus + 1, str.length()));

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

		for(int indexOfStar = str.indexOf('*'); indexOfStar >= 0; indexOfStar = str.indexOf('*', indexOfStar + 1)) {
//		if(indexOfStar > 0 && (indexOfOpenParen == -1 || indexOfOpenParen > indexOfStar)){
			if (indexOfStar > 0 && areParenthesisBalanced(str, indexOfStar)) {
//		if(indexOfStar > 0){
				System.out.println("Multiply me, daddy");

				MultiplicativeExpression expression = new MultiplicativeExpression();
				Expression childExpression1 = parseExpression(str.substring(0, indexOfStar));
				Expression childExpression2 = parseExpression(str.substring(indexOfStar + 1, str.length()));

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

	private boolean stringIsDigit(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

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
}