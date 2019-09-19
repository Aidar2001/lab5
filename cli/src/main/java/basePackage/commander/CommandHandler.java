package basePackage.commander;

import java.io.IOException;


/**
 * This class acts as an intermediary between the CommandParser class and the Executor. He have one method to do that function.
 *
 * @author Aidar Sinetov
 * @see CommandParser
 */
public class CommandHandler {
    private CommandParser parser = new CommandParser();

    /**
     * This method depends on the value <code>rawCommand</code> understands,
     * what my program needs to do or says, that the command is not correctly.
     *
     * @param rawCommand - string, which entered in console
     */
    public void handle(String rawCommand) {
        Command command;
        try {
            command = parser.parseCommand(rawCommand);
        } catch (IOException e) {
            System.out.println("Error with processing command argument");
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown command");
            return;
        }

        switch (command.getNameOfCommand()) {
            case INFO:

                break;
            case REMOVE:

                break;
            case ADD:

                break;
            case ADD_IF_MAX:

            case REMOVE_FIRST:

                break;
            case SHOW:

                break;
            case REMOVE_GREATER:

                break;
            case HELP:

                break;
            case IMPORT:

                break;
            case EXIT:

                break;
        }
    }
}
