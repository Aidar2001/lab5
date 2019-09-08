package basePackage.exeptions;

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
        return "Exception: not correct name -- \""+name+"\"."+"\nExplanation: "+msg;
    } else {return "Exception: not correct name -- \""+name+"\"";}}
}
