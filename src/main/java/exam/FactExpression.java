package exam;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.Set;

public class FactExpression implements Expression {

    @XmlAttribute(name = "fact")
//    @Pattern(regexp="_*\\p{IsAlphabetic}+[\\p{IsAlphabetic}_\\d]*")
    private String fact;

    @Override
    public boolean evaluate(Set<String> allFacts) {
        return allFacts.contains(fact);
    }

    @Override
    public Object getExpressions() {
        return fact;
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
