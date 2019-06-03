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
            System.err.println("Ошибка при работе с xml");
        else if (e.contains("JAXBException"))
            System.err.println("Ошибка при работе с xml");
        else if (e.contains("IOException"))
            System.err.println("Ошибка при работе с файлом, не найден или поврежден.");
        else if (e.contains("FileNotFoundException"))
            System.err.println("Ошибка при работе с файлом, не найден или поврежден.");
        else if (e.contains("SAXParseException"))
            System.err.println("Ошибка формата данных в xml");
        else if (e.contains("PersistenceException"))
            System.err.println("Ошибка при работе с базой данных");
        else System.err.println(e);
    }
}
