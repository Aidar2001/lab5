package basePackage;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface Server {
    void start(int port) throws IOException;
}
