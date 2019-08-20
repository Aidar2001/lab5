package basePackage.objectModel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
public class Human extends Creature implements Comparable<Human> {
    private static int count = 0;
    private static int number = 1;
    @XmlAttribute
    @Getter
    private int id;
    @Getter
    @Setter
    @XmlElement(name = "action", type = Action.class)
    private List<IAction> actions = new ArrayList<>();
    @Getter
    @Setter
    @XmlElement(name = "profession", type = Profession.class)
    private List<IProfession> professions = new ArrayList<>();
    @Setter
    @XmlElement
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

    public boolean addAction(IAction action) {
        if (actions.add(action)) {
            System.out.println("Человеку - \"" + this.name + "\" успешно присвоено действие " + action.getActionName());
            return true;
        } else {
            System.out.println("При добавлении действия произошла ошибка...");
            return false;
        }
    }

    @Override
    public int compareTo(Human o) {
        return (name.compareTo(o.getName()) != 0) ? name.compareTo(o.getName()) : Integer.compare(id, o.id);
    }
}


