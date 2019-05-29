package exam;

import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface Parser {
    Model parse(String fileName, String modelName) throws IOException, InputException, JAXBException, SAXException;
}
