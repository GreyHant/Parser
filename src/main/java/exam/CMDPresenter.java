package exam;

import java.util.Iterator;
import java.util.Set;

public class CMDPresenter implements Presenter {
    @Override
    public void showResult(Set<String> fullFacts) {
        Iterator<String> fi = fullFacts.iterator();
        if (fi.hasNext())
            System.out.print(fi.next());

        while (fi.hasNext()) {
            System.out.print(", ");
            System.out.print(fi.next());
        }
    }

    @Override
    public void showError(String e) {
        if (e.contains("SAXException"))
            System.err.println("������ ��� ������ � xml");
        else if (e.contains("JAXBException"))
            System.err.println("������ ��� ������ � xml");
        else if (e.contains("IOException"))
            System.err.println("������ ��� ������ � ������, �� ������ ��� ���������.");
        else if (e.contains("FileNotFoundException"))
            System.err.println("������ ��� ������ � ������, �� ������ ��� ���������.");
        else if (e.contains("SAXParseException"))
            System.err.println("������ ������� ������ � xml");
        else if (e.contains("PersistenceException"))
            System.err.println("������ ��� ������ � ����� ������");
        else System.err.println(e);
    }
}
