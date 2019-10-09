package basePackage;

import basePackage.commander.CommandHandler;
import basePackage.commander.CommandReader;
import basePackage.connect.RequestResult;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * @author Aidar Sinetov
 * This is main class of my project.
 */
public class Main {
    public static void main(String[] args) throws JAXBException, IOException {
        Client client = new ClientImpl();
        try {
            RequestResult<Void> result = client.connection("localhost", 6124);
            if (!result.getSuccess()) {
                System.err.println("Connection error");
                System.exit(-1);
            }
        } catch (IllegalStateException e) {
            System.err.println("Failed to establish connection with server. "+e.getMessage());
            System.exit(-1);
        }

        CommandHandler commandHandler = new CommandHandler(client);
        CommandReader commandReader = new CommandReader();
        while (true) {
            commandHandler.handle(commandReader.readCommand());
        }
    }

}
