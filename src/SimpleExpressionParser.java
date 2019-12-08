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
        CompoundExpressionImpl dummyExpression = new CompoundExpressionImpl();
        CompoundExpressionImpl parsedStr = (CompoundExpressionImpl) parseE(str, dummyExpression);
        if(parsedStr != null) {
            //parsedStr = (CompoundExpressionImpl) dummyExpression.getSubexpressionAt(0);
            System.out.println("PARSED STRING  " + parsedStr.getChildren());
            return parsedStr;
        }
        return null;
    }

    //im going to put as private but double check later
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
            System.out.println("Parent E: " + parent);
            addExpression.setParent(parent);

            System.out.println("addExpression E: " + addExpression);

            parent.addSubexpression(addExpression);
            return addExpression;
        }

        // Checking M
        final Expression multExpression = parseM(str, parent);
        if (multExpression != null) {
            System.out.println("PARSE E + WAS NO FOUND");

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
            System.out.println("PARSEM ** was FOUND");
            multExpression.setParent(parent);
            parent.addSubexpression(multExpression);
            return multExpression;
        }

        /*
        // Checking X -- I dont like this code because it creates layers of nodes
        final CompoundExpression parenExpression = new ParentheticalExpression();
        if (parseX(str, parenExpression) != null) {
            parenExpression.setParent(parent);
            parent.addSubexpression(parenExpression);
            return parenExpression;
        }
        */
        // Checking X
        final Expression parenExpression = parseX(str, parent);
         if (parenExpression != null) {
            System.out.println("PARSEM NO ** FOUND");
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
        for (int i = 1; i < str.length() - 1; i++) {

            final CompoundExpression parenExpression = new ParentheticalExpression();

            if (str.charAt(0) == '(' &&
                    (parseE(str.substring(i-1, i), parenExpression) != null) &&
                    str.charAt(i+1) == ')') {
                System.out.println("PARSEX FOUND A PARENTHISY");
                parenExpression.setParent(parent);
                parent.addSubexpression(parenExpression);
                return parenExpression;
            }
        }

        // Checking L
        final Expression litExpression = parseL(str, parent);
        if (litExpression != null) {
            System.out.println("CHECKING PARSEL");
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

        //checks if the string contains [a-z] or is a number
        if (str.matches(".*[a-z].*") || str.contains("[0-9]+")) {
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

        for(int i = 1; i < str.length() -1; i++) {
            final Expression leftExpression = m1.apply(str.substring(0, i), parent);
            final Expression rightExpression = m2.apply(str.substring(i + 1), parent);
            if ((str.charAt(i) == op) &&
                    (leftExpression != null) &&
                    (rightExpression != null)) {

                leftExpression.setParent(parent);
                rightExpression.setParent(parent);

                parent.addSubexpression(leftExpression);
                parent.addSubexpression(rightExpression);
                System.out.println("Parent HELPER: " + parent);
                System.out.println("Left HELPER: " + leftExpression);
                System.out.println("Right HELPER: " + rightExpression);

                return parent;
            }
        }
        return null;
    }
}
