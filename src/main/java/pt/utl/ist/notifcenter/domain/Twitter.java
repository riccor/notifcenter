package pt.utl.ist.notifcenter.domain;

/*
Twitter Direct Messages: https://developer.twitter.com/en/docs/direct-messages/sending-and-receiving/api-reference/new-event

Using Twitter Oauth legacy authentication

Get twitter user id through username (dadosContacto): http://gettwitterid.com/

my twitter account id: 1085250046176702466

Limit:
Requests / 24-hour window	1000 per user; 15000 per app
*/

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.fenixedu.bennu.core.domain.User;
import org.springframework.http.*;
import org.springframework.web.context.request.async.DeferredResult;
import pt.utl.ist.notifcenter.api.HTTPClient;
import pt.utl.ist.notifcenter.api.MainAPIResource;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;
import pt.utl.ist.notifcenter.utils.Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class Twitter extends Twitter_Base {

    static {
        final JsonObject example = new JsonObject();
        example.addProperty("oauth_consumer_key", "example oauth_consumer_key");
        example.addProperty("oauth_consumer_secret", "example oauth_token_secret");
        example.addProperty("oauth_token", "example oauth_token");
        example.addProperty("oauth_token_secret", "example oauth_token_secret");
        CanalProvider provider = new CanalProvider(example.toString(), (config) -> new Twitter(config));
        Canal.CHANNELS.put(Twitter.class, provider);
    }

    private static String URI = "https://api.twitter.com/1.1/direct_messages/events/new.json";

    public Twitter(final String config) {
        super();
        this.setConfig(config);
    }


    //Note: only one recipient per message!
    @Override
    public void sendMessage(Mensagem msg){

        //Verifying message params length restrictions to this channel
        if (msg.createSimpleMessageNotificationWithLink().length() > 10000) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_TEXTO_LONGO_ERROR, "TextoCurto for Twitter must be at most " + (10000-59) + " characters long.");
        }

        for (Contacto contact : getContactsFromMessageRecipientUsers(msg)) {

            UserMessageDeliveryStatus edm = UserMessageDeliveryStatus.createUserMessageDeliveryStatus(msg, contact.getUtilizador(), "none_yet", "none_yet");

            HttpHeaders httpHeaders = createTwitterOAuthHeader("POST", edm.getExternalId());
            httpHeaders.set("Content-type", "application/json");
            String bodyContent = HTTPClient.stringToJson(createTwitterBody(msg.createSimpleMessageNotificationWithLink(), contact.getDadosContacto())).toString();

            //debug:
            ///System.out.println(Utils.MAGENTA + "\n\nJson body:\n" + Utils.CYAN + bodyContent);

            DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
            deferredResult.setResultHandler((Object responseEntityOrigin) -> {

                ResponseEntity<String> responseEntity = (ResponseEntity<String>) responseEntityOrigin;

                //Debug
                HTTPClient.printResponseEntity(responseEntity);

                /*

                Twitter response body:
                body: {"event":{"type":"message_create","id":"1086481292764606471","created_timestamp":"1547872300698",
                "message_create":{"target":{"recipient_id":"1085250046176702466"},"sender_id":"1085250046176702466",
                "message_data":{"text":"3anexosmensagem Check localhost:8080\/notifcenter\/mensagens\/281681135140903",
                "entities":{"hashtags":[],"symbols":[],"user_mentions":[],"urls":[]}}}}}

                */

                /*
                Known response errors:
                {"errors":[{"code":150,"message":"You cannot send messages to users who are not following you."}]}
                {"errors":[{"code":108,"message":"Cannot find specified user."}]}
                */

                JsonElement jObj = new JsonParser().parse(responseEntity.getBody());
                String idExterno = UtilsResource.getRequiredValueOrReturnNullInsteadRecursive(jObj.getAsJsonObject(), "id");
                String estadoEntrega;

                if (idExterno == null) {
                    idExterno = "null";
                }

                if (responseEntity.getStatusCode() == HttpStatus.OK || responseEntity.getStatusCode() == HttpStatus.CREATED) {
                    estadoEntrega = "Delivered";
                    ///System.out.println("Success on sending message to user id " + user.getExternalId() + "! external id is: " + idExterno + ", and delivery status is: " + estadoEntrega);
                }
                else {
                    estadoEntrega = UtilsResource.getRequiredValueOrReturnNullInsteadRecursive(jObj.getAsJsonObject(), "errors");
                    System.out.println("Failed to send message to user id " + contact.getUtilizador().getExternalId() + "! external id is: " + idExterno + ", and delivery status is: " + estadoEntrega);
                }

                edm.changeIdExternoAndEstadoEntrega(idExterno, estadoEntrega);

                MainAPIResource.notificateAppViaWebhook(edm);
            });

            //send message
            HTTPClient.restASyncClient(HttpMethod.POST, URI, httpHeaders, bodyContent, deferredResult);
        }
    }

    @Override
    public UserMessageDeliveryStatus dealWithDeliveryStatusNotifications(HttpServletRequest request) {

        return null;
    }

    public String createTwitterBody(String text, String recipient) {
        return String.format("{\"event\": {\"type\": \"message_create\", \"message_create\": {\"target\": {\"recipient_id\": \"%s\"}, \"message_data\": {\"text\": \"%s\"}}}}", recipient, text);
    }

    //https://developer.twitter.com/en/docs/basics/authentication/guides/creating-a-signature.html
    public HttpHeaders createTwitterOAuthHeader(String httpMethod, String nonce) {

        String epochO = Utils.getCurrentEpochTimeAsString();
        //String nonce = generateNonce(); //I am using external id from UserMessageDeliveryStatus

        //Map<String, String> map = createMap(oauth_consumer_keyA, oauth_tokenA, nonceA, epochA);
        Map<String, String> map = createMap(this.getConfigAsJson().get("oauth_consumer_key").getAsString(), this.getConfigAsJson().get("oauth_token").getAsString(), nonce, epochO);

        String parameterString = createParameterString(map);

        String baseString = createOAuthBaseString(httpMethod, URI, parameterString);

        String signingKey = createOAuthSigningKey(this.getConfigAsJson().get("oauth_consumer_secret").getAsString(), this.getConfigAsJson().get("oauth_token_secret").getAsString());

        String signatureBase64 = createSignatureBase64(baseString, signingKey);

        String headerString = createHeaderString(map, signatureBase64);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("authorization", headerString);

        return httpHeaders;
    }

    public Map<String, String> createMap(String oauth_consumer_key, String oauth_token, String nonce, String epochTime) {
        Map<String, String> map = new TreeMap<>(new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.toLowerCase().compareTo(o2.toLowerCase());
            }
        });

        //NOTE: Omit the json body from the signature!!
        map.put(HTTPClient.percentEncode("oauth_consumer_key"), HTTPClient.percentEncode(oauth_consumer_key));
        map.put(HTTPClient.percentEncode("oauth_nonce"), HTTPClient.percentEncode(nonce));
        map.put(HTTPClient.percentEncode("oauth_signature_method"), HTTPClient.percentEncode("HMAC-SHA1"));
        map.put(HTTPClient.percentEncode("oauth_timestamp"), HTTPClient.percentEncode(epochTime));
        map.put(HTTPClient.percentEncode("oauth_token"), HTTPClient.percentEncode(oauth_token));
        map.put(HTTPClient.percentEncode("oauth_version"), HTTPClient.percentEncode("1.0"));

        //RECREATING TWITTER EXAMPLE:
        //map.put(HTTPClient.percentEncode("status"), HTTPClient.percentEncode("Hello Ladies + Gentlemen, a signed OAuth request!"));
        //map.put(HTTPClient.percentEncode("include_entities"), HTTPClient.percentEncode("true"));

        return map;
    }

    public String createParameterString(Map<String, String> map) {

        //debug:
        ///Utils.printMap(map, "MAP that originates Parameter String:");

        StringBuilder sb = new StringBuilder("");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String a;
            if (sb.toString().equals("")) {
                a = entry.getKey() + "=" + entry.getValue();
            } else {
                a = "&" + entry.getKey() + "=" + entry.getValue();
            }
            sb.append(a);
        }

        //debug
        //System.out.println(Utils.MAGENTA + "Parameter String:\n" + Utils.CYAN + sb.toString() + Utils.WHITE);

        return sb.toString();
    }

    private String generateNonce() {
        return Utils.generateRandomLettersString(30);
    }

    public String createHeaderString(Map<String, String> map, String signature) {

        //RECREATING TWITTER EXAMPLE:
        //map.remove("status");
        //map.remove("include_entities");

        //add signature to map:
        map.put(HTTPClient.percentEncode("oauth_signature"), HTTPClient.percentEncode(signature));

        //debug:
        ///Utils.printMap(map, "MAP that originates Header String:");

        StringBuilder sb2 = new StringBuilder("Oauth ");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String a;
            if(sb2.toString().equals("Oauth ")) {
                a = entry.getKey() + "=\"" + entry.getValue() + "\"";
            }
            else {
                a = ", " + entry.getKey() + "=\"" + entry.getValue() + "\"";
            }
            sb2.append(a);
        }

        return sb2.toString();
    }



    public String createOAuthBaseString(String httpMethod, String url, String parameterString) {
        StringBuilder sb = new StringBuilder(httpMethod.toUpperCase());
        sb.append("&");
        sb.append(HTTPClient.percentEncode(url));
        sb.append("&");
        sb.append(HTTPClient.percentEncode(parameterString));

        /* uncomment this for the example case from https://developer.twitter.com/en/docs/basics/authentication/guides/creating-a-signature.html
        if (sb.toString().equals(twitterBaseStringExample)) {
            System.out.println(GREEN + "GOOD =D BASE STRING IS EQUAL TO TWITTER EXAMPLE!");
        }
        else {
            System.out.println(RED + "Signatures do not match =(");
        }*/

        return sb.toString();
    }

    public String createOAuthSigningKey(String oauth_consumer_secret, String oauth_token_secret) {
        StringBuilder sb = new StringBuilder(HTTPClient.percentEncode(oauth_consumer_secret));
        sb.append("&");
        sb.append(HTTPClient.percentEncode(HTTPClient.percentEncode(oauth_token_secret)));

        //debug
        //System.out.println(">>>>>signingKey: " + sb.toString());

        return sb.toString();
    }

    public String createSignatureBase64(String baseString, String signingKey) {

        byte[] signature = null;

        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec spec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
            mac.init(spec);
            signature = mac.doFinal(baseString.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

        String signatureBase64 = HTTPClient.bytesBase64Encode(signature);

        return signatureBase64;
    }

}
