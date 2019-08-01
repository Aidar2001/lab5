package basePackage;

import basePackage.сommander.CommandHandler;
import basePackage.сommander.CommandReader;

public class Main {
    public static void main(String[] args) {
        // import + pathFile
        // readCommand -> CommandHandler
        CommandHandler commandHandler = new CommandHandler();
        CommandReader commandReader = new CommandReader();
        String importCommand = "import " + getPathFromEnvironmentVariable();
        commandHandler.handle(importCommand);
        while (true) {
            commandHandler.handle(commandReader.readCommand());
        }

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
