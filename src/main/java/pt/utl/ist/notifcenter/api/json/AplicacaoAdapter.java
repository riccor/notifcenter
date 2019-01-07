package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.api.AplicacaoResource;
import pt.utl.ist.notifcenter.domain.Aplicacao;

@DefaultJsonAdapter(Aplicacao.class)
public class AplicacaoAdapter implements JsonAdapter<Aplicacao> {

    @Override
    public Aplicacao create(JsonElement jsonElement, JsonBuilder ctx) {
        return AplicacaoResource.create2(jsonElement);
    }

    @Override
    public Aplicacao update(JsonElement jsonElement, Aplicacao app, JsonBuilder ctx) {
        return AplicacaoResource.update2(jsonElement, app);
    }

    @Override
    public JsonElement view(Aplicacao obj, JsonBuilder ctx) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("name", obj.getName());
        jObj.addProperty("clientId", obj.getExternalId());
        jObj.addProperty("author", obj.getAuthorName());
        jObj.addProperty("permissoes", obj.getPermissoesAplicacao().name());
        jObj.addProperty("description", obj.getDescription());
        jObj.addProperty("site_url", obj.getSiteUrl());
        jObj.addProperty("redirect_uri", obj.getRedirectUrl());
        jObj.addProperty("client_secret", obj.getSecret());
        return jObj;
    }

}
