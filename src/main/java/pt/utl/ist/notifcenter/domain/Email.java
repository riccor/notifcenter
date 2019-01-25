package pt.utl.ist.notifcenter.domain;

/*
Email - using Gmail SMTP server

Tutorial on how to authorize a third-party app (like this project) to send mails via Gmail:
1. https://support.google.com/accounts/answer/185833
2. Take note of the password generated for the app

*/

import org.apache.avro.reflect.Nullable;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.notifcenter.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

@AnotacaoCanal
public class Email extends Email_Base {

    public Email() {
        super();
    }

    @Atomic
    public static Email createChannel(String smtpServer, String smtpPort, String smtpUsername, String smtpPassword) {

        Email email = new Email();
        email.setSmtpServer(smtpServer);
        email.setSmtpPort(smtpPort);
        email.setSmtpUsername(smtpUsername);
        email.setSmtpPassword(smtpPassword);

        //Debug
        ///email.setEmail("Email-" + email.getExternalId() + "@notifcenter.com");

        return email;
    }

    @Atomic
    public Email updateChannel(@Nullable String smtpServer, @Nullable String smtpPort, @Nullable String smtpUsername, @Nullable String smtpPassword) {

        if (Utils.isValidString(smtpServer)) {
            this.setSmtpServer(smtpServer);
        }
        if (Utils.isValidString(smtpPort)) {
            this.setSmtpPort(smtpPort);
        }

        if (Utils.isValidString(smtpUsername)) {
            this.setSmtpUsername(smtpUsername);
        }

        if (Utils.isValidString(smtpPassword)) {
            this.setSmtpPassword(smtpPassword);
        }

        return this;
    }

    
    @Override
    public void checkIsMessageAdequateForChannel(Mensagem msg) {

    }

    @Override
    public void sendMessage(Mensagem msg) {

        checkIsMessageAdequateForChannel(msg);

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); //it seems this is not needed
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.host", this.getSmtpServer());
        props.put("mail.smtp.port", this.getSmtpPort());

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Email.this.getSmtpUsername(), Email.this.getSmtpPassword());
            }
        });

        try {
            Message mailToSend = new MimeMessage(session);
            mailToSend.setFrom(new InternetAddress(this.getSmtpUsername(), false));

            ArrayList<InternetAddress> listOfToAddresses = new ArrayList<>();
            listOfToAddresses.add(new InternetAddress("notifcentremail@gmail.com")); //dadosContacto



            //mailToSend.addRecipients(Message.RecipientType.BCC, InternetAddress.parse("abc@abc.com,abc@def.com,ghi@abc.com"));
            mailToSend.setRecipients(Message.RecipientType.BCC, listOfToAddresses.toArray(new InternetAddress[0]));

            mailToSend.setSubject(msg.getAssunto());
            //mailToSend.setContent("Notifcentre email - CONTENT", "text/html"); //it seems this is not needed
            mailToSend.setSentDate(new Date());

            Multipart multipart = new MimeMultipart();

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(msg.createSimpleMessageNotificationWithLink(), "text/html");
            multipart.addBodyPart(messageBodyPart);

            MimeBodyPart attachPart = new MimeBodyPart();
            attachPart.attachFile("/home/cr/imgg.png");
            multipart.addBodyPart(attachPart);

            mailToSend.setContent(multipart);
            Transport.send(mailToSend);
        }
        catch (AddressException e) {
            e.printStackTrace();
            System.out.println("AddressException");
            //throw new Exception();
        }
        catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("MessagingException");
            //throw new Exception();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException");
            //throw new Exception();
        }
    }


    @Override
    public EstadoDeEntregaDeMensagemEnviadaAContacto dealWithMessageDeliveryStatusCallback(HttpServletRequest request) {

        return null;
    }


}
