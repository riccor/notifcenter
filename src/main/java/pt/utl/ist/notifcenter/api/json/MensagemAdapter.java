package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.domain.Attachment;
import pt.utl.ist.notifcenter.domain.Mensagem;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

@DefaultJsonAdapter(Mensagem.class)
public class MensagemAdapter implements JsonAdapter<Mensagem> {

    @Override
    public Mensagem create(JsonElement jsonElement, JsonBuilder ctx) {
        //Não é conveninente usar, porque:
        //- preciso de passar uma instancia de Aplicacao para aqui para poder fazer uma verificação necessária no /sendmessage2;
        //- e o /sendmessage já usa o spring para fazer a conversão dos parametros recebidos para os objectos de dominio envolvidos numa mensagem
        return null;
    }

    @Override
    public Mensagem update(JsonElement jsonElement, Mensagem app, JsonBuilder ctx) {
        //instâncias de mensagens não precisam de atualizações
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
        jObj.addProperty("canalnotificacao", obj.getCanalNotificacao().getExternalId());
        jObj.addProperty("remetente", obj.getCanalNotificacao().getRemetente().getExternalId());
        jObj.add("gruposDestinatarios", jArrayGruposDestinatarios);
        jObj.addProperty("assunto", obj.getAssunto());
        jObj.addProperty("textoCurto", obj.getTextoCurto());
        jObj.addProperty("textoLongo", obj.getTextoLongo());
        jObj.addProperty("dataEntrega", obj.getDataEntrega().toString("dd.MM.yyyy HH:mm:ss.SSS"));
        jObj.addProperty("callbackUrlEstadoEntrega", obj.getCallbackUrlEstadoEntrega());
        jObj.add("attachments", jArrayAttachments);
        jObj.addProperty("link",NotifcenterSpringConfiguration.getConfiguration().notifcenterUrl() + "/notifcenter/" + obj.getExternalId() + "/view");

        return jObj;
    }

    private String getRequiredValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            if (!obj.get(property).getAsString().isEmpty()) {
                return obj.get(property).getAsString();
            }
        }
        throw new NotifcenterException(ErrorsAndWarnings.INVALID_ENTITY_ERROR, "Missing parameter " + property + "!");
    }

}

