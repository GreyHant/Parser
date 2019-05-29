package exam;

import java.io.Serializable;

public class DBRule implements Serializable {

    private int idRule;
    private int idExpr;
    private String resultFact;
    private int idModel;

    public String getResultFact() {
        return resultFact;
    }

    public Integer getIdExpr() {
        return idExpr;
    }

    @Override
    public String toString() {
        return "DBRule{" +
                "idRule=" + idRule +
                ", idExpr=" + idExpr +
                ", resultFact='" + resultFact + '\'' +
                ", model=" + idModel +
                '}';
    }
}
