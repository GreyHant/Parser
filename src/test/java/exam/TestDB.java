package exam;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

class TestDB {

    @Test
    void openSesssion() throws FileNotFoundException {

        try (SqlSession session = SQLConnector.openSession("resources/SQLConfiguration.xml")) {
            ModelMapper mapper = session.getMapper(ModelMapper.class);
//            Integer integer = mapper.getModel("123");
//            System.out.println(integer);
            WriterSQL writer = new WriterSQL(mapper, session);
        }
    }
    @Test
    void parserSql() throws IOException {
        ParserSQL parserSQL = new ParserSQL();
    }
//    @Test
//    void deleterSql() throws IOException {
//        DeleterSQL deleterSQL = new DeleterSQL();
//        deleterSQL.delete("resources/SQLConfiguration.xml", "test");
//    }
}