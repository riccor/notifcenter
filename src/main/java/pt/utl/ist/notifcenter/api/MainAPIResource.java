/*
API

GET /groups - get a list of existing (persistent) groups
GET /channels - get a list of existing channels

GET /applications/{applicationId}- show application data
GET /applications/{applicationId}/senders - get a list of senders of an application
POST /applications/{applicationId}/senders - add a new sender to the application (JSON fields required: name)
GET /applications/{applicationId}/senders/{senderId} - show sender data
DELETE /applications/{applicationId}/senders/{senderId} - deletes a sender

POST /applications/{applicationId}/senders/{senderId}/grouppermissions - add permissions to allow a sender to send messages to a group (JSON fields required: name)
GET /applications/{applicationId}/senders/{senderId}/grouppermissions - get a list of group permissions from a sender
DELETE /applications/{applicationId}/senders/{senderId}/grouppermissions/{groupName} - delete group permissions from a sender

POST /applications/{applicationId}/senders/{senderId}/notificationchannels - add permissions to allow a sender to send messages to a channel (JSON fields required: channelId)
GET /applications/{applicationId}/senders/{senderId}/notificationchannels - get a list of channels permissions from a sender
DELETE /applications/{applicationId}/senders/{senderId}/notificationchannels/{notificationChannelId} - delete channel permissions from a sender

POST /applications/{applicationId}/messages - send a message to a group via some channel
Requests to this endpoint must include Multipart-FormData with the following parameters:
- "json" - a JSON containing fields paracanalnotificacao, gdest, assunto, textocurto, textolongo, callbackurl, (optional) dataentrega
- (optional) "attachment" - as many attachments as desired

GET /applications/{applicationId}/messages/{messageId}/deliverystatus - get delivery status from a message
GET /applications/{applicationId}/messages/{messageId} - view a message
GET /applications/{applicationId}/messages - view all messages from an application

POST /channels/{channelId}/messagedeliverystatus - a channel may invoke this endpoint in order to notify notification center of a message delivery status update
*/

package pt.utl.ist.notifcenter.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.UserProfile;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.rest.BennuRestResource;

import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.api.json.*;

import pt.utl.ist.notifcenter.domain.*;
import pt.utl.ist.notifcenter.ui.NotifcenterController;

import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

import javax.naming.SizeLimitExceededException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;

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

    @RequestMapping(value = "/groups", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listGroups() {

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (PersistentGroup g : FenixFramework.getDomainRoot().getBennu().getGroupSet()) {
            jArray.add(view(g, PersistentGroupAdapter.class));
        }

        jObj.add("groups", jArray);

        return jObj;
    }

    @SkipCSRF //Used due to a incompatibility issue with Spring that is making server to reject POST and DELETE requests (https://github.com/FenixEdu/bennu/tree/master/bennu-spring/src/main/java/org/fenixedu/bennu/spring/security)
    @RequestMapping(value = "/channels/{channelId}/messagedeliverystatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement messageDeliveryStatusUpdate(@PathVariable("channelId") Canal canal, HttpServletRequest request) {
        //Received content might not be JSON, so we do not use "@RequestBody JsonElement body"

        if (!FenixFramework.isDomainObjectValid(canal)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CHANNEL_ERROR);
        }

        //Debug
        System.out.println("Got new message delivery status message!!");
        System.out.println(HTTPClient.getHttpServletRequestParamsAsJson(request).toString());

        UserMessageDeliveryStatus ede = canal.dealWithDeliveryStatusNotifications(request);

        if (ede == null) {
            throw new NotifcenterException(ErrorsAndWarnings.UNKNOWN_MESSAGE_ID);
        }
        else {
            notificateAppViaWebhook(ede);
            throw new NotifcenterException(ErrorsAndWarnings.SUCCESS_THANKS);
        }
    }

    public static void notificateAppViaWebhook(UserMessageDeliveryStatus ede) {

        //If message parameter callbackUrlEstadoEntrega is not "none", then send message delivery status to the application
        if (!ede.getMensagem().getCallbackUrlEstadoEntrega().equals("none")) {

            JsonObject jObj = new JsonObject();
            jObj.addProperty("MessageId", ede.getMensagem().getExternalId());
            jObj.addProperty("User", ede.getUtilizador().getUsername());
            jObj.addProperty("MessageStatus", ede.getEstadoEntrega());

            DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
            deferredResult.setResultHandler((Object responseEntity) -> {
                HTTPClient.printResponseEntity((ResponseEntity<String>) responseEntity);
            });

            //Debug
            System.out.println("sent to channel webhook: " + jObj.toString());

            HTTPClient.restASyncClientJSON(HttpMethod.POST, ede.getMensagem().getCallbackUrlEstadoEntrega(), jObj, deferredResult);
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

        JsonArray jArray = new JsonArray();

        for (Remetente r : app.getRemetentesSet()) {
            jArray.add(view(r, RemetenteAdapter.class));
        }

        return jArray;
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

    @SkipCSRF
    @RequestMapping(value = "/applications/{applicationId}/senders/{senderId}/grouppermissions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement addGroupPermissionsToSender(@PathVariable("applicationId") Aplicacao app,
                                                   @PathVariable(value = "senderId") Remetente remetente,
                                                   @RequestBody JsonElement body) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.NONE)) {
            throw new NotifcenterException(ErrorsAndWarnings.BLOCKED_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        String name = UtilsResource.getRequiredValue(body.getAsJsonObject(), "name");

        if (!Group.dynamic(name).isDefined()) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_GROUP_ERROR, "Group " + name + " does not exist.");
        }

        PersistentGroup group = Group.dynamic(name).toPersistentGroup();

        if (remetente.getGruposSet().contains(group)) {
            throw new NotifcenterException(ErrorsAndWarnings.ALREADY_EXISTING_PERMISSIONS_ERROR, "Sender " + remetente.getExternalId() + " has already permissions to send messages to group " + group.getExternalId() + "!");
        }

        remetente.addGroupToSendMesssages(group);

        JsonObject jObj = new JsonObject();

        jObj.addProperty("senderId", remetente.getExternalId());
        jObj.add("addedGroup", view(group, PersistentGroupAdapter.class));

        return jObj;
    }

    @SkipCSRF
    @RequestMapping(value = "/applications/{applicationId}/senders/{senderId}/grouppermissions/{groupName}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement removeGroupPermissionsFromSender(@PathVariable("applicationId") Aplicacao app,
                                                        @PathVariable(value = "senderId") Remetente remetente,
                                                        @PathVariable(value = "groupName") String groupName) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.NONE)) {
            throw new NotifcenterException(ErrorsAndWarnings.BLOCKED_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        if (!Group.dynamic(groupName).isDefined()) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_GROUP_ERROR, "Group " + groupName + " does not exist.");
        }

        PersistentGroup group = Group.dynamic(groupName).toPersistentGroup();

        if (!remetente.getGruposSet().contains(group)) {
            throw new NotifcenterException(ErrorsAndWarnings.NON_EXISTING_PERMISSIONS_ERROR, "Sender " + remetente.getExternalId() + " does not have group " + group.getPresentationName() + " permissions!");
        }

        remetente.removeGroupToSendMesssages(group);

        JsonObject jObj = new JsonObject();

        jObj.addProperty("senderId", remetente.getExternalId());
        jObj.add("removedGroup", view(group, PersistentGroupAdapter.class));

        return jObj;
    }

    @RequestMapping(value = "/applications/{applicationId}/senders/{senderId}/grouppermissions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listSenderGroups(@PathVariable("applicationId") Aplicacao app, @PathVariable(value = "senderId") Remetente remetente) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.NONE)) {
            throw new NotifcenterException(ErrorsAndWarnings.BLOCKED_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        JsonArray jArray = new JsonArray();

        for (PersistentGroup g : remetente.getGruposSet()) {
            jArray.add(view(g, PersistentGroupAdapter.class));
        }

        return jArray;
    }

    @SkipCSRF
    @RequestMapping(value = "/applications/{applicationId}/senders/{senderId}/notificationchannels", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement addChannelPermissionsToSender(@PathVariable("applicationId") Aplicacao app,
                                                     @PathVariable(value = "senderId") Remetente remetente,
                                                     @RequestBody JsonElement body) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.NONE)) {
            throw new NotifcenterException(ErrorsAndWarnings.BLOCKED_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        Canal canal = UtilsResource.getDomainObjectFromJsonProperty(body.getAsJsonObject(), Canal.class,"channelId");

        for (CanalNotificacao cn : remetente.getCanaisNotificacaoSet()) {
            if (cn.getCanal().equals(canal)) {
                throw new NotifcenterException(ErrorsAndWarnings.ALREADY_EXISTING_PERMISSIONS_ERROR, "Sender " + remetente.getExternalId() + " has already permissions to send messages to channel " + canal.getExternalId() + "! (Existing notification channel id: " + cn.getExternalId() + ")");
            }
        }

        JsonObject jObjTemp = new JsonObject();
        jObjTemp.addProperty("app", app.getExternalId());
        jObjTemp.addProperty("remetente", remetente.getExternalId());
        jObjTemp.addProperty("canal", canal.getExternalId());

        CanalNotificacao pedidoCriacaoCanalNotificacao = create(jObjTemp, CanalNotificacao.class);

        if (app.getPermissoesAplicacao().equals(AppPermissions.ALLOW_ALL)) {
            pedidoCriacaoCanalNotificacao.approveCanalNotificacao();
        }

        JsonObject jObj = new JsonObject();
        jObj.addProperty("senderId", remetente.getExternalId());
        jObj.add("addedNotificationChannel", view(pedidoCriacaoCanalNotificacao, CanalNotificacaoAdapter.class));

        return jObj;
    }

    @SkipCSRF
    @RequestMapping(value = "/applications/{applicationId}/senders/{senderId}/notificationchannels/{notificationChannelId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement removeChannelPermissionsFromSender(@PathVariable("applicationId") Aplicacao app,
                                                          @PathVariable("senderId") Remetente remetente,
                                                          @PathVariable("notificationChannelId") CanalNotificacao cn) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.NONE)) {
            throw new NotifcenterException(ErrorsAndWarnings.BLOCKED_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(cn) || !remetente.getCanaisNotificacaoSet().contains(cn)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CANALNOTIFICACAO_ERROR);
        }

        JsonObject jObj = new JsonObject();
        jObj.addProperty("senderId", remetente.getExternalId());
        jObj.add("removedNotificationChannel", view(cn, CanalNotificacaoAdapter.class));

        cn.delete();

        return jObj;
    }

    @RequestMapping(value = "/applications/{applicationId}/senders/{senderId}/notificationchannels", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listSenderNotificationChannels(@PathVariable("applicationId") Aplicacao app, @PathVariable(value = "senderId") Remetente remetente) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.NONE)) {
            throw new NotifcenterException(ErrorsAndWarnings.BLOCKED_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        JsonArray jArray = new JsonArray();

        for (CanalNotificacao cn : remetente.getCanaisNotificacaoSet()) {
            jArray.add(view(cn, CanalNotificacaoAdapter.class));
        }

        return jArray;
    }

    @SkipCSRF
    @RequestMapping(value = "/applications/{applicationId}/messages", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement sendMessage(@PathVariable("applicationId") Aplicacao app,
                                   @RequestPart(value = "json", required = true) String json,
                                   @RequestPart(value = "attachment", required = false) MultipartFile[] anexos,
                                   HttpServletRequest request) {

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
                    msg.delete(); //delete created message on failed attachment upload
                    throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ERROR, "Attachment " + file.getOriginalFilename() + " could not be loaded.");
                }
                catch (SizeLimitExceededException e) {
                    msg.delete(); //delete created message on failed attachment upload
                    String maxSize = String.valueOf(Long.parseLong(NotifcenterSpringConfiguration.getConfiguration().notifcenterMensagemAttachmentMaxSizeBytes())/(1000000L));
                    throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ATTACHMENT_SIZE_ERROR, "Attachment " + file.getOriginalFilename() + " exceeds max. file size allowed (" + maxSize + "MB).");
                }
            }
        }

        Canal ic = msg.getCanalNotificacao().getCanal();
        ic.sendMessage(msg);

        JsonObject jObjF = view(msg, MensagemAdapter.class).getAsJsonObject();
        jObjF.addProperty("link", request.getHeader("host") + request.getContextPath() + "/mensagens/" + msg.getExternalId());

        return jObjF;
    }

    @RequestMapping(value = "/applications/{applicationId}/messages/{messageId}/deliverystatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement getMessageDeliveryStatus(@PathVariable("applicationId") Aplicacao app, @PathVariable("messageId") Mensagem msg) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.NONE)) {
            throw new NotifcenterException(ErrorsAndWarnings.BLOCKED_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(msg) || !msg.getCanalNotificacao().getRemetente().getAplicacao().equals(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ERROR);
        }

        JsonArray jArray = new JsonArray();

        for (UserMessageDeliveryStatus e : msg.getUserMessageDeliveryStatusSet()) {
            jArray.add(view(e, UserMessageDeliveryStatusAdapter.class));
        }

        return jArray;
    }

    @RequestMapping(value = "/applications/{applicationId}/messages/{messageId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement viewMessage(@PathVariable("applicationId") Aplicacao app, @PathVariable("messageId") Mensagem msg, HttpServletRequest request) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.NONE)) {
            throw new NotifcenterException(ErrorsAndWarnings.BLOCKED_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(msg) || !msg.getCanalNotificacao().getRemetente().getAplicacao().equals(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ERROR);
        }

        JsonObject jObj = view(msg, MensagemAdapter.class).getAsJsonObject();
        jObj.addProperty("link", request.getHeader("host") + request.getContextPath() + "/mensagens/" + msg.getExternalId());

        return jObj;
    }

    @RequestMapping(value = "/applications/{applicationId}/messages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement getMessagesList(@PathVariable("applicationId") Aplicacao app) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.NONE)) {
            throw new NotifcenterException(ErrorsAndWarnings.BLOCKED_APP_ERROR);
        }

        JsonArray jArray = new JsonArray();

        for (Remetente r : app.getRemetentesSet()) {
            for (CanalNotificacao cn : r.getCanaisNotificacaoSet()) {
                for (Mensagem msg : cn.getMensagemSet()) {
                    jArray.add(msg.getExternalId());
                }
            }
        }

        return jArray;
    }

    //Called when NotifcenterException is thrown due to some error or warning
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

    //Performance tests
    @Atomic
    public static void createOneHundredUsers(int ii, int f) {
        Locale l = new Locale("pt");

        for (int i = ii; i < f; i++) {
            UserProfile up = new UserProfile("a", "b", "a b", "c@ccc.com", l);
            User u = new User("user-" + i, up);
        }
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement test(@RequestParam(value = "i", required = true) int i, @RequestParam(value = "f", required = true) int f) {

        createOneHundredUsers(i, f);

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (User u : FenixFramework.getDomainRoot().getBennu().getUserSet()) {
            jArray.add(view(u, UserAdapter.class));
        }

        jObj.add("users", jArray);
        return jObj;
    }

}
