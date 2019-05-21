package exam;

import java.io.Serializable;

public class DBRule implements Serializable {

    private int idRule;
    private int idExpr;
    private String result;
    private int idModel;

    public String getResult() {
        return result;
    }

    public Integer getIdExpr() {
        return idExpr;
    }

    @Override
    public String toString() {
        return "DBRule{" +
                "idRule=" + idRule +
                ", idExpr=" + idExpr +
                ", result='" + result + '\'' +
                ", model=" + idModel +
                '}';
    }
}
