package exam;

import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Set;

public class Engine {

    public enum Type {Txt, Xml, Sql}

    public Set<String> deduce(Type type, String inputFileName, String inputModelName) {
        Parser parser = makeParser(type);
        Model model = null;
        try {
            model = parser.parse(inputFileName, inputModelName);
        } catch (IOException e) {
            System.err.println("Îרטבךא ןנט נאבמעו ס פאיכמל");
        } catch (InputException e) {
            System.err.println(e.getProblem());
        } catch (JAXBException | SAXException e) {
            System.err.println("Îרטבךא ןנט נאבמעו ס ץml");
        }

        return model.deduce();
    }

    public boolean convert(Type inputType, Type outputType, String inputFileName, String inputModelName, String outputFileName, String outputModelName) {
        Parser parser = makeParser(inputType);
        try {
            Model model = parser.parse(inputFileName, inputModelName);
            Writer writer = makeWriter(outputType);
            writer.write(outputFileName, model, outputModelName);
        } catch (IOException e) {
            System.err.println("Îרטבךא ןנט נאבמעו ס פאיכמל");
        } catch (InputException e) {
            System.err.println(e.getProblem());
        } catch (JAXBException | SAXException e) {
            System.err.println("Îרטבךא ןנט נאבמעו ס ץml");
        }
        return true;
    }

    private Writer makeWriter(Type type) {
        Writer writer = null;
        switch (type) {
            case Xml:
                writer = new WriterXml();
                break;
            case Sql:
                writer = new WriterSQL();
                break;
        }
        return writer;
    }

    private Parser makeParser(Type type) {
        Parser parser = null;
        switch (type) {
            case Txt:
                parser = new ParserTxt();
                break;
            case Xml:
                parser = new ParserXml();
                break;
            case Sql:
                parser = new ParserSQL();
                break;
        }
        return parser;
    }


}
