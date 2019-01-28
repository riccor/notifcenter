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
import com.google.gson.JsonParser;
import org.apache.avro.reflect.Nullable;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.springframework.http.*;
import org.springframework.web.context.request.async.DeferredResult;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.notifcenter.api.HTTPClient;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;
import pt.utl.ist.notifcenter.utils.Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@AnotacaoCanal
public class Twitter extends Twitter_Base {

    public Twitter() {
        super();
    }

    @Override
    public String getUri() {
        return "https://api.twitter.com/1.1/direct_messages/events/new.json";
    }

    @Atomic
    public static Twitter createChannel(String oauth_consumer_key, String oauth_consumer_secret,
                                        String oauth_token, String oauth_token_secret/*, String uri*/) {

        Twitter twitter = new Twitter();
        twitter.setOauth_consumer_key(oauth_consumer_key);
        twitter.setOauth_consumer_secret(oauth_consumer_secret);
        twitter.setOauth_token(oauth_token);
        twitter.setOauth_token_secret(oauth_token_secret);
        /*twitter.setUri(uri);*/

        //Debug
        ///twitter.setEmail("Twitter-" + twitter.getExternalId() + "@notifcenter.com");

        return twitter;
    }

    @Atomic
    public Twitter updateChannel(@Nullable final String oauth_consumer_key, @Nullable final String oauth_consumer_secret,
                                 @Nullable final String oauth_token, @Nullable final String oauth_token_secret /*, @Nullable final String uri*/) {

        if (Utils.isValidString(oauth_consumer_key)) {
            this.setOauth_consumer_key(oauth_consumer_key);
        }

        if (Utils.isValidString(oauth_consumer_secret)) {
            this.setOauth_consumer_secret(oauth_consumer_secret);
        }

        if (Utils.isValidString(oauth_token)) {
            this.setOauth_token(oauth_token);
        }

        if (Utils.isValidString(oauth_token_secret)) {
            this.setOauth_token_secret(oauth_token_secret);
        }

        /*
        if (Utils.isValidString(uri)) {
            this.setUri(uri);
        }*/

        return this;
    }

    @Override
    public void checkIsMessageAdequateForChannel(Mensagem msg) /*throws NotifcenterException*/ {
        if (msg.createSimpleMessageNotificationWithLink().length() > 10000) {
            ///IllegalArgumentException
            //" Check localhost:8080/notifcenter/mensagens/281681135140901".length() = 59
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_TEXTO_LONGO_ERROR, "TextoCurto must be at most " + (10000-59) + " characters long.");
        }
    }

    //Note: only one recipient per message!
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
                            System.out.println("has dadosContacto " + contacto.getDadosContacto());

                            UserMessageDeliveryStatus edm = UserMessageDeliveryStatus.createUserMessageDeliveryStatus(this, msg, user, "none_yet", "none_yet");

                            HttpHeaders httpHeaders = createTwitterOAuthHeader("POST", edm.getExternalId());
                            httpHeaders.set("Content-type", "application/json");
                            String bodyContent = HTTPClient.stringToJson(createTwitterBody(msg.createSimpleMessageNotificationWithLink(), contacto.getDadosContacto())).toString();

                            //debug:
                            ///System.out.println(Utils.MAGENTA + "\n\nJson body:\n" + Utils.CYAN + bodyContent);

                            DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
                            deferredResult.setResultHandler((Object responseEntity) -> {

                                handleDeliveryStatus((ResponseEntity<String>) responseEntity, edm, user);

                            });

                            HTTPClient.restASyncClientBody(HttpMethod.POST, this.getUri(), httpHeaders, bodyContent, deferredResult);

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
            System.out.println("Success on sending message to user id " + user.getExternalId() + "! external id is: " + idExterno + ", and delivery status is: " + estadoEntrega);
        }
        else {
            estadoEntrega = UtilsResource.getRequiredValueOrReturnNullInsteadRecursive(jObj.getAsJsonObject(), "errors");
            System.out.println("Failed to send message to user id " + user.getExternalId() + "! external id is: " + idExterno + ", and delivery status is: " + estadoEntrega);
        }

        edm.changeIdExternoAndEstadoEntrega(idExterno, estadoEntrega);
    }

    @Override
    public UserMessageDeliveryStatus dealWithMessageDeliveryStatusCallback(HttpServletRequest request) {

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
        Map<String, String> map = createMap(this.getOauth_consumer_key(), this.getOauth_token(), nonce, epochO);

        String parameterString = createParameterString(map);

        String baseString = createOAuthBaseString(httpMethod, this.getUri(), parameterString);

        String signingKey = createOAuthSigningKey(this.getOauth_consumer_secret(), this.getOauth_token_secret());

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

        //WARNING1:
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
        ////System.out.println();
        ////System.out.println(Utils.MAGENTA + "Parameter String:\n" + Utils.CYAN + sb.toString() + Utils.WHITE);

        return sb.toString();
    }

    private String generateNonce() {
        return Utils.generateRandomLettersString(30);
    }

    public String createHeaderString(Map<String, String> map, String signature) {

        //WARNING2:
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

        //debug
        String b = HTTPClient.percentEncode(signature);
        ////System.out.println(Utils.MAGENTA + "HTTPClient.percentEncoded(Signature):\n" + Utils.CYAN + b + Utils.WHITE);
        ////System.out.println(Utils.MAGENTA + "Signature:\n" + Utils.CYAN + signature + Utils.WHITE);

        //debug
        /*
        if (signature.equals(twitterSignatureResultExample)) {
            ////System.out.println(GREEN + "GOOD =D SIGNATURE IS EQUAL TO TWITTER EXAMPLE!");
        }
        else {
            ////System.out.println(RED + "Signatures do not match =(");
            ////System.out.println(RED + "Signature string SHOULD BE:\n" + Utils.WHITE + twitterSignatureResultExample);
        }

        if (b.equals(twitterSignatureResultExampleENCODED)) {
            ////System.out.println(GREEN + "GOOD =D SIGNATURE ENCODED IS EQUAL TO TWITTER EXAMPLE!");
        }
        else {
            ////System.out.println(RED + "And obviously encoded signatures do not match too =(");
        }

        //debug 2
        if (sb2.toString().equals(twitteHeaderStringExample)) {
            ////System.out.println(GREEN + "GOOD =D HEADER STRING IS EQUAL TO TWITTER EXAMPLE!");
        }
        else {
            ////System.out.println(RED + "\nHeader strings do not match =(");
            ////System.out.println(RED + "Header string SHOULD BE:\n" + Utils.WHITE + twitteHeaderStringExample);
        }*/

        //debug
        ////System.out.println();
        ////System.out.println(Utils.MAGENTA + "Header String:\n" + Utils.CYAN + sb2.toString() + Utils.WHITE);

        return sb2.toString();
    }



    public String createOAuthBaseString(String httpMethod, String url, String parameterString) {
        StringBuilder sb = new StringBuilder(httpMethod.toUpperCase());
        sb.append("&");
        sb.append(HTTPClient.percentEncode(url));
        sb.append("&");
        sb.append(HTTPClient.percentEncode(parameterString));

        //debug
        ////System.out.println();
        ////System.out.println(Utils.MAGENTA + "Signature Base String:\n" + Utils.CYAN + sb.toString() + Utils.WHITE);

        /* uncomment this for the example case from https://developer.twitter.com/en/docs/basics/authentication/guides/creating-a-signature.html
        if (sb.toString().equals(twitterBaseStringExample)) {
            ////System.out.println(GREEN + "GOOD =D BASE STRING IS EQUAL TO TWITTER EXAMPLE!");
        }
        else {
            ////System.out.println(RED + "Signatures do not match =(");
        }*/

        return sb.toString();
    }

    public String createOAuthSigningKey(String oauth_consumer_secret, String oauth_token_secret) {
        StringBuilder sb = new StringBuilder(HTTPClient.percentEncode(oauth_consumer_secret));
        sb.append("&");
        sb.append(HTTPClient.percentEncode(HTTPClient.percentEncode(oauth_token_secret)));

        //debug
        //////System.out.println(">>>>>signingKey: " + sb.toString());

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

        //////System.out.println(">>>>>>>>>signature: " + bytesToString(signature));

        String signatureBase64 = HTTPClient.bytesBase64Encode(signature);

        //////System.out.println(Utils.WHITE + ">>>>>>>>>signature_Base64: " + signatureBase64);

        return signatureBase64;
    }


}


//example response:

    /*
     body:{
       "event":{
          "type":"message_create",
          "id":"1086130419588845574",
          "created_timestamp":"1547788646014",
          "message_create":{
             "target":{
                "recipient_id":"1085250046176702466"
             },
             "sender_id":"1085250046176702466",
             "message_data":{
                "text":"hello??",
                "entities":{
                   "hashtags":[

                   ],
                   "symbols":[

                   ],
                   "user_mentions":[

                   ],
                   "urls":[

                   ]
                }
             }
          }
       }
    }
    */

    /*

Known response errors:
{"errors":[{"code":150,"message":"You cannot send messages to users who are not following you."}]}
{"errors":[{"code":108,"message":"Cannot find specified user."}]}


request:
    twurl authorize --consumer-key .... --consumer-secret ...

response:
    https://api.twitter.com/oauth/authorize?
    oauth_consumer_key=XI1Kkm9b
    &oauth_nonce=gJm8Bf7sRScSUK
    &oauth_signature=5Kddnbgx9DmC
    &oauth_signature_method=HMAC-SHA1
    &oauth_timestamp=1547769472
    &oauth_token=nWioT6g
    &oauth_version=1.0


request:
     twurl -A 'Content-type: application/json' -X POST /1.1/direct_messages/events/new.json
     -d '{"event": {"type": "message_create", "message_create": {"target": {"recipient_id": "1085250046176702466"}, "message_data": {"text": "Hello World!"}}}}'

response:
    {
       "event":{
          "type":"message_create",
          "id":"1086052839703629829",
          "created_timestamp":"1547770149528",
          "message_create":{
             "target":{
                "recipient_id":"1085250046176702466"
             },
             "sender_id":"1085250046176702466",
             "message_data":{
                "text":"Hello World!",
                "entities":{
                   "hashtags":[

                   ],
                   "symbols":[

                   ],
                   "user_mentions":[

                   ],
                   "urls":[

                   ]
                }
             }
          }
       }
    }

    */


/*
    //Values taken from https://developer.twitter.com/en/docs/basics/authentication/guides/creating-a-signature.html :
    private final static String urlA = "https://api.twitter.com/1.1/statuses/update.json";
    private final static String postMethodA = "POST";
    private final static String oauth_consumer_keyA = "xvz1evFS4wEEPTGEFPHBog";
    private final static String oauth_tokenA = "370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb";
    private final static String oauth_consumer_secretA = "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw";
    private final static String oauth_token_secretA = "LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE";
    private final static String nonceA = "kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg";
    private final static String epochA = "1318622958";

    //results from Twitter example to compare with my results:
    private final static String twitterSignatureResultExample = "hCtSmYh+iHYCEqBWrE7C7hYmtUk=";
    private final static String twitterBaseStringExample = "POST&https%3A%2F%2Fapi.twitter.com%2F1.1%2Fstatuses%2Fupdate.json&include_entities%3Dtrue%26oauth_consumer_key%3Dxvz1evFS4wEEPTGEFPHBog%26oauth_nonce%3DkYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1318622958%26oauth_token%3D370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb%26oauth_version%3D1.0%26status%3DHello%2520Ladies%2520%252B%2520Gentlemen%252C%2520a%2520signed%2520OAuth%2520request%2521";
*/

/*
    //Values taken from https://developer.twitter.com/en/docs/basics/authentication/guides/authorizing-a-request :
    private final static String urlA = "https://api.twitter.com/1.1/statuses/update.json";
    private final static String postMethodA = "POST";
    private final static String oauth_consumer_keyA = "xvz1evFS4wEEPTGEFPHBog";
    private final static String oauth_tokenA = "370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb";
    private final static String nonceA = "kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg";
    private final static String epochA = "1318622958";

    //WARNING: assumed values, because these values are not presented in https://developer.twitter.com/en/docs/basics/authentication/guides/authorizing-a-request
    private final static String oauth_consumer_secretA = "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw";
    private final static String oauth_token_secretA = "LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE";

    //results from Twitter example to compare with my results:
    private final static String twitterSignatureResultExample = "tnnArxj06cWHq44gCs1OSKk/jLY=";
    private final static String twitterSignatureResultExampleENCODED = "tnnArxj06cWHq44gCs1OSKk%2FjLY%3D";
    private final static String twitteHeaderStringExample = "OAuth oauth_consumer_key=\"xvz1evFS4wEEPTGEFPHBog\", oauth_nonce=\"kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg\", oauth_signature=\"tnnArxj06cWHq44gCs1OSKk%2FjLY%3D\", oauth_signature_method=\"HMAC-SHA1\", oauth_timestamp=\"1318622958\", oauth_token=\"370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb\", oauth_version=\"1.0\"";

*/