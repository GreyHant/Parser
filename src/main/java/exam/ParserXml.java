package exam;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;

public class ParserXml {
    public Model parseXml(String filename) throws JAXBException, SAXException {
        JAXBContext context = JAXBContext.newInstance(Model.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema employeeSchema = sf.newSchema(new File("XmlModel.xsd"));
        unmarshaller.setSchema(employeeSchema);
        return (Model) unmarshaller.unmarshal(new File(filename));
    }

}
