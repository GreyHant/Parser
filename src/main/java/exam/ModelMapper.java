package exam;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface ModelMapper {
    List<Integer> getModels();

    Set<String> getFacts(int model);

    List<DBRule> getRules(int model);

    DBExpression getExpression(int expr);

    List<DBExpression> getChildExpressions(int parent);

    Integer getLastInsert();

    Integer insertNewModel(@Param("description") String description);

    void insertFact(@Param("fact") String fact,@Param("idModel") int idModel);

    void insertRule(@Param("result") String result, @Param("idExpr") int idExpr,@Param("idModel") int idModel);

    void insertExpression(@Param("op") String op,@Param("value") String value,@Param("parent") int parent);

}
