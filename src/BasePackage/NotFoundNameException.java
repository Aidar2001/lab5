package BasePackage;

public class NotFoundNameException extends Exception {
    private String exc;

    NotFoundNameException() {
        super();
    }

    NotFoundNameException(String s) {
        super(s);
        exc = s;
    }


    public String getExc() {
        return exc;
    }

    @Override
    public String toString() {
        return "Исключение: не найдено имя ";
    }
}
