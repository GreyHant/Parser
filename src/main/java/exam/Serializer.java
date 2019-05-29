package exam;

import java.util.Collection;

interface Serializer
{
    void SerializeRules(Collection<Rule> rules);
    void SerializeKnownFacts(Collection<String> facts);
    void SerializeRule(Expression expression, String resultFact);
    void SerializeOrExpression(Collection<Expression> expressions);
    void SerializeAndExpression(Collection<Expression> expressions);
    void SerializeFactExpression(String fact);

}