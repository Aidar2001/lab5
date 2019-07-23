package BasePackage.Commander;

import BasePackage.JSONParser;

public class CommandHandler {
    private JSONParser parser = new JSONParser();
    private Executor executor = new Executor();

    public void handle(String rawCommand) {
        Command command = parser.fromJSON(rawCommand);
        switch (command.getNameOfCommand()) {
            case INFO:
                executor.info();
                break;
            case REMOVE:
                executor.remove(command.getArgument().getHuman());
                break;
            case ADD:
                break;
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
        }
    }
}
