package basePackage.objectModel;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@XmlRootElement
public class Humans {
    @XmlElement(name = "human")
    private List<Human> humans;
}
