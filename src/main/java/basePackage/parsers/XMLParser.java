package basePackage.parsers;

import basePackage.objectModel.Humans;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * It's class to parse to/from XML.
 */
public class XMLParser {
    JAXBContext context;

    {
        try {
            context = JAXBContext.newInstance(Humans.class);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param humans <code>{@link Humans}</code> with <code>Vector<Human></code>
     * @param file   file where need save <code>humans</code>
     * @throws JAXBException
     * @throws IOException
     */
    public void toXML(Humans humans, File file) throws JAXBException, IOException {
        if (humans != null && file != null) {
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(humans, new BufferedWriter(new FileWriter(file)));}
//        } else {
//            if (file)
//        }
    }

    /**
     * @param file from which need deserialize
     * @return object <code>Humans</code>
     * @throws JAXBException
     */
    public Humans fromXML(File file) throws JAXBException {
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Humans) unmarshaller.unmarshal(file);
    }
}
