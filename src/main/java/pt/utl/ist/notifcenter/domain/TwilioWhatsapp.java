package pt.utl.ist.notifcenter.domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.avro.reflect.Nullable;
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

@AnotacaoCanal(classFields = {"accountSID", "authToken", "fromPhoneNumber", "uri"})
public class TwilioWhatsapp extends TwilioWhatsapp_Base {

    private TwilioWhatsapp() {
        super();
        //this.setSistemaNotificacoes(SistemaNotificacoes.getInstance());
    }

    @Atomic
    public static TwilioWhatsapp createChannel(final String accountSID, final String authToken, final String fromPhoneNumber, final String uri) {
        TwilioWhatsapp twilioWhatsapp = new TwilioWhatsapp();
        twilioWhatsapp.setAccountSID(accountSID);
        twilioWhatsapp.setAuthToken(authToken);
        twilioWhatsapp.setFromPhoneNumber(fromPhoneNumber);
        twilioWhatsapp.setUri(uri);

        //Debug
        twilioWhatsapp.setEmail("twiliowhatsapp-" + twilioWhatsapp.getExternalId() + "@notifcenter.com");

        return twilioWhatsapp;
    }

    @Atomic
    public TwilioWhatsapp updateChannel(@Nullable final String accountSID, @Nullable final String authToken, @Nullable final String fromPhoneNumber, @Nullable final String uri) {

        if (isValidString(accountSID)) {
            this.setAccountSID(accountSID);
        }

        if (isValidString(authToken)) {
            this.setAuthToken(authToken);
        }

        if (isValidString(fromPhoneNumber)) {
            this.setFromPhoneNumber(fromPhoneNumber);
        }

        if (isValidString(uri)) {
            this.setUri(uri);
        }

        return this;
    }

    public boolean isValidString(@Nullable String str) {
        return (str != null && !str.isEmpty());
    }

    public static TwilioWhatsapp createTwilioWhatsappFromPropertiesFile(final String file) {
        String filename = String.format(NotifcenterSpringConfiguration.getConfiguration().notifcenterChannelsCredentials(), file);
        Map<String, String> propertiesMap = Utils.loadPropertiesFromPropertiesFile(TwilioWhatsapp.class, filename, "accountSID", "authToken", "fromPhoneNumber", "uri");

        if (!Utils.isMapFilled(propertiesMap)) {
            System.out.println("Error: Cannot create entity from file.");
            return null;
        }

        return createChannel/*TwilioWhatsApp*/(propertiesMap.get("accountSID"), propertiesMap.get("authToken"), propertiesMap.get("fromPhoneNumber"), propertiesMap.get("uri"));
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

        body.put("To", Arrays.asList("initializing...")); ///
        body.put("From", Arrays.asList(this.getFromPhoneNumber()));

        String linkForMessage = " Check " + NotifcenterSpringConfiguration.getConfiguration().notifcenterUrl() + "/notifcenter/" + msg.getExternalId() + "/view";
        String message = msg.getTextoCurto() + linkForMessage;
        body.put("Body", Arrays.asList(message));

        for (PersistentGroup group : msg.getGruposDestinatariosSet()) {
            group.getMembers().forEach(user -> {

                //Debug
                System.out.println("LOG: user: " + user.getUsername() + " with email: " + user.getEmail());

                boolean userHasNoContactForThisChannel = true;

                for (Contacto contacto : user.getContactosSet()) {

                    if (contacto.getCanal().getExternalId().equals(this.getExternalId())) {

                        //Debug
                        System.out.println("has dadosContacto " + contacto.getDadosContacto());

                        //impedir que a mesma mensagem seja enviada duas vezes para o mesmo destinatário:
                        if (contacto.getEstadoDeEntregaDeMensagemEnviadaAContactoSet().stream().anyMatch(e -> e.getMensagem().getExternalId().equals(msg.getExternalId()))) {
                            System.out.println("DEBUG: Prevented duplicated message for user " + user.getUsername());
                        }
                        else {
                            //responseEntities.add(tw.sendMessage(contacto.getDadosContacto(), msg.getTextoCurto()));
                            body.remove("To");
                            body.put("To", Collections.singletonList(contacto.getDadosContacto()));

                            EstadoDeEntregaDeMensagemEnviadaAContacto edm = EstadoDeEntregaDeMensagemEnviadaAContacto.createEstadoDeEntregaDeMensagemEnviadaAContacto(this, msg, contacto, "none_yet", "none_yet");

                            DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
                            deferredResult.setResultHandler((Object responseEntity) -> {

                                //handleDeliveryStatus((ResponseEntity<String>) responseEntity, this, msg, contacto);
                                handleDeliveryStatus((ResponseEntity<String>) responseEntity, edm);

                            });

                            //HTTPClient.restSyncClient(HttpMethod.POST, this.getUri(), header, body);
                            HTTPClient.restASyncClient(HttpMethod.POST, this.getUri(), header, body, deferredResult);
                        }

                        userHasNoContactForThisChannel = false;

                        break; //no need to search more contacts for this user on this channel.
                    }
                }

                if (userHasNoContactForThisChannel) {
                    System.out.println("WARNING: user " + user.getUsername() + " has no contact for " + this.getClass().getSimpleName());

                    //TODO: EstadoDeEntregaDeMensagemEnviadaAContacto -> EstadoDeEntregaDeMensagemEnviadaAUtilizador
                    //EstadoDeEntregaDeMensagemEnviadaAContacto.createEstadoDeEntregaDeMensagemEnviadaAContacto(this, msg, contacto, "none", "failed:no_channel_contact_data");
                }

            });
        }
    }

    static void handleDeliveryStatus(ResponseEntity<String> responseEntity, EstadoDeEntregaDeMensagemEnviadaAContacto edm) {

        //Debug
        HTTPClient.printResponseEntity(responseEntity);

        JsonElement jObj = new JsonParser().parse(responseEntity.getBody());
        String idExterno = getRequiredValueOrReturnNullInstead(jObj.getAsJsonObject(), "sid");
        String estadoEntrega = getRequiredValueOrReturnNullInstead(jObj.getAsJsonObject(), "status");

        //EstadoDeEntregaDeMensagemEnviadaAContacto.createEstadoDeEntregaDeMensagemEnviadaAContacto(canal, msg, contacto, idExterno, estadoEntrega);
        edm.changeIdExternoAndEstadoEntrega(idExterno, estadoEntrega);

        if (responseEntity.getStatusCode() != HttpStatus.OK || responseEntity.getStatusCode() != HttpStatus.CREATED || idExterno == null || estadoEntrega == null) {
            System.out.println("Failed to send message to " + edm.getContacto().getUtilizador().getUsername() + "! sid is: " + idExterno + ", and delivery status is: " + estadoEntrega);
        }
        else {
            System.out.println("Success on sending message to " + edm.getContacto().getUtilizador().getUsername() + "! sid is: " + idExterno + ", and delivery status is: " + estadoEntrega);
        }
    }

    private static String getRequiredValueOrReturnNullInstead(JsonObject obj, String property) {
        if (obj.has(property)) {
            if (!obj.get(property).getAsString().isEmpty()) {
                return obj.get(property).getAsString();
            }
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
