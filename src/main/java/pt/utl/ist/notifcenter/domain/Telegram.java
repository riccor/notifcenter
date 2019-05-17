package pt.utl.ist.notifcenter.domain;

/*
Telegram chat

How to get access token:
1. Refer to https://web.telegram.org/#/im?p=@BotFather
2. Write /newbot
3. Write notifcentre_bot (2x)
4. Take note of access_token

Recipients must allow notifcentre_bot to send messages to them by doing this (might not be needed):
1. Go to https://web.telegram.org/#/im?p=@notifcentre_bot
2. Write /start

How to get my telegram id (chat_id) (to use as dadosContacto)?
1. https://web.telegram.org/#/im?p=@get_id_bot
2. Write /start and /my_id
3. Take note of id

send messages to notifcentre_bot itself via chat id: 720595986

API:
POST https://api.telegram.org/bot%s/sendMessage (%s = token)
GET https://api.telegram.org/bot%s/getUpdates (this gets a list of ids of people who started a conversation with the bot)
*/

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.fenixedu.bennu.core.domain.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import pt.utl.ist.notifcenter.api.HTTPClient;
import pt.utl.ist.notifcenter.api.MainAPIResource;
import pt.utl.ist.notifcenter.api.UtilsResource;

import javax.servlet.http.HttpServletRequest;

public class Telegram extends Telegram_Base {

    static {
        final JsonObject example = new JsonObject();
        example.addProperty("access_token", "example token");
        CanalProvider provider = new CanalProvider(example.toString(), (config) -> new Telegram(config));
        Canal.CHANNELS.put(Telegram.class, provider);
    }

    private static String URI = "https://api.telegram.org/bot%s/sendMessage";

    public Telegram(final String config) {
        super();
        this.setConfig(config);
    }

    @Override
    public void sendMessage(Mensagem msg) {

        for (Contacto contact : getContactsFromMessageRecipientUsers(msg)) {
            UserMessageDeliveryStatus edm = UserMessageDeliveryStatus.createUserMessageDeliveryStatus(msg, contact.getUtilizador(), "none_yet", "none_yet");

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Content-type", "application/json");
            String bodyContent = HTTPClient.stringToJson(createTelegramBody(msg.createSimpleMessageNotificationWithLink(), contact.getDadosContacto())).toString();

            //debug:
            //System.out.println(Utils.MAGENTA + "\n\nJson body:\n" + Utils.CYAN + bodyContent);

            String url = String.format(URI, this.getConfigAsJson().get("access_token").getAsString());

            DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
            deferredResult.setResultHandler((Object responseEntityOrigin) -> {

                ResponseEntity<String> responseEntity = (ResponseEntity<String>) responseEntityOrigin;

                //Debug
                HTTPClient.printResponseEntity(responseEntity);

                /*
                    example success response:
                        {"ok":true,"result":{"message_id":15,"from":{"id":710606157,"is_bot":true,
                        "first_name":"notifcentre_bot","username":"notifcentre_bot"},"chat":{"id":720595986,
                        "first_name":"NofifCentre","type":"private"},"date":1547953154,
                        "text":"testingapi1 Check localhost:8080/notifcenter/mensagens/281681135140942"}}

                    example error response:
                        {"ok":false,"error_code":403,"description":"Forbidden: bot can't send messages to bots"}
                */

                JsonElement jObj = new JsonParser().parse(responseEntity.getBody());
                String idExterno = UtilsResource.getRequiredValueOrReturnNullInsteadRecursive(jObj.getAsJsonObject(), "message_id");
                String estadoEntrega;

                if (idExterno == null) {
                    idExterno = "null";
                }

                if (responseEntity.getStatusCode() == HttpStatus.OK || responseEntity.getStatusCode() == HttpStatus.CREATED) {
                    estadoEntrega = "Delivered";
                    ///System.out.println("Success on sending message to user id " + user.getExternalId() + "! external id is: " + idExterno + ", and delivery status is: " + estadoEntrega);
                }
                else {
                    estadoEntrega = UtilsResource.getRequiredValueOrReturnNullInsteadRecursive(jObj.getAsJsonObject(), "error_code") + " " + UtilsResource.getRequiredValueOrReturnNullInsteadRecursive(jObj.getAsJsonObject(), "description");
                    System.out.println("Failed to send message to user id " + contact.getUtilizador().getExternalId() + "! external id is: " + idExterno + ", and delivery status is: " + estadoEntrega);
                }

                edm.changeIdExternoAndEstadoEntrega(idExterno, estadoEntrega);

                MainAPIResource.notificateAppViaWebhook(edm); //Might not be called for unknown reasons.
            });

            //send message
            HTTPClient.restASyncClient(HttpMethod.POST, url, httpHeaders, bodyContent, deferredResult);
        }
    }

    public String createTelegramBody(String text, String recipient) {
        return String.format("{\"chat_id\": \"%s\", \"text\": \"%s\"}", recipient, text);
    }

    @Override
    public UserMessageDeliveryStatus dealWithDeliveryStatusNotifications(HttpServletRequest request) {

        return null;
    }

}
