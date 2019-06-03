package exam;

import java.util.Collection;

interface Serializer
{
    void SerializeModel(Collection<Rule> rules, Collection<String> knownFacts) throws SerializationException;
    void SerializeRule(Expression expression, String resultFact) throws SerializationException;
    void SerializeOrExpression(Collection<Expression> expressions) throws SerializationException;
    void SerializeAndExpression(Collection<Expression> expressions) throws SerializationException;
    void SerializeFactExpression(String fact) throws SerializationException;
}