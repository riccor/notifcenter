package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.domain.Aplicacao;
import pt.utl.ist.notifcenter.domain.Remetente;

@DefaultJsonAdapter(Remetente.class)
public class RemetenteAdapter implements JsonAdapter<Remetente> {

    //used for admin panel
    public static Remetente create2(JsonElement jsonElement) {
        final JsonObject jObj = jsonElement.getAsJsonObject();
        Aplicacao app = UtilsResource.getDomainObjectFromJsonProperty(jsonElement, Aplicacao.class, "app");
        String name = UtilsResource.getRequiredValue(jObj, "name");
        return Remetente.createRemetente(app, name);
    }

    public static Remetente update2(JsonElement jsonElement, Remetente remetente) {
        final JsonObject jObj = jsonElement.getAsJsonObject();
        String nome = UtilsResource.getRequiredValue(jObj, "name");
        return remetente.update(nome);
    }

    @Override
    public Remetente create(JsonElement jsonElement, JsonBuilder ctx) {
        return create2(jsonElement);
    }

    @Override
    public Remetente update(JsonElement jsonElement, Remetente remetente, JsonBuilder ctx) {
        return update2(jsonElement, remetente);
    }

    @Override
    public JsonElement view(Remetente obj, JsonBuilder ctx) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("id", obj.getExternalId());
        jObj.addProperty("name", obj.getNome());
        jObj.addProperty("appId", obj.getAplicacao().getExternalId());
        return jObj;
    }


}
