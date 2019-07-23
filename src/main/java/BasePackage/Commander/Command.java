package BasePackage.Commander;

import BasePackage.ObjectModel.Human;
import lombok.*;

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
        REMOVE_FIRST,
        SHOW,
        REMOVE_GREATER,
        HELP,
        IMPORT;
    }

    @Getter
    @Setter
    public class Argument {
        private File file;
        private Human human;
    }
}
