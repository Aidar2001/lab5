package basePackage.сommander;

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
    XMLParser parser = new XMLParser();
    private Date dateOfInit;
    private File file;
    private Vector<Human> humanVector;

    {
        humanVector = new Vector<>();
        dateOfInit = new Date();
        Runtime.getRuntime().addShutdownHook(new Thread(this::saveFile));
    }

    public void info() {
        System.out.println("Коллекция типа Vector и содержит людей");
        System.out.println("Дата инициализации: " + dateOfInit);
        System.out.println("Размер коллекции на данный момент: " + humanVector.size());
    }

    public void remove(Human humanForRemove) {
        if (humanVector.remove(humanForRemove))
            System.out.println("Элемент удалён");
        else
            System.out.println("Коллекция не содержит данный элемент");
    }

    public void add(Human human) {
        humanVector.add(human);
        Collections.sort(humanVector);
        System.out.println("Элемент добавлен");
    }

    public void addIfMax(Human human) {
        if (human.compareTo(humanVector.lastElement()) > 0)
            add(human);
    }

    public void removeFirst() {
        humanVector.remove(0);
        System.out.println("Превый элемент коллекции удален");
    }

    public void show() {
        humanVector.forEach(System.out::println);
    }

    public void removeGreater(Human human) {
        humanVector.removeIf(element -> element.compareTo(human) > 0);
    }

    public void help() {
        System.out.println(
                "\tremove {element}: удалить элемент из коллекции по его значению\n" +
                        "\tinfo: вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                        "\tadd {element}: добавить новый элемент в коллекцию\n" +
                        "\tadd_if_max {element}: добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n" +
                        "\tremove_greater {element}: удалить из коллекции все элементы, превышающие заданный\n" +
                        "\tshow: вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                        "\tremove_first: удалить первый элемент из коллекции\n" +
                        "\texit: выход из программы\n" +
                        "\n");
    }

    public void importFile(File file) {
        this.file = file;
        try {
            this.humanVector = parser.fromXML(file).getHumans();
        } catch (JAXBException e) {
            System.out.println("Файл не может быть считан.");
            exit();
        }
    }

    public void exit() {
        // todo проверить сохранение файла
        System.exit(0);
    }

    private void saveFile() {
        Humans humans = new Humans();
        humans.setHumans(this.humanVector);
        try {
            parser.toXML(humans, file);
        } catch (JAXBException | IOException e) {
            System.out.println("Ошибка при сохранении файла.");
        }
    }
}
