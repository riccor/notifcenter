package pt.utl.ist.notifcenter.domain;

/*
Twilio - Whatsapp chat

Configuration link: https://www.twilio.com/console/sms/whatsapp/sandbox
STATUS CALLBACK URL for the created example (used to receive message delivery statuses from Twilio): http://www.notifcentre.com:8080/notifcenter/apicanais/281835753963522/messagedeliverystatus
USERID (dadosContacto): whatsapp:+351<phoneNumber>

*/

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.avro.reflect.Nullable;
import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.async.DeferredResult;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.notifcenter.api.HTTPClient;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@AnotacaoCanal//(classFields = {"accountSID", "authToken", "fromPhoneNumber", "uri"})
public class TwilioWhatsapp extends TwilioWhatsapp_Base {

    @Override
    public String getUri() {
        return "https://api.twilio.com/2010-04-01/Accounts/AC6cbbd7d6eb26d8dc34fce44a4bea8a1c/Messages.json";
    }

    private TwilioWhatsapp() {
        super();
        //this.setSistemaNotificacoes(SistemaNotificacoes.getInstance());
    }

    @Atomic
    public static TwilioWhatsapp createChannel(final String accountSID, final String authToken, final String fromPhoneNumber /*, final String uri*/) {
        TwilioWhatsapp twilioWhatsapp = new TwilioWhatsapp();
        twilioWhatsapp.setAccountSID(accountSID);
        twilioWhatsapp.setAuthToken(authToken);
        twilioWhatsapp.setFromPhoneNumber(fromPhoneNumber);
        //twilioWhatsapp.setUri(uri);

        //Debug
        ///twilioWhatsapp.setEmail("twiliowhatsapp-" + twilioWhatsapp.getExternalId() + "@notifcenter.com");

        return twilioWhatsapp;
    }

    @Atomic
    public TwilioWhatsapp updateChannel(@Nullable final String accountSID, @Nullable final String authToken, @Nullable final String fromPhoneNumber /*, @Nullable final String uri*/) {

        if (Utils.isValidString(accountSID)) {
            this.setAccountSID(accountSID);
        }

        if (Utils.isValidString(authToken)) {
            this.setAuthToken(authToken);
        }

        if (Utils.isValidString(fromPhoneNumber)) {
            this.setFromPhoneNumber(fromPhoneNumber);
        }

        /*
        if (Utils.isValidString(uri)) {
            this.setUri(uri);
        }
        */

        return this;
    }

    public static TwilioWhatsapp createTwilioWhatsappFromPropertiesFile(final String file) {
        String filename = String.format(NotifcenterSpringConfiguration.getConfiguration().notifcenterChannelsCredentials(), file);
        Map<String, String> propertiesMap = Utils.loadPropertiesFromPropertiesFile(TwilioWhatsapp.class, filename, "accountSID", "authToken", "fromPhoneNumber", "uri");

        if (!Utils.isMapFilled(propertiesMap)) {
            System.out.println("Error: Cannot create entity from file.");
            return null;
        }

        return createChannel/*TwilioWhatsApp*/(propertiesMap.get("accountSID"), propertiesMap.get("authToken"), propertiesMap.get("fromPhoneNumber")/*, propertiesMap.get("uri")*/);
    }

    /*
    public static adaptMessageToChannel(Mensagem mensagem) {

    }
    */

    @Override
    public void checkIsMessageAdequateForChannel(Mensagem msg) {

    }

    @Override
    public void sendMessage(Mensagem msg){

        checkIsMessageAdequateForChannel(msg);

        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        header.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        header.add("Authorization", HTTPClient.createBasicAuthString(this.getAccountSID(), this.getAuthToken()));

        body.put("To", Collections.singletonList("initializing...")); ///
        body.put("From", Collections.singletonList(this.getFromPhoneNumber()));
        body.put("Body", Collections.singletonList(msg.createSimpleMessageNotificationWithLink()));

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
                            System.out.println("has dadosContacto " + contacto.getDadosContacto());

                            //responseEntities.add(tw.sendMessage(contacto.getDadosContacto(), msg.getTextoCurto()));
                            body.remove("To");
                            body.put("To", Collections.singletonList(contacto.getDadosContacto()));

                            UserMessageDeliveryStatus edm = UserMessageDeliveryStatus.createUserMessageDeliveryStatus(this, msg, user, "none_yet", "none_yet");

                            DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
                            deferredResult.setResultHandler((Object responseEntity) -> {

                                //handleDeliveryStatus((ResponseEntity<String>) responseEntity, this, msg, contacto);
                                handleDeliveryStatus((ResponseEntity<String>) responseEntity, edm, user);

                            });

                            //HTTPClient.restSyncClient(HttpMethod.POST, this.getUri(), header, body);
                            HTTPClient.restASyncClient(HttpMethod.POST, this.getUri(), header, body, deferredResult);

                            userHasNoContactForThisChannel = false;

                            break; //no need to search more contacts for this user on this channel.
                        }
                    }
                }

                if (userHasNoContactForThisChannel) {
                    System.out.println("WARNING: user " + user.getUsername() + " has no contact for " + this.getClass().getSimpleName());
                    UserMessageDeliveryStatus edm = UserMessageDeliveryStatus.createUserMessageDeliveryStatus(this, msg, user, "userHasNoContactForSuchChannel", "userHasNoContactForSuchChannel");
                }

            });
        }
    }

    public void handleDeliveryStatus(ResponseEntity<String> responseEntity, UserMessageDeliveryStatus edm, User user) {

        //Debug
        HTTPClient.printResponseEntity(responseEntity);

        JsonElement jObj = new JsonParser().parse(responseEntity.getBody());
        String idExterno = UtilsResource.getRequiredValueOrReturnNullInstead(jObj.getAsJsonObject(), "sid");
        String estadoEntrega = UtilsResource.getRequiredValueOrReturnNullInstead(jObj.getAsJsonObject(), "status");

        if (idExterno == null) {
            idExterno = "null";
        }

        if (estadoEntrega == null) {
            estadoEntrega = "null";
        }

        //UserMessageDeliveryStatus.createUserMessageDeliveryStatus(canal, msg, contacto, idExterno, estadoEntrega);
        edm.changeIdExternoAndEstadoEntrega(idExterno, estadoEntrega);

        if (responseEntity.getStatusCode() == HttpStatus.OK || responseEntity.getStatusCode() == HttpStatus.CREATED) {
            System.out.println("Success on sending message to user id " + user.getExternalId() + "! external id is: " + idExterno + ", and delivery status is: " + estadoEntrega);
        }
        else {
            System.out.println("Failed to send message to user id " + user.getExternalId() + "! external id is: " + idExterno + ", and delivery status is: " + estadoEntrega);
        }
    }

    @Override
    public UserMessageDeliveryStatus dealWithMessageDeliveryStatusCallback(HttpServletRequest request){

        MultiValueMap<String, String> requestParams = HTTPClient.getHttpServletRequestParams(request);

        //"MessageStatus":"delivered","MessageSid":"SM9f705525cc4143ef8dece27557549a5f"
        String idExterno = UtilsResource.getRequiredValueFromMultiValueMap(requestParams, "MessageSid");
        String estadoEntrega = UtilsResource.getRequiredValueFromMultiValueMap(requestParams, "MessageStatus");

        for (UserMessageDeliveryStatus e : this.getUserMessageDeliveryStatusSet()) {
            if (e.getIdExterno().equals(idExterno)) {
                e.changeEstadoEntrega(estadoEntrega);
                return e;
            }
        }

        return null;
    }


}
