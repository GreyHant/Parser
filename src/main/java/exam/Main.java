package exam;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Отсутствует имя файла. Введите имя файла в качестве параметра при вызове утилиты.");
            return;
        }
        if (args[0].equals("xml")) {
            try {
                JAXBContext context = JAXBContext.newInstance(Model.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                Model model = (Model) unmarshaller.unmarshal(new File(args[1]));
                model.calculate();
                printFullFacts(model);
            } catch (JAXBException e) {
                System.out.println("Ошибка при работе с файлом.");
                return;
            }

        }
        if (args[0].equals("txt")) {
            Parser parser = new Parser();
            Model model;
            try {
                model = parser.parseFile(args[1]);
            } catch (InputException e) {
                System.out.println(e.getProblem());
                return;
            } catch (IOException ex) {
                System.out.println("Не удалось найти файл с указанным именем.");
                return;
            }
//            try {
//                convertTxtToXml(model);
//            } catch (JAXBException e) {
//                e.printStackTrace();
//            }
            model.calculate();
            printFullFacts(model);
        }
    }

    private static void printFullFacts(Model model) {
        Iterator<String> fi = model.getFacts().iterator();
        if (fi.hasNext())
            System.out.print(fi.next());

        while (fi.hasNext()) {
            System.out.print(", ");
            System.out.print(fi.next());
        }
    }

    private static void convertTxtToXml(Model model) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Model.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        m.marshal(model, new File("Model.xml"));
    }
}

