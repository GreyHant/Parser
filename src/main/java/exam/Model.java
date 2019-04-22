package exam;

import java.util.List;
import java.util.Set;

public class Model {
    private Set<String> facts;
    private List<Rule> rules;

    public Model(Set<String> facts, List<Rule> rules) {
        this.facts = facts;
        this.rules = rules;
    }

    public void calculate(){
        int factsSize;
        do {
            factsSize = facts.size();
            for (Rule rule : rules) {
                rule.evaluate(facts);
            }
        } while (factsSize != facts.size());
    }

    public Set<String> getFacts() {
        return facts;
    }
}
