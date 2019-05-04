package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.domain.UserMessageDeliveryStatus;

@DefaultJsonAdapter(UserMessageDeliveryStatus.class)
public class UserMessageDeliveryStatusAdapter implements JsonAdapter<UserMessageDeliveryStatus> {

    @Override
    public UserMessageDeliveryStatus create(JsonElement jsonElement, JsonBuilder ctx) {
        return null;
    }

    @Override
    public UserMessageDeliveryStatus update(JsonElement jsonElement, UserMessageDeliveryStatus UserMessageDeliveryStatus, JsonBuilder ctx) {
        return null;
    }

    @Override
    public JsonElement view(UserMessageDeliveryStatus obj, JsonBuilder ctx) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("id", obj.getExternalId());
        jObj.addProperty("channel", obj.getMensagem().getCanalNotificacao().getCanal().getExternalId());
        jObj.addProperty("user", obj.getUtilizador().getExternalId());
        jObj.addProperty("externalId", obj.getIdExterno());
        jObj.addProperty("deliveryStatus", obj.getEstadoEntrega());
        return jObj;
    }

}
