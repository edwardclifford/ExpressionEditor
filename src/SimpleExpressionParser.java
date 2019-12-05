import javax.swing.text.html.parser.Parser;
import java.util.HashMap;
import java.util.function.Function;

/**
 * Starter code to implement an ExpressionParser. Your parser methods should use the following grammar:
 * E := E+M | M
 * M := M*M | X
 * X := (E) | L
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
		Expression expression;
		// TODO implement me
		//createTree(str);
		return null;
	}

	/**
	 * Creates a tree depending on the grammar of the string
	 * @param str
	 * @return
	 */
	/*
	private Expression createTree(String str) {
		HashMap treeeee = new HashMap<Expression, Expression>();
		//TODO implemenut me
		if(parseE) {
			return null;
		}
		return null;
	}
	*/

	//im going to put as private but double check later
	/**
	 * Checks if the string follows the parsing rules for A
	 * E -> E + M | M
	 * @param str, the string being parsed
	 * @return a boolean whether or not the the string follows the parsing rules.
	 */
	private boolean parseE(String str) {
		if(parseHelper(str, '+', Parser::parseE, Parser::parseM)) {
			return true;
		}
		else if (parseM(str)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the string follows the parsing rules for M
	 * M -> M * M | X
	 * @param str, the string being parsed
	 * @return a boolean whether or not the the string follows the parsing rules.
	 */
	private boolean parseM(String str) {
		if(parseHelper(str, '*', Parser::parseM, Parser::parseM)) {
			return true;
		}
		else if (parseX(str)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the string follows the parsing rules for X
	 * X -> (E) | L
	 * @param str, the string being parsed
	 * @return a boolean whether or not the the string follows the parsing rules.
	 */
	private boolean parseX(String str) {
		for (int i = 1; i < str.length() - 1; i++) {
			if (str.charAt(0, i) == '(' &&
			     parseE(str.substring(i)) &&
			     str.charAt(i+1) == ')') {
				return true;
			}
		}
		if (parseL(str)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the string follows the parsing rules for L
	 * L -> [a-z] | [0-9]
	 * @param str, the string being parsed
	 * @return a boolean whether or not the the string follows the parsing rules.
	 */
	private boolean parseL(String str) {
		//checks if the string contains [a-z]
		if (str.matches(".*[a-z].*")) {
			return true;
		}
		//checks if the string contains [0-9]
		else if (str.contains("[0-9]+")) {
			return true;
		}
		return false;
	}

	/**
	 * Abstracts the bounds for parsing mathmatical expressions
	 * @param str, string that is being parsed
	 * @param op, operation symbol (i.e. '+' or '*')
	 * @param m1, method 1
	 * @param m2, method 2
	 * @return a boolean whether or not the the string follows the parsing rules.
	 */
	private boolean parseHelper(String str, char op,
								Function<String, Boolean> m1,
								Function<String, Boolean> m2) {
		for(int i = 1; i < str.length() -1; i++) {
			if (str.charAt(i) == op &&
			     m1(str.substring(0, i)) &&
			     m2(str.substring(i+1))) {
				return true;
			}
		}
		return false;
	}

}
