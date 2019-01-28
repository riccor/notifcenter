package pt.utl.ist.notifcenter.domain;

/*
Facebook messenger

NOTE: We can only send messages to users after they sent our Facebook page a message.

Text sent must be UTF-8 and max. 2000 characters

Access token aka "Page Access Token"
https://developers.facebook.com/apps/<appId>/messenger/settings/

dadosContacto = +351<phoneNumber>

Rate limits:
"Messenger Platform supports a high rate of calls to the Send API. However, you should architect your system
such that you distribute any sudden high amounts of load over time and are able to control your throughput
should you hit our rate limits."

API:
POST https://graph.facebook.com/v2.6/me/messages?access_token=%s (%s = PAGE_ACCESS_TOKEN)
*/

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.avro.reflect.Nullable;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.async.DeferredResult;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.notifcenter.api.HTTPClient;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.utils.AnotherNotifcenterException;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;
import pt.utl.ist.notifcenter.utils.Utils;

import javax.servlet.http.HttpServletRequest;

@AnotacaoCanal
public class Messenger extends Messenger_Base {

    public Messenger() {
        super();
    }

    @Override
    public String getUri() {
        return "https://graph.facebook.com/v2.6/me/messages?access_token=%s";
    }

    @Atomic
    public static Messenger createChannel(String access_token /*, String uri*/) {

        Messenger messenger = new Messenger();
        messenger.setAccess_token(access_token);
        /*messenger.setUri(uri);*/

        //Debug
        ///messenger.setEmail("messenger-" + messenger.getExternalId() + "@notifcenter.com");

        return messenger;
    }

    @Atomic
    public Messenger updateChannel(@Nullable final String access_token /*, @Nullable final String uri*/) {

        if (Utils.isValidString(access_token)) {
            this.setAccess_token(access_token);
        }

        /*
        if (Utils.isValidString(uri)) {
            this.setUri(uri);
        }*/

        return this;
    }

    @Override
    public void checkIsMessageAdequateForChannel(Mensagem msg) {
        if (msg.createSimpleMessageNotificationWithLink().length() > 2000) {
            ///IllegalArgumentException
            //" Check localhost:8080/notifcenter/mensagens/281681135140901".length() = 59
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_TEXTO_LONGO_ERROR, "TextoCurto must be at most " + (2000-59) + " characters long.");
        }
    }

    @Override
    public void sendMessage(Mensagem msg){

        checkIsMessageAdequateForChannel(msg);

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

                            UserMessageDeliveryStatus edm = UserMessageDeliveryStatus.createUserMessageDeliveryStatus(this, msg, user, "none_yet", "none_yet");

                            HttpHeaders httpHeaders = new HttpHeaders();
                            httpHeaders.set("Content-type", "application/json");
                            String bodyContent = HTTPClient.stringToJson(createMessengerBodyPhoneNumber(msg.createSimpleMessageNotificationWithLink(), contacto.getDadosContacto())).toString();

                            //debug:
                            //System.out.println(Utils.MAGENTA + "\n\nJson body:\n" + Utils.CYAN + bodyContent);

                            String url;
                            try {
                                url = String.format(this.getUri(), this.getAccess_token());
                            } catch (Exception e) {
                                throw new NotifcenterException(ErrorsAndWarnings.INTERNAL_SERVER_ERROR, "Please contact system administration. URL error on channel " + this.getExternalId());
                            }


                            DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
                            deferredResult.setResultHandler((Object responseEntity) -> {

                                handleDeliveryStatus((ResponseEntity<String>) responseEntity, edm, user);

                            });

                            HTTPClient.restASyncClientBody(HttpMethod.POST, url, httpHeaders, bodyContent, deferredResult);

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

        /*
            example success response:
                {
                  "recipient_id": "1254477777772919",
                  "message_id": "m_AG5Hz2Uq7tuwNEhXfYYKj8mJEM_QPpz5jdCK48PnKAjSdjfipqxqMvK8ma6AC8fplwlqLP_5cgXIbu7I3rBN0P"
                }

            example error response:
                {
                  "error": {
                    "message": "Invalid OAuth access token.",
                    "type": "OAuthException",
                    "code": 190,
                    "error_subcode": 1234567,
                    "fbtrace_id": "BLBz/WZt8dN"
                  }
                }
        */

        JsonElement jObj = new JsonParser().parse(responseEntity.getBody());
        String idExterno = UtilsResource.getRequiredValueOrReturnNullInsteadRecursive(jObj.getAsJsonObject(), "message_id");
        String estadoEntrega;

        if (idExterno == null) {
            idExterno = "null";
        }

        if (responseEntity.getStatusCode() == HttpStatus.OK || responseEntity.getStatusCode() == HttpStatus.CREATED) {
            estadoEntrega = "Delivered";
            System.out.println("Success on sending message to user id " + user.getExternalId() + "! external id is: " + idExterno + ", and delivery status is: " + estadoEntrega);
        }
        else {
            estadoEntrega = UtilsResource.getRequiredValueOrReturnNullInsteadRecursive(jObj.getAsJsonObject(), "error");
            System.out.println("Failed to send message to user id " + user.getExternalId() + "! external id is: " + idExterno + ", and delivery status is: " + estadoEntrega);
        }

        edm.changeIdExternoAndEstadoEntrega(idExterno, estadoEntrega);
    }

    /*
    WARNING: NOT TESTED! Because:
    1. We can only send messages to users after they sent our Facebook page a message.
    2. When a user sends us their first message, Facebook automatically will send us a HTTP request to our webhook
        with respective user "page-scoped ID" (PSID) = dadosContacto
    3. BUT when registering our webhook at https://developers.facebook.com/apps/<appId>/webhooks/,
        I CANNOT register it because the following message appears: "A secure Callback URL (https) is required"
        which means I cannot register my webhook on Facebook servers and COULD NOT test sending messages.
     */
    public String createMessengerBodyPageScopeId(String text, String recipient_PSID) {
        String messaging_type = "UPDATE";  //https://developers.facebook.com/docs/messenger-platform/send-messages/#messaging_types
        return String.format("{\"messaging_type\": \"%s\", \"recipient\": { \"id\": \"%s\" }, \"message\": { \"text\": \"%s\" } }", messaging_type, recipient_PSID, text);
    }

    public String createMessengerBodyPhoneNumber(String text, String phoneNumber) {
        String messaging_type = "UPDATE";  //https://developers.facebook.com/docs/messenger-platform/send-messages/#messaging_types
        return String.format("{\"messaging_type\": \"%s\", \"recipient\": { \"phone_number\": \"%s\" }, \"message\": { \"text\": \"%s\" } }", messaging_type, phoneNumber, text);
    }

    @Override
    public UserMessageDeliveryStatus dealWithMessageDeliveryStatusCallback(HttpServletRequest request) {

        MultiValueMap<String, String> requestParams = HTTPClient.getHttpServletRequestParams(request);

        //NOTE: Not tested because "A secure Callback URL (https) is required"
        //"verify token": hub_verify_token -> set by us on https://developers.facebook.com/apps/298908694309495/webhooks/
        String hub_challenge = UtilsResource.getRequiredValueFromMultiValueMapOrReturnNullInstead(requestParams, "hub_challenge");
        if (hub_challenge != null) {
            throw new AnotherNotifcenterException(ErrorsAndWarnings.SUCCESS, hub_challenge);
        }

        return null;
    }


}
