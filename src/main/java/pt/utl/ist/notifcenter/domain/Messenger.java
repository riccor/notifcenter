package pt.utl.ist.notifcenter.domain;

/*

Facebook Messenger

IMPORTANT NOTE: Currently not working due to Facebook policy changes at February 2018. The following error message is received when trying to send a message:
"Access to the Customer Matching API and Customer Matching via the Send API is currently available in limited release. To learn more, contact your Facebook partner manager or representative."
For more information, check this link: https://developers.facebook.com/docs/messenger-platform/identity/customer-matching

Text sent must be UTF-8 and max. 2000 characters

Access token aka "Page Access Token"
https://developers.facebook.com/apps/<appId>/messenger/settings/

my app id: 298908694309495

dadosContacto = +351<phoneNumber>

Rate limits:
"Messenger Platform supports a high rate of calls to the Send API. However, you should architect your system
such that you distribute any sudden high amounts of load over time and are able to control your throughput
should you hit our rate limits."

API:
POST https://graph.facebook.com/v2.6/me/messages?access_token=%s (%s = PAGE_ACCESS_TOKEN)

NOTE: We can only send messages to users after they sent our Facebook page a message.
*/

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.fenixedu.bennu.core.domain.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.async.DeferredResult;
import pt.utl.ist.notifcenter.api.HTTPClient;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

import javax.servlet.http.HttpServletRequest;

public class Messenger extends Messenger_Base {

    static {
        final JsonObject example = new JsonObject();
        example.addProperty("access_token", "example access_token");
        CanalProvider provider = new CanalProvider(example.toString(), (config) -> new Messenger(config));
        Canal.CHANNELS.put(Messenger.class, provider);
    }

    private static String URI = "https://graph.facebook.com/v2.6/me/messages?access_token=%s";

    public Messenger(final String config) {
        super();
        this.setConfig(config);
    }

    @Override
    public void sendMessage(Mensagem msg){

        //Verifying message params length restrictions to this channel
        if (msg.createSimpleMessageNotificationWithLink().length() > 2000) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_TEXTO_LONGO_ERROR, "TextoCurto for Messenger must be at most " + (2000-59) + " characters long.");
        }

        for (Contacto contact : getContactsFromMessageRecipientUsers(msg)) {
            UserMessageDeliveryStatus edm = UserMessageDeliveryStatus.createUserMessageDeliveryStatus(msg, contact.getUtilizador(), "none_yet", "none_yet");

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Content-type", "application/json");
            String bodyContent = HTTPClient.stringToJson(createMessengerBodyPhoneNumber(msg.createSimpleMessageNotificationWithLink(), contact.getDadosContacto())).toString();

            //debug:
            //System.out.println(Utils.MAGENTA + "\n\nJson body:\n" + Utils.CYAN + bodyContent);

            String url = String.format(URI, this.getConfigAsJson().get("access_token"));
            url = url.replace("\"", ""); //remove double quotes

            DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
            deferredResult.setResultHandler((Object responseEntityOrigin) -> {

                ResponseEntity<String> responseEntity = (ResponseEntity<String>) responseEntityOrigin;

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
                    ///System.out.println("Success on sending message to user id " + contact.getUtilizador().getExternalId() + "! external id is: " + idExterno + ", and delivery status is: " + estadoEntrega);
                }
                else {
                    estadoEntrega = UtilsResource.getRequiredValueOrReturnNullInsteadRecursive(jObj.getAsJsonObject(), "error");
                    System.out.println("Failed to send message to user id " + contact.getUtilizador().getExternalId() + "! external id is: " + idExterno + ", and delivery status is: " + estadoEntrega);
                }

                edm.changeIdExternoAndEstadoEntrega(idExterno, estadoEntrega);

            });

            //send message
            HTTPClient.restASyncClient(HttpMethod.POST, url, httpHeaders, bodyContent, deferredResult);
        }
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
    public UserMessageDeliveryStatus dealWithDeliveryStatusNotifications(HttpServletRequest request) {

        MultiValueMap<String, String> requestParams = HTTPClient.getHttpServletRequestParams(request);

        //NOTE: Not tested because "A secure Callback URL (https) is required" by Facebook
        //"verify token": hub_verify_token -> set by us on https://developers.facebook.com/apps/<appId>/webhooks/
        String hub_challenge = UtilsResource.getRequiredValueFromMultiValueMapOrReturnNullInstead(requestParams, "hub_challenge");
        if (hub_challenge != null) {
            throw new NotifcenterException(ErrorsAndWarnings.SUCCESS, hub_challenge);
        }

        return null;
    }

}
