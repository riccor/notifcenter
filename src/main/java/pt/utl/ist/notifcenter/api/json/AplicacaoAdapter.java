package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.domain.Aplicacao;

@DefaultJsonAdapter(Aplicacao.class)
public class AplicacaoAdapter implements JsonAdapter<Aplicacao> {

    @Override
    public Aplicacao create(JsonElement jsonElement, JsonBuilder ctx) {
        final JsonObject jObj = jsonElement.getAsJsonObject();
        String name = UtilsResource.getRequiredValue(jObj, "name");
        String redirectUrl = UtilsResource.getRequiredValue(jObj, "redirect_uri");
        String description = UtilsResource.getRequiredValue(jObj, "description");
        String authorName = UtilsResource.getRequiredValue(jObj, "author");
        String siteUrl = UtilsResource.getRequiredValue(jObj, "site_url");
        return Aplicacao.createAplicacao(name, redirectUrl, description, authorName, siteUrl);
    }

    @Override
    public Aplicacao update(JsonElement jsonElement, Aplicacao app, JsonBuilder ctx) {
        final JsonObject jObj = jsonElement.getAsJsonObject();
        String name = UtilsResource.getRequiredValue(jObj, "name");
        String redirectUrl = UtilsResource.getRequiredValueOrReturnNullInstead(jObj, "redirect_uri");
        String description = UtilsResource.getRequiredValueOrReturnNullInstead(jObj, "description");
        String authorName = UtilsResource.getRequiredValueOrReturnNullInstead(jObj, "author");
        String siteUrl = UtilsResource.getRequiredValueOrReturnNullInstead(jObj, "site_url");
        return app.updateAplicacao(name, redirectUrl, description, authorName, siteUrl);
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

/*
    ///not needed: app.setPermissoesAplicacao(UtilsResource.getRequiredValue_AppPermissions(jObj));
    protected AppPermissions UtilsResource.getRequiredValue_AppPermissions(JsonObject obj) {
        if (obj.has("permissions")) {
            String permissionsString = obj.get("permissions").getAsString();

            if (permissionsString.equalsIgnoreCase("ALLOW_ALL")) {
                return AppPermissions.ALLOW_ALL;
            }
            else if (permissionsString.equalsIgnoreCase("RREQUIRES_APPROVAL")) {
                return AppPermissions.RREQUIRES_APPROVAL;
            }
            else if (permissionsString.equalsIgnoreCase("NONE")) {
                return AppPermissions.NONE;
            }
        }
        throw new NotifcenterException(ErrorsAndWarnings.INVALID_ENTITY_ERROR, "Missing parameter " + property + "!");
    }

}*/
