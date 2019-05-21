package exam;

import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Отсутствует тип данных. Введите тип данных в качестве параметра при вызове утилиты.");
            return;
        }
        if (args.length == 1) {
            System.out.println("Отсутствует имя файла. Введите имя файла в качестве параметра при вызове утилиты.");
            return;
        }
        if (args[0].equals("sql")) {
            ParserSQL parserSQL = new ParserSQL();
            try {
                Model model = parserSQL.parseFromDB(Integer.parseInt(args[1]));
                model.calculate();
                printFullFacts(model);
            } catch (IOException e) {
                System.out.println("Ошибка при работе с базой данных.");
//                e.printStackTrace();
            }
        }
        if (args[0].equals("xml")) {
            try {
                ParserXml parserXml = new ParserXml();
                Model model = parserXml.parseXml(args[1]);
                model.calculate();
                printFullFacts(model);
            } catch (JAXBException | SAXException e) {
                System.out.println("Ошибка при работе с файлом.");
//                e.printStackTrace();
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
//                Converter converter = new Converter();
//                converter.convertModelToXml(model);
//                converter.convertModelToSql(model);
//            } catch (JAXBException | FileNotFoundException e) {
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


}

