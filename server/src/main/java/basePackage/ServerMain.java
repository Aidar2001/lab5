package basePackage;

import java.io.IOException;

public class ServerMain {

    private static final int PORT = 6124;

    public static void main(String[] args) throws IOException {
        Server server = new ServerImpl();
        System.out.println("Staring server at port " + PORT);
        server.start(PORT);
    }
}
