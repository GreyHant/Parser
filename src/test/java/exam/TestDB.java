package exam;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

class TestDB {

    @Test
    void openSesssion() throws FileNotFoundException {
        Reader reader = new FileReader("SQLConfiguration.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader);

        try (SqlSession session = factory.openSession()) {
            ModelMapper mapper = session.getMapper(ModelMapper.class);
            mapper.insertNewModel("description");
            int idModel = mapper.getLastInsert();
            System.out.println(idModel);
        }
    }
    @Test
    void parserSql() throws IOException {
        ParserSQL parserSQL = new ParserSQL();
        System.out.println(parserSQL.parseFromDB(34));
    }
}