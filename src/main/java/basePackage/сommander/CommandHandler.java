package basePackage.сommander;

import basePackage.parsers.CommandParser;

public class CommandHandler {
    private CommandParser parser = new CommandParser();
    private Executor executor = new Executor();

    public void handle(String rawCommand) {
        Command command = parser.parseCommand(rawCommand);
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
