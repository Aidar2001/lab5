package BasePackage.Commander;

import BasePackage.ObjectModel.Human;

public class Command{
    enum NameOfCommand{
        INFO,
        REMOVE,
        ADD,
        REMOVE_FIRST,
        SHOW,
        REMOVE_GREATER,
        HELP,
        IMPORT;

        NameOfCommand(Human human) {

        }

        NameOfCommand() {

        }
    }
}
