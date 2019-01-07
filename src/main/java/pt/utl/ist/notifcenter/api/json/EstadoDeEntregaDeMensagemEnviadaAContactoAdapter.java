package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.domain.EstadoDeEntregaDeMensagemEnviadaAContacto;

@DefaultJsonAdapter(EstadoDeEntregaDeMensagemEnviadaAContacto.class)
public class EstadoDeEntregaDeMensagemEnviadaAContactoAdapter implements JsonAdapter<EstadoDeEntregaDeMensagemEnviadaAContacto> {

    @Override
    public EstadoDeEntregaDeMensagemEnviadaAContacto create(JsonElement jsonElement, JsonBuilder ctx) {
        return null;
    }

    @Override
    public EstadoDeEntregaDeMensagemEnviadaAContacto update(JsonElement jsonElement, EstadoDeEntregaDeMensagemEnviadaAContacto EstadoDeEntregaDeMensagemEnviadaAContacto, JsonBuilder ctx) {
        return null;
    }

    @Override
    public JsonElement view(EstadoDeEntregaDeMensagemEnviadaAContacto obj, JsonBuilder ctx) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("id", obj.getExternalId());
        jObj.addProperty("canal", obj.getCanal().getExternalId());
        jObj.addProperty("contacto", obj.getContacto().getExternalId());
        jObj.addProperty("idExterno", obj.getIdExterno());
        jObj.addProperty("estadoEntrega", obj.getEstadoEntrega());
        return jObj;
    }


}
