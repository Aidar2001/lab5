package basePackage.commander;

import basePackage.Client;
import basePackage.connect.CollectionInfo;
import basePackage.connect.RequestError;
import basePackage.connect.RequestResult;
import basePackage.objectModel.Human;

import java.io.IOException;
import java.util.List;


/**
 * This class acts as an intermediary between the CommandParser class and the Executor. He have one method to do that function.
 *
 * @author Aidar Sinetov
 * @see CommandParser
 */
public class CommandHandler {
    private CommandParser parser = new CommandParser();
    private Client client;

    public CommandHandler(Client client) {
        this.client = client;
        Runtime.getRuntime().addShutdownHook(new Thread(client::save));
    }

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
            System.err.println("Error with processing command argument.");
            return;
        } catch (IllegalArgumentException e) {
            System.err.println("Unknown command");
            return;
        }

        switch (command.getNameOfCommand()) {
            case INFO:
                RequestResult<CollectionInfo> infoResult = client.getInfo();
                if (infoResult.getSuccess()) {
                    CollectionInfo collectionInfo = infoResult.getCollectionInfoResult();
                    System.out.println(
                            "\tCollection name: " + collectionInfo.getCollectionName() + "\n" +
                                    "\tCollection type: " + collectionInfo.getCollectionType() + "\n" +
                                    "\tInitialization date: " + collectionInfo.getInitializationDate() + "\n" +
                                    "\tCollection size at the moment: " + collectionInfo.getCollectionSize());
                } else {
                    System.err.println(getErrorMessage(infoResult.getError()));
                }
                ;
                break;

            case REMOVE:
                RequestResult<Boolean> removeResult = client.remove(command.getArgument().getHumanId());
                if (removeResult.getSuccess()) {
                    if (removeResult.getResult()) {
                        System.out.println("Element was removed");
                    } else {
                        System.out.println("Collection doesn't have person wit id=" + command.getArgument().getHumanId());
                    }
                } else {
                    System.err.println(getErrorMessage(removeResult.getError()));
                }
                break;

            case ADD:
                RequestResult<Boolean> addResult = client.addHuman(command.getArgument().getHuman());
                if (addResult.getSuccess()) {
                    if (addResult.getResult()) {
                        System.out.println("Element was added");
                    } else {
                        System.out.println("Collection already has human with id=" + command.getArgument().getHuman().getId());
                    }
                } else {
                    System.err.println(getErrorMessage(addResult.getError()));
                }
                break;

            case ADD_IF_MAX:
                Human human = command.getArgument().getHuman();
                RequestResult<Boolean> addIfMaxResult = client.addIfMax(human);
                if (addIfMaxResult.getSuccess()) {
                    if (addIfMaxResult.getResult()) {
                        System.out.println("Element was added");
                    } else {
                        System.out.println("Collection already has human with id=" + human.getId() + " or" +
                                " human " + human.getName() + "#" + human.getId() + " isn't max.");
                    }
                } else {
                    System.err.println(getErrorMessage(addIfMaxResult.getError()));
                }
                break;

            case REMOVE_FIRST:
                RequestResult<Boolean> removeFirstResult = client.removeFirst();
                if (removeFirstResult.getSuccess()) {
                    if (removeFirstResult.getResult()) {
                        System.out.println("First element was removed");
                    } else {
                        System.out.println("Collection doesn't have any persons");
                    }
                } else {
                    System.err.println(getErrorMessage(removeFirstResult.getError()));
                }
                break;

            case SHOW:
                RequestResult<List<Human>> showResult = client.getAllHumans();
                if (showResult.getSuccess()) {
                    showResult.getHumansListResult().forEach(System.out::println);
                } else {
                    System.err.println(getErrorMessage(showResult.getError()));
                }
                break;

            case REMOVE_GREATER:
                RequestResult<Boolean> removeGreaterResult = client.removeGreater(command.getArgument().getHuman());
                if (removeGreaterResult.getSuccess()) {
                    if (removeGreaterResult.getResult()) {
                        System.out.println("Humans greater then " + command.getArgument().getHuman().getName() + "#" + command.getArgument().getHuman().getId()
                                + " was successfully removed.");
                    }
                } else {
                    System.err.println(getErrorMessage(removeGreaterResult.getError()));
                }
                break;

            case HELP:
                printHelp();
                break;

            case IMPORT:
                try {
                    RequestResult<Boolean> importResult = client.importFile(command.getArgument().getFile());
                    if (importResult.getSuccess()) {
                        if(importResult.getResult()) {
                            System.out.println("Humans from file was successfully imported to collection");
                        } else {
                            System.out.println("Humans were added. But not all");
                        }
                    } else {
                        System.err.println(getErrorMessage(importResult.getError()));
                    }
                    break;
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                    return;
                }

            case LOAD:
                RequestResult<Boolean> loadResult = client.load();
                if (loadResult.getSuccess()) {
                    if (loadResult.getResult()) {
                        System.out.println("Collection was successfully loaded. You work with default collection.");
                    } else {
//                        System.out.println("Collection with name " + collectionToLoad + " isn't on server.");
                    }
                } else {
                    System.err.println("Error: collection wasn't loaded. " + getErrorMessage(loadResult.getError()));
                }
                break;

            case SAVE:
                RequestResult<Void> saveResult = client.save();
                if (saveResult.getSuccess()) {
                    System.out.println("Collection was successfully saved.");
                } else {
                    System.err.println("Error: collection wasn't saved. " + getErrorMessage(saveResult.getError()));
                }

                break;

            case EXIT:
                RequestResult<Void> closeConnectionResult = client.closeConnection();
                if (closeConnectionResult.getSuccess()) {
                    System.out.println("Changes was successfully saved.");
                    System.exit(0);
                } else {
                    System.out.println("Oops! Changes wasn't saved.");
                    System.exit(1);
                }
                break;
        }

    }

    private static void printHelp() {
        System.out.println(
                "\tremove [humans id]: remove an element from the collection by its id\n" +
                        "\tinfo: show information about collection (type, initialization date , number of items  etc.)\n" +
                        "\tadd {element}: add new item to collection on server\n" +
                        "\tadd_if_max {element}: add a new element to the collection, if its value exceeds the the value of largest element of this collection\n" +
                        "\tremove_greater {element}: remove from the collection, all elements that exceed specified\n" +
                        "\tshow: вывести show all elements of the collection in the string view\n" +
                        "\tremove_first: remove first element of collection\n" +
                        "\timport [XML file]: add humans from XML to collection on server \n " +
                        "\tload [file with collection]: load file with name \"file with collection\" to work with it. Without argument load default file\n" +
                        "\tsave [file name]: save collection to file with name \"file name\". Without argument save to file which you work now." +
                        "\texit: end the session");
    }

    private static String getErrorMessage(RequestError error) {
        String message = null;

        switch (error) {
            case SOCKET_CONNECTION_ERROR:
                message = "Connection Error. Please retry attempt.";
                break;
            case FILE_PARSE_ERROR:
                message = "Error with parsing XML file with humans.";
                break;
            case IO_ERROR:
                message = "Error communicating with server";
                break;
            case JSON_PARSE_ERROR:
                message = "Error during send request.";
                break;
            case WRONG_SIGNATURE:
                message = "Wrong command signature.";
                break;
            case WRONG_DATA:
                message = "Wrong date in command argument.";
                break;
        }
        return message;
    }
}
