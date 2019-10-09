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
public class Executor {
    static String fs = System.getProperty("file.separator");
    private static final File COLLECTIONS_DIRECTORY = new File("C:\\Users\\Aidar\\Desktop\\lab6\\lab6\\Collectins"); // TODO rewrite pathname
    private static final String DEFAULT_COLLECTION_NAME = "default.xml";

    private XMLParser parser;
    private Date dateOfInit;
    private String name;
    private List<Human> humanList;

    {
        parser = new XMLParser();
        humanList = new Vector<>();
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

        if(has) {
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

    public boolean loadCollection(String collectionName) throws JAXBException {
        if(collectionName == null) {
            collectionName = name;
        }

            File collectionFile = new File(COLLECTIONS_DIRECTORY, collectionName);
            if(!collectionFile.exists()) {
                return false;
            }

        List<Human> humans = parser.fromXML(collectionFile).getHumans();
        if (humans == null) {
                System.out.println("XML file is empty. Please add humans to XML or give correct XML.");
                return false;
            }

        this.humanList = humans;
            this.name = collectionName;
            return true;
    }

    public boolean saveCollection(String collectionName) {
        if(collectionName == null) {
            collectionName = name;
        }
        File collectionFile = new File(COLLECTIONS_DIRECTORY, collectionName  + ".xml");

        Humans humans = new Humans();
        CopyOnWriteArrayList list = new CopyOnWriteArrayList();
        list.addAll(humanList);
        humans.setHumans(list);
        try {
            parser.toXML(humans, collectionFile);
            return true;
        } catch (JAXBException | IOException e) {
            System.err.println("Error saving file " + e.getClass().getName());
            return false;
        }
    }
}
