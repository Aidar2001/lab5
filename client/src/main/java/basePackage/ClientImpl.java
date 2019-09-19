package basePackage;

import static basePackage.connect.RequestError.FILE_PARSE_ERROR;
import static basePackage.connect.RequestError.IO_ERROR;
import static basePackage.connect.RequestError.JSON_PARSE_ERROR;

import basePackage.connect.CollectionInfo;
import basePackage.connect.Request;
import basePackage.connect.RequestResult;
import basePackage.objectModel.Human;
import basePackage.objectModel.Humans;
import basePackage.parsers.XMLParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.JAXBException;

public class ClientImpl implements Client {

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
  public RequestResult<Void> connect(String host, int port) {
    Objects.requireNonNull(host);
    if (socket != null && socket.isConnected()) {
      throw new IllegalStateException("Socket is already connected.");
    }

    try {
      socket = new Socket();
      socket.connect(new InetSocketAddress(host, port), 10000); // TODO: Поместить таймаут в константу

      return readServer();
    } catch (IOException e) {
      return RequestResult.createFailResult(e, IO_ERROR.getErrorCode());
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

      RequestResult<Void> result = sendRequest(request);
      return result;
    } catch (JAXBException e) {
      return RequestResult.createFailResult(e, FILE_PARSE_ERROR.getErrorCode());
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
      return RequestResult.createFailResult(e, JSON_PARSE_ERROR.getErrorCode());
    } catch (IOException e) {
      return RequestResult.createFailResult(e, IO_ERROR.getErrorCode());
    }
  }

  private synchronized <T> RequestResult<T> readServer() {
    try {
      ByteBuffer amount = ByteBuffer.allocate(Integer.BYTES);

      int totalRead = 0;
      // read size of charset
      while(totalRead != amount.capacity()) {
        int read = socket.getInputStream().read(amount.array(), totalRead, (amount.capacity() - totalRead));
        totalRead += read;
      }
      int jsonLength = amount.getInt();

      ByteBuffer jsonBuffer = ByteBuffer.allocate(jsonLength);
      totalRead = 0;
      // read charset bytes unit all bytes are read
      while(totalRead != jsonBuffer.capacity()) {
        int read = socket.getInputStream().read(jsonBuffer.array(), totalRead, (jsonBuffer.capacity() - totalRead));
        totalRead += read;
      }

      // convert charset bytes into string
      String json = decoder.decode(jsonBuffer).toString();

      // map json string into request result
      return mapper.readValue(json, RequestResult.class);
    } catch (IOException e) {
      return RequestResult.createFailResult(e, IO_ERROR.getErrorCode());
    }
  }
}
