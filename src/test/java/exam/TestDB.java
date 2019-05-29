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

        }
    }
    @Test
    void parserSql() throws IOException {
        ParserSQL parserSQL = new ParserSQL();
    }
    @Test
    void deleterSql() throws IOException {
        DeleterSQL deleterSQL = new DeleterSQL();
        deleterSQL.delete("resources/SQLConfiguration.xml", "test");
    }
}