package basePackage.exeptions;

public class NotFoundNameException extends Exception {
    private String exc;

    public NotFoundNameException() {
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
        return "Exception: not found name ";
    }
}
