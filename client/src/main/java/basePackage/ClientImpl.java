package basePackage;

import basePackage.connect.CollectionInfo;
import basePackage.connect.Request;
import basePackage.connect.RequestError;
import basePackage.connect.RequestResult;
import basePackage.objectModel.Human;
import basePackage.objectModel.Humans;
import basePackage.parsers.XMLParser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

public class ClientImpl implements Client {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    @Override
    public RequestResult<Void> connect(String host, int port) {
        Objects.requireNonNull(host);
        if (socket != null && socket.isConnected()) {
            throw new IllegalStateException("Socket is already connected.");
        }

        try {
            socket = new Socket(host, port);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            RequestResult<Void> result = (RequestResult<Void>) in.readObject();// TODO: исправить зависание
            return result;
        } catch (IOException | ClassNotFoundException e) {
            return RequestResult.createFailResult(e, RequestError.SOCKET_CONNECTION_ERROR.getErrorCode());
        }
    }

    @Override
    public RequestResult<Void> importFile(File file) {
        Objects.requireNonNull(file);
        if (!file.exists() || !file.isFile()) {
            throw new IllegalStateException();
        }
        XMLParser parser = new XMLParser();
        try {
            Humans humans = parser.fromXML(file);
            Request request = new Request()
                    .withSignature(NameOfCommand.IMPORT.toString())
                    .withData(humans.getHumans());
            out.writeObject(request);
            out.flush();
            RequestResult<Void> result = (RequestResult<Void>) in.readObject();
            return result;
        } catch (JAXBException e) {
            return RequestResult.createFailResult(e, RequestError.FILE_PARSE_ERROR.getErrorCode());
        } catch (IOException | ClassNotFoundException e) {
            return RequestResult.createFailResult(e, RequestError.SOCKET_CONNECTION_ERROR.getErrorCode());
        }
    }

    @Override
    public RequestResult<Void> addHuman(Human human) {
        return null;
    }

    @Override
    public RequestResult<CollectionInfo> getInfo() {
        return null;
    }

    @Override
    public RequestResult<List<Human>> getAllHumans() {
        return null;
    }
}
