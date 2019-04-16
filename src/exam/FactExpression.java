package exam;

import java.util.Set;

public class FactExpression implements Expression {

    private String fact;

    @Override
    public boolean evaluate(Set<String> allFacts) {
        return allFacts.contains(fact);
    }

    public FactExpression(String fact) {
        this.fact = fact;
    }

    @Override
    public String toString() {
        return fact;
    }
}
