package basePackage.commander;

import basePackage.objectModel.Human;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;

@Getter
@Setter
@ToString
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

    @Getter
    @Setter
    public class Argument {
        private File file;
        private Human human;
    }
}
