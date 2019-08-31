package basePackage.commander;

import basePackage.objectModel.Human;
import lombok.Data;

import java.io.File;

@Data
public class Command {
    private NameOfCommand nameOfCommand;
    private Argument argument;

    public enum NameOfCommand {
        INFO,
        REMOVE,
        ADD,
        ADD_IF_MAX,
        REMOVE_FIRST,
        SHOW,
        REMOVE_GREATER,
        HELP,
        IMPORT,
        EXIT
    }

    @Data
    public class Argument {
        private File file;
        private Human human;
    }
}
