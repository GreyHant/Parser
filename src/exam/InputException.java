package exam;

public class InputException extends Throwable {

    private String problem;

    public InputException(int row, int column) {
        this.problem = "������ ������� ������ � ������ [" + row + "], ������� " + "[" + column + "].";
    }

    public InputException(int row, String op) {
        this.problem = "������ ������� ������ � ������ [" + row + "]. ����������� �������� " + op + ".";
    }

    public InputException(int row) {
        this.problem = "������ ������� ������ � ������ [" + row + "].";
    }

    public String getProblem() {
        return problem;
    }
}
