package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.domain.Mensagem;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

@DefaultJsonAdapter(Mensagem.class)
public class MensagemAdapter implements JsonAdapter<Mensagem> {

    @Override
    public Mensagem create(JsonElement jsonElement, JsonBuilder ctx) {

        return null;
    }

    @Override
    public Mensagem update(JsonElement jsonElement, Mensagem app, JsonBuilder ctx) {
        
        return null;
    }

    @Override
    public JsonElement view(Mensagem obj, JsonBuilder ctx) {
        JsonObject jFinal = new JsonObject();

        //TODO JSON MESSAGE             //mostrar no MensagemAdapter o remetente da mensagem!

        /*JsonArray jArray = new JsonArray();
        jObj.addProperty("canalnotificacao", obj.getCanalNotificacao().getExternalId());


        jArray.add(jObj);
        jObj = new JsonObject();
        jObj.addProperty("canalnotificacao", obj.getCanalNotificacao().getExternalId());
        jObj.add("gruposDestinatarios", jArray);

        jObj.addProperty("", obj.getGruposDestinatariosSet().stream().map(PersistentGroup::getPresentationName).collect(Collectors.joining(",")));
        jObj.addProperty("assunto", obj.getAssunto());
        jObj.addProperty("textoCurto", obj.getTextoCurto());
        jObj.addProperty("textoLongo", obj.getTextoLongo());
        jObj.addProperty("callbackUrlEstadoEntrega", obj.getCallbackUrlEstadoEntrega());
        jObj.addProperty("dataEntrega", obj.getDataEntrega().toString("dd.MM.yyyy HH:mm:ss.SSSZ"));


        JsonArray jAttach = new JsonArray();
        for (Attachment at : obj.getAttachmentsSet()) {
            jAttach.add;
        }

        jObj.addProperty("anexos", obj.getAttachmentsSet().stream().map(Attachment::getDisplayName).collect(Collectors.joining(",")));
            */

        return jFinal;
    }

    private String getRequiredValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            return obj.get(property).getAsString();
        }
        throw new NotifcenterException(ErrorsAndWarnings.INVALID_ENTITY_ERROR);
    }

}

