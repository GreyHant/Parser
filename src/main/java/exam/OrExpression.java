package exam;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import java.util.List;
import java.util.Set;

public class OrExpression implements Expression {
    @XmlElements({
            @XmlElement(type = OrExpression.class, name = "or"),
            @XmlElement(type = AndExpression.class, name = "and"),
            @XmlElement(type = FactExpression.class, name = "fact")
    })
    private List<Expression> expressionList;

    @Override
    public boolean evaluate(Set<String> allFacts) {
        for (Expression expression : expressionList) {
            if (expression.evaluate(allFacts)) return true;
        }
        return false;
    }

    @Override
    public Object getExpressions() {
        return expressionList;
    }

    public OrExpression() {
    }

    public OrExpression(List<Expression> expressionList) {
        this.expressionList = expressionList;
    }

    @Override
    public String toString() {
        return "OrExpression=" + expressionList;
    }
}
