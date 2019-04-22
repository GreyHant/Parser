package exam;

import java.io.IOException;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("����������� ��� �����. ������� ��� ����� � �������� ��������� ��� ������ �������.");
            return;
        }

        Parser parser = new Parser();
        Model model;
        try {
            model = parser.parseFile(args[0]);
        } catch (InputException e) {
            System.out.println(e.getProblem());
            return;
        } catch (IOException ex) {
            System.out.println("�� ������� ����� ���� � ��������� ������.");
            return;
        }
        model.calculate();
        Iterator<String> fi = model.getFacts().iterator();
        if (fi.hasNext())
            System.out.print(fi.next());

        while (fi.hasNext()) {
            System.out.print(", ");
            System.out.print(fi.next());
        }
    }
}

