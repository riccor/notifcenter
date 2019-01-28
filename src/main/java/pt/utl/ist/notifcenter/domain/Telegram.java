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
import com.google.gson.JsonParser;
import org.apache.avro.reflect.Nullable;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.notifcenter.api.HTTPClient;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;
import pt.utl.ist.notifcenter.utils.Utils;

import javax.servlet.http.HttpServletRequest;

@AnotacaoCanal
public class Telegram extends Telegram_Base {
    
    public Telegram() {
        super();
    }

    @Override
    public String getUri() {
        return "https://api.telegram.org/bot%s/sendMessage";
    }

    @Atomic
    public static Telegram createChannel(String access_token/*, String uri*/) {

        Telegram telegram = new Telegram();
        telegram.setAccess_token(access_token);
        //telegram.setUri(uri);

        //Debug
        ///telegram.setEmail("Telegram-" + telegram.getExternalId() + "@notifcenter.com");

        return telegram;
    }

    @Atomic
    public Telegram updateChannel(@Nullable final String access_token/*, @Nullable final String uri*/) {

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
                            String bodyContent = HTTPClient.stringToJson(createTelegramBody(msg.createSimpleMessageNotificationWithLink(), contacto.getDadosContacto())).toString();

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
            System.out.println("Success on sending message to user id " + user.getExternalId() + "! external id is: " + idExterno + ", and delivery status is: " + estadoEntrega);
        }
        else {
            estadoEntrega = UtilsResource.getRequiredValueOrReturnNullInsteadRecursive(jObj.getAsJsonObject(), "error_code") + " " + UtilsResource.getRequiredValueOrReturnNullInsteadRecursive(jObj.getAsJsonObject(), "description");
            System.out.println("Failed to send message to user id " + user.getExternalId() + "! external id is: " + idExterno + ", and delivery status is: " + estadoEntrega);
        }

        edm.changeIdExternoAndEstadoEntrega(idExterno, estadoEntrega);
    }

    public String createTelegramBody(String text, String recipient) {
        return String.format("{\"chat_id\": \"%s\", \"text\": \"%s\"}", recipient, text);
    }

    @Override
    public UserMessageDeliveryStatus dealWithMessageDeliveryStatusCallback(HttpServletRequest request) {

        return null;
    }

}
