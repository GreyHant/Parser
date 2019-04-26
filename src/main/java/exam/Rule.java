package exam;

import javax.xml.bind.annotation.*;
import java.util.Set;

@XmlRootElement(name = "rule")
@XmlAccessorType(XmlAccessType.FIELD)
public class Rule {

    @XmlElements({
            @XmlElement(type = OrExpression.class, name = "or"),
            @XmlElement(type = AndExpression.class, name = "and"),
            @XmlElement(type = FactExpression.class, name = "expr")
    })
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

    public Rule() {
    }

    @Override
    public String toString() {
        return "Rule{" +
                expression.toString() +
                ", resultFact='" + resultFact + '\'' +
                '}';
    }

}
