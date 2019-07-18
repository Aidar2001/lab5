package BasePackage;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        Human namelessPoliceman = new Human();
        Human namelessSpaceman = new Human("");
        namelessPoliceman.setLocation(Location.FOREST);
        namelessSpaceman.setLocation(Location.SPACESHIP);
        IProfession policeman = new Profession(null);
        IProfession spaceman = new Profession("Космонавт");
        IAction attack = new Action("нападает");
        IAction defend = new Action("укрывается");
        namelessPoliceman.addProfession(policeman);
        namelessSpaceman.addProfession(spaceman);
        namelessPoliceman.addAction(attack);
        namelessSpaceman.addAction(defend);
        System.out.println(namelessPoliceman);
        System.out.println(namelessSpaceman);
        CommandReaderAndExecutor readerAndExecutor = new CommandReaderAndExecutor(new HumanManager(getFileFromEnvironmentVariable()));
        HumanManager collectionWithHumans = new HumanManager(getFileFromEnvironmentVariable());
        readerAndExecutor.govern();
    }


    private static File getFileFromEnvironmentVariable() {
        String collectionPath = System.getenv("HUMAN_PATH");
        if (collectionPath == null) {
            System.out.println("Путь должен передаваться через переменную окружения HUMAN_PATH");
            return null;
        } else {
            return new File(collectionPath);// обработать исключения - абсолютный/относительный путь, dev/null, dev/zero, dev/random
        }
    }
}
