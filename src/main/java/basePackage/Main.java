package basePackage;

import basePackage.objectModel.*;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        // import + pathFile
        // readCommand -> CommandHandler
    }


    private static File getFileFromEnvironmentVariable() {
        String collectionPath = System.getenv("HUMAN_PATH");
        if (collectionPath == null) {
            System.out.println("Путь должен передаваться через переменную окружения HUMAN_PATH");
            return null;
        } else {
            return new File(collectionPath);// обработать исключения - абсолютный/относительный путь, dev/null, dev/zero, dev/random
        }
    }
}
