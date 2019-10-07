package basePackage;

import basePackage.connect.CollectionInfo;
import basePackage.objectModel.Human;
import basePackage.objectModel.Humans;
import basePackage.parsers.XMLParser;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import javax.xml.bind.JAXBException;

/**
 * This class execute commands.
 */
public class Executor {
    static String fs = System.getProperty("file.separator");
    private static final File COLLECTIONS_DIRECTORY = new File("C:\\Users\\Aidar\\Collectins");
    private static final String DEFAULT_COLLECTION_NAME = "default.xml";

    private XMLParser parser;
    private Date dateOfInit;
    private String name;
    private Vector<Human> humanVector;

    {
        parser = new XMLParser();
        humanVector = new Vector<>();
        dateOfInit = new Date();
        name = DEFAULT_COLLECTION_NAME;

        try {
            loadCollection(name);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Saving collection \'" + name + "\'");
            saveCollection(name);
        }));
    }

    public CollectionInfo info() {
        return new CollectionInfo()
            .withCollectionName(name)
            .withCollectionType(humanVector.getClass().getName())
            .withInitializationDate(dateOfInit)
            .withCollectionSize(humanVector.size());
    }

    public boolean remove(Integer id) {
        Objects.requireNonNull(id);
        return humanVector.removeIf(human -> id.equals(human.getId()));
    }

    public boolean add(Human human) {
        boolean has = humanVector
            .stream()
            .anyMatch(collectionHuman -> collectionHuman.getId() == human.getId());

        if(has) {
            return false;
        }

        humanVector.add(human);
        Collections.sort(humanVector);
        return true;
    }

    public boolean addIfMax(Human human) {
        if (human.compareTo(humanVector.lastElement()) > 0) {
            boolean hasHuman = humanVector
                .stream()
                .anyMatch(collectionHuman -> collectionHuman.getId() == human.getId());

            if(hasHuman) {
                return false;
            }

            add(human);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeFirst() {
        boolean empty = (humanVector.size() == 0);
        humanVector.remove(0);
        return !empty;
    }

    public List<Human> show() {
        return humanVector;
    }

    public boolean removeGreater(Human human) {
        Objects.requireNonNull(human);
        return humanVector.removeIf(element -> element.compareTo(human) > 0);
    }

    public boolean loadCollection(String collectionName) throws JAXBException {
        if(collectionName == null) {
            collectionName = name;
        }

            File collectionFile = new File(COLLECTIONS_DIRECTORY, collectionName);
            if(!collectionFile.exists()) {
                return false;
            }

            Vector<Human> humans = parser.fromXML(collectionFile).getHumans();
            if (humans == null) {
                System.out.println("XML file is empty. Please add humans to XML or give correct XML.");
                return false;
            }

            this.humanVector = humans;
            this.name = collectionName;
            return true;
    }

    public boolean saveCollection(String collectionName) {
        if(collectionName == null) {
            collectionName = name;
        }
        File collectionFile = new File(COLLECTIONS_DIRECTORY, collectionName);

        Humans humans = new Humans();
        humans.setHumans(this.humanVector);
        try {
            parser.toXML(humans, collectionFile);
            return true;
        } catch (JAXBException | IOException e) {
            System.out.println("Error saving file");
            return false;
        }
    }
}
