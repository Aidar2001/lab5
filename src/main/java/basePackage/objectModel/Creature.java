package basePackage.objectModel;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Data
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Creature{
    @XmlAttribute
    protected String name;
}
