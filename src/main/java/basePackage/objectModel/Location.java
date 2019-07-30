package basePackage.objectModel;

public enum Location {
    SPACESHIP("Космический корабль") ,
    FOREST("Лес") ;
    private String locationName;
    Location(String locationName){
        this.locationName=locationName;
    }
}