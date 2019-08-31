package basePackage.commander;

import basePackage.parsers.CommandParser;

import java.io.IOException;

public class CommandHandler {
    private CommandParser parser = new CommandParser();
    private Executor executor = new Executor();

    public void handle(String rawCommand) {
        Command command = null;
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
                executor.info();
                break;
            case REMOVE:
                executor.remove(command.getArgument().getHuman());
                break;
            case ADD:
                executor.add(command.getArgument().getHuman());
                break;
            case ADD_IF_MAX:
                executor.addIfMax(command.getArgument().getHuman());
            case REMOVE_FIRST:
                executor.removeFirst();
                break;
            case SHOW:
                executor.show();
                break;
            case REMOVE_GREATER:
                executor.removeGreater(command.getArgument().getHuman());
                break;
            case HELP:
                executor.help();
                break;
            case IMPORT:
                executor.importFile(command.getArgument().getFile());
                break;
            case EXIT:
                executor.exit();
        }
    }
}
