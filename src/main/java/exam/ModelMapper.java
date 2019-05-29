package exam;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface ModelMapper {
    Integer getModel(String name);

    Set<String> getFacts(int model);

    List<DBRule> getRules(int model);

    DBExpression getExpression(int expr);

    List<DBExpression> getChildExpressions(int parent);

    void insertNewModel(DBModel dbModel);

    void insertFact(@Param("fact") String fact,@Param("idModel") int idModel);

    void insertRule(@Param("resultfact") String result, @Param("idExpr") int idExpr,@Param("idModel") int idModel);

    void insertExpression(DBExpression dbExpression);

    void deleteModel(@Param("idmodel") int idmodel);

    void deleteFacts(@Param("model") int model);

    void deleteRules(@Param("model") int model);

    void deleteExpressions(@Param("list") List<Integer> idExpressions);

    void deleteChildExpressions(@Param("parent") int parent);

}
