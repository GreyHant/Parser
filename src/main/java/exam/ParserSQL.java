package exam;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ParserSQL {

    public Model parseFromDB(int idModel) throws IOException {

        Reader reader = new FileReader("SQLConfiguration.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader);

        try (SqlSession session = factory.openSession()) {
            ModelMapper mapper = session.getMapper(ModelMapper.class);
            Set<String> factsSet = mapper.getFacts(idModel);
            List<DBRule> dbRules = mapper.getRules(idModel);
            List<Rule> ruleList = new ArrayList<>();
            for (DBRule dbRule : dbRules) {
                Expression expression = makeExpression(mapper, mapper.getExpression(dbRule.getIdExpr()));
                Rule rule = new Rule(expression, dbRule.getResult());
                ruleList.add(rule);
            }
            return new Model(factsSet, ruleList);
        }
    }

    private Expression makeExpression(ModelMapper mapper, DBExpression dbExpression) {
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
                expression = new FactExpression(dbExpression.getValue());
                break;
        }
        return expression;
    }


}
