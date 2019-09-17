package basePackage;

import basePackage.connect.Request;
import basePackage.connect.RequestResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
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
        if (server != null && server.isBound()) {
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

        }).start();
    }

    @Override
    public CompletableFuture<Void> stop() {
        return null;
    }

    private class SocketRunnable implements Runnable {
        private Executor executor;
        private Socket socket;

        private ObjectMapper mapper;

        private CharsetEncoder encoder;
        private CharsetDecoder decoder;

        {
            mapper = new ObjectMapper().registerModules();

            encoder = StandardCharsets.UTF_8.newEncoder();
            decoder = StandardCharsets.UTF_8.newDecoder();
        }

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
                    Request request = readClient();
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
                            request.getData().stream()
                                   .peek(System.out::println) // TODO: Just for debugging. Remove later
                                   .forEach((human)->{executor.add(human);});
                            sendResponse(RequestResult.createSuccessResult());
                            break;
                        case EXIT:
                            break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
            threads.remove(Thread.currentThread());
        }

        private void init() throws IOException {
            sendResponse(RequestResult.createSuccessResult());
        }

        private <T> void sendResponse(RequestResult<T> result) throws IOException {
            String json = mapper.writeValueAsString(result);
            ByteBuffer charset = encoder.encode(CharBuffer.wrap(json));
            ByteBuffer amount = ByteBuffer.allocate(4).putInt(charset.capacity());

            socket.getOutputStream().write(amount.array());
            socket.getOutputStream().write(charset.array());
        }

        private Request readClient() throws IOException {
            ByteBuffer amount = ByteBuffer.allocate(4);

            int totalRead = 0;
            while(totalRead != Integer.BYTES) {
                int read = socket.getInputStream().read(amount.array(), totalRead, (Integer.BYTES - totalRead));
                totalRead += read;
            }

            amount.rewind();

            int jsonLength = amount.getInt();

            ByteBuffer jsonBuffer = ByteBuffer.allocate(jsonLength);
            socket.getInputStream().read(jsonBuffer.array());

            String json = decoder.decode(jsonBuffer).toString();

            return mapper.readValue(json, Request.class);
        }
    }
}
