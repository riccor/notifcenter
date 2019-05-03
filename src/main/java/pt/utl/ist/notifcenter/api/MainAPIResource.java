/*



200 - SUCESS
404 - RESOURCE NOT FOUND
400 - BAD REQUEST
201 - CREATED
401 - UNAUTHORIZED
415 - UNSUPPORTED TYPE - Representation not supported for the resource
500 - SERVER ERROR




    /addaplicacao
    /listaplicacoes/


    /{app}
    /{app}/update
    /{app}/delete

    /{app}/addremetente
    /{app}/listremetentes
    /{app}/{remetente}
    /{app}/{remetente}/update
    /{app}/{remetente}/delete
    /{app}/{remetente}/addgrupodestinario
    /{app}/{remetente}/removegrupodestinario
    /{app}/{remetente}/listgruposdestinatarios

    /{app}/{remetente}/pedidocanalnotificacao
    /{app}/{remetente}/{canalnotificacao}/delete
    /{app}/{remetente}/listcanaisnotificacao

    /{app}/sendmensagem
    /{app}/listmensagens
    /{app}/{msg}/listattachments
    /{app}/attachments/{fileId}
    /attachments/{fileId}

    NOTE: Some resources are just for debugging purposes and must be deleted before implementing this module in production environment.
*/

package pt.utl.ist.notifcenter.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.DynamicGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.rest.BennuRestResource;

import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.io.domain.FileStorage;
import org.fenixedu.bennu.io.domain.GenericFile;
import org.fenixedu.bennu.io.servlet.FileDownloadServlet;
import org.fenixedu.bennu.oauth.annotation.OAuthEndpoint;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;

import org.fenixedu.bennu.spring.security.CSRFTokenRepository;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.api.json.*;

import pt.utl.ist.notifcenter.domain.*;
import pt.utl.ist.notifcenter.security.SkipAccessTokenValidation;
import pt.utl.ist.notifcenter.ui.NotifcenterController;

import org.fenixedu.bennu.core.domain.User;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

import javax.naming.SizeLimitExceededException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/apiv1")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.api.main")
public class MainAPIResource extends BennuRestResource {

    @RequestMapping(value = "/channels",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement getChannelsList() {

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (Canal c : SistemaNotificacoes.getInstance().getCanaisSet()) {
            jArray.add(view(c, CanalAdapter.class));
        }

        jObj.add("channels", jArray);
        return jObj;
    }

    @SkipCSRF //Used due to a incompatibility issue with Spring that is making server to reject POST and DELETE requests (/bennu-5.2.1/bennu-spring/src/main/java/org/fenixedu/bennu/spring/security)
    @RequestMapping(value = "/channels/{channelId}/messagedeliverystatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement messageDeliveryStatusUpdate(@PathVariable("channelId") Canal canal, HttpServletRequest request) {
        //Received content might not be JSON, so we do not use "@RequestBody JsonElement body"

        if (!FenixFramework.isDomainObjectValid(canal)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CHANNEL_ERROR);
        }

        //Debug
        System.out.println("Got new message delivery status message!!");
        System.out.println(HTTPClient.getHttpServletRequestParamsAsJson(request).toString());

        UserMessageDeliveryStatus ede = canal.dealWithMessageDeliveryStatusNotificationsFromChannel(request);

        if (ede == null) {
            throw new NotifcenterException(ErrorsAndWarnings.UNKNOWN_MESSAGE_ID);
        }
        else {

            //If message parameter callbackUrlEstadoEntrega is not "none", then send message delivery status to the application
            if (!ede.getMensagem().getCallbackUrlEstadoEntrega().equals("none")) {

                MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
                MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

                header.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                body.put("MessageId", Collections.singletonList(ede.getMensagem().getExternalId()));
                body.put("User", Collections.singletonList(ede.getUtilizador().getUsername()));
                body.put("MessageStatus", Collections.singletonList(ede.getEstadoEntrega()));

                DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
                deferredResult.setResultHandler((Object responseEntity) -> {
                    HTTPClient.printResponseEntity((ResponseEntity<String>) responseEntity);
                });

                HTTPClient.restASyncClient(HttpMethod.POST, ede.getMensagem().getCallbackUrlEstadoEntrega(), header, body, deferredResult);
            }

            throw new NotifcenterException(ErrorsAndWarnings.SUCCESS_THANKS);
        }
    }

    @RequestMapping(value = "/applications/{applicationId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement viewApplication(@PathVariable("applicationId") Aplicacao app) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.NONE)) {
            throw new NotifcenterException(ErrorsAndWarnings.BLOCKED_APP_ERROR);
        }

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (Remetente r : app.getRemetentesSet()) {
            jArray.add(r.getExternalId());
        }

        jObj.addProperty("applicationId", app.getExternalId());
        jObj.add("senders", jArray);

        return view(app, AplicacaoAdapter.class);
    }

    @RequestMapping(value = "/applications/{applicationId}/senders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement getSendersList(@PathVariable("applicationId") Aplicacao app) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.NONE)) {
            throw new NotifcenterException(ErrorsAndWarnings.BLOCKED_APP_ERROR);
        }

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (Remetente r : app.getRemetentesSet()) {
            jArray.add(view(r, RemetenteAdapter.class));
        }

        jObj.addProperty("applicationId", app.getExternalId());
        jObj.add("senders", jArray);

        return jObj;
    }

    @SkipCSRF
    @RequestMapping(value = "/applications/{applicationId}/senders", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement addSender(@PathVariable("applicationId") Aplicacao app, @RequestBody JsonElement body) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.NONE)) {
            throw new NotifcenterException(ErrorsAndWarnings.BLOCKED_APP_ERROR);
        }

        JsonObject jObj = body.getAsJsonObject();
        UtilsResource.deletePropertyFromJsonObject(jObj, "app"); //avoid hacks
        jObj.addProperty("app", app.getExternalId());

        Remetente r = create(jObj, Remetente.class);

        if (app.getPermissoesAplicacao().equals(AppPermissions.ALLOW_ALL)) {
            r.approveRemetente();
        }

        return view(r, RemetenteAdapter.class);
    }

    @RequestMapping(value = "/applications/{applicationId}/senders/{senderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement viewSender(@PathVariable("applicationId") Aplicacao app, @PathVariable(value = "senderId") Remetente remetente) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.NONE)) {
            throw new NotifcenterException(ErrorsAndWarnings.BLOCKED_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        return view(remetente, RemetenteAdapter.class);
    }

    @SkipCSRF
    @RequestMapping(value = "/applications/{applicationId}/senders/{senderId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement deleteSender(@PathVariable("applicationId") Aplicacao app, @PathVariable(value = "senderId") Remetente remetente) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.NONE)) {
            throw new NotifcenterException(ErrorsAndWarnings.BLOCKED_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        JsonObject jObj = new JsonObject();
        jObj.add("deletedSender", view(remetente, RemetenteAdapter.class));

        remetente.delete();

        return jObj;
    }

            /*
API v1

TODO api:
    GET /channels - get a list of existing channels
    GET /applications/{applicationId}- show application data
    GET /applications/{applicationId}/senders - get a list of senders of an application
    POST /applications/{applicationId}/senders - add a new sender to the application (fields required: name)
    GET /applications/{applicationId}/senders/{senderId} - show sender data
    DELETE /applications/{applicationId}/senders/{senderId} - deletes a sender

    OTHER:
    POST /channels/{channelId}/messagedeliverystatus - a channel may invoke this endpoint in order to notify notification center of a message delivery status update
*/



    @SkipCSRF
    @RequestMapping(value = "/{app}/{remetente}/addgrupodestinario", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement addGrupoDestinario(@PathVariable("app") Aplicacao app, @PathVariable(value = "remetente") Remetente remetente,
                                          @RequestParam("group") PersistentGroup group) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.NONE)) {
            throw new NotifcenterException(ErrorsAndWarnings.BLOCKED_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        if (remetente.getGruposSet().contains(group)) {
            throw new NotifcenterException(ErrorsAndWarnings.ALREADY_EXISTING_RELATION_ERROR, "Remetente " + remetente.getExternalId() + " already contains group " + group.getExternalId() + "!");
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.NONE)) {
            throw new NotifcenterException(ErrorsAndWarnings.NOTALLOWED_TO_ADD_GROUP_ERROR, "Please contact system administrators.");
        }

        remetente.addGroupToSendMesssages(group);

        JsonObject jObj = new JsonObject();

        jObj.addProperty("appId", app.getExternalId());
        jObj.addProperty("remetenteId", remetente.getExternalId());
        jObj.add("added group", view(group, PersistentGroupAdapter.class));

        //TODO - missing RREQUIRES APPROVAL ...or simply remove this feature since it needs another entity to be created...

        return jObj;
    }

    @SkipCSRF
    @RequestMapping(value = "/{app}/{remetente}/removegrupodestinario", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement removeGrupoDestinario(@PathVariable("app") Aplicacao app, @PathVariable(value = "remetente") Remetente remetente,
                                             @RequestParam("group") PersistentGroup group) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.NONE)) {
            throw new NotifcenterException(ErrorsAndWarnings.BLOCKED_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        if (!remetente.getGruposSet().contains(group)) {
            throw new NotifcenterException(ErrorsAndWarnings.NON_EXISTING_RELATION, "Remetente " + remetente.getExternalId() + " does not have group id " + group.getExternalId() + " permissions!");
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.NONE)) {
            throw new NotifcenterException(ErrorsAndWarnings.NOTALLOWED_TO_ADD_GROUP_ERROR, "Please contact system administrators.");
        }

        remetente.removeGroupToSendMesssages(group);

        JsonObject jObj = new JsonObject();

        jObj.addProperty("appId", app.getExternalId());
        jObj.addProperty("remetenteId", remetente.getExternalId());
        jObj.add("removed group", view(group, PersistentGroupAdapter.class));

        return jObj;
    }

    @RequestMapping(value = "/{app}/{remetente}/listgruposdestinatarios", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listGruposDestinatarios(@PathVariable("app") Aplicacao app, @PathVariable(value = "remetente") Remetente remetente) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        if (app.getPermissoesAplicacao().equals(AppPermissions.ALLOW_ALL)) {
            for (PersistentGroup g : FenixFramework.getDomainRoot().getBennu().getGroupSet()) {
                jArray.add(view(g, PersistentGroupAdapter.class));
            }
        }
        else if (app.getPermissoesAplicacao().equals(AppPermissions.RREQUIRES_APPROVAL)) {
            for (PersistentGroup g : remetente.getGruposSet()) {
                jArray.add(view(g, PersistentGroupAdapter.class));
            }
        }

        jObj.addProperty("appId", app.getExternalId());
        jObj.addProperty("remetenteId", remetente.getExternalId());
        jObj.add("grupos", jArray);

        return jObj;
    }


    @SkipCSRF
    @RequestMapping(value = "/{app}/{remetente}/pedidocanalnotificacao", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement pedidoCanalNotificacao(@PathVariable("app") Aplicacao app, @PathVariable(value = "remetente") Remetente remetente,
                                              @RequestParam("canal") Canal canal) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.NONE)) {
            throw new NotifcenterException(ErrorsAndWarnings.BLOCKED_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(canal)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CHANNEL_ERROR);
        }

        JsonObject jObj = new JsonObject();
        jObj.addProperty("app", app.getExternalId());
        jObj.addProperty("remetente", remetente.getExternalId());
        jObj.addProperty("canal", canal.getExternalId());

        CanalNotificacao pedidoCriacaoCanalNotificacao = create(jObj, CanalNotificacao.class);

        if (app.getPermissoesAplicacao().equals(AppPermissions.ALLOW_ALL)) {
            pedidoCriacaoCanalNotificacao.approveCanalNotificacao();
        }

        return view(pedidoCriacaoCanalNotificacao, CanalNotificacaoAdapter.class);
    }

    @SkipCSRF
    @RequestMapping(value = "/{app}/{remetente}/{canalnotificacao}/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement deleteCanalNotificacao(@PathVariable("app") Aplicacao app, @PathVariable(value = "remetente") Remetente remetente,
                                              @PathVariable("canalnotificacao") CanalNotificacao cn) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(cn) || !remetente.getCanaisNotificacaoSet().contains(cn)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CANALNOTIFICACAO_ERROR);
        }

        JsonObject jObj = new JsonObject();
        jObj.addProperty("appId", app.getExternalId());
        jObj.addProperty("remetenteId", remetente.getExternalId());
        jObj.add("deleted_canalnotificacao", view(cn, CanalNotificacaoAdapter.class));

        cn.delete();

        return jObj;
    }

    @RequestMapping(value = "/{app}/{remetente}/listcanaisnotificacao", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listCanaisNotificacao(@PathVariable("app") Aplicacao app, @PathVariable(value = "remetente") Remetente remetente) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (CanalNotificacao cn : remetente.getCanaisNotificacaoSet()) {
            jArray.add(view(cn, CanalNotificacaoAdapter.class));
        }

        jObj.addProperty("appId", app.getExternalId());
        jObj.addProperty("remetenteId", remetente.getExternalId());
        jObj.add("canais_notificacao", jArray);

        return jObj;
    }

    //CSRF notes:
    //header for token: X-CSRF-TOKEN (taken from CSRFToken)
    //body param for token: _csrf
    //Link: https://github.com/FenixEdu/bennu/tree/master/bennu-spring/src/main/java/org/fenixedu/bennu/spring/security
    @SkipCSRF
    @RequestMapping(value = "/{app}/sendmensagem", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement sendMensagem(@PathVariable("app") Aplicacao app,
                                     @RequestPart(value = "json", required = true) String json,
                                     @RequestPart(value = "anexo", required = false) MultipartFile[] anexos) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.NONE)) {
            throw new NotifcenterException(ErrorsAndWarnings.BLOCKED_APP_ERROR);
        }

        JsonObject jObj = UtilsResource.stringToJson(json);
        UtilsResource.deletePropertyFromJsonObject(jObj, "app"); //avoid hacks
        jObj.addProperty("app", app.getExternalId());
        Mensagem msg = create(jObj, Mensagem.class);

        if (anexos != null) {
            for (MultipartFile file: anexos) {
                try {

                    long maxSize = Long.parseLong(NotifcenterSpringConfiguration.getConfiguration().notifcenterMensagemAttachmentMaxSizeBytes());
                    if (file.getSize() > maxSize) {
                        throw new SizeLimitExceededException();
                    }

                    Attachment at =  Attachment.createAttachment(msg, file.getOriginalFilename(), "lowlevelname-" + msg.getExternalId() + "-" + file.getOriginalFilename(), file.getInputStream());
                    //debug
                    //System.out.println("anexo: " + FileDownloadServlet.getDownloadUrl(at));
                    //System.out.println(view(at, AttachmentAdapter.class).toString());
                }
                catch (IOException e) {
                    //debug
                    //e.printStackTrace();
                    msg.delete(); //apagar mensagem criada em caso de falha num anexo
                    throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ERROR, "Attachment " + file.getOriginalFilename() + " could not be loaded.");
                }
                catch (SizeLimitExceededException e) {
                    msg.delete(); //apagar mensagem criada em caso de falha num anexo
                    String maxSize = String.valueOf(Long.parseLong(NotifcenterSpringConfiguration.getConfiguration().notifcenterMensagemAttachmentMaxSizeBytes())/(1000000L));
                    throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ATTACHMENT_SIZE_ERROR, "Attachment " + file.getOriginalFilename() + " exceeds max. file size allowed (" + maxSize + "MB).");
                }
            }
        }

        Canal ic = msg.getCanalNotificacao().getCanal();
        ic.sendMessage(msg);

        return view(msg, MensagemAdapter.class);
    }

    @RequestMapping(value = "/{app}/{msg}/deliverystatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement getMessageStatus(@PathVariable("app") Aplicacao app, @PathVariable("msg") Mensagem msg) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(msg) || !msg.getCanalNotificacao().getRemetente().getAplicacao().equals(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ERROR);
        }

        JsonObject jObj = view(msg, MensagemAdapter.class).getAsJsonObject();

        JsonArray jArray = new JsonArray();

        for (UserMessageDeliveryStatus e : msg.getUserMessageDeliveryStatusSet()) {
            jArray.add(view(e, UserMessageDeliveryStatusAdapter.class));
        }

        jObj.add("status", jArray);

        return jObj;
    }

    @RequestMapping(value = "/{app}/listmensagens", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listMensagensApp(@PathVariable("app") Aplicacao app) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (Remetente r : app.getRemetentesSet()) {
            for (CanalNotificacao cn : r.getCanaisNotificacaoSet()) {
                for (Mensagem msg : cn.getMensagemSet()) {
                    jArray.add(view(msg, MensagemAdapter.class));
                }
            }
        }

        jObj.add("mensagens", jArray);

        return jObj;
    }

    @RequestMapping(value = "/{app}/attachments/{fileId}", method = RequestMethod.GET)
    public HttpEntity<byte[]> downloadAttachmentApp(@PathVariable("app") Aplicacao app, @PathVariable("fileId") Attachment attachment) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(attachment)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_ATTACHMENT_ERROR);
        }

        if (!attachment.isAccessibleByApp(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.NOTALLOWED_VIEW_ATTACHMENT_ERROR);
        }

        byte[] fileContent = attachment.getContent();

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", attachment.getContentType());
        header.add("Content-Disposition", "attachment; filename=" + attachment.getDisplayName().replace(" ", "_"));
        header.add("Content-Length", String.valueOf(fileContent.length));

        return new HttpEntity<>(fileContent, header);
    }

    @RequestMapping(value = "/{app}/{msg}/listattachments", method = RequestMethod.GET)
    public JsonElement listMessageAttachments(@PathVariable("app") Aplicacao app, @PathVariable("msg") Mensagem msg) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(msg) || !msg.getCanalNotificacao().getRemetente().getAplicacao().equals(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ERROR);
        }

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (Attachment atch : msg.getAttachmentsSet()) {
            jArray.add(view(atch, AttachmentAdapter.class));
        }

        jObj.addProperty("messageId", msg.getExternalId());
        jObj.add("attachments", jArray);
        return jObj;
    }

    //Called when NotifcenterException is thrown due to some error
    @ExceptionHandler({NotifcenterException.class})
    public ResponseEntity<JsonElement> errorHandler(NotifcenterException ex) {

        HttpHeaders header = new HttpHeaders();

        if (ex.getMoreDetails() != null) {
            return new ResponseEntity<>(ex.getErrorsAndWarnings().toJsonWithDetails(ex.getMoreDetails()), header, ex.getErrorsAndWarnings().getHttpStatus());
        }
        else {
            return new ResponseEntity<>(ex.getErrorsAndWarnings().toJson(), header, ex.getErrorsAndWarnings().getHttpStatus());
        }
    }

    //Debug
    @RequestMapping(value = "/listaplicacoes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listAplicacoes() {

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (Aplicacao a : SistemaNotificacoes.getInstance().getAplicacoesSet()) {
            jArray.add(view(a, AplicacaoAdapter.class));
        }

        jObj.add("aplicacoes", jArray);
        return jObj;
    }

    //Debug
    @SkipCSRF
    @RequestMapping(value = "/notifcentercallback", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement notifcenterCallback(HttpServletRequest request) {

        JsonObject jObj = HTTPClient.getHttpServletRequestParamsAsJson(request);

        System.out.println("####### got new notifcentercallback message!!");

        System.out.println(jObj.toString());

        return jObj;
    }

    //Debug
    @SkipCSRF
    @RequestMapping(value = "/approvecanalnotificacao", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement approveCanalNotificacao(@RequestParam("cn") CanalNotificacao cn) {

        if (!FenixFramework.isDomainObjectValid(cn)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CANALNOTIFICACAO_ERROR);
        }

        cn.approveCanalNotificacao();

        return view(cn, CanalNotificacaoAdapter.class);
    }

    //Debug
    @SkipCSRF
    @RequestMapping(value = "/disapprovecanalnotificacao", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement disapproveCanalNotificacao(@RequestParam("cn") CanalNotificacao cn) {

        if (!FenixFramework.isDomainObjectValid(cn)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CANALNOTIFICACAO_ERROR);
        }

        cn.disapproveCanalNotificacao();

        return view(cn, CanalNotificacaoAdapter.class);
    }

    //Debug
    @RequestMapping(value = "/groupdebug", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement groupDebug(@RequestParam("name") String name) {

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        DynamicGroup g = Group.dynamic(name);

        jObj.addProperty("name", g.getName());
        g.getMembers().forEach(e -> jArray.add(e.getUsername()));
        jObj.add("membros", jArray);

        return jObj;
    }

    //Debug
    @RequestMapping(value = "/listcontactos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listContactos() {

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (User u : FenixFramework.getDomainRoot().getBennu().getUserSet()) {
            for (Contacto c : u.getContactosSet()) {
                jArray.add(view(c, ContactoAdapter.class));
            }
        }

        jObj.add("contactos", jArray);
        return jObj;
    }

    /*Debug
    @RequestMapping(value = "/deletecanais", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteCanais() {

        for (Canal c: SistemaNotificacoes.getInstance().getCanaisSet()) {
            c.delete();
        }

        return "All channels were deleted!";
    }*/

    //Debug
    @RequestMapping(value = "/listgrupos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listGrupos() {

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (PersistentGroup g: FenixFramework.getDomainRoot().getBennu().getGroupSet()) {
            jArray.add(view(g, PersistentGroupAdapter.class));
        }

        jObj.add("grupos", jArray);
        return jObj;
    }

    //Debug
    @RequestMapping(value = "/listattachments", method = RequestMethod.GET)
    public JsonElement listAttachments() {

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (FileStorage fs : FenixFramework.getDomainRoot().getBennu().getFileSupport().getFileStorageSet()) {

            //Search all File Storages with objects of class Attachment
            if (fs.getName().equals(NotifcenterSpringConfiguration.getConfiguration().notifcenterFileStorageName())) {

                for (GenericFile atch : fs.getFileSet()) {
                    jArray.add(view(atch, AttachmentAdapter.class));
                }
                break;
            }
        }

        jObj.add("attachments", jArray);
        return jObj;
    }

    @RequestMapping(value = "/listmensagens", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listMensagens() {

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (Canal c: SistemaNotificacoes.getInstance().getCanaisSet()) {
            for (CanalNotificacao cn : c.getCanalNotificacaoSet()) {
                for (Mensagem msg : cn.getMensagemSet()) {
                    jArray.add(view(msg, MensagemAdapter.class));
                }
            }
        }

        jObj.add("mensagens", jArray);

        return jObj;
    }

    //Debug
    @SkipCSRF
    @RequestMapping(value = "/deletemensagens", method = RequestMethod.POST)
    public String deleteMnsagens(@RequestParam(value = "msg"/*, required = false FOR SAFETY*/) Mensagem msg) {

        //FOR SAFETY
        if (!FenixFramework.isDomainObjectValid(msg)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ERROR);
        }

        boolean flag = false;

        for (Canal c: SistemaNotificacoes.getInstance().getCanaisSet()) {
            for (CanalNotificacao cn : c.getCanalNotificacaoSet()) {
                for (Mensagem msg2 : cn.getMensagemSet()) {
                    if (FenixFramework.isDomainObjectValid(msg)) {
                        if (msg.equals(msg2)) {
                            msg2.delete();
                            flag = true;
                            return "Message deleted!";
                        }
                    }
                    else {
                        msg2.delete();
                    }
                }
            }
        }

        if (FenixFramework.isDomainObjectValid(msg) && !flag) {
            return "Message not found!";
        }
        else {
            return "All messages were deleted!";
        }
    }

    //Upload attachment for a message (debug)
    @SkipCSRF
    @RequestMapping(value = "/upload/{msg}", method = RequestMethod.POST)
    public String uploadFile(@PathVariable("msg") Mensagem msg, @RequestParam(value = "file", required = false) MultipartFile[] files) {

        //System.out.println("fenix storages: " + FenixFramework.getDomainRoot().getBennu().getFileSupport().getFileStorageSet().stream().map(FileStorage::getName).collect(Collectors.joining(",")));

        System.out.println(" ");
        System.out.println("files in fenix (2): " + FenixFramework.getDomainRoot().getBennu().getFileSupport().getFileStorageSet().stream().map(e -> e.getFileSet().stream().map(GenericFile::getDisplayName).collect(Collectors.joining(","))).collect(Collectors.joining("|")));
        System.out.println(" ");

        Attachment at;
        String toReturn = "no file to save\n";

        for (MultipartFile file : files) {

            if (file != null) {
                try {
                    at = Attachment.createAttachment(msg, file.getOriginalFilename(), "lowlevelname-" + file.getOriginalFilename(), file.getInputStream());

                    System.out.println("getOriginalFileName: " + file.getOriginalFilename());
                    System.out.println("externalId: " + at.getExternalId());

                    toReturn = toReturn + "\n" + NotifcenterSpringConfiguration.getConfiguration().notifcenterUrl() + "/apiaplicacoes/attachments/" + at.getExternalId() + "\n";

                    System.out.println("getDownloadUrl(): " + FileDownloadServlet.getDownloadUrl(at));
                    System.out.println("file url: " + NotifcenterSpringConfiguration.getConfiguration().notifcenterUrl() + "/apiaplicacoes/attachments/" + at.getExternalId());

                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return toReturn;
    }

    //Debug
    @RequestMapping(value = "/urlencode", method = RequestMethod.GET)
    public String urlEncode(@RequestParam(value = "string") String string) {
        try {
            String r = URLEncoder.encode(string, "UTF-8");
            r = r.replace("+", "%2B");
            return r;
        }
        catch (Exception e) {
            return "error!";
        }
    }

    /*Debug
    @RequestMapping(value = "/update/{app}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement updateAplic(@PathVariable("app") Aplicacao app, JsonElement json) {
        return updateApp(app, json);
    }

    protected JsonElement updateApp(Aplicacao app, JsonElement json) {
        app = update(json, app);
        return view(app, AplicacaoAdapter.class);
    }

    @OAuthEndpoint("scope3")
    @RequestMapping(value = "test5", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String test5() {
        String t4 = "test5";
        return t4;
    }*/

}

/* IGNORE!

//curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"param1":"value1"}'

//OK5:
//GET http://{{DOMAIN}}:8080/notifcenter/mensagens/attachments/{fileId} //attachment is downloaded if user belongs to gruposDestinatarios of its message
//GET http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/attachments/{fileId}
//GET http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/{msg}/listattachments


//OK4:
//GET http://{{DOMAIN}}:8080/notifcenter/apicanais/listcanais
//GET http://{{DOMAIN}}:8080/notifcenter/apicanais/listclassescanais
//GET http://{{DOMAIN}}:8080/notifcenter/apicanais/281835753963522 (show canal)
//POST {"createChannel": "Messenger", "accountSID":"accountSID1", "authToken":"authToken1", "fromPhoneNumber":"fromPhoneNumber1", "uriaa":"uri2"} -> http://{{DOMAIN}}:8080/notifcenter/apicanais/addcanal

//OK3:
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/addaplicacao?name=app_99&redirect_uri=http://app99_site.com/code&description=descricao_app99
//POST {"name":"app_55", "description":"d5", "redirect_uri":"http://app55_site.com/code&description=descricao_app55", "author":"author1", "site_url": "siteurl1"} http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/addaplicacao2
//GET http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/listaplicacoes

//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715716/update?description=d1&name=n1&redirect_uri=r1&author=a1&site_urlTONULL=s1
//POST {"description":"d2", "name":"n2", "redirect_uri":"r2", "author":"a2", "site_url": "s2"} http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715716/update2

//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715716/delete

//GET http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714 (view app)

//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281835753963522/messagedeliverystatus // {canal}/messagedeliverystatus
//GET http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281681135140872/deliverystatus
////POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/sendmensagem?canalnotificacao=281775624421380&gdest=281702609977345&assunto=umassunto3&textocurto=aparecenowhatsppcurto3&textolongo=algumtextolongo3
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/sendmensagem {json, anexo, anexo, anexo...}
//DEBUG http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/deletemensagens //?msg=opcional

//POST {"nameEEE":"novo_nome"} -> http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/281724084813855/update
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/281724084813855/delete
//GET http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/listremetentes

//GET http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/281724084813855 //{app}/{remetente}
//GET http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/281724084813855/listgruposdestinatarios
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/281724084813855/removegrupodestinario?group=281702609977345
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/281724084813855/adddrupodestinario?group=281702609977345

//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/281724084813855/pedidocanalnotificacao?canal=281835753963522
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/281724084813855/281775624421380/delete ///{app}/{remetente}/{canalnotificacao}/delete"
//GET http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/281724084813855/listcanaisnotificacao
//DEBUG POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/approvecanalnotificacao?cn=281775624421380
//DEBUG POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/disapprovecanalnotificacao?cn=281775624421380

//GET http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/listmensagens

//GET http://{{DOMAIN}}:8080/notifcenter/apiutilizadores/listutilizadores
//GET http://{{DOMAIN}}:8080/notifcenter/apiutilizadores/{utilizador}
//POST http://{{DOMAIN}}:8080/notifcenter/apiutilizadores/281582350893057/addcontacto?canal=281835753963522&dados=whatsapp:%2B351961077271
//GET http://{{DOMAIN}}:8080/notifcenter/apiutilizadores/281582350893057/listcontactos
//GET http://{{DOMAIN}}:8080/notifcenter/apiutilizadores/281582350893057/281715494879236 ///{utilizador}/{contacto}
//POST http://{{DOMAIN}}:8080/notifcenter/apiutilizadores/281582350893057/281715494879234/delete
//POST http://{{DOMAIN}}:8080/notifcenter/apiutilizadores/281582350893057/281715494879236/update
//POST {"dados":"novos_dados"} http://{{DOMAIN}}:8080/notifcenter/apiutilizadores/281582350893057/281715494879236/update2

//https://www.twilio.com/console/sms/whatsapp/learn
////POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/sendmensagem?canalnotificacao=281775624421380&gdest=281702609977345&assunto=umassunto1&textocurto=aparecenowhatsppcurto&textolongo=algumtextolongo

//DADOS EXEMPLO:
//app "app_77": 281736969715714
//remetente "rem1": 281724084813855
//user "bennu0": 281582350893059
//user "admin": 281582350893057
//grupo "managers": 281702609977345
//canal TwilioWhatsApp: 281835753963522
//(pedido) de canal de notificacao: 281775624421380
//user "admin" -> contacto whatsapp: 281715494879236

//REGISTAR APP:
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/addaplicacao?name=app_77&redirect_uri=http://app77_site.com/code&description=descricao_app77
//GET http://{{DOMAIN}}:8080/notifcenter/oauth/userdialog?client_id=281736969715714&redirect_uri=http://app77_site.com/code
//POST http://{{DOMAIN}}:8080/notifcenter/oauth/access_token?client_id=281736969715714&client_secret=y3MW4pX%2B3hGu9DbfSpYYtFx71llEx5qCpKsJdWrtlVjuG9%2FRozatZkYvWj9FbHkDEM52%2B3oWUuRCI7HYowXEfw%3D%3D&redirect_uri=http://app77_site.com/code&code=d46d6939c0564846fed10cdcb3233b18716f6fb5770d3f58c134379e43316d138471e47d42f1a49160c65c22577180705fc615df027e0017a68f47f4b595a0c3&grant_type=authorization_code
//POST http://{{DOMAIN}}:8080/notifcenter/oauth/refresh_token?client_id=281736969715714&client_secret=y3MW4pX%2B3hGu9DbfSpYYtFx71llEx5qCpKsJdWrtlVjuG9%2FRozatZkYvWj9FbHkDEM52%2B3oWUuRCI7HYowXEfw%3D%3D&refresh_token=MjgxNjg1NDMwMTA4MTYzOjdhOWIzNDIyNTRmZDIzN2ZmODQ4N2U2NjFjMjllYWQyODAxYjhhZWMwNDFiZDhiZDU1MDEwZjI5OWNiZmQzOGI3NDQwMGEwZGNhMTAwMjFhYjMyOTYwN2U2NDJkNjMzMWMwZDQ3YmFjYmNkZDk2ZjA3ZGQ3ZmI0NTMyZTg3MzRj&grant_type=refresh_token

//METODOS
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/addaplicacao?name=app_77&redirect_uri=http://app77_site.com/code&description=descricao_app77
//GET http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714   (view app) ?access_token=NTYzMTYwNDA2ODE4ODIwOjYwNWJiYTg4OGViMTAwYzdmMTc3ZjQ1OWVlZmM3MjE2NmMyZGY4MGNiOGVlNDk4NDI0Mzc0MmNhMzZiYTk0YmY0MDRkMGI3MDYzYzAzMzE2NTJjYzRhZDRmMzI1NzUyZDUyNzk1MjQ5YzdkNWNhZWMyZTI3MDQ2NTUxMzc1Mjdi
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/addremetente?name=ric&access_token=NTYzMTYwNDA2ODE4ODIwOjYwNWJiYTg4OGViMTAwYzdmMTc3ZjQ1OWVlZmM3MjE2NmMyZGY4MGNiOGVlNDk4NDI0Mzc0MmNhMzZiYTk0YmY0MDRkMGI3MDYzYzAzMzE2NTJjYzRhZDRmMzI1NzUyZDUyNzk1MjQ5YzdkNWNhZWMyZTI3MDQ2NTUxMzc1Mjdi
//GET/POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/notifcentercallback
////POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/sendmensagem?canalnotificacao=281775624421380&gdest=281702609977345&assunto=umassunto1&textocurto=aparecenowhatsppcurto&textolongo=algumtextolongo

//UTEIS:
//http://{{DOMAIN}}:8080/notifcenter/apicanais/listcanais
//http://{{DOMAIN}}:8080/notifcenter/apiutilizadores/listutilizadores
//http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/listaplicacoes
//http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/listgrupos
//http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/attachments/list

//"no" http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/isusergroupmember?user=281582350893059&group=281702609977345
//"yes" http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/isusergroupmember?user=281582350893057&group=281702609977345

//CURL
//curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"id":100}' http://localhost/api/postJsonReader.do
//curl -H "Content-type: application/json" -X POST -d '{"id":101, "content":"ola1"}' http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/greet
//curl -H "Content-type: application/json" -X POST -d '{"email":"someemail@awd.com", "password":"pass1"}' http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/canal1

//curl -F 'file=@/home/cr/imgg.png' http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/upload


//curl -H "Content-type: application/x-www-form-urlencoded; charset=utf-8" -x POST -d "param1=value1&param2=value2" http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/xyz
*/