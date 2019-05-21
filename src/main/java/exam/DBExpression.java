package exam;

import java.io.Serializable;
import java.util.Set;

public class DBExpression implements Expression, Serializable {

    private int idExpr;
    private String op;
    private String value;
    private int parent;

    public String getValue() {
        return value;
    }

    public Integer getIdExpr() {
        return idExpr;
    }

    public String getOp() {
        return op;
    }

    @Override
    public boolean evaluate(Set<String> allFacts) {
        return false;
    }

    @Override
    public Object getExpressions() {
        return null;
    }
}
