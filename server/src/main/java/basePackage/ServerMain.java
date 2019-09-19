package basePackage;

import java.io.IOException;

public class ServerMain {

    private static final int PORT = 6123;

    public static void main(String[] args) throws IOException {
        Server server = new ServerImpl(new Executor());

        System.out.println("Staring server at port " + PORT);
        server.start(PORT);
    }
}
