package basePackage;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        Server server = new ServerImpl(new Executor());
        server.start(6123);
    }
}
