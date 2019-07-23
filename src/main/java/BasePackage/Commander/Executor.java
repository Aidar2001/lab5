package BasePackage.Commander;

import BasePackage.ObjectModel.Human;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

public class Executor {
    private Vector<Human> humans;
    private Date dateOfInit;

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

    public void removeFirst() {
        humans.remove(0);
        System.out.println("Превый элемент коллекции удален");
    }

    public void show() {
        humans.forEach(System.out::println);
    }

    public void removeGreater(Human human) {
        humans.removeIf(element -> element.compareTo(human) > 0);


        if (humans.contains(human)) {
            int index = humans.indexOf(human);
            while (index < humans.size()) {
                humans.remove(++index);
            }
        } else {
            System.out.println("");
        }
    }
}
