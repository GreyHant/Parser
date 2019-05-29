package exam;

import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface Writer {
    void write(String fileName, Model model, String modelName) throws FileNotFoundException, JAXBException, SAXException;
}
