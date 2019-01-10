package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.domain.*;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

@DefaultJsonAdapter(CanalNotificacao.class)
public class CanalNotificacaoAdapter implements JsonAdapter<CanalNotificacao> {

    public static CanalNotificacao create2(JsonElement jsonElement) {

        Aplicacao app = UtilsResource.getDomainObjectFromJsonProperty(jsonElement, Aplicacao.class, "app");
        Remetente remetente = UtilsResource.getDomainObjectFromJsonProperty(jsonElement, Remetente.class, "remetente");
        Canal canal = UtilsResource.getDomainObjectFromJsonProperty(jsonElement, Canal.class, "canal");

        if (!app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        for (CanalNotificacao cn : remetente.getCanaisNotificacaoSet()) {
            if (cn.getCanal().equals(canal)) {
                throw new NotifcenterException(ErrorsAndWarnings.ALREADY_EXISTING_RESOURCE, "Notification channel id " + cn.getExternalId() + " for sender " + remetente.getExternalId() + " and channel " + canal.getExternalId() + " was already created before.");
            }
        }

        return CanalNotificacao.createCanalNotificacao(canal, remetente);
    }

    @Override
    public CanalNotificacao create(JsonElement jsonElement, JsonBuilder ctx) {
        return create2(jsonElement);
    }

    @Override
    public CanalNotificacao update(JsonElement jsonElement, CanalNotificacao canalNotificacao, JsonBuilder ctx) {
        return null;
    }

    @Override
    public JsonElement view(CanalNotificacao obj, JsonBuilder ctx) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("id", obj.getExternalId());
        jObj.addProperty("remetente", obj.getRemetente().getExternalId());
        jObj.addProperty("canal", obj.getCanal().getExternalId());
        jObj.addProperty("aguardandoAprovacao", obj.getAguardandoAprovacao());
        return jObj;
    }


}

