package basePackage;

import basePackage.database.repository.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ServerMain {

    private static final int PORT = 6124;

    public static void main(String[] args) throws IOException, InterruptedException, SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");

        Properties properties = new Properties();
        properties.load(ServerMain.class.getClassLoader().getResourceAsStream("database.properties"));
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", properties);

        UserRepository users = new UserRepositoryImpl(connection);
        HumanRepository humans = new HumanRepositoryImpl(connection);
        AssociationRepository associations = new AssociationRepositoryImpl(connection);

        Properties mailProperties = new Properties();
        mailProperties.load(ServerMain.class.getClassLoader().getResourceAsStream("mail.properties"));

        String fromEmail = mailProperties.getProperty("fromEmail");
        String fromUsername = mailProperties.getProperty("fromUsername");
        String fromPassword = mailProperties.getProperty("fromPassword");
        String smtpHost = mailProperties.getProperty("smtpHost");
        Integer smtpPort = Integer.valueOf(mailProperties.getProperty("smtpPort"));

        EmailSender sender = new EmailSenderImpl(fromEmail, fromUsername, fromPassword, smtpHost,smtpPort);

        Server server = null;
        try {
            server = new ServerImpl(sender, users, humans, associations);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Server could not start. Exiting...");
            Thread.sleep(2000);
            System.exit(-1);
        }
        System.out.println("Staring server at port " + PORT);
        server.start(PORT);

        System.out.println("Server has successfully started at port " + PORT);
    }
}
