package exam;

import java.util.Set;

public interface Expression {

    boolean evaluate(Set<String> allFacts);

    void Serialize(Serializer s) throws SerializationException;
}
