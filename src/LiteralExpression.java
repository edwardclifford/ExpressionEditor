class LiteralExpression extends ExpressionImpl {

    public String value;
    /**
     * Implements a terminal expression
     */
    LiteralExpression (String val) {
        super();
        this.value = val;
    } 
}
