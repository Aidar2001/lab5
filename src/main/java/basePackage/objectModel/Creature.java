package basePackage.objectModel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Creature{
    @XmlAttribute
    protected String name;

    public Creature(String name) {
        setName(name);
    }

}
