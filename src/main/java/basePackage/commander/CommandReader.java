package basePackage.commander;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class to read command from console. It use for this <code>Scanner</code>.
 */

public class CommandReader {
    /**
     * @return string which was entered to console
     */
    public String readCommand() {
        Scanner consoleScanner = new Scanner(System.in);
        System.out.println("Enter command");
        while (true) {
            try {
                return consoleScanner.nextLine().trim();
            } catch (NoSuchElementException e) {
                System.out.println("Entered invalid command");
            }
        }
    }
}
