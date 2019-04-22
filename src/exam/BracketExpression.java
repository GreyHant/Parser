package exam;

import java.util.Set;

public class BracketExpression implements Expression {

    private Expression expression;
    private int position;

    public BracketExpression(Expression expression, int position) {
        this.expression = expression;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public boolean evaluate(Set<String> allFacts) {
        return expression.evaluate(allFacts);
    }

    @Override
    public String toString() {
        return "BracketExpression{" + expression.toString() +
                '}';
    }
}
