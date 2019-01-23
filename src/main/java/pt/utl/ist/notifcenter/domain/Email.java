package pt.utl.ist.notifcenter.domain;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

public class Email extends Email_Base {

    private static String smtpServer = "smtp.gmail.com";
    private static String smtpPort = "587";
    private static String smtpUsername = "notifcentre@gmail.com";
    private static String smtpPassword = "aaa";


    public Email() {
        super();
    }

    @Override
    public void checkIsMessageAdequateForChannel(Mensagem msg) {

    }

    @Override
    public void sendMessage(Mensagem msg) {

        checkIsMessageAdequateForChannel(msg);
    }

    @Override
    public EstadoDeEntregaDeMensagemEnviadaAContacto dealWithMessageDeliveryStatusCallback(HttpServletRequest request) {

        return null;
    }

    public static void sendMail() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpServer);
        props.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(smtpUsername, false));

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(smtpUsername));
            msg.setSubject("Notifcentre email - SUBJECT");
            msg.setContent("Notifcentre email - CONTENT", "text/html");
            msg.setSentDate(new Date());

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent("Notifcentre email", "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            MimeBodyPart attachPart = new MimeBodyPart();

            attachPart.attachFile("/home/cr/imgg.png");
            //attachPart.attachFile();

            multipart.addBodyPart(attachPart);
            msg.setContent(multipart);
            Transport.send(msg);
        }
        catch (AddressException e) {
            System.out.println("AddressException");
            //throw new Exception();
        }
        catch (MessagingException e) {
            System.out.println("MessagingException");
            //throw new Exception();
        }
        catch (IOException e) {
            System.out.println("IOException");
            //throw new Exception();
        }
    }
}
