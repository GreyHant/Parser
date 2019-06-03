package exam;

import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ParserSQL implements Parser {

    @Override
    public Model parse(String fileName, String modelName) throws IOException, FormatException {

        try (SqlSession session = SQLConnector.openSession(fileName)) {
            ModelMapper mapper = session.getMapper(ModelMapper.class);
            int idModel = mapper.getModel(modelName);
            Set<String> factsSet = mapper.getFacts(idModel);
            List<DBRule> dbRules = mapper.getRules(idModel);
            List<Rule> ruleList = new ArrayList<>();
            for (DBRule dbRule : dbRules) {
                Expression expression = makeExpression(mapper, mapper.getExpression(dbRule.getIdExpr()));
                Rule rule = new Rule(expression, dbRule.getResultFact());
                ruleList.add(rule);
            }
            return new Model(factsSet, ruleList);
        }
    }

    private Expression makeExpression(ModelMapper mapper, DBExpression dbExpression) throws FormatException {
        List<DBExpression> childExpressions = mapper.getChildExpressions(dbExpression.getIdExpr());
        Expression expression = null;
        List<Expression> expressionList = new ArrayList<>();
        switch (dbExpression.getOp()) {
            case "or":
                for (DBExpression childExpression : childExpressions) {
                    expressionList.add(makeExpression(mapper, childExpression));
                }
                expression = new OrExpression(expressionList);
                break;
            case "and":
                for (DBExpression childExpression : childExpressions) {
                    expressionList.add(makeExpression(mapper, childExpression));
                }
                expression = new AndExpression(expressionList);
                break;
            case "fact":
                String fact = dbExpression.getFact();
                if (fact.matches("_*\\p{IsAlphabetic}+[\\p{IsAlphabetic}_\\d]*"))
                    expression = new FactExpression(fact);
                else throw new FormatException("Ошибка формата в базе данных: " + fact);
                break;
        }
        return expression;
    }


}
