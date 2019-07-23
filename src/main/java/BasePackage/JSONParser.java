package BasePackage;

import BasePackage.Commander.Command;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONParser {
    public Command fromJSON(String rawCommand) {
//        System.exit(0);
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            System.out.println("Не выключай меня!!!");
//        }));

        return new Command();
    }

}
