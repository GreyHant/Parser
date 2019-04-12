package exam;

import java.util.List;
import java.util.Set;

public class OrExpression implements Expression {

    private List<Expression> expressionList;

    @Override
    public boolean evaluate(Set<String> allFacts) {
        for (Expression expression : expressionList) {
            if (expression.evaluate(allFacts)) return true;
        }
        return false;
    }

    public OrExpression(List<Expression> expressionList) {
        this.expressionList = expressionList;
    }
}
