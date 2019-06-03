package exam;

import org.apache.ibatis.exceptions.PersistenceException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class Engine {

    public enum Type {Txt, Xml, Sql}

    private Presenter presenter;

    public Engine(Presenter presenter) {
        this.presenter = presenter;
    }

    public void deduce(Type type, String inputFileName, String inputModelName) {
        try {
            Parser parser = createParser(type);
            Model model = parser.parse(inputFileName, inputModelName);
            presenter.showResult(model.deduce());
        } catch (IOException | JAXBException | SAXException | PersistenceException e) {
            presenter.showError(e.toString());
        } catch (FormatException | IllegalStateException e) {
            presenter.showError(e.getMessage());
        }
    }

    public void convert(Type inputType, Type outputType, String inputFileName, String inputModelName, String outputFileName, String outputModelName) {
        try {
            Parser parser = createParser(inputType);
            Model model = parser.parse(inputFileName, inputModelName);
            Writer writer = createWriter(outputType);
            writer.write(outputFileName, model, outputModelName);
        } catch (IOException | JAXBException | SAXException | PersistenceException e) {
            presenter.showError(e.toString());
        } catch (FormatException | IllegalStateException | SerializationException e) {
            presenter.showError(e.getMessage());
        }
    }

    private Writer createWriter(Type type) {
        Writer writer;
        switch (type) {
            case Xml:
                writer = new WriterXml();
                break;
            case Sql:
                writer = new WriterSQL();
                break;
            default:
                throw new IllegalStateException("Неизвестное значение типа: " + type);
        }
        return writer;
    }

    private Parser createParser(Type type) {
        Parser parser;
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
            default:
                throw new IllegalStateException("Неизвестное значение типа: " + type);
        }
        return parser;
    }


}
