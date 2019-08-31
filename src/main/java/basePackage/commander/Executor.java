package basePackage.commander;

import basePackage.objectModel.Human;
import basePackage.objectModel.Humans;
import basePackage.parsers.XMLParser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

public class Executor {
    XMLParser parser;
    private Date dateOfInit;
    private File file;
    private Vector<Human> humanVector;

    {
        parser = new XMLParser();
        humanVector = new Vector<>();
        dateOfInit = new Date();
        Runtime.getRuntime().addShutdownHook(new Thread(this::saveFile));
    }

    public void info() {
        System.out.println("Collection is Vector type and contains humans");
        System.out.println("Initialization date: " + dateOfInit);
        System.out.println("Collection size at the moment: " + humanVector.size());
    }

    public void remove(Human humanForRemove) {
        if (humanVector.remove(humanForRemove))
            System.out.println("Element was removed");
        else
            System.out.println("The collection doesn't contains this element");
    }

    public void add(Human human) {
        humanVector.add(human);
        Collections.sort(humanVector);
        System.out.println("Element was added");
    }

    public void addIfMax(Human human) {
        if (human.compareTo(humanVector.lastElement()) > 0) {
            add(human);
            System.out.println("Element was added");
        } else {
            System.out.println("Element wasn't added");
        }
    }

    public void removeFirst() {
        humanVector.remove(0);
        System.out.println("First element of collection was removed");
    }

    public void show() {
        humanVector.forEach(System.out::println);
    }

    public void removeGreater(Human human) {
        humanVector.removeIf(element -> element.compareTo(human) > 0);
    }

    public void help() {
        System.out.println(
                "\tremove {element}: remove an element from the collection by its value\n" +
                        "\tinfo: show information about collection (type, initialization date , number of items  etc.)\n" +
                        "\tadd {element}: add new item to collection\n" +
                        "\tadd_if_max {element}: add a new element to the collection, if its value exceeds the the value of largest element of this collection\n" +
                        "\tremove_greater {element}: remove from the collection, all elements that exceed specified\n" +
                        "\tshow: вывести show all elements of the collection in the string view\n" +
                        "\tremove_first: remove first element of collection\n" +
                        "\texit: end the program");
    }

    public void importFile(File file) {
        this.file = file;
        try {
            this.humanVector = parser.fromXML(file).getHumans();
        } catch (JAXBException e) {
            System.out.println("File can not be read");
            exit();
        }
    }

    public void exit() {
        saveFile();
        System.exit(0);
    }

    private void saveFile() {
        Humans humans = new Humans();
        humans.setHumans(this.humanVector);
        try {
            parser.toXML(humans, file);
        } catch (JAXBException | IOException e) {
            System.out.println("Error saving file");
        }
    }
}
