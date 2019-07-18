package BasePackage;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class CommandReaderAndExecutor {
    HumanManager collectionManager;

    public CommandReaderAndExecutor(HumanManager humanManager) {
    }

    public void govern() {
        String[] fullCommand = readCommand();
        //fullCommand[0] is command. fullCommand[1] is argument
        Human humanforAction = null;
        switch (fullCommand[0]) {
            case "info":
                collectionManager.info();
                break;
            case "add":
                collectionManager.add(humanforAction);
                break;
            case "remove":
                collectionManager.remove(humanforAction);
                break;
            case "add_if_max":
                // что должно быть
                break;
            case "remove_greater":
                collectionManager.removeGreater(humanforAction);
                break;
            case "show":
                collectionManager.show();
                break;
            case "remove_first":
                collectionManager.removeFirst();
                break;
            default:
                System.out.println("Ошибка, Неизвестная команда");
        }
    }

    private String[] readCommand() {
        Scanner consoleScanner = new Scanner(System.in);
        try {
            String command;
            System.out.println("Введите команду");
            command = consoleScanner.nextLine();
            command = command.trim();
            String[] fullCommand = command.split(" ", 2);
            return fullCommand;
        } catch (NoSuchElementException ex) {
            consoleScanner.close();
        }
        return null;
    }


}

