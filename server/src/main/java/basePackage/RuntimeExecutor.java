package basePackage;

import basePackage.connect.CollectionInfo;
import basePackage.objectModel.Human;
import basePackage.objectModel.Humans;
import basePackage.parsers.XMLParser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class execute commands.
 */
public class RuntimeExecutor implements Executor {
    static String fs = System.getProperty("file.separator");
    //    private static final File COLLECTIONS_DIRECTORY = new File("C:\\Users\\Aidar\\Desktop\\lab6\\lab6\\Collectins"); // TODO rewrite pathname
    private static final File COLLECTIONS_DIRECTORY = new File("/home/pihanya/Development/Couching/aydar/lab6");
    private static final String DEFAULT_COLLECTION_NAME = "Default";

    private String collectionName;

    private XMLParser parser = new XMLParser();
    private Date dateOfInit = new Date();
    private CopyOnWriteArrayList<Human> humanList = new CopyOnWriteArrayList<>();

    public RuntimeExecutor() throws JAXBException {
        this.collectionName = DEFAULT_COLLECTION_NAME;
        loadCollection();
    }

    public CollectionInfo info() {
        return new CollectionInfo()
                .withCollectionName(collectionName)
                .withCollectionType(humanList.getClass().getName())
                .withInitializationDate(dateOfInit)
                .withCollectionSize(humanList.size());
    }

    public boolean remove(Integer id) {
        Objects.requireNonNull(id);
        return humanList.removeIf(human -> id.equals(human.getId()));
    }

    public boolean add(Human human) {
        boolean has = humanList
                .stream()
                .anyMatch(collectionHuman -> collectionHuman.getId() == human.getId());

        if (has) {
            return false;
        }

        humanList.add(human);
        Collections.sort(humanList);
        return true;
    }

    public boolean addIfMax(Human human) {
        if (human.compareTo(humanList.get(humanList.size())) > 0) {
            boolean hasHuman = humanList
                    .stream()
                    .anyMatch(collectionHuman -> collectionHuman.getId() == human.getId());

            if (hasHuman) {
                return false;
            }

            add(human);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeFirst() {
        boolean empty = (humanList.size() == 0);
        humanList.remove(0);
        return !empty;
    }

    public List<Human> show() {
        return humanList;
    }

    public boolean removeGreater(Human human) {
        Objects.requireNonNull(human);
        return humanList.removeIf(element -> element.compareTo(human) > 0);
    }

    public boolean loadCollection() {
        File collectionFile = new File(COLLECTIONS_DIRECTORY, collectionName + ".xml");
        if (!collectionFile.exists()) {
            return false;
        }

        List<Human> humans;
        try {
            humans = parser.fromXML(collectionFile).getHumans();
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
        if (humans == null) {
            System.out.println("XML file does not contain Humans. Please add humans to XML or give a correct XML.");
            return false; // TODO: Maybe throw exception
        }
        this.humanList = new CopyOnWriteArrayList<>(humans);

        return true;
    }

    public void saveCollection() throws IOException {
        File collectionFile = new File(COLLECTIONS_DIRECTORY, collectionName + ".xml");

        Humans humans = new Humans();
        humans.setHumans(humanList);
        try {
            parser.toXML(humans, collectionFile);
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }
}
