package basePackage;

import basePackage.connect.CollectionInfo;
import basePackage.connect.Request;
import basePackage.connect.RequestError;
import basePackage.connect.RequestResult;
import basePackage.objectModel.Human;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.bind.JAXBException;
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
    private List<Thread> threads = new ArrayList<>();
    private ServerSocket server;

    public void start(int port) throws IOException {
        if (server != null && server.isBound()) {
            throw new IllegalStateException();
        }
        server = new ServerSocket(port);

        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = server.accept();
                    Thread thread = new Thread(new SocketRunnable(new Executor(), socket));
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
                    NameOfCommand command;
                    List<Human> humans = request.getData();
                    String[] args;

                    String signatureStr = request.getSignature();
                    int index = signatureStr.indexOf(' ');
                    if (index != -1) {
                        command = NameOfCommand.valueOf(signatureStr.substring(0, index));
                        args = signatureStr.substring(index + 1).split(" ");
                    } else {
                        command = NameOfCommand.valueOf(signatureStr);
                        args = new String[0];
                    }

                    boolean status;
                    switch (command){
                        case INFO:
                            CollectionInfo info = executor.info();
                            sendResponse(RequestResult.createSuccessResultWith(info));
                            break;

                        case REMOVE:
                            if (args.length == 0) {
                                sendResponse(RequestResult.createFailResult(RequestError.WRONG_SIGNATURE));
                                continue;
                            }
                            try {
                                int id = Integer.parseInt(args[0]);
                                boolean removed = executor.remove(id);
                                sendResponse(RequestResult.createSuccessResultWith(removed));
                            } catch (NumberFormatException ex) {
                                sendResponse(RequestResult.createFailResult(RequestError.WRONG_SIGNATURE));
                                continue;
                            }
                            break;

                        case ADD:
                            sendResponse(RequestResult.createSuccessResultWith(executor.add(humans.get(0))));
                            break;

                        case ADD_IF_MAX:
                            if (humans == null || humans.size() != 1) {
                                sendResponse(RequestResult.createFailResult(RequestError.WRONG_DATA));
                                break;
                            }
                            sendResponse(RequestResult.createSuccessResultWith(executor.addIfMax(humans.get(0))));
                            break;

                        case REMOVE_FIRST:
                            sendResponse(RequestResult.createSuccessResultWith(executor.removeFirst()));
                            break;

                        case SHOW:
                            sendResponse(RequestResult.createSuccessResultWith(executor.show()));
                            break;

                        case REMOVE_GREATER:
                            if (humans == null || humans.size() != 1) {
                                sendResponse(RequestResult.createFailResult(RequestError.WRONG_DATA));
                                break;
                            }
                            sendResponse(RequestResult.createSuccessResultWith(executor.removeGreater(humans.get(0))));
                            break;

                        case IMPORT:
                            request.getData().forEach((human) -> executor.add(human));
                            sendResponse(RequestResult.createSuccessResult());
                            break;

                        case LOAD:
                            try {
                                if (args.length != 1) {
                                    sendResponse(RequestResult.createFailResult(RequestError.WRONG_SIGNATURE));
                                    break;
                                } else if (args.length == 1) {
                                    status = executor.loadCollection(args[0]);
                                } else {
                                    status = executor.loadCollection(null);
                                }
                                sendResponse(RequestResult.createSuccessResultWith(status));
                            } catch (JAXBException e) {
                                sendResponse(RequestResult.createFailResult(RequestError.FILE_PARSE_ERROR));
                            }

                            break;

                        case EXIT:
                            sendResponse(new RequestResult<Void>().withSuccess(executor.saveCollection(null)));
                            socket.close();
                            break;

                        case SAVE:
                            if (args.length > 1) {
                                sendResponse(RequestResult.createFailResult(RequestError.WRONG_SIGNATURE));
                                break;
                            } else if (args.length == 1) {
                                status = executor.saveCollection(args[0]);
                            } else {
                                status = executor.saveCollection(null);
                            }

                            sendResponse(new RequestResult<Void>().withSuccess(status));
                            break;
                    }

                } catch (IOException e) {
                    try {
                        sendResponse(RequestResult.createFailResult(e, RequestError.IO_ERROR));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            threads.remove(Thread.currentThread());
        }

        private void init() throws IOException {
            sendResponse(RequestResult.createSuccessResult());
        }

        private <T> void sendResponse(RequestResult<T> result) throws IOException {
            // convert request result into json string
            String json = mapper.writeValueAsString(result);

            // get byte buffer from json string
            ByteBuffer charset = encoder.encode(CharBuffer.wrap(json));

            // byte buffer holding the size of charset
            ByteBuffer amount = ByteBuffer.allocate(Integer.BYTES).putInt(charset.capacity());

            // send size of charset
            socket.getOutputStream().write(amount.array());
            // and then send charset
            socket.getOutputStream().write(charset.array());
        }

        private Request readClient() throws IOException {
            // byte buffer storing the size of input charset
            ByteBuffer amount = ByteBuffer.allocate(Integer.BYTES);

            // read size of input charset into byte buffer
            int totalRead = 0;
            // loop until read 4 bytes (size of input charset)
            while(totalRead != amount.capacity()) {
                int read = socket.getInputStream().read(amount.array(), totalRead, (amount.capacity() - totalRead));
                totalRead += read;
            }
            int jsonLength = amount.getInt();

            ByteBuffer jsonBuffer = ByteBuffer.allocate(jsonLength);
            totalRead = 0;
            // loop until read all bytes of charset
            while (totalRead != jsonBuffer.capacity()) {
                int read = socket.getInputStream().read(jsonBuffer.array(), totalRead, (jsonBuffer.capacity() - totalRead));
                totalRead += read;
            }

            // request json representation
            String json = decoder.decode(jsonBuffer).toString();

            // mapping json to request object
            return mapper.readValue(json, Request.class);
        }
    }
}
