package exam;

import java.util.Set;

public class Rule {

    private Expression expression;
    private String resultFact;


    public void evaluate(Set<String> allFacts) {
        if (allFacts.contains(resultFact))
            return;
        if (expression.evaluate(allFacts))
            allFacts.add(resultFact);
    }

    public Rule(Expression expression, String resultFact) {
        this.expression = expression;
        this.resultFact = resultFact;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "expression=" + expression.toString() +
                ", resultFact='" + resultFact + '\'' +
                '}';
    }
}
