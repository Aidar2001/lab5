package basePackage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.Properties;

public class EmailSenderImpl implements EmailSender {

    private final boolean IS_DEBUG = Boolean.TRUE;

    private final String MESSAGE_THEME = "Сообщение от сервера Айдара";

    private String fromEmail;

    private String smtpHost;
    private int smtpPort;

    private Properties sessionProperties;

    private Session session;

    public EmailSenderImpl(String fromEmail, String fromUsername, String fromPassword,
                           String smtpHost, int smtpPort) {
        this.fromEmail = Objects.requireNonNull(fromEmail);
        this.smtpHost = Objects.requireNonNull(smtpHost);
        this.smtpPort = Objects.requireNonNull(smtpPort);

        this.sessionProperties = generateProperties();

        // Get the Session object.
        createSessionObject(fromUsername, fromPassword);
    }

    private void createSessionObject(String fromUsername, String fromPassword) {
        this.session = Session.getInstance(sessionProperties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromUsername, fromPassword);
                    }
                });
    }

    private Properties generateProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.quitwait", "false");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.debug", IS_DEBUG ? "true" : "false");
        return props;
    }

    @Override public boolean sendEmail(String email, String message) {
        try {
            // Create a default MimeMessage object.
            Message messageObject = new MimeMessage(session);

            // Set From: header field of the header.
            messageObject.setFrom(new InternetAddress(fromEmail));

            // Set To: header field of the header.
            messageObject.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

            // Set Subject: header field
            messageObject.setSubject(MESSAGE_THEME);

            // Now set the actual messageObject
            messageObject.setText(message);

            // Send messageObject
            Transport.send(messageObject);
        } catch (MessagingException ex) {
            return false;
        }

        return true;
    }
}

