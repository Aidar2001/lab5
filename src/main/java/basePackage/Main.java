package basePackage;

import basePackage.commander.CommandHandler;
import basePackage.commander.CommandReader;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws JAXBException, IOException {
        // import + pathFile
        // readCommand -> CommandHandler
        CommandHandler commandHandler = new CommandHandler();
        CommandReader commandReader = new CommandReader();
        String importCommand = "import " + getPathFromEnvironmentVariable();
        commandHandler.handle(importCommand);
        while (true) {
            commandHandler.handle(commandReader.readCommand());
        }
/*        Human namelessPoliceman = new Human("Алексей");
        Human namelessSpaceman = new Human("Аркадий");
        namelessPoliceman.setLocation(Location.FOREST);
        namelessSpaceman.setLocation(Location.SPACESHIP);
        IProfession policeman = new Profession("Полицейский");
        IProfession spaceman = new Profession("Космонавт");
        IAction attack = new Action("нападает");
        IAction gump = new Action("прыгает");
        IAction shoot = new Action("стреляет");
        IAction defend = new Action("укрывается");
        namelessPoliceman.addProfession(policeman);
        namelessSpaceman.addProfession(spaceman);
        namelessPoliceman.addAction(attack);
        namelessSpaceman.addAction(defend);
        namelessSpaceman.addAction(gump);
        namelessPoliceman.addAction(shoot);
        System.out.println(namelessPoliceman);
        System.out.println(namelessSpaceman);

        Humans humans = new Humans();
        Vector<Human> vector = new Vector<>();
        vector.add(namelessPoliceman);
        vector.add(namelessSpaceman);
        humans.setHumans(vector);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("humans.json"),humans);*/
//        XMLParser parser = new XMLParser();
//        parser.toXML(humans, new File("C:\\Users\\Aidar\\OneDrive\\Документы\\Учеба\\Айдар\\ИТМО\\Программирование\\Лабы_программирование\\5\\Lab_5_код\\data.xml"));

    }

    private static String getPathFromEnvironmentVariable() {
        String collectionPath = System.getenv("HUMAN_PATH");
        if (collectionPath == null) {
            System.out.println("Путь должен передаваться через переменную окружения HUMAN_PATH");
            System.exit(1);
            return null;
        } else {
            return collectionPath;
        }
    }

}
