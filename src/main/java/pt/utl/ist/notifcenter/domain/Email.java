package pt.utl.ist.notifcenter.domain;

/*
Email - using Gmail SMTP server

Tutorial on how to authorize a third-party app (like this project) to send mails via Gmail:
1. https://support.google.com/accounts/answer/185833
2. Take note of the password generated for the app

*/

import org.apache.avro.reflect.Nullable;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.notifcenter.utils.Utils;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

@AnotacaoCanal
public class Email extends Email_Base {

    private Session emailClient = null;

    @Override
    public String getUri() {
        return null;
    }

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

        emailClient = null;

        return this;
    }

    
    @Override
    public void checkIsMessageAdequateForChannel(Mensagem msg) {

    }

    private void createEmailClient() {
        if (emailClient == null) {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true"); //this might not be needed
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.host", this.getSmtpServer());
            props.put("mail.smtp.port", this.getSmtpPort());

            emailClient = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(Email.this.getSmtpUsername(), Email.this.getSmtpPassword());
                }
            });
        }
    }

    @Override
    public void sendMessage(Mensagem msg) {

        checkIsMessageAdequateForChannel(msg);

        createEmailClient();

        try {
            Message mailToSend = new MimeMessage(emailClient);
            mailToSend.setFrom(new InternetAddress(this.getSmtpUsername(), false));

            //A way to try to get delivery status
            //mailToSend.setHeader("Disposition-Notification-To", "notifcentremail@gmail.com");

            ArrayList<InternetAddress> listOfToAddresses = new ArrayList<>();
            //listOfToAddresses.add(new InternetAddress("notifcentremail@gmail.com")); //dadosContacto
            for (PersistentGroup group : msg.getGruposDestinatariosSet()) {
                group.getMembers().forEach(user -> {

                    //Debug
                    System.out.println("LOG: user: " + user.getUsername() + " with email: " + user.getEmail());

                    boolean userHasNoContactForThisChannel = true;

                    //prevent duplicated message for same user:
                    if (user.getUserMessageDeliveryStatusSet().stream().anyMatch(e -> e.getMensagem().equals(msg))) {
                        System.out.println("DEBUG: Prevented duplicated message for user " + user.getUsername());
                        userHasNoContactForThisChannel = false;
                    }
                    else {
                        for (Contacto contacto : user.getContactosSet()) {

                            if (contacto.getCanal().equals(this)) {

                                //Debug
                                //System.out.println("has dadosContacto " + contacto.getDadosContacto());

                                try {
                                    listOfToAddresses.add(new InternetAddress(contacto.getDadosContacto()));
                                    UserMessageDeliveryStatus edm = UserMessageDeliveryStatus.createUserMessageDeliveryStatus(this, msg, user, "unavailable", "unavailable");

                                    userHasNoContactForThisChannel = false;
                                }
                                catch (AddressException e) {
                                    System.out.println("WARNING: Wrong Email address " + contacto.getDadosContacto() + " for user id " + user.getExternalId());
                                }

                                break; //no need to search more contacts for this user on this channel.
                            }
                        }
                    }

                    if (userHasNoContactForThisChannel) {
                        System.out.println("WARNING: user " + user.getUsername() + " has no available contact for " + this.getClass().getSimpleName());
                        UserMessageDeliveryStatus edm = UserMessageDeliveryStatus.createUserMessageDeliveryStatus(this, msg, user, "userHasNoContactForSuchChannel", "userHasNoContactForSuchChannel");
                    }

                });
            }

            //mailToSend.addRecipients(Message.RecipientType.BCC, InternetAddress.parse("abc@abc.com,abc@def.com,ghi@abc.com"));
            mailToSend.setRecipients(Message.RecipientType.BCC, listOfToAddresses.toArray(new InternetAddress[0]));

            mailToSend.setSubject(msg.getAssunto());
            mailToSend.setSentDate(new Date());

            Multipart multipart = new MimeMultipart();

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(msg.getTextoLongo(), "text/html");
            multipart.addBodyPart(messageBodyPart);

            //MimeBodyPart attachPart = new MimeBodyPart();
            //attachPart.attachFile("/home/cr/imgg.png");
            //multipart.addBodyPart(attachPart);
            for (Attachment a : msg.getAttachmentsSet()) {
                MimeBodyPart attachPart = new MimeBodyPart();
                ByteArrayDataSource bds = new ByteArrayDataSource(a.getContent(), a.getContentType());
                attachPart.setDataHandler(new DataHandler(bds));
                attachPart.setFileName(a.getDisplayName());
                multipart.addBodyPart(attachPart);
            }

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
        /*catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException");
            //throw new Exception();
        }
        */

    }


    @Override
    public UserMessageDeliveryStatus dealWithMessageDeliveryStatusCallback(HttpServletRequest request) {

        return null;
    }


}
