package BasePackage.Exeptions;

public class NotCorrectNameExeption extends RuntimeException {
    private String name,msg;

    public NotCorrectNameExeption() {
        super();
    }

    public NotCorrectNameExeption(String name, String msg) {
        super(msg);
        this.msg=msg;
        this.name=name;
    }
    public NotCorrectNameExeption(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        if(this.msg!=null){
        return "Исключение: не корректное имя -- \""+name+"\"."+"\nПояснение: "+msg;
    } else {return "Исключение: не корректное имя -- \""+name+"\".";}}
}
