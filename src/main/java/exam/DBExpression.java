package exam;

import java.io.Serializable;

public class DBExpression implements Serializable {

    private int idExpr;
    private String op;
    private String fact;
    private int parent;

    public String getFact() {
        return fact;
    }

    public Integer getIdExpr() {
        return idExpr;
    }

    public String getOp() {
        return op;
    }

    public DBExpression(String op, String fact, int parent) {
        this.op = op;
        this.fact = fact;
        this.parent = parent;
    }

    public DBExpression(int idExpr, String op, String fact, int parent) {
        this.idExpr = idExpr;
        this.op = op;
        this.fact = fact;
        this.parent = parent;
    }
}
