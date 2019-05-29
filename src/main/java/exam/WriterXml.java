package exam;

import org.xml.sax.SAXException;

import javax.validation.constraints.Null;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.util.Objects;

public class WriterXml implements Writer{

    @Override
    public void write(String fileName, Model model, @Null String modelName) throws JAXBException, SAXException {
        JAXBContext context = JAXBContext.newInstance(Model.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        ClassLoader classLoader = getClass().getClassLoader();
        Schema modelSchema = sf.newSchema(Objects.requireNonNull(classLoader.getResource("XmlModel.xsd")));
        m.setSchema(modelSchema);

        if (fileName.substring(fileName.length()-4).equals(".xml")){
            m.marshal(model, new File(fileName));
        } else m.marshal(model, new File(fileName + ".xml"));

    }
}
