import javax.swing.text.html.parser.DTD;
import java.util.function.Function;

/**
 * Starter code to implement an ExpressionParser. Your parser methods should use the following grammar:
 * E := E + M | M
 * M := M * X | X
 * X := ( E ) | L
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

	//im going to put as private but double check later
	/**
	 * Checks if the string follows the parsing rules for A
	 * E -> E + M | M
	 * @param str, the string being parsed
	 * @return a boolean whether or not the the string follows the parsing rules.
	 */
	private static Expression parseE(String str) {
		Expression express_parser = parseHelper(str, '+', SimpleExpressionParser::parseE, SimpleExpressionParser::parseM);
		if(express_parser != null) {
			//adds the operation, '+'

			return express_parser;
		}
		else if (parseM(str) != null) {
			//TODO CHECK THIS
			return parseM(str);
		}
		return null;
	}

	/**
	 * Checks if the string follows the parsing rules for M
	 * M -> M * X | X
	 * @param str, the string being parsed
	 * @return a boolean whether or not the the string follows the parsing rules.
	 */
	private static Expression parseM(String str) {
		Expression value = parseHelper(str, '*', SimpleExpressionParser::parseM, SimpleExpressionParser::parseX);
		if(value != null) {
			return value;
		}
		else if (parseX(str) != null) {
			return null;
		}
		return null;
	}

	/**
	 * Checks if the string follows the parsing rules for X
	 * X -> (E) | L
	 * @param str, the string being parsed
	 * @return a boolean whether or not the the string follows the parsing rules.
	 */
	private static Expression parseX(String str) {
		for (int i = 1; i < str.length() - 1; i++) {
			if (str.charAt(0) == '(' &&
					(parseE(str.substring(i-1, i)) != null) &&
					str.charAt(i+1) == ')') {
				//add
				//wwhhhatat expression do i returnnnnnnn
				return null;
			}
		}
		if (parseL(str) != null) {
			return null;
		}
		return null;
	}

	/**
	 * Checks if the string follows the parsing rules for L
	 * L -> [a-z] | [0-9]
	 * @param str, the string being parsed
	 * @return a boolean whether or not the the string follows the parsing rules.
	 */
	private static Expression parseL(String str) {
		//checks if the string contains [a-z]
		if (str.matches(".*[a-z].*")) {
			return null;
		}
		//checks if the string contains [0-9]
		else if (str.contains("[0-9]+")) {
			return null;
		}
		return null;
	}

	/**
	 * Abstracts the bounds for parsing mathematical expressions
	 * @param str, string that is being parsed
	 * @param op, operation symbol (i.e. '+' or '*')
	 * @param m1, method 1
	 * @param m2, method 2
	 * @return a boolean whether or not the the string follows the parsing rules.
	 */
	public static Expression parseHelper(String str, char op,
								  Function<String, Expression> m1,
								  Function<String, Expression> m2) {
		//TODO finish this method's return objects
		CompoundExpressionImpl CEI = new CompoundExpressionImpl();
		for(int i = 1; i < str.length() -1; i++) {
			final Expression method1 = m1.apply(str.substring(0, i));
			final Expression method2 = m2.apply(str.substring(i + 1));
			if ((str.charAt(i) == op) &&
					(method1 != null) &&
					(method2 != null)) {
				//means both method 1 and two are NOT literals
				return null;
			}
		}
		return null;
	}
}
