package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.domain.CanalNotificacao;

@DefaultJsonAdapter(CanalNotificacao.class)
public class CanalNotificacaoAdapter implements JsonAdapter<CanalNotificacao> {

    @Override
    public CanalNotificacao create(JsonElement jsonElement, JsonBuilder ctx) {

        return null;
    }

    @Override
    public CanalNotificacao update(JsonElement jsonElement, CanalNotificacao canalNotificacao, JsonBuilder ctx) {

        return null;
    }

    @Override
    public JsonElement view(CanalNotificacao obj, JsonBuilder ctx) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("id", obj.getExternalId());
        jObj.addProperty("remetente", obj.getRemetente().getNome());
        jObj.addProperty("canal", obj.getCanal().getExternalId());
        return jObj;
    }


}

