package exam;

import org.apache.ibatis.session.SqlSession;

import java.io.FileNotFoundException;
import java.util.Collection;

public class WriterSQL implements Writer, Serializer {

    private ModelMapper mapper;
    private DBModel dbModel;
    private int expressionId;

    @Override
    public void write(String fileName, Model model, String modelName) throws FileNotFoundException {
        try (SqlSession session = SQLConnector.openSession(fileName)) {
            mapper = session.getMapper(ModelMapper.class);
            dbModel = new DBModel(modelName);
            mapper.insertNewModel(dbModel);

            model.Serialize(this);

            session.commit();
        }
    }


    @Override
    public void SerializeRules(Collection<Rule> rules) {
        for (Rule rule : rules) {
            rule.Serialize(this);
        }
    }

    @Override
    public void SerializeKnownFacts(Collection<String> facts) {
        for (String fact : facts) {
            mapper.insertFact(fact, dbModel.getIdModel());
        }
    }

    @Override
    public void SerializeRule(Expression expression, String resultFact) {
        expressionId = 0;
        expression.Serialize(this);
        mapper.insertRule(resultFact, expressionId, dbModel.getIdModel());
    }

    @Override
    public void SerializeOrExpression(Collection<Expression> expressions) {
        DBExpression dbExpression = new DBExpression("or", null, expressionId);
        mapper.insertExpression(dbExpression);
        int exprId = dbExpression.getIdExpr();

        expressionId = exprId;
        for (Expression expression: expressions) {
            expression.Serialize(this);
        }

        expressionId = exprId;
    }

    @Override
    public void SerializeAndExpression(Collection<Expression> expressions) {
        DBExpression dbExpression = new DBExpression("and", null, expressionId);
        mapper.insertExpression(dbExpression);
        int exprId = dbExpression.getIdExpr();

        expressionId = exprId;
        for (Expression expression: expressions) {
            expression.Serialize(this);
        }

        expressionId = exprId;
    }

    @Override
    public void SerializeFactExpression(String fact) {
        DBExpression dbExpression = new DBExpression("fact", fact, expressionId);
        mapper.insertExpression(dbExpression);
        expressionId = dbExpression.getIdExpr();
    }


}
