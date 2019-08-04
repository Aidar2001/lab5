package basePackage.objectModel;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Human extends Creature implements Comparable<Human> {
    private static int count = 0;
    private static int number = 1;
    @XmlAttribute
    private int id;
    @XmlElement(name = "action", type = Action.class)
    private List<IAction> actions = new ArrayList<>();
    @XmlElement(name = "profession", type = Profession.class)
    private List<IProfession> professions = new ArrayList<>();
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

    @Override
    public int compareTo(Human o) {
        return (name.compareTo(o.getName()) != 0) ? name.compareTo(o.getName()) : Integer.compare(id, o.id);
    }
}


