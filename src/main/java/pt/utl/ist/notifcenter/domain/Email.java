/*
Email - using Gmail SMTP server

Tutorial on how to authorize a third-party app (like this project) to send mails via Gmail:
1. https://support.google.com/accounts/answer/185833
2. Take note of the password generated for the app
*/

package pt.utl.ist.notifcenter.domain;

import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

public class Email extends Email_Base {

    private Session emailClient = null;

    static {
        final JsonObject example = new JsonObject();
        example.addProperty("smtpServer", "example smtpServer");
        example.addProperty("smtpPort", "example smtpPort");
        example.addProperty("smtpUsername", "example smtpUsername");
        example.addProperty("smtpPassword", "example smtpPassword");
        CanalProvider provider = new CanalProvider(example.toString(), (config) -> new Email(config));
        Canal.CHANNELS.put(Email.class, provider);
    }

    public Email(final String config) {
        super();
        this.setConfig(config);
    }

    private void createEmailClient() {
        if (emailClient == null) {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true"); //this might not be needed
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.host", this.getConfigAsJson().get("smtpServer").getAsString());
            props.put("mail.smtp.port", this.getConfigAsJson().get("smtpPort").getAsString());

            emailClient = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(Email.this.getConfigAsJson().get("smtpUsername").getAsString(), Email.this.getConfigAsJson().get("smtpPassword").getAsString());
                }
            });
        }
    }

    @Override
    public void sendMessage(Mensagem msg) {

        createEmailClient();

        try {
            Message mailToSend = new MimeMessage(emailClient);
            mailToSend.setFrom(new InternetAddress(this.getConfigAsJson().get("smtpUsername").getAsString(), false));

            //A way to try to get delivery status
            //mailToSend.setHeader("Disposition-Notification-To", "notifcentremail@gmail.com");

            ArrayList<InternetAddress> listOfToAddresses = new ArrayList<>();
            //listOfToAddresses.add(new InternetAddress("notifcentremail@gmail.com")); //debug

            for (Contacto contact : getContactsFromMessageRecipientUsers(msg)) {
                try {
                    listOfToAddresses.add(new InternetAddress(contact.getDadosContacto()));
                    UserMessageDeliveryStatus edm = UserMessageDeliveryStatus.createUserMessageDeliveryStatus(msg, contact.getUtilizador(), "unavailable", "unavailable");
                }
                catch (AddressException e) {
                    System.out.println("WARNING: Wrong Email address " + contact.getDadosContacto() + " for user id " + contact.getUtilizador().getExternalId());
                }
            }

            if (listOfToAddresses.size() > 0) { //it's needed at least 1 email recipient to send the message

                mailToSend.setRecipients(Message.RecipientType.BCC, listOfToAddresses.toArray(new InternetAddress[0]));

                mailToSend.setSubject(msg.getAssunto());
                mailToSend.setSentDate(new Date());

                Multipart multipart = new MimeMultipart();

                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(msg.getTextoLongo(), "text/html");
                multipart.addBodyPart(messageBodyPart);

                for (Attachment a : msg.getAttachmentsSet()) {
                    MimeBodyPart attachPart = new MimeBodyPart();
                    ByteArrayDataSource bds = new ByteArrayDataSource(a.getContent(), a.getContentType());
                    attachPart.setDataHandler(new DataHandler(bds));
                    attachPart.setFileName(a.getDisplayName());
                    multipart.addBodyPart(attachPart);
                }

                mailToSend.setContent(multipart);

                //Send message
                Transport.send(mailToSend);
            }
        }
        catch (AddressException e) {
            e.printStackTrace();
            System.out.println("AddressException");
        }
        catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("MessagingException");
        }
    }

    @Override
    public UserMessageDeliveryStatus dealWithDeliveryStatusNotifications(HttpServletRequest request) {

        return null;
    }

}
