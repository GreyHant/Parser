package exam;

import org.apache.ibatis.session.SqlSession;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WriterSQL implements Writer, Serializer {

    private ModelMapper mapper;
    private DBModel dbModel;
    private int expressionId;
    private SqlSession session;
    private String modelName;
    private boolean writingRule;

    @Override
    public void write(String fileName, Model model, String modelName) throws FileNotFoundException, SerializationException {
        this.modelName = modelName;
        session = SQLConnector.openSession(fileName);
        try {
            mapper = session.getMapper(ModelMapper.class);

            model.Serialize(this);

            session.commit();
        } finally {
            session.close();
        }
    }

    private void delete(String modelName) {

        int idModel = mapper.getModel(modelName);
        List<DBRule> dbRuleList = mapper.getRules(idModel);
        for (DBRule dbRule : dbRuleList) {
            mapper.deleteExpressions(getExpressionsId(mapper.getExpression(dbRule.getIdExpr())));
        }
        mapper.deleteRules(idModel);
        mapper.deleteFacts(idModel);
        mapper.deleteModel(idModel);
        session.commit();

    }

    private List<Integer> getExpressionsId(DBExpression dbExpression) {
        List<DBExpression> childExpressions = mapper.getChildExpressions(dbExpression.getIdExpr());
        List<Integer> idList = new ArrayList<>();
        if (dbExpression.getOp().equals("or")) {
            idList.add(dbExpression.getIdExpr());
            for (DBExpression childExpression : childExpressions) {
                idList.addAll(getExpressionsId(childExpression));
            }
        }
        if (dbExpression.getOp().equals("and")) {
            idList.add(dbExpression.getIdExpr());
            for (DBExpression childExpression : childExpressions) {
                idList.addAll(getExpressionsId(childExpression));
            }
        }
        if ("fact".equals(dbExpression.getOp())) {
            idList.add(dbExpression.getIdExpr());
        }
        return idList;
    }

    @Override
    public void SerializeModel(Collection<Rule> rules, Collection<String> knownFacts) throws SerializationException {
        if (session == null || mapper == null || modelName == null) {
            throw new SerializationException("Ошибка инициализации базы данных");
        }
        if (dbModel != null) {
            throw new SerializationException("Неожиданный вызов метода");
        }
        if (mapper.getModel(modelName) != null) {
            delete(modelName);
        }
        dbModel = new DBModel(modelName);
        mapper.insertNewModel(dbModel);
        expressionId = 0;

        for (String fact : knownFacts)
            mapper.insertFact(fact, dbModel.getIdModel());
        for (Rule rule : rules)
            rule.Serialize(this);

        dbModel = null;
    }

    @Override
    public void SerializeRule(Expression expression, String resultFact) throws SerializationException {
        if (mapper == null || session == null) {
            throw new SerializationException("Ошибка инициализации базы данных");
        }
        if (dbModel == null ||expressionId != 0) {
            throw new SerializationException("Неожиданный вызов метода");
        }
        writingRule = true;
        expression.Serialize(this);
        mapper.insertRule(resultFact, expressionId, dbModel.getIdModel());

        expressionId = 0;
        writingRule = false;
    }

    @Override
    public void SerializeOrExpression(Collection<Expression> expressions) throws SerializationException {
        if (session == null || mapper == null) {
            throw new SerializationException("Ошибка инициализации базы данных");
        }
        if (!writingRule || dbModel == null)
            throw new SerializationException("Неожиданный вызов метода");

        DBExpression dbExpression = new DBExpression("or", null, expressionId);
        mapper.insertExpression(dbExpression);
        int exprId = dbExpression.getIdExpr();

        for (Expression expression : expressions) {
            expressionId = exprId;
            expression.Serialize(this);
        }

        expressionId = exprId;
    }

    @Override
    public void SerializeAndExpression(Collection<Expression> expressions) throws SerializationException {
        if (session == null || mapper == null) {
            throw new SerializationException("Ошибка инициализации базы данных");
        }
        if (!writingRule || dbModel == null)
            throw new SerializationException("Неожиданный вызов метода");

        DBExpression dbExpression = new DBExpression("and", null, expressionId);
        mapper.insertExpression(dbExpression);
        int exprId = dbExpression.getIdExpr();


        for (Expression expression : expressions) {
            expressionId = exprId;
            expression.Serialize(this);
        }

        expressionId = exprId;
    }

    @Override
    public void SerializeFactExpression(String fact) throws SerializationException {
        if (session == null || mapper == null) {
            throw new SerializationException("Ошибка инициализации базы данных");
        }
        if (!writingRule || dbModel == null)
            throw new SerializationException("Неожиданный вызов метода");

        DBExpression dbExpression = new DBExpression("fact", fact, expressionId);
        mapper.insertExpression(dbExpression);
    }

    public WriterSQL(ModelMapper mapper, SqlSession session) {
        this.mapper = mapper;
        this.session = session;
    }

    public WriterSQL() {
    }
}
