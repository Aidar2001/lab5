package basePackage.parsers;

import basePackage.objectModel.Humans;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XMLParser {
    JAXBContext context;

    {
        try {
            context = JAXBContext.newInstance(Humans.class);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void toXML(Humans humans, File file) throws JAXBException {
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(humans, file);
    }

    public Humans fromXML(File file) throws JAXBException {
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Humans) unmarshaller.unmarshal(file);
    }
}
