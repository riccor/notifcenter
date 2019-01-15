package pt.utl.ist.notifcenter.domain;

import com.google.gson.JsonElement;
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
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;
import pt.utl.ist.notifcenter.utils.Utils;

import java.util.Arrays;
import java.util.Collections;

@AnotacaoCanal
public class Twitter extends Twitter_Base {
    
    public Twitter() {
        super();
    }

    @Atomic
    public static Twitter createChannel(String numeroTelemovel, String idAplicacao, String chaveAplicacao) {
        Twitter twitter = new Twitter();
        twitter.setNumeroTelemovel(numeroTelemovel);
        twitter.setIdAplicacao(idAplicacao);
        twitter.setChaveAplicacao(chaveAplicacao);

        //Debug
        twitter.setEmail("Twitter-" + twitter.getExternalId() + "@notifcenter.com");

        return twitter;
    }

    @Atomic
    public Twitter updateChannel(@Nullable final String numeroTelemovel, @Nullable final String idAplicacao, @Nullable final String chaveAplicacao) {

        if (Utils.isValidString(numeroTelemovel)) {
            this.setNumeroTelemovel(numeroTelemovel);
        }

        if (Utils.isValidString(idAplicacao)) {
            this.setIdAplicacao(idAplicacao);
        }

        if (Utils.isValidString(chaveAplicacao)) {
            this.setChaveAplicacao(chaveAplicacao);
        }

        return this;
    }

    public void checkIsMessageAdequateForChannel(Mensagem msg) /*throws NotifcenterException*/ {
        if (msg.getTextoLongo().length() > 10000) {
            ///IllegalArgumentException
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_TEXTO_LONGO_ERROR, "TextoLongo must be at most 10000 characters long for " + this.getClass().getSimpleName() + " channel.");
        }
    }

/*
//https://developer.twitter.com/en/docs/direct-messages/sending-and-receiving/api-reference/new-event

sendMessage:
Requires a JSON POST body and Content-Type header to be set to application/json.
Setting Content-Length may also be required if it is not automatically.

resource url: https://api.twitter.com/1.1/direct_messages/events/new.json


Limit:
Requests / 24-hour window	1000 per user; 15000 per app

*/

    ///
    @Override
    public void sendMessage(Mensagem msg){

        checkIsMessageAdequateForChannel(msg);



        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        header.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        ///header.add("Authorization", HTTPClient.createBasicAuthString(this.getAccountSID(), this.getAuthToken()));

        body.put("To", Arrays.asList("initializing...")); ///
        /// body.put("From", Arrays.asList(this.getFromPhoneNumber()));

        String linkForMessage = " Check " + NotifcenterSpringConfiguration.getConfiguration().notifcenterUrl() + "/mensagens/" + msg.getExternalId();
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
                            ///HTTPClient.restASyncClient(HttpMethod.POST, this.getUri(), header, body, deferredResult);
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

    ///
    static void handleDeliveryStatus(ResponseEntity<String> responseEntity, EstadoDeEntregaDeMensagemEnviadaAContacto edm) {

        //Debug
        HTTPClient.printResponseEntity(responseEntity);

        JsonElement jObj = new JsonParser().parse(responseEntity.getBody());
        String idExterno = UtilsResource.getRequiredValueOrReturnNullInstead(jObj.getAsJsonObject(), "sid");
        String estadoEntrega = UtilsResource.getRequiredValueOrReturnNullInstead(jObj.getAsJsonObject(), "status");

        //EstadoDeEntregaDeMensagemEnviadaAContacto.createEstadoDeEntregaDeMensagemEnviadaAContacto(canal, msg, contacto, idExterno, estadoEntrega);
        edm.changeIdExternoAndEstadoEntrega(idExterno, estadoEntrega);

        if (responseEntity.getStatusCode() != HttpStatus.OK || responseEntity.getStatusCode() != HttpStatus.CREATED || idExterno == null || estadoEntrega == null) {
            System.out.println("Failed to send message to " + edm.getContacto().getUtilizador().getUsername() + "! sid is: " + idExterno + ", and delivery status is: " + estadoEntrega);
        }
        else {
            System.out.println("Success on sending message to " + edm.getContacto().getUtilizador().getUsername() + "! sid is: " + idExterno + ", and delivery status is: " + estadoEntrega);
        }
    }


}
