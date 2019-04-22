package exam;

public class InputException extends Throwable {

    private String problem;

    public InputException(int row, int column) {
        this.problem = "Ошибка формата данных в строке [" + row + "], столбце " + "[" + column + "].";
    }

    public InputException(int row, String op) {
        this.problem = "Ошибка формата данных в строке [" + row + "]. Отсутствует оператор " + op + ".";
    }

    public InputException(int row) {
        this.problem = "Ошибка формата данных в строке [" + row + "].";
    }

    public String getProblem() {
        return problem;
    }
}
