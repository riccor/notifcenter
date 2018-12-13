package pt.utl.ist.notifcenter.domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.async.DeferredResult;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.notifcenter.api.HTTPClient;
import pt.utl.ist.notifcenter.utils.Utils;

import java.util.*;

public class TwilioWhatsapp extends TwilioWhatsapp_Base {

    private TwilioWhatsapp() {
        super();
        //this.setSistemaNotificacoes(SistemaNotificacoes.getInstance());
    }

    @Atomic
    public static TwilioWhatsapp createTwilioWhatsApp(final String accountSID, final String authToken, final String fromPhoneNumber, final String uri) {
        TwilioWhatsapp twilioWhatsapp = new TwilioWhatsapp();
        twilioWhatsapp.setAccountSID(accountSID);
        twilioWhatsapp.setAuthToken(authToken);
        twilioWhatsapp.setFromPhoneNumber(fromPhoneNumber);
        twilioWhatsapp.setUri(uri);

        //Debug
        twilioWhatsapp.setEmail("twiliowhatsapp@notifcenter.com");

        return twilioWhatsapp;
    }

    public static TwilioWhatsapp createTwilioWhatsappFromPropertiesFile(final String file) {
        String filename = String.format(NotifcenterSpringConfiguration.getConfiguration().notifcenterChannelsCredentials(), file);
        Map<String, String> propertiesMap = Utils.loadPropertiesFromPropertiesFile(TwilioWhatsapp.class, filename, "accountSID", "authToken", "fromPhoneNumber", "uri");

        if (!Utils.isMapFilled(propertiesMap)) {
            System.out.println("Error: Cannot create entity from file.");
            return null;
        }

        return createTwilioWhatsApp(propertiesMap.get("accountSID"), propertiesMap.get("authToken"), propertiesMap.get("fromPhoneNumber"), propertiesMap.get("uri"));
    }

    /*
    public static adaptMessageToChannel(Mensagem mensagem) {

    }
    */

    @Override
    public void sendMessage(Mensagem msg){

        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        header.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        header.add("Authorization", HTTPClient.createBasicAuthString(this.getAccountSID(), this.getAuthToken()));

        body.put("To", Arrays.asList("initialized")); ///
        body.put("From", Arrays.asList(this.getFromPhoneNumber()));
        body.put("Body", Arrays.asList(msg.getTextoCurto()));

        for (PersistentGroup group : msg.getGruposDestinatariosSet()) {
            group.getMembers().forEach(user -> {

                //Debug
                System.out.println("LOG: user: " + user.getUsername() + " with email: " + user.getEmail());

                boolean userHasNoContactForThisChannel = true;

                for (Contacto contacto : user.getContactosSet()) {

                    if (contacto.getCanal().getExternalId().equals(this.getExternalId())) {
                        //responseEntities.add(tw.sendMessage(contacto.getDadosContacto(), msg.getTextoCurto()));

                        //Debug
                        System.out.println("has dadosContacto " + contacto.getDadosContacto());

                        body.remove("To");
                        body.put("To", Collections.singletonList(contacto.getDadosContacto()));

                        DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
                        deferredResult.setResultHandler((Object responseEntity) -> {

                            handleDeliveryStatus((ResponseEntity<String>) responseEntity, this, msg, contacto);

                        });

                        //HTTPClient.restSyncClient(HttpMethod.POST, this.getUri(), header, body);
                        HTTPClient.restASyncClient(HttpMethod.POST, this.getUri(), header, body, deferredResult);

                        userHasNoContactForThisChannel = false;

                        break; //no need to search more contacts for this user on this channel.
                    }
                }

                if (userHasNoContactForThisChannel) {
                    System.out.println("WARNING: user " + user.getUsername() + " has no contact for " + this.getClass().getSimpleName());
                }

            });
        }
    }

    static void handleDeliveryStatus(ResponseEntity<String> responseEntity, Canal canal, Mensagem msg, Contacto contacto) {

        //Debug
        HTTPClient.printResponseEntity(responseEntity);

        JsonElement jObj = new JsonParser().parse(responseEntity.getBody());
        String idExterno = getRequiredValue(jObj.getAsJsonObject(), "sid");
        String estadoEntrega = getRequiredValue(jObj.getAsJsonObject(), "status");

        EstadoDeEntregaDeMensagemEnviadaAContacto.createEstadoDeEntregaDeMensagemEnviadaAContacto(canal, msg, contacto, idExterno, estadoEntrega);

        if (responseEntity.getStatusCode() != HttpStatus.OK || responseEntity.getStatusCode() != HttpStatus.CREATED || idExterno == null || estadoEntrega == null) {
            System.out.println("Failed to send message to " + contacto.getUtilizador().getUsername() + "! sid is: " + idExterno + ", and delivery status is: " + estadoEntrega);
        }
        else {
            System.out.println("Success on sending message to " + contacto.getUtilizador().getUsername() + "! sid is: " + idExterno + ", and delivery status is: " + estadoEntrega);
        }
    }

    private static String getRequiredValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            return obj.get(property).getAsString();
        }
        return null;
    }

    /*OLD
    public ResponseEntity<String> sendMessage(final String to, final String message){

        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        ///HttpHeaders header = HTTPClient.createBasicAuthHeader(this.getAccountSID(), this.getAuthToken());
        ///header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        header.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        header.add("Authorization", HTTPClient.createBasicAuthString(this.getAccountSID(), this.getAuthToken()));

        body.put("To", Arrays.asList(to));
        body.put("From", Arrays.asList(this.getFromPhoneNumber()));
        body.put("Body", Arrays.asList(message));

        return HTTPClient.restSyncClient(HttpMethod.POST, this.getUri(), header, body);
    }
    */

}
