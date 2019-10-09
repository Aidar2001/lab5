package basePackage;

import basePackage.connect.CollectionInfo;
import basePackage.connect.Request;
import basePackage.connect.RequestResult;
import basePackage.objectModel.Human;
import basePackage.objectModel.Humans;
import basePackage.parsers.XMLParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static basePackage.connect.RequestError.*;

public class ClientImpl implements Client {

  private final static Integer CONNECTION_TIMEOUT = 10_000;

  private Socket socket;
  private ObjectMapper mapper;

  private CharsetEncoder encoder;
  private CharsetDecoder decoder;

  {
    mapper = new ObjectMapper().registerModules();
    encoder = StandardCharsets.UTF_8.newEncoder();
    decoder = StandardCharsets.UTF_8.newDecoder();
  }

  @Override
  public RequestResult<Void> connection(String host, int port) {
    Objects.requireNonNull(host);
    if (socket != null && socket.isConnected()) {
      throw new IllegalStateException("Socket is already connected.");
    }

    try {
      socket = new Socket();
      socket.connect(new InetSocketAddress(host, port), CONNECTION_TIMEOUT);
      socket.setSoTimeout(300_000);

      return readServer();
    } catch (IOException e) {
      return RequestResult.createFailResult(e, IO_ERROR);
    }
  }

  @Override
  public RequestResult<Void> importFile(File file) {
    if (!file.exists() || !file.isFile()) {
      throw new IllegalStateException(file.getName()+" doesn't exist or isn't file.");
    }
    XMLParser parser = new XMLParser();
    try {
      Humans humans = parser.fromXML(file);
      Request request = new Request()
          .withSignature(NameOfCommand.IMPORT.toString())
          .withData(humans.getHumans());

      RequestResult<Void> result = sendRequest(request);
      return result;
    } catch (JAXBException e) {
      return RequestResult.createFailResult(e, FILE_PARSE_ERROR);
    }
  }

  @Override
  public RequestResult<Void> addHuman(Human human) {
    Objects.requireNonNull(human, "Added human should not be null!");

    Request request = new Request()
            .withSignature(NameOfCommand.ADD.toString())
            .withData(Collections.singletonList(human));

    return sendRequest(request);
  }

  @Override
  public RequestResult<Void> addIfMax(Human human) {
    Objects.requireNonNull(human, "Human should not be null");

    Request request = new Request()
            .withSignature(NameOfCommand.ADD_IF_MAX.toString())
            .withData(Collections.singletonList(human));

    return sendRequest(request);
  }

  @Override
  public RequestResult<CollectionInfo> getInfo() {
    Request request = new Request().withSignature(NameOfCommand.INFO.toString());
    return sendRequest(request);
  }

  @Override
  public RequestResult<List<Human>> getAllHumans() {
    Request request = new Request()
            .withSignature(NameOfCommand.SHOW.toString());

    return sendRequest(request);
  }

  @Override
  public RequestResult<Boolean> remove(Integer id) {
    Objects.requireNonNull(id);
    Request request = new Request()
            .withSignature(NameOfCommand.REMOVE + " " + id);

    return sendRequest(request);
  }

  @Override
  public RequestResult<Boolean> removeGreater(Human human) {
    Objects.requireNonNull(human);
    Request request = new Request()
            .withSignature(NameOfCommand.REMOVE_GREATER.toString())
            .withData(Collections.singletonList(human));

    return sendRequest(request);
  }

  @Override
  public RequestResult<Boolean> removeFirst() {
    Request request = new Request()
            .withSignature(NameOfCommand.REMOVE_FIRST.toString());

    return sendRequest(request);
  }

  @Override
  public RequestResult<Void> closeConnection(){
    Request request = new Request()
            .withSignature(NameOfCommand.EXIT.toString());

    return sendRequest(request);
  }

  @Override
  public RequestResult<Boolean> load() {
    Request request = new Request()
            .withSignature(NameOfCommand.LOAD.toString());

    return sendRequest(request);
  }

  @Override
  public RequestResult<Boolean> load(String collectionToLoad){
    if (collectionToLoad==null){load();}
    Request request = new Request()
            .withSignature(NameOfCommand.LOAD.toString() + " " + collectionToLoad);

    return sendRequest(request);
  }

  @Override
  public RequestResult<Boolean> save(String collectionToSave){
    Request request = new Request()
            .withSignature(NameOfCommand.SAVE.toString() + " " + collectionToSave);

    return sendRequest(request);
  }



  private synchronized <T> RequestResult<T> sendRequest(Request request) {
    try {
      // map request to json
      String json = mapper.writeValueAsString(request);

      // convert string into charset byte buffer
      ByteBuffer charset = encoder.encode(CharBuffer.wrap(json));

      // size of charset byte buffer
      ByteBuffer amount = ByteBuffer.allocate(Integer.BYTES).putInt(charset.capacity());

      // write size of charset and then the charset bytes
      socket.getOutputStream().write(amount.array());
      socket.getOutputStream().write(charset.array());

      // read server response
      return readServer();
    } catch (JsonProcessingException e) {
      return RequestResult.createFailResult(e, JSON_PARSE_ERROR);
    } catch (IOException e) {
      return RequestResult.createFailResult(e, IO_ERROR);
    }
  }

  private synchronized <T> RequestResult<T> readServer() {
    try {
      ByteBuffer amount = ByteBuffer.allocate(Integer.BYTES);

      int totalRead = 0;
      // read size of charset
      while (totalRead != amount.capacity()) {
        int read = socket.getInputStream()
                .read(amount.array(), totalRead, (amount.capacity() - totalRead));
        totalRead += read;
      }
      int jsonLength = amount.getInt();

      ByteBuffer jsonBuffer = ByteBuffer.allocate(jsonLength);
      totalRead = 0;
      // read charset bytes unit all bytes are read
      while (totalRead != jsonBuffer.capacity()) {
        int read = socket.getInputStream()
                .read(jsonBuffer.array(), totalRead, (jsonBuffer.capacity() - totalRead));
        totalRead += read;
      }

      // convert charset bytes into string
      String json = decoder.decode(jsonBuffer).toString();

      // map json string into request result
      return mapper.readValue(json, RequestResult.class);
    } catch (IOException e) {
      return RequestResult.createFailResult(e, IO_ERROR);
    }
  }
}
