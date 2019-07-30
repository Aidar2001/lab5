package basePackage.objectModel;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum
public enum Location {
    SPACESHIP("Космический корабль"),
    FOREST("Лес");
    private String locationName;

    Location(String locationName) {
        this.locationName = locationName;
    }
}