package exam;

import javax.xml.bind.annotation.XmlElement;
import java.util.Set;

public class FactExpression implements Expression {

    @XmlElement(name = "fact")
    private String fact;

    @Override
    public boolean evaluate(Set<String> allFacts) {
        return allFacts.contains(fact);
    }

    public FactExpression(String fact) {
        this.fact = fact;
    }

    public FactExpression() {
    }

    @Override
    public String toString() {
        return fact;
    }
}
