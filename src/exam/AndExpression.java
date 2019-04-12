package exam;

import java.util.List;
import java.util.Set;

public class AndExpression implements Expression {

    private List<Expression> expressionList;

    @Override
    public boolean evaluate(Set<String> allFacts) {
        for (Expression expression : expressionList) {
            if (!expression.evaluate(allFacts)) return false;
        }
        return true;
    }

    public AndExpression(List<Expression> expressionList) {
        this.expressionList = expressionList;
    }
}

