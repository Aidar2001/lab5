package basePackage.commander;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class CommandReader {
    public String readCommand() {
        Scanner consoleScanner = new Scanner(System.in);
        System.out.println("Введите команду");
        while (true) {
            try {
                String stringFromTerminal = consoleScanner.nextLine().trim();
                return stringFromTerminal;
            } catch (NoSuchElementException e) {
                System.out.println("Введена невалидная строка");
            }
        }
    }
}
