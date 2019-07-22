package BasePackage.ObjectModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Human extends Creature implements Comparable<Human>  {
    private static int count = 0;
    private static int number = 1;
    private int id;
    private List<IAction> actions = new ArrayList<>();
    private List<IProfession> professions = new ArrayList<>();
    private Location location;

    public Human(String Name, Location location) {
        this(Name);
        setLocation(location);
    }

    public Human() {
        this("Безликий" + number);
        number++;
        this.id = ++count;
    }

    public Human(String Name) {
        super(Name);
        this.id = ++count;
    }

    public void setLocation(Location location) {
        this.location = location;

    }

    public String getlocation() {
        String locationInRU = "";
        if (location != null) {
            switch (location) {
                case FOREST:
                    locationInRU = "в лесу";
                    break;
                case SPACESHIP:
                    locationInRU = "в ракете";
                    break;
            }
        }
        return locationInRU;
    }

    public boolean addAction(IAction action) {
        if (actions.add(action)) {
            System.out.println("Человеку - \"" + this.name + "\" успешно присвоено действие " + action.getActionName());
            return true;
        } else {
            System.out.println("При добавлении действия произошла ошибка...");
            return false;
        }
    }

    public boolean addProfession(IProfession profession) {
        if (professions.add(profession) & profession.getProfession() != null) {
            System.out.println("Человеку - " + this.name + " успешно присвоена профессия " + profession.getProfession());
            return true;
        } else {
            if (profession.getProfession() == null) {
                System.out.println("Человеку - " + this.name + " не может быть присвоена неинициализированная профессия");
                return false;
            } else {
                System.out.println("При добавлении профессии произошла ошибка...");
                return false;
            }
        }
    }

    public void setActions(List<IAction> actions) {
        this.actions = actions;
    }

    public List<IAction> getActions() {
        return actions;
    }

    public int getId() {
        return id;
    }
/*@Override
    public String toString() {
        return professions.stream().map(IProfession::getProfession).collect(Collectors.joining(", ", "", " ")) + "\"" + name + "\""
                + actions.stream().map(IAction::getActionName).collect(Collectors.joining(", ", " ", " ")) + getlocation();
    }*/

    @Override
    public String toString() {
        return "Human{" +
                "id=" + id +
                ", actions=" + actions +
                ", professions=" + professions +
                ", location=" + location +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Human human = (Human) o;
        return name.equals(human.name) && location == human.location && equalsActions(human.actions) && equalsProfessions(human.professions);
    }

    private boolean equalsProfessions(List<IProfession> professions) {
        if (this.professions.size() != professions.size()) {
            return false;
        }
        professions.sort(Comparator.comparing(IProfession::getProfession));
        this.professions.sort(Comparator.comparing(IProfession::getProfession));
        for (int i = 0; i < professions.size(); i++) {
            if (!professions.get(i).equals(this.professions.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean equalsActions(List<IAction> actions) {
        if (this.actions.size() != actions.size()) {
            return false;
        }
        actions.sort(Comparator.comparing(IAction::getActionName));
        this.actions.sort(Comparator.comparing(IAction::getActionName));
        for (int i = 0; i < actions.size(); i++) {
            if (!actions.get(i).equals(this.actions.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, actions, professions, location);
    }

    @Override
    public int compareTo(Human o) {
        return (name.compareTo(o.getName()) != 0)? name.compareTo(o.getName()): Integer.compare(id, o.id);
    }

    class Hand {
        private double lenght;

        Hand(double lenght) {
            this.lenght = lenght;
        }

        public double getLenght() {
            return lenght;
        }

        public void setLenght(double lenght) {
            this.lenght = lenght;
        }
    }

}


