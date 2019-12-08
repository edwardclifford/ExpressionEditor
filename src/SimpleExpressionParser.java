import javax.swing.text.html.parser.DTD;
import java.util.function.BiFunction;

/**
 * Starter code to implement an ExpressionParser. Your parser methods should use the following grammar:
 * E := E + M | M
 * M := M * X | X
 * X := ( E ) | L
 * L := [0-9]+ | [a-z]
 */
public class SimpleExpressionParser implements ExpressionParser {
    /**
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

    private Expression parseExpression (String str) {

        // Use dummy to act as temporary root node
        CompoundExpressionImpl dummyExpression = new CompoundExpressionImpl();
        Expression parsedStr = parseE(str, dummyExpression);

        if (parsedStr != null) {
            return parsedStr;
        }

        return null;
    }

    /**
     * Checks if the string follows the parsing rules for A
     * E -> E + M | M
     * @param str, the string being parsed
     * @param parent the expression the parsed string will belong to
     * @return the valid expression, or null if none exists
     */
    private static Expression parseE(String str, CompoundExpression parent) {
    
        // Checking E+M
        final CompoundExpression addExpression = new AdditiveExpression();
        Expression helpParse = parseHelper(str, '+', addExpression, SimpleExpressionParser::parseE, SimpleExpressionParser::parseM);
        if (helpParse != null) {

            addExpression.setParent(parent);
            parent.addSubexpression(addExpression);
            return addExpression;
        }

        // Checking M
        final Expression multExpression = parseM(str, parent);
        if (multExpression != null) {

            multExpression.setParent(parent);
            parent.addSubexpression(multExpression);
            return multExpression;
        }

        return null;
    }

    /**
     * Checks if the string follows the parsing rules for M
     * M -> M * X | X
     * @param str, the string being parsed
     * @param parent the expression the parsed string will belong to
     * @return the valid expression, or null if none exists
     */
    private static Expression parseM(String str, CompoundExpression parent) {

        // Checking M*X
        final CompoundExpression multExpression = new MultiplicativeExpression();
        if(parseHelper(str, '*', multExpression, SimpleExpressionParser::parseM, SimpleExpressionParser::parseX) != null) {

            multExpression.setParent(parent);
            parent.addSubexpression(multExpression);
            return multExpression;
        }

        // Checking X
        final Expression parenExpression = parseX(str, parent);
         if (parenExpression != null) {

            parenExpression.setParent(parent);
            parent.addSubexpression(parenExpression);
            return parenExpression;
        }

        return null;
    }

    /**
     * Checks if the string follows the parsing rules for X
     * X -> (E) | L
     * @param str, the string being parsed
     * @param parent the expression the parsed string will belong to
     * @return the valid expression, or null if none exists
     */
    private static Expression parseX(String str, CompoundExpression parent) {

        // Checking (E)
        if (str.length() > 0 && str.charAt(0) == '(') {
            int parenCounter = 0;

            for (int i = 0; i < str.length(); i++) {
                // Ensures that brackets are balanced 
                if (str.charAt(i) == '(') parenCounter++;
                else if (str.charAt(i) == ')') parenCounter--;
            }

            // If brackets are balanced and last char is a ), check contents 
            if (parenCounter == 0 && str.charAt(str.length() - 1) == ')') {

                final CompoundExpression parenExpression = new ParentheticalExpression();
                if (parseE(str.substring(1, str.length() - 1), parenExpression) != null) {

                        parenExpression.setParent(parent);
                        parent.addSubexpression(parenExpression);
                        return parenExpression;
                }
            }
        }

        // Checking L
        final Expression litExpression = parseL(str, parent);
        if (litExpression != null) {

            parent.addSubexpression(litExpression);
            return litExpression;
        }

        return null;
    }

    /**
     * Checks if the string follows the parsing rules for L
     * L -> [a-z] | [0-9]
     * @param str, the string being parsed
     * @param parent the expression the parsed string will belong to
     * @return the valid expression, or null if none exists
     */
    private static Expression parseL(String str, CompoundExpression parent) {

        // Empty string is not a literal
        if (str.length() == 0){
            return null;
        }

        // Check that a string only contains letters or only contains numbers
        if (str.matches("^[a-z]+$") || str.matches("^[0-9]*$")) {
            final Expression litExpression = new LiteralExpression(str);
            litExpression.setParent(parent);
            return litExpression;
        }

        return null;
    }

    /**
     * Abstracts the bounds for parsing mathematical expressions
     * @param str, string that is being parsed
     * @param op, operation symbol (i.e. '+' or '*')
     * @param parent the expression the parsed string will belong to
     * @param m1, method 1
     * @param m2, method 2
     * @return the valid expression, or null if none exists
     */
    public static Expression parseHelper(String str, char op, CompoundExpression parent,
                                         BiFunction<String, CompoundExpression, Expression> m1,
                                         BiFunction<String, CompoundExpression, Expression> m2) {

        // Check each pivot point for a valid operator
        for(int i = 1; i < str.length() - 1; i++) {
            if(str.charAt(i) == op) {

                // Dummy used to create sub trees
                CompoundExpressionImpl dummyExpression = new CompoundExpressionImpl();
                final Expression leftExpression = m1.apply(str.substring(0, i), dummyExpression);
                final Expression rightExpression = m2.apply(str.substring(i + 1), dummyExpression);

                if ((str.charAt(i) == op) &&
                        (leftExpression != null) &&
                        (rightExpression != null)) {

                    // Replace dummy and add expressions to root node
                    leftExpression.setParent(parent);
                    rightExpression.setParent(parent);

                    parent.addSubexpression(leftExpression);
                    parent.addSubexpression(rightExpression);

                    return parent;
                }
            }
        }

        return null;
    }
}
