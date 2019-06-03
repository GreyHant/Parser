package exam;

public class FormatException extends Exception {

    public FormatException(int row, int column) {
        super("Ошибка формата данных в строке [" + row + "], столбце " + "[" + column + "].");
    }

    public FormatException(int row, String op) {
        super("Ошибка формата данных в строке [" + row + "]. Отсутствует оператор " + op + ".");
    }

    public FormatException(String message) {
        super(message);
    }

    public FormatException(int row) {
        super("Ошибка формата данных в строке [" + row + "].");
    }

}
