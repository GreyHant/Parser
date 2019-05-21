package exam;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

public class Converter {

    public void convertModelToXml(Model model) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Model.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        m.marshal(model, new File("Model.xml"));
    }

    public void convertModelToSql(Model model) throws FileNotFoundException {
        Reader reader = new FileReader("SQLConfiguration.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader);

        try (SqlSession session = factory.openSession()) {
            ModelMapper mapper = session.getMapper(ModelMapper.class);
            mapper.insertNewModel("description");
            int idModel = mapper.getLastInsert();
            for (String fact : model.getFacts()) {
                mapper.insertFact(fact, idModel);
            }
            List<Rule> ruleList = model.getRules();
            for (Rule rule : ruleList) {
                mapper.insertRule(rule.getResultFact(), insertExpression(mapper, rule.getExpression(), 0), idModel);
            }
            session.commit();
        }
    }

    private int insertExpression(ModelMapper mapper, Expression expression, Integer parent){
        Integer insertedId = 0;
        if (expression.getClass() == OrExpression.class){
            mapper.insertExpression("or", null, parent);
            insertedId = mapper.getLastInsert();
            List<Expression> expressionList = (List<Expression>) expression.getExpressions();
            for (Expression childExpression : expressionList){
                insertExpression(mapper, childExpression, insertedId);
            }
        }
        if (expression.getClass() == AndExpression.class){
            mapper.insertExpression("and", null, parent);
            insertedId = mapper.getLastInsert();
            List<Expression> expressionList = (List<Expression>) expression.getExpressions();
            for (Expression childExpression : expressionList){
                insertExpression(mapper, childExpression, insertedId);
            }
        }
        if (expression.getClass() == FactExpression.class){
            mapper.insertExpression("fact", expression.getExpressions().toString(), parent);
            insertedId = mapper.getLastInsert();
        }
        return insertedId;
    }
}
