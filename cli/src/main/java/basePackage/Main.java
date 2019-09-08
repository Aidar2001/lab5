package basePackage;

import basePackage.commander.CommandHandler;
import basePackage.commander.CommandReader;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * @author Aidar Sinetov
 * This is main class of my project.
 */
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
    }

    /**
     * @return path to file with collection from environmental variable <code>PATH_TO_COLLECTION</code>,
     * or null if environmental variable equal to null.
     */
    private static String getPathFromEnvironmentVariable() {
        String collectionPath = System.getenv("PATH_TO_COLLECTION");
        if (collectionPath == null) {
            System.out.println("Path should be passed via environment variable PATH_TO_COLLECTION");
            System.exit(1);
            return null;
        } else {
            return collectionPath;
        }
    }

}
