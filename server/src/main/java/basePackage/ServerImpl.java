package basePackage;

import basePackage.connect.Request;
import basePackage.connect.RequestResult;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ServerImpl implements Server {
    private Executor executor;
    private List<Thread> threads = new ArrayList<>();
    private ServerSocket server;

    public ServerImpl(Executor executor) {
        this.executor = executor;
    }

    public void start(int port) throws IOException {
        if (server != null || server.isBound()) {
            throw new IllegalStateException();
        }
        server = new ServerSocket(port);
        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = server.accept();
                    Thread thread = new Thread(new SocketRunnable(executor, socket));
                    thread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }

        });
    }

    @Override
    public CompletableFuture<Void> stop() {
        return null;
    }

    private class SocketRunnable implements Runnable {
        private Executor executor;
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public SocketRunnable(Executor executor, Socket socket) {
            this.executor = executor;
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                init();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            threads.add(Thread.currentThread());
            while (true) {
                if (!socket.isConnected()) {
                    break;
                }
                try {
                    Request request = (Request) in.readObject();
                    NameOfCommand command = NameOfCommand.valueOf(request.getSignature());
                    switch (command){
                        case INFO:
                            break;
                        case REMOVE:
                            break;
                        case ADD:
                            break;
                        case ADD_IF_MAX:
                            break;
                        case REMOVE_FIRST:
                            break;
                        case SHOW:
                            break;
                        case REMOVE_GREATER:
                            break;
                        case HELP:
                            break;
                        case IMPORT:
                            request.getData().stream().forEach((human)->{executor.add(human);});
                            out.writeObject(RequestResult.createSuccessResult());
                            out.flush();
                            break;
                        case EXIT:
                            break;
                    }

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    break;
                }
            }
            threads.remove(Thread.currentThread());
        }

        private void init() throws IOException {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(RequestResult.createSuccessResult());
            out.flush();
        }
    }
}
