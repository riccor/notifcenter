package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.joda.time.DateTime;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.domain.*;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

import java.util.ArrayList;

@DefaultJsonAdapter(Mensagem.class)
public class MensagemAdapter implements JsonAdapter<Mensagem> {

    @Override
    public Mensagem create(JsonElement jsonElement, JsonBuilder ctx) {

        //NOTE: jsonElement property 'app' must be inserted by the server
        Aplicacao app = UtilsResource.getDomainObjectFromJsonProperty(jsonElement, Aplicacao.class, "app");

        if (app.getPermissoesAplicacao().equals(AppPermissions.NONE)) {
            throw new NotifcenterException(ErrorsAndWarnings.BLOCKED_APP_ERROR);
        }

        CanalNotificacao canalNotificacao = UtilsResource.getDomainObjectFromJsonProperty(jsonElement, CanalNotificacao.class, "canalnotificacao");

        String[] gd = UtilsResource.getRequiredArrayValue(jsonElement.getAsJsonObject(), "gdest");

        //PersistentGroup[] gruposDestinatarios = UtilsResource.getDomainObjectsArray(PersistentGroup.class, gd).toArray(new PersistentGroup[0]);
        ArrayList<PersistentGroup> al = new ArrayList<>();
        for (String g : gd) {
            if (!Group.dynamic(g).isDefined()) {
                throw new NotifcenterException(ErrorsAndWarnings.INVALID_GROUP_ERROR, "Group " + g + " does not exist.");
            }
            al.add(Group.dynamic(g).toPersistentGroup());
        }
        PersistentGroup[] gruposDestinatarios = al.toArray(new PersistentGroup[0]);

        String assunto = UtilsResource.getRequiredValue(jsonElement.getAsJsonObject(), "assunto");
        String textoCurto = UtilsResource.getRequiredValue(jsonElement.getAsJsonObject(), "textocurto");
        String textoLongo = UtilsResource.getRequiredValue(jsonElement.getAsJsonObject(), "textolongo");

        String de = UtilsResource.getRequiredValueOrReturnNullInstead(jsonElement.getAsJsonObject(), "dataentrega");
        DateTime dataEntrega = UtilsResource.getDatetime(de);

        String callbackUrlEstadoEntrega = UtilsResource.getRequiredValueOrReturnNullInstead(jsonElement.getAsJsonObject(), "callbackurl");

        //verify params
        if (!FenixFramework.isDomainObjectValid(canalNotificacao) || !app.getRemetentesSet().contains(canalNotificacao.getRemetente())) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CANALNOTIFICACAO_ERROR, "Such canalnotificacao does not exist.");
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.RREQUIRES_APPROVAL) && !canalNotificacao.getRemetente().isApproved()) {
            throw new NotifcenterException(ErrorsAndWarnings.NOTALLOWED_REMETENTE_ERROR, "Remetente id " + canalNotificacao.getRemetente().getExternalId() + " is awaiting approval by system administrators.");
        }

        if (app.getPermissoesAplicacao().equals(AppPermissions.RREQUIRES_APPROVAL) && !canalNotificacao.isApproved()) {
            throw new NotifcenterException(ErrorsAndWarnings.NOTALLOWED_CANALNOTIFICACAO_ERROR, "Canalnotificacao id " + canalNotificacao.getExternalId() + " is awaiting approval by system administrators.");
        }

        for (PersistentGroup group : gruposDestinatarios) {
            if (canalNotificacao.getRemetente().getGruposSet().stream().noneMatch(e -> e.equals(group))) {
                throw new NotifcenterException(ErrorsAndWarnings.NOTALLOWED_GROUP_ERROR, "No permissions to send messages to group " + group.getPresentationName() + "! Add them first.");
            }
        }

        //Imposing a maximum length for assunto and textoCurto
        if (assunto.length() > Integer.parseInt(NotifcenterSpringConfiguration.getConfiguration().notifcenterMensagemAssuntoMaxSize())) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ERROR, "Assunto must be at most " +
                    NotifcenterSpringConfiguration.getConfiguration().notifcenterMensagemAssuntoMaxSize() + " characters long.");
        }

        if (textoCurto.length() > Integer.parseInt(NotifcenterSpringConfiguration.getConfiguration().notifcenterMensagemTextoCurtoMaxSize())) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ERROR, "TextoCurto must be at most " +
                    NotifcenterSpringConfiguration.getConfiguration().notifcenterMensagemTextoCurtoMaxSize() + " characters long.");
        }

        return Mensagem.createMensagem(canalNotificacao, gruposDestinatarios, assunto, textoCurto, textoLongo, dataEntrega, callbackUrlEstadoEntrega);
    }

    @Override
    public Mensagem update(JsonElement jsonElement, Mensagem app, JsonBuilder ctx) {
        //messages usually don't get updated
        return null;
    }

    @Override
    public JsonElement view(Mensagem obj, JsonBuilder ctx) {

        JsonArray jArrayGruposDestinatarios = new JsonArray();
        for (PersistentGroup group : obj.getGruposDestinatariosSet()) {
            jArrayGruposDestinatarios.add(group.getExternalId());
        }

        JsonArray jArrayAttachments = new JsonArray();
        for (Attachment attachment : obj.getAttachmentsSet()) {
            jArrayAttachments.add(attachment.getExternalId());
        }

        JsonObject jObj = new JsonObject();
        jObj.addProperty("id", obj.getExternalId());
        jObj.addProperty("notificationChannel", obj.getCanalNotificacao().getExternalId());
        jObj.addProperty("sender", obj.getCanalNotificacao().getRemetente().getExternalId());
        jObj.add("recipientGroups", jArrayGruposDestinatarios);
        jObj.addProperty("subject", obj.getAssunto());
        jObj.addProperty("shortText", obj.getTextoCurto());
        jObj.addProperty("longText", obj.getTextoLongo());
        jObj.addProperty("deliveryDate", obj.getDataEntrega().toString("dd.MM.yyyy HH:mm:ss.SSS"));
        jObj.addProperty("deliveryStatusCallback", obj.getCallbackUrlEstadoEntrega());
        jObj.add("attachments", jArrayAttachments);
        //jObj.addProperty("link",NotifcenterSpringConfiguration.getConfiguration().notifcenterUrl() + "/mensagens/" + obj.getExternalId());

        return jObj;
    }

}
