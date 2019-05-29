package exam;

import org.apache.ibatis.session.SqlSession;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class DeleterSQL {

    public void delete(String config, String modelName) throws FileNotFoundException {
        try (SqlSession session = SQLConnector.openSession(config)) {
            ModelMapper mapper = session.getMapper(ModelMapper.class);
            int idModel = mapper.getModel(modelName);
            List<DBRule> dbRuleList = mapper.getRules(idModel);
            for (DBRule dbRule : dbRuleList) {
                mapper.deleteExpressions(getChildren(mapper, mapper.getExpression(dbRule.getIdExpr())));
            }
            mapper.deleteRules(idModel);
            mapper.deleteFacts(idModel);
            mapper.deleteModel(idModel);
            session.commit();
        }
    }

    private List<Integer> getChildren(ModelMapper mapper, DBExpression dbExpression) {
        List<DBExpression> childExpressions = mapper.getChildExpressions(dbExpression.getIdExpr());
        List<Integer> idList = new ArrayList<>();
        if (dbExpression.getOp().equals("or")) {
            idList.add(dbExpression.getIdExpr());
            for (DBExpression childExpression : childExpressions) {
                idList.addAll(getChildren(mapper, childExpression));
            }
        }
        if (dbExpression.getOp().equals("and")) {
            idList.add(dbExpression.getIdExpr());
            for (DBExpression childExpression : childExpressions) {
                idList.addAll(getChildren(mapper, childExpression));
            }
        }
        if ("fact".equals(dbExpression.getOp())) {
            idList.add(dbExpression.getIdExpr());
        }
        return idList;
    }
}
