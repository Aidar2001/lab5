package basePackage;

import basePackage.connect.CollectionInfo;
import basePackage.connect.Request;
import basePackage.connect.RequestError;
import basePackage.connect.RequestResult;
import basePackage.database.model.User;
import basePackage.database.repository.AssociationRepository;
import basePackage.database.repository.HumanRepository;
import basePackage.database.repository.UserRepository;
import basePackage.objectModel.Human;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static basePackage.PasswordUtils.generatePassword;

public class ServerImpl implements Server {
    private Map<String, Executor> executors = new ConcurrentHashMap<>();
    private List<Thread> threads = new ArrayList<>();

    private ServerSocket server;

    private UserRepository users;
    private HumanRepository humans;
    private AssociationRepository associations;
    private EmailSender emailSender;

    private ObjectMapper mapper;
    private CharsetEncoder encoder;
    private CharsetDecoder decoder;

    {
        mapper = new ObjectMapper().findAndRegisterModules();
        encoder = StandardCharsets.UTF_8.newEncoder();
        decoder = StandardCharsets.UTF_8.newDecoder();
    }

    public ServerImpl(EmailSender emailSender,
                      UserRepository users, HumanRepository humans, AssociationRepository associations) {
        this.users = Objects.requireNonNull(users);
        this.humans = Objects.requireNonNull(humans);
        this.associations = Objects.requireNonNull(associations);
        this.emailSender = Objects.requireNonNull(emailSender);

        try {
            users.createTable();
            humans.createTable();
            associations.createTable();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void start(int port) throws IOException {
        if (server != null && server.isBound()) {
            throw new IllegalStateException();
        }
        server = new ServerSocket(port);
        new Thread(this::serverRoutine).start();
    }

    private void serverRoutine() {
        while (true) {
            try {
                Socket socket = server.accept();
                new Thread(() -> handleConnection(socket)).start();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void handleConnection(Socket socket) {
        try {
            sendResponse(socket, RequestResult.createSuccessResult());

            Request request = readClient(socket);
            String username = request.getUsername();
            Executor executor;
            if (!executors.containsKey(username)) {
                executor = new DatabaseExecutor(username, humans, associations);
                executors.put(username, executor);
            } else {
                executor = executors.get(username);
            }

            sendResponse(socket, RequestResult.createSuccessResult());

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Saving collection...");
                try {
                    executor.saveCollection();
                } catch (Exception ignored) {

                }
            }
            ));

            SocketRunnable runnable = new SocketRunnable(executor, socket);
            runnable.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <T> void sendResponse(Socket socket, RequestResult<T> result) throws IOException {
        // convert request result into json string
        String json = mapper.writeValueAsString(result);

        // get byte buffer from json string
        ByteBuffer charset = encoder.encode(CharBuffer.wrap(json));

        // byte buffer holding the size of charset
        ByteBuffer amount = ByteBuffer.allocate(Integer.BYTES).putInt(charset.capacity());
        try {
            // send size of charset
            socket.getOutputStream().write(amount.array());
            // and then send charset
            socket.getOutputStream().write(charset.array());
        } catch (SocketException e) {
            threads.remove(Thread.currentThread());
        }

    }

    private Request readClient(Socket socket) throws IOException {
        // byte buffer storing the size of input charset
        ByteBuffer amount = ByteBuffer.allocate(Integer.BYTES);

        // read size of input charset into byte buffer
        int totalRead = 0;
        // loop until read 4 bytes (size of input charset)
        while (totalRead != amount.capacity()) {
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

    private class SocketRunnable implements Runnable {
        private Executor executor;
        private Socket socket;

        public SocketRunnable(Executor executor, Socket socket) {
            this.executor = executor;
            this.socket = socket;
        }

        @Override
        public void run() {
            threads.add(Thread.currentThread());

            while (true) {
                if (!socket.isConnected()) {
                    break;
                }

                try {
                    Request request = readClient(socket);
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

                    if (command != NameOfCommand.LOGIN && command != NameOfCommand.REGISTER && !isAuthorized(request)) {
                        sendResponse(socket, RequestResult.createFailResult(RequestError.NOT_AUTHORIZED));
                        continue;
                    }

                    switch (command) {
                        case INFO:
                            CollectionInfo info = executor.info();
                            sendResponse(socket, RequestResult.createSuccessResultWith(info));
                            break;

                        case REMOVE:
                            if (args.length == 0) {
                                sendResponse(socket, RequestResult.createFailResult(RequestError.WRONG_SIGNATURE));
                                continue;
                            }
                            try {
                                int id = Integer.parseInt(args[0]);
                                boolean removed = executor.remove(id);
                                sendResponse(socket, RequestResult.createSuccessResultWith(removed));
                            } catch (NumberFormatException ex) {
                                sendResponse(socket, RequestResult.createFailResult(RequestError.WRONG_SIGNATURE));
                                continue;
                            }
                            break;

                        case ADD:
                            sendResponse(socket, RequestResult.createSuccessResultWith(executor.add(humans.get(0))));
                            break;

                        case ADD_IF_MAX:
                            if (humans == null || humans.size() != 1) {
                                sendResponse(socket, RequestResult.createFailResult(RequestError.WRONG_DATA));
                                break;
                            }
                            sendResponse(socket, RequestResult.createSuccessResultWith(executor.addIfMax(humans.get(0))));
                            break;

                        case REMOVE_FIRST:
                            sendResponse(socket, RequestResult.createSuccessResultWith(executor.removeFirst()));
                            break;

                        case SHOW:
                            sendResponse(socket, RequestResult.createSuccessResultWith(executor.show()));
                            break;

                        case REMOVE_GREATER:
                            if (humans == null || humans.size() != 1) {
                                sendResponse(socket, RequestResult.createFailResult(RequestError.WRONG_DATA));
                                break;
                            }
                            sendResponse(socket, RequestResult.createSuccessResultWith(executor.removeGreater(humans.get(0))));
                            break;

                        case IMPORT:
                            RuntimeExecutor importExecutor = new RuntimeExecutor();
                            boolean importSuccess = humans.stream().allMatch(importExecutor::add);
                            importExecutor.saveCollection();
                            sendResponse(socket, RequestResult.createSuccessResultWith(importSuccess));
                            break;

                        case LOAD:
                            RuntimeExecutor loadExecutor = new RuntimeExecutor();
                            boolean loadSuccess = loadExecutor.loadCollection();
                            loadSuccess &= loadExecutor.show().stream().allMatch(human -> executor.add(human));
                            sendResponse(socket, RequestResult.createSuccessResultWith(loadSuccess));
                            break;

                        case SAVE:
                            executor.saveCollection();
                            sendResponse(socket, RequestResult.createSuccessResult());
                            break;

                        case EXIT:
                            executor.saveCollection();
                            sendResponse(socket, RequestResult.createSuccessResult());
                            threads.remove(Thread.currentThread());
                            socket.close();
                            break;

                        case LOGIN:
                            sendResponse(socket, RequestResult.createSuccessResultWith(isAuthorized(request)));
                            break;

                        case REGISTER:
                            if (args.length == 0) {
                                sendResponse(socket, RequestResult.createFailResult(RequestError.WRONG_DATA));
                                break;
                            }

                            if (users.getUserByUsername(request.getUsername()) != null) {
                                sendResponse(socket, RequestResult.createFailResult(RequestError.ALREADY_REGISTERED));
                                break;
                            }

                            String email = args[0];
                            String password = generatePassword(10);
                            String passwordHash = PasswordUtils.getPasswordHash(password);

                            boolean sendSuccess = emailSender.sendEmail(email,
                                    String.format(
                                            "Приветствую, %s!\nТы зарегистрировался на сервере.\nТвой пароль: %s\n",
                                            request.getUsername(), password
                                    )
                            );
                            if (!sendSuccess) {
                                sendResponse(socket, RequestResult.createSuccessResultWith(false));
                            }

                            boolean createSuccess = users.createUser(new User()
                                    .withUsername(request.getUsername())
                                    .withPasswordHash(passwordHash)
                                    .withEmail(email)
                            ) != -1;

                            sendResponse(socket, RequestResult.createSuccessResultWith(createSuccess));
                            break;
                    }

                } catch (IOException e) {
                    try {
                        sendResponse(socket, RequestResult.createFailResult(e, RequestError.IO_ERROR));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        break;
                    }
                } catch (JAXBException e) {
                    try {
                        sendResponse(socket, RequestResult.createFailResult(RequestError.FILE_PARSE_ERROR));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        break;
                    }
                } catch (SQLException ex) {
                    try {
                        ex.printStackTrace();
                        sendResponse(socket, RequestResult.createFailResult(RequestError.DATABASE_ERROR));
                    } catch (IOException ioex) {
                        ioex.printStackTrace();
                        break;
                    }
                }
            }
            threads.remove(Thread.currentThread());
        }

        private boolean isAuthorized(Request request) throws SQLException {
            String username = request.getUsername();
            String passwordHash = request.getPasswordHash();
            if (username == null || passwordHash == null) {
                return false;
            }

            User user = users.getUserByUsername(username);
            if (user == null) {
                return false;
            }

            return passwordHash.equals(user.getPasswordHash());
        }
    }
}
