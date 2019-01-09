package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.domain.Aplicacao;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

@DefaultJsonAdapter(Aplicacao.class)
public class AplicacaoAdapter implements JsonAdapter<Aplicacao> {

    public static Aplicacao create2(JsonElement jsonElement) {
        final JsonObject jObj = jsonElement.getAsJsonObject();
        String name = UtilsResource.getRequiredValue(jObj, "name");

        if (Aplicacao.findByAplicacaoName(name) != null) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APPNAME_ERROR);
        }

        String redirectUrl = UtilsResource.getRequiredValue(jObj, "redirect_uri");
        String description = UtilsResource.getRequiredValue(jObj, "description");
        String authorName = UtilsResource.getRequiredValue(jObj, "author");
        String siteUrl = UtilsResource.getRequiredValue(jObj, "site_url");
        return Aplicacao.createAplicacao(name, redirectUrl, description, authorName, siteUrl);
    }

    public static Aplicacao update2(JsonElement jsonElement, Aplicacao app) {
        final JsonObject jObj = jsonElement.getAsJsonObject();
        String name = UtilsResource.getRequiredValueOrReturnNullInstead(jObj, "name");

        Aplicacao foundApp;
        if ((foundApp = Aplicacao.findByAplicacaoName(name)) != null) {
            if (!app.equals(foundApp)) {
                throw new NotifcenterException(ErrorsAndWarnings.INVALID_APPNAME_ERROR);
            }
        }

        String redirectUrl = UtilsResource.getRequiredValueOrReturnNullInstead(jObj, "redirect_uri");
        String description = UtilsResource.getRequiredValueOrReturnNullInstead(jObj, "description");
        String authorName = UtilsResource.getRequiredValueOrReturnNullInstead(jObj, "author");
        String siteUrl = UtilsResource.getRequiredValueOrReturnNullInstead(jObj, "site_url");
        return app.updateAplicacao(name, redirectUrl, description, authorName, siteUrl);
    }

    @Override
    public Aplicacao create(JsonElement jsonElement, JsonBuilder ctx) {
        return create2(jsonElement);
    }

    @Override
    public Aplicacao update(JsonElement jsonElement, Aplicacao app, JsonBuilder ctx) {
        return update2(jsonElement, app);
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
