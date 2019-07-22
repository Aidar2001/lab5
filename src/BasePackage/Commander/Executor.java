package BasePackage.Commander;

import BasePackage.ObjectModel.Human;

import java.io.File;
import java.util.Date;
import java.util.Vector;

public class Executor {
    private Vector<Human> humans;
    private Date dateOfInit;
    private Human human;

    public Executor(Human human) {
        this.human = human;
    }

    public Executor() {
    }

    public void info() {
        System.out.println("Коллекция типа Vector и содержит людей");
        System.out.println("Дата инициализации: " + dateOfInit);
        System.out.println("Размер коллекции на данный момент: " + humans.size());
    }

    public void remove(Human humanForRemove) {
        if (humans.remove(humanForRemove)) System.out.println("Элемент удалён");
        else System.out.println("Коллекция не содержит данный элемент");
    }

    public void add(Human human) {
        humans.add(human);
        System.out.println("Элемент добавлен");
    }

    public void removeFirst() {
        humans.remove(0);
        System.out.println("Превый элемент коллекции удален");
    }

    public void show() {
        for (Human human : humans) {
            System.out.println(humans.stream().map(Human::toString));
        }
    }

    public void removeGreater(Human human) {
        if (humans.contains(human)) {
            int index = humans.indexOf(human);
            int i = index;
            while (i < humans.size()) {
                humans.remove(++i);
            }
        }
    }
}
