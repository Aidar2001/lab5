package BasePackage;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class XMLHandler extends DefaultHandler {
private Vector<Human> vectorWithHuman= new Vector<Human>();
    private SAXParserFactory factory = SAXParserFactory.newInstance();
    private SAXParser parser = factory.newSAXParser();


    XMLHandler(File file) throws ParserConfigurationException, SAXException {
        try {
            parser.parse(file,new DefaultHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("Human")){
            vectorWithHuman.add(new Human(attributes.getValue("name")));
        }
        if (qName.equals("actions")){

        }
    }


}
