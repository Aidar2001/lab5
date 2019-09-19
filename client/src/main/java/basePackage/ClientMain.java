package basePackage;

import basePackage.connect.RequestResult;
import java.io.File;

public class ClientMain {

  private static final String SERVER_HOST = "localhost";
  private static final int SERVER_PORT = 6123;

    private static final String PATH_TO_HUMANS_COLLECTION = "DEFINE PATH TO humans.xml HERE";

  public static void main(String[] args) {
    Client client = new ClientImpl();

    System.out.println("Connection to " + SERVER_HOST + ":" + SERVER_PORT);
    RequestResult<Void> connectionResult = client.connect(SERVER_HOST, SERVER_PORT);

    System.out.println("Got connection result:");
    System.out.println(connectionResult.toString());

    if(connectionResult.getSuccess()) {
      System.out.println("Trying to send collection from client's file " + PATH_TO_HUMANS_COLLECTION + " to server");
      RequestResult<Void> importResult = client.importFile(new File(PATH_TO_HUMANS_COLLECTION));

      System.out.println("Got import result:");
      System.out.println(importResult.toString());
    }
  }
}
