package basePackage.objectModel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * General class of my object model. Extends extends <code>Creature</code> and implements <code>Comparable<Human></code>
 *
 * @see Creature
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@Getter @Setter
public class Human extends Creature implements Comparable<Human> {
    private static int count = 0;
    private static int number = 1;

    /**
     * Date of create human
     */
    private ZonedDateTime creationTime = ZonedDateTime.now();
    // LocalDate
    // LocalTime
    // с часовым поясом
    // ZonedDateTime
    // ZonedDate
    // ZonedTime

    @XmlAttribute
    private int id;

    /**
     * List of IActions
     *
     * @see IAction
     * @see Action
     */
    @XmlElement(name = "action", type = Action.class)
    private List<IAction> actions = new ArrayList<>();

    /**
     * List of IProfessions
     *
     * @see IProfession
     * @see Profession
     */
    @XmlElement(name = "profession", type = Profession.class)
    private List<IProfession> professions = new ArrayList<>();

    @XmlElement
    private Location location;

    public Human(int id, String name, Location location, List<IAction> actions, List<IProfession> professions, ZonedDateTime creationDate) {
        super(name);
        this.creationTime = creationDate;
        this.id = id;
        this.actions = actions;
        this.professions = professions;
        this.location = location;
    }

    public Human(String name, Location location) {
        this(name);
        setLocation(location);
    }

    public Human() {
        this("Nameless human" + number);
        number++;
        this.id = ++count;
    }

    /**
     * @param name name of human
     */
    public Human(String name) {
        super(name);
        this.id = ++count;
    }

    /**
     * Add new profession to list professions
     *
     * @param profession object of class implements {@link IProfession}
     * @return true if element added, false if element didn't add
     */
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

    /**
     * Add new action to list actions
     *
     * @param action object of class implements {@link IAction}
     * @return true if element added, false if element didn't add
     */
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
        return name.compareTo(o.getName());
    }
}


