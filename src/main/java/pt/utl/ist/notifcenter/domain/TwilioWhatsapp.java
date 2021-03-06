package pt.utl.ist.notifcenter.domain;

/*
Twilio - Whatsapp chat

Configuration link: https://www.twilio.com/console/sms/whatsapp/sandbox
USERID (dadosContacto): whatsapp:+351<phoneNumber>

*/

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.async.DeferredResult;
import pt.utl.ist.notifcenter.api.HTTPClient;
import pt.utl.ist.notifcenter.api.MainAPIResource;
import pt.utl.ist.notifcenter.api.UtilsResource;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class TwilioWhatsapp extends TwilioWhatsapp_Base {

    static {
        final JsonObject example = new JsonObject();
        example.addProperty("accountSID", "example accountSID");
        example.addProperty("authToken", "example authToken");
        example.addProperty("fromPhoneNumber", "example fromPhoneNumber");
        CanalProvider provider = new CanalProvider(example.toString(), (config) -> new TwilioWhatsapp(config));
        Canal.CHANNELS.put(TwilioWhatsapp.class, provider);
    }

    private static String URI = "https://api.twilio.com/2010-04-01/Accounts/%s/Messages.json";

    public TwilioWhatsapp(final String config) {
        super();
        this.setConfig(config);
    }

    @Override
    public void sendMessage(Mensagem msg){

        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        header.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        header.add("Authorization", HTTPClient.createBasicAuthString(this.getConfigAsJson().get("accountSID").getAsString(), this.getConfigAsJson().get("authToken").getAsString()));

        body.put("To", Collections.singletonList("initializing...")); ///
        body.put("From", Collections.singletonList(this.getConfigAsJson().get("fromPhoneNumber").getAsString()));
        body.put("Body", Collections.singletonList(msg.createSimpleMessageNotificationWithLink()));

        for (PersistentGroup group : msg.getGruposDestinatariosSet()) {
            group.getMembers().forEach(user -> {

                boolean userHasNoContactForThisChannel = true;

                //prevent duplicated message for same user:
                if (user.getUserMessageDeliveryStatusSet().stream().anyMatch(e -> e.getMensagem().equals(msg))) {
                    System.out.println("DEBUG: Prevented duplicated message for user " + user.getUsername());
                    userHasNoContactForThisChannel = false;
                }
                else {
                    for (Contacto contacto : user.getContactosSet()) {
                        if (contacto.getCanal().equals(this)) {

                            body.remove("To");
                            body.put("To", Collections.singletonList("whatsapp:" + contacto.getDadosContacto()));

                            UserMessageDeliveryStatus edm = UserMessageDeliveryStatus.createUserMessageDeliveryStatus(msg, user, "none_yet", "none_yet");

                            DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
                            deferredResult.setResultHandler((Object responseEntityOrigin) -> {

                                ResponseEntity<String> responseEntity = (ResponseEntity<String>) responseEntityOrigin;

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

                                if (responseEntity.getStatusCode() == HttpStatus.OK || responseEntity.getStatusCode() == HttpStatus.CREATED) {
                                    //System.out.println("Success on sending message to user id " + user.getExternalId() + "! external id is: " + idExterno + ", and delivery status is: " + estadoEntrega);
                                }
                                else {
                                    System.out.println("Failed to send message to user id " + user.getExternalId() + "! external id is: " + idExterno + ", and delivery status is: " + estadoEntrega);
                                }

                                edm.changeIdExternoAndEstadoEntrega(idExterno, estadoEntrega);

                                MainAPIResource.notificateAppViaWebhook(edm); //Might not be called for unknown reasons.
                            });

                            String uri = String.format(URI, this.getConfigAsJson().get("accountSID").getAsString());

                            //send message
                            HTTPClient.restASyncClientMultiValueMap(HttpMethod.POST, uri, header, body, deferredResult);

                            userHasNoContactForThisChannel = false;

                            break; //no need to search more contacts for this user on this channel.
                        }
                    }
                }

                if (userHasNoContactForThisChannel) {
                    System.out.println("WARNING: user " + user.getUsername() + " has no contact for " + this.getClass().getSimpleName());
                    UserMessageDeliveryStatus edm = UserMessageDeliveryStatus.createUserMessageDeliveryStatus(msg, user, "userHasNoContactForSuchChannel", "userHasNoContactForSuchChannel");
                }

            });
        }
    }

    @Override
    public UserMessageDeliveryStatus dealWithDeliveryStatusNotifications(HttpServletRequest request){

        MultiValueMap<String, String> requestParams = HTTPClient.getHttpServletRequestParams(request);

        //"MessageStatus":"delivered","MessageSid":"SM9f705525cc4143ef8dece27557549a5f"
        String idExterno = UtilsResource.getRequiredValueFromMultiValueMap(requestParams, "MessageSid");
        String estadoEntrega = UtilsResource.getRequiredValueFromMultiValueMap(requestParams, "MessageStatus");

        for (CanalNotificacao cn : this.getCanalNotificacaoSet()) {
            for (Mensagem m : cn.getMensagemSet()) {
                for (UserMessageDeliveryStatus e : m.getUserMessageDeliveryStatusSet()) {
                    if (e.getIdExterno().equals(idExterno)) {
                        e.changeEstadoEntrega(estadoEntrega);
                        return e;
                    }
                }
            }
        }

        return null;
    }

}
