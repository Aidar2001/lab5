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
        this("Nameless human" + number);
        number++;
        this.id = ++count;
    }

    public Human(String Name) {
        super(Name);
        this.id = ++count;
    }

    public boolean addProfession(IProfession profession) {
        if (professions.add(profession) & profession.getProfession() != null) {
            System.out.println("Human " + this.name + " successfully was assigned profession " + profession.getProfession());
            return true;
        } else {
            if (profession.getProfession() == null) {
                System.out.println("Human " + this.name + " can not be assigned uninitialized profession");
                return false;
            } else {
                System.out.println("Error happened while adding a profession...");
                return false;
            }
        }
    }

    public boolean addAction(IAction action) {
        if (actions.add(action)) {
            System.out.println("Human \"" + this.name + "\" is successfully assigned action " + action.getActionName());
            return true;
        } else {
            System.out.println("Error happened while adding a action...");
            return false;
        }
    }

    @Override
    public int compareTo(Human o) {
        return (name.compareTo(o.getName()) != 0) ? name.compareTo(o.getName()) : Integer.compare(id, o.id);
    }
}


