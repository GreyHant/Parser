package exam;

public class FormatException extends Exception {

    public FormatException(int row, int column) {
        super("������ ������� ������ � ������ [" + row + "], ������� " + "[" + column + "].");
    }

    public FormatException(int row, String op) {
        super("������ ������� ������ � ������ [" + row + "]. ����������� �������� " + op + ".");
    }

    public FormatException(String message) {
        super(message);
    }

    public FormatException(int row) {
        super("������ ������� ������ � ������ [" + row + "].");
    }

}
