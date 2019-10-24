package basePackage.objectModel;

import lombok.AllArgsConstructor;

import javax.xml.bind.annotation.XmlEnum;

@AllArgsConstructor
@XmlEnum
public enum Location {
    SPACESHIP("Spaceship"),
    FOREST("Forest");

    private String locationName;
}