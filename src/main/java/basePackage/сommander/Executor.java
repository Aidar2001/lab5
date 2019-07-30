package basePackage.сommander;

import basePackage.objectModel.Human;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

public class Executor {
    private Vector<Human> humans;
    private Date dateOfInit;
    private File file;

    {
        humans = new Vector<>();
        dateOfInit = new Date();
        Runtime.getRuntime().addShutdownHook(new Thread(this::saveFile));
    }

    public void info() {
        System.out.println("Коллекция типа Vector и содержит людей");
        System.out.println("Дата инициализации: " + dateOfInit);
        System.out.println("Размер коллекции на данный момент: " + humans.size());
    }

    public void remove(Human humanForRemove) {
        if (humans.remove(humanForRemove))
            System.out.println("Элемент удалён");
        else
            System.out.println("Коллекция не содержит данный элемент");
    }

    public void add(Human human) {
        humans.add(human);
        Collections.sort(humans);
        System.out.println("Элемент добавлен");
    }

    public void addIfMax(Human human) {
        if (human.compareTo(humans.lastElement()) > 0)
            add(human);
    }

    public void removeFirst() {
        humans.remove(0);
        System.out.println("Превый элемент коллекции удален");
    }

    public void show() {
        humans.forEach(System.out::println);
    }

    public void removeGreater(Human human) {
        humans.removeIf(element -> element.compareTo(human) > 0);
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
        // TODO fromXML
        this.file = file;
    }

    public void exit() {
// todo проверить сохранение файла
        System.exit(0);
    }

    private void saveFile() {
        //TODO Написать метод, toXML

    }
}
